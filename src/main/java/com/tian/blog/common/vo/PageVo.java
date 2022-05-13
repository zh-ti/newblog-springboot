package com.tian.blog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {
    private Long currentPage;
    private Long pageSize;
    private Long total;
    private Boolean hasPrev;
    private Boolean hasNext;
    private List<T> dataList;
}
