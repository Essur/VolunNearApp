package com.volunnear.mapper;

import com.volunnear.dto.response.PagedResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PaginationMapper {
    public <T, U> PagedResponseDTO<U> mapPage(Page<T> page, Function<T, U> converter) {
        List<U> content = page.getContent().stream()
                .map(converter)
                .toList();

        return new PagedResponseDTO<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
