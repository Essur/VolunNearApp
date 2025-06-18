package com.volunnear.infrastructure.redis.repository;

import com.volunnear.infrastructure.redis.entity.IdempotencyEntry;
import org.springframework.data.repository.CrudRepository;

public interface IdempotencyRepository extends CrudRepository<IdempotencyEntry, String> {
}
