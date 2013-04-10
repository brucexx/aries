package com.brucexx.aries.db;

import java.util.Collection;

/**
 * 分页对象 
 *
 * @author brucexx
 */
public class Page implements java.io.Serializable {

    public static final Page EMPTY_PAGE = new Page();
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 每页的记录数，实际记录数小于或等于它
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 当前页第一条数据在数据库中的位置
     */
    private int start;
    /**
     * 当前页包含的记录数，avaCount <= pageSize
     */
    private int avaCount;
    /**
     * 总记录数
     */
    private int totalSize;
    /**
     * 当前页中存放的记录
     */
    private Object data;
    /**
     * 当前页码
     */
    private int currentPageNo;
    /**
     * 总页数
     */
    private int totalPageCount;

    /**
     * 构造方法，只构造空页
     */
    public Page() {
        this(0, 0, 0, DEFAULT_PAGE_SIZE, new Object());
    }

    public Page(Object list, int totalHits, int curPage, int pageSize) {
        this.data = list;
        if (data instanceof Collection) {
            avaCount = ((Collection) list).size();
        }
        this.totalSize = totalHits;
        this.currentPageNo = curPage;
        this.pageSize = pageSize;
        int k = totalHits / pageSize + (totalHits % pageSize == 0 ? 0 : 1);
        totalPageCount = k == 0 ? 1 : k;
        start = (curPage - 1) * pageSize + 1;
    }

    /**
     * 默认构造方法
     *
     * @param start     本页数据在数据库中的起始位置
     * @param avaCount  本页包含的数据条数
     * @param totalSize 数据库中总记录条数
     * @param pageSize  本页容量
     * @param data      本页包含的数据
     */
    public Page(int start, int avaCount, int totalSize, int pageSize, Object data) {
        this.avaCount = avaCount;
        if (pageSize == 0) {
            pageSize = 1;
        }
        this.pageSize = pageSize;
        this.start = start;
        this.totalSize = totalSize;
        this.data = data;

        this.currentPageNo = (start - 1) / pageSize + 1;
        this.totalPageCount = (totalSize + pageSize - 1) / pageSize;
        if (totalSize == 0 && avaCount == 0) {
            this.currentPageNo = 1;
            this.totalPageCount = 1;
        }
    }

    /**
     * 当前页中的记录
     */
    public Object getResult() {
        return this.data;
    }

    /**
     * 取本页数据容量（本页能包含的记录数）
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * 是否有下一页
     */
    public boolean hasNextPage() {
        return (this.getCurrentPageNo() < this.getTotalPageCount());
    }

    /**
     * 是否有上一页
     */
    public boolean hasPreviousPage() {
        return (this.getCurrentPageNo() > 1);
    }

    /**
     * 获取当前页第一条数据在数据库中的位置
     */
    public int getStart() {
        return start;
    }

    /**
     * 获取当前页最后一条数据在数据库中的位置
     */
    public int getEnd() {
        int end = this.getStart() + this.getSize() - 1;
        if (end < 0) {
            end = 0;
        }
        return end;
    }

    /**
     * 获取上一页第一条数据在数据库中的位置
     */
    public int getStartOfPreviousPage() {
        return Math.max(start - pageSize, 1);
    }

    /**
     * 获取下一页第一条数据在数据库中的位置
     */
    public int getStartOfNextPage() {
        return start + avaCount;
    }

    /**
     * 获取任一页第一条数据在数据库中的位置，每页条数使用默认值
     */
    public static int getStartOfAnyPage(int pageNo) {
        return getStartOfAnyPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 获取任一页第一条数据在数据库中的位置
     */
    public static int getStartOfAnyPage(int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize + 1;
        if (startIndex < 1) {
            startIndex = 1;
        }
        return startIndex;
    }

    /**
     * 取本页包含的记录数
     */
    public int getSize() {
        return avaCount;
    }

    /**
     * 取数据库中包含的总记录数
     */
    public int getTotalSize() {
        return this.totalSize;
    }

    /**
     * 取当前页码
     */
    public int getCurrentPageNo() {
        return this.currentPageNo;
    }

    /**
     * 取总页码
     */
    public int getTotalPageCount() {
        return this.totalPageCount;
    }

    /**
     * 当前页数据
     * @return
     */
    public Object getData() {
        return this.data;
    }
}
