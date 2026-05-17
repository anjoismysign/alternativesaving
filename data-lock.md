Optimistic locking is actually **not the best fit for your situation**. Here's why, and what actually works better:

---

## Why optimistic locking is wrong here

Optimistic locking assumes conflicts are *rare*. In your case, the conflict scenario is **a player being on two shards simultaneously** — which is a network/proxy-level problem, not a data-level one. Detecting it at write time means:

- You already let the player load on two shards
- You already have two diverging in-memory states
- You now have to decide which one "wins" — and there's no good answer

Catching the problem *after* it happens is strictly worse than preventing it.

---

## The real problem to solve

You need a guarantee that **a player's data is loaded on at most one shard at a time**. Everything else follows from that.

---

## Option A — BungeeCord / Velocity server transfer lock (what you suggested)

This is the right instinct. The proxy already knows which server a player is on, so it's the natural place to enforce the invariant.

**How it works:**

When a player tries to connect to a shard, the proxy checks a shared lock store (Redis is standard here). If the player already has an active session on any shard, the proxy either:
- Kicks them from the old shard first, waits for save confirmation, then allows the new connection
- Or rejects the new connection entirely until the old session cleanly closes

```
Player connects → Proxy checks Redis →
  "player:uuid:shard" key exists? → kick from old shard, await save ACK → allow join
  key absent? → write "player:uuid:shard", allow join
Player disconnects → shard publishes save-complete to Redis → proxy deletes key
```

**Pros:**
- Prevents the problem entirely — data layer never sees concurrent access
- No schema changes
- Clean separation of concerns

**Cons:**
- Requires Redis (or equivalent shared store)
- Requires custom proxy plugin
- Save ACK adds latency to server switches

---

## Option B — Redis session lock at the shard level

If you can't control the proxy, enforce the lock in `AccountCruder` itself using Redis. Before loading a player's account, the shard tries to acquire a distributed lock:

```java
// On login
String lockKey = "session:lock:" + uuid;
boolean acquired = redis.set(lockKey, shardId,
    SetArgs.Builder.nx().ex(30)); // NX = only if not exists, 30s TTL

if (!acquired) {
    // Another shard holds this player — kick them
    event.disallow(Result.KICK_OTHER,
        "Your session is still active on another server.");
    return;
}
```

```java
// On logout — after save completes
redis.del("session:lock:" + uuid);
```

The TTL is a safety net: if a shard crashes without releasing the lock, it expires automatically. You extend it periodically while the player is online (like a heartbeat):

```java
// Every 10 seconds while player is online
redis.expire("session:lock:" + uuid, 30);
```

**Pros:**
- Works without proxy cooperation
- Handles shard crashes gracefully via TTL
- Can also replace your auto-save with Redis pub/sub for cross-shard notifications

**Cons:**
- Adds Redis as a hard dependency
- Slightly more complex lock lifecycle management

---

## Option C — Database session table (no Redis)

If Redis is off the table entirely, use a `sessions` table in your existing database:

```sql
CREATE TABLE IF NOT EXISTS tblSessions (
    player_uuid VARCHAR(36) PRIMARY KEY,
    shard_id    VARCHAR(64) NOT NULL,
    locked_at   BIGINT      NOT NULL
);
```

On login, attempt an atomic upsert:

```java
// MySQL
String sql = """
    INSERT INTO tblSessions (player_uuid, shard_id, locked_at)
    VALUES (?, ?, ?)
    ON DUPLICATE KEY UPDATE
        shard_id  = IF(locked_at < ?, VALUES(shard_id),  shard_id),
        locked_at = IF(locked_at < ?, VALUES(locked_at), locked_at)
    """;
```

Then read back to confirm *your* shard_id won:

```java
String owningShard = queryShard(uuid);
if (!owningShard.equals(thisShardId)) {
    // Another shard beat us — deny login
    event.disallow(...);
    return;
}
```

On logout, delete the row after saving.

Add a periodic cleanup job to remove stale rows (crashed shards):

```java
// Run every 60 seconds on each shard
String cleanup = "DELETE FROM tblSessions WHERE locked_at < ?";
long staleThreshold = System.currentTimeMillis() - 60_000;
```

**Pros:**
- No additional infrastructure
- Works with your existing HikariCP setup

**Cons:**
- Two round-trips on every login (insert + verify)
- Stale lock cleanup is eventually consistent, not instant
- Higher DB load than Redis

---

## What to actually pick

| Scenario | Best option |
|---|---|
| You control the proxy and can write plugins | **Option A** — cleanest, zero data-layer complexity |
| You can add Redis | **Option B** — most robust for dynamic shard topologies |
| No Redis, no proxy control | **Option C** — works but DB-level locking is a compromise |
| All of the above available | **A + B together** — proxy as first line, Redis as safety net for crashes |

Given that you mentioned BungeeCord specifically — **Option A is your best path**. The proxy is already the authority on player routing, so enforcing session exclusivity there is architecturally correct and keeps your data layer simple. The only thing worth adding on top is Option B's Redis heartbeat, purely as a crash-recovery safety net, not as the primary lock.