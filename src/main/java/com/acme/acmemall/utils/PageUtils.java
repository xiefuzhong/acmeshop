package com.acme.acmemall.utils;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author IHPANGZI
 * @date 2023-02-07
 */
@Data
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private int count;

    /**
     * 每页记录数
     */
    private int numsPerPage;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 当前页数
     */
    private int currentPage;

    /**
     * 列表数据
     */
    private List<?> data;

    /**
     * 扩展
     */
    private Object filterCategory;

    /**
     * 商品清单-扩展
     */
    private Object goodsList;

    public PageUtils() {

    }

    /**
     * 分页
     *
     * @param list        列表数据
     * @param count       总记录数
     * @param numsPerPage 每页记录数
     * @param currentPage 当前页数
     */
    public PageUtils(List<?> list, int count, int numsPerPage, int currentPage) {
        this.data = list;
        this.count = count;
        this.numsPerPage = numsPerPage;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) count / numsPerPage);
    }

    public PageUtils(PageInfo pageInfo) {
        this.count = (int) pageInfo.getTotal();
        this.numsPerPage = pageInfo.getPageSize();
        this.currentPage = pageInfo.getPageNum();
        this.totalPages = pageInfo.getPages();
        this.data = pageInfo.getList();
    }
}
