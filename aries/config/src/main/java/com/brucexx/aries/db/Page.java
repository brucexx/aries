package com.brucexx.aries.db;

import java.util.Collection;

/**
 * ��ҳ���� 
 *
 * @author brucexx
 */
public class Page implements java.io.Serializable {

    public static final Page EMPTY_PAGE = new Page();
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * ÿҳ�ļ�¼����ʵ�ʼ�¼��С�ڻ������
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * ��ǰҳ��һ�����������ݿ��е�λ��
     */
    private int start;
    /**
     * ��ǰҳ�����ļ�¼����avaCount <= pageSize
     */
    private int avaCount;
    /**
     * �ܼ�¼��
     */
    private int totalSize;
    /**
     * ��ǰҳ�д�ŵļ�¼
     */
    private Object data;
    /**
     * ��ǰҳ��
     */
    private int currentPageNo;
    /**
     * ��ҳ��
     */
    private int totalPageCount;

    /**
     * ���췽����ֻ�����ҳ
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
     * Ĭ�Ϲ��췽��
     *
     * @param start     ��ҳ���������ݿ��е���ʼλ��
     * @param avaCount  ��ҳ��������������
     * @param totalSize ���ݿ����ܼ�¼����
     * @param pageSize  ��ҳ����
     * @param data      ��ҳ����������
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
     * ��ǰҳ�еļ�¼
     */
    public Object getResult() {
        return this.data;
    }

    /**
     * ȡ��ҳ������������ҳ�ܰ����ļ�¼����
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * �Ƿ�����һҳ
     */
    public boolean hasNextPage() {
        return (this.getCurrentPageNo() < this.getTotalPageCount());
    }

    /**
     * �Ƿ�����һҳ
     */
    public boolean hasPreviousPage() {
        return (this.getCurrentPageNo() > 1);
    }

    /**
     * ��ȡ��ǰҳ��һ�����������ݿ��е�λ��
     */
    public int getStart() {
        return start;
    }

    /**
     * ��ȡ��ǰҳ���һ�����������ݿ��е�λ��
     */
    public int getEnd() {
        int end = this.getStart() + this.getSize() - 1;
        if (end < 0) {
            end = 0;
        }
        return end;
    }

    /**
     * ��ȡ��һҳ��һ�����������ݿ��е�λ��
     */
    public int getStartOfPreviousPage() {
        return Math.max(start - pageSize, 1);
    }

    /**
     * ��ȡ��һҳ��һ�����������ݿ��е�λ��
     */
    public int getStartOfNextPage() {
        return start + avaCount;
    }

    /**
     * ��ȡ��һҳ��һ�����������ݿ��е�λ�ã�ÿҳ����ʹ��Ĭ��ֵ
     */
    public static int getStartOfAnyPage(int pageNo) {
        return getStartOfAnyPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * ��ȡ��һҳ��һ�����������ݿ��е�λ��
     */
    public static int getStartOfAnyPage(int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize + 1;
        if (startIndex < 1) {
            startIndex = 1;
        }
        return startIndex;
    }

    /**
     * ȡ��ҳ�����ļ�¼��
     */
    public int getSize() {
        return avaCount;
    }

    /**
     * ȡ���ݿ��а������ܼ�¼��
     */
    public int getTotalSize() {
        return this.totalSize;
    }

    /**
     * ȡ��ǰҳ��
     */
    public int getCurrentPageNo() {
        return this.currentPageNo;
    }

    /**
     * ȡ��ҳ��
     */
    public int getTotalPageCount() {
        return this.totalPageCount;
    }

    /**
     * ��ǰҳ����
     * @return
     */
    public Object getData() {
        return this.data;
    }
}
