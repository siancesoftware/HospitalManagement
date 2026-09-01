package com.siance.hm.common.response;

import com.siance.hm.common.util.RequestContext;

import java.util.List;

/**
 * Standard list/search envelope, matching the original
 * {@code PaginatedResponseDto}: data + total + page + limit + totalPages +
 * hasNextPage + hasPreviousPage, plus the requestId every response carries.
 */
public record PagedResult<T>(
        List<T> data,
        long total,
        int page,
        int limit,
        int totalPages,
        boolean hasNextPage,
        boolean hasPreviousPage,
        String requestId
) {

    public static <T> PagedResult<T> of(List<T> data, long total, int page, int limit) {
        int totalPages = limit <= 0 ? 0 : (int) Math.ceil((double) total / (double) limit);
        boolean hasNext = page < totalPages;
        boolean hasPrev = page > 1;
        return new PagedResult<>(data, total, page, limit, totalPages, hasNext, hasPrev,
                RequestContext.getRequestId());
    }

    /** Convenience for callers who already have a 0-based Spring {@code Page}. */
    public static <T> PagedResult<T> fromZeroBasedPage(List<T> data, long total, int zeroBasedPage, int limit) {
        return of(data, total, zeroBasedPage + 1, limit);
    }
}
