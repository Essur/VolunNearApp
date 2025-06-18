package com.volunnear.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volunnear.infrastructure.redis.entity.IdempotencyEntry;
import com.volunnear.infrastructure.redis.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.security.Principal;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAspect {
    private final ObjectMapper objectMapper;
    private final IdempotencyRepository idempotencyRepository;

    @Around("@annotation(com.volunnear.annotation.Idempotent)")
    public Object handleIdempotent(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        String principal = getPrincipalName(args);
        String hash = DigestUtils.md5DigestAsHex(objectMapper.writeValueAsBytes(args));
        String key = principal + ":" + method.getName() + ":" + hash;

        Optional<IdempotencyEntry> existing = idempotencyRepository.findById(key);
        if (existing.isPresent()) {
            log.info("Returning cached idempotency entry for key {}", key);
            return objectMapper.readValue(existing.get().getResponse(), method.getReturnType());
        }

        Object result = joinPoint.proceed(args);

        String resultJson = objectMapper.writeValueAsString(result);
        IdempotencyEntry entry = IdempotencyEntry.builder()
                .key(key)
                .response(resultJson)
                .createdAt(Instant.now())
                .build();
        idempotencyRepository.save(entry);
        return result;
    }

    private String getPrincipalName(Object[] args) {
        for (Object arg: args){
            if (arg instanceof Principal principal){
                return principal.getName();
            }
        }
        return "anonymous";
    }
}
