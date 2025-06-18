package com.volunnear.infrastructure.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "idempotency", timeToLive = 86400)
public class IdempotencyEntry {
    @Id
    private String key;
    private String response;
    private Instant createdAt;
    private int statusCode;
}
