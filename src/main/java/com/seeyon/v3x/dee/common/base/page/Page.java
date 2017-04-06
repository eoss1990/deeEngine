package com.seeyon.v3x.dee.common.base.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述数据库分页信息的Java类
 *
 * @author liuls
 */
@SuppressWarnings("unchecked")
public class Page<T> {
    public static final String ASC = "asc";

    public static final String DESC = "desc";

    //分页参数
    protected int pageNo = 1;

    protected int pageSize = 1;

    protected String orderBy = null;

    protected String order = null;

    protected boolean autoCount = true;


    //返回结果
    protected List<T> result = new ArrayList();

    protected long totalCount = -1;

    protected long totalPages = -1;


    public Page() {
    }

    public Page(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     *
     * @return 当前页数
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置当前页的页号，序号从1开始，低于1时自动调整为1。
     *
     * @param pageNo 前页的页号
     */
    public void setPageNo(int pageNo) {
        if (pageNo < 1) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }

    public Page<T> pageNo(int thePageNo) {
        setPageNo(thePageNo);
        return this;
    }

    /**
     * 获得每页的记录数量，默认为1。
     *
     * @return 记录数量
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的记录数量,低于1时自动调整为1.
     *
     * @param pageSize 每页数量
     */
    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            this.pageSize = 1;
        } else {
            this.pageSize = pageSize;
        }
    }

    public Page<T> pageSize(int thePageSize) {
        setPageSize(thePageSize);
        return this;
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置，序号从1开始。
     *
     * @return 所在位置号
     */
    public int getFirst() {
        return ((pageNo - 1) * pageSize) + 1;
    }

    /**
     * 获得排序字段，无默认值。
     *
     * @return 排序方式
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段
     *
     * @param orderBy 排序方式
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 设置排序方式
     *
     * @param theOrderBy 排序方式
     * @return page对象
     */
    public Page<T> orderBy(String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    /**
     * 获得排序方式。
     *
     * @return 排序方向
     */
    public String getOrder() {
        return order;
    }

    /**
     * 设置排序方式。
     *
     * @param order 可选值为desc或asc
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * 设置page排序方式。
     *
     * @param theOrder 排序方式
     * @return page
     */
    public Page<T> order(final String theOrder) {
        setOrder(theOrder);
        return this;
    }

    /**
     * 是否已设置排序字段，无默认值。
     *
     * @return
     */
    public boolean isOrderBySetted() {
        return orderBy != null || "".equals(orderBy) && order != null || "".equals(order);
    }

    /**
     * 查询对象时是否自动另外执行count查询获取总记录数, 默认为false。
     *
     * @return 是：true，否：false
     */
    public boolean isAutoCount() {
        return autoCount;
    }

    /**
     * 查询对象时是否自动另外执行count查询获取总记录数.
     *
     * @param autoCount true/false
     */
    public void setAutoCount(boolean autoCount) {
        this.autoCount = autoCount;
    }

    /**
     * 查询对象时是否自动另外执行count查询获取总记录数。
     *
     * @param theAutoCount true或false
     * @return page自动查询总页数
     */
    public Page<T> autoCount(final boolean theAutoCount) {
        setAutoCount(theAutoCount);
        return this;
    }

    /**
     * 取得页内的记录列表。
     *
     * @return 页内的记录列表
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 设置页内的记录列表。
     *
     * @param result 页内的记录列表
     */
    public void setResult(final List<T> result) {
        this.result = result;
    }

    /**
     * 取得总记录数，默认值为-1。
     *
     * @return 总记录数
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数。
     *
     * @param totalCount 总记录数
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 根据pageSize与totalCount计算总页数，默认值为-1。
     *
     * @return 总页数
     */
    public long getTotalPages() {
        if (totalCount < 0) {
            return -1;
        }

        long count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     * 设置总记总页数.
     *
     * @param totalPages 总页数
     */
    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 是否还有下一页。
     *
     * @return 有下一页：true，无：false
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPages());
    }

    /**
     * 取得下页的页号，序号从1开始，当前页为尾页时仍返回尾页序号。
     *
     * @return 下页的页号
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        } else {
            return pageNo;
        }
    }

    /**
     * 是否还有上一页。
     *
     * @return 有上一页：true，无：false
     */
    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     * 取得上页的页号，序号从1开始，当前页为首页时返回首页序号。
     *
     * @return 上页的页号
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }
}