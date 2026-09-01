package com.siance.hm.common.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 1-based page/limit query params, matching the {@code page}/{@code limit}
 * query parameters used by {@code QueryHospitalDto} and other list endpoints
 * in the original API.
 */
@Getter
@Setter
public class PageQuery {

    @Min(1)
    private int page = 1;

    @Min(1)
    @Max(200)
    private int limit = 20;

    public Pageable toPageable(String... sortByDesc) {
        Sort sort = sortByDesc.length > 0 ? Sort.by(Sort.Direction.DESC, sortByDesc) : Sort.unsorted();
        return PageRequest.of(Math.max(page - 1, 0), limit, sort);
    }
}
