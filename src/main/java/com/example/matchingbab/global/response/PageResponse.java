package com.example.matchingbab.global.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        int numberOfElements,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    public static <T> PageResponse<T> from(
            Page<?> pageResult,
            List<T> content
    ) {
        return new PageResponse<>(
                content,
                pageResult.getNumber(),
                pageResult.getSize(),
                content.size(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast()
        );
    }
}