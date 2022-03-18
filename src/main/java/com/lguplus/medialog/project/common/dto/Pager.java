package com.lguplus.medialog.project.common.dto;

public class Pager {
	protected Integer offset;
	protected Integer page;
	protected int psize = 10; // 한 페이지에 10 건을 표시한다.
	protected int total = 0;
	protected int bsize = 10; // 10 page 단위로 표시한다. 1~10, 11~20, ...
	
	public Integer getOffset() {
		if (offset == null)
			return page == null ? 0 : (page - 1) * psize;
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getPage() {
		if (page == null)
			return offset == null ? 1 : offset / psize + 1;
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public int getPsize() {
		return psize;
	}
	public void setPsize(int psize) {
		this.psize = psize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getBsize() {
		return bsize;
	}
	public void setBsize(int bsize) {
		this.bsize = bsize;
	}

	public int getCurrentBlock() { // 0 based index
		return (page - 1) / bsize;
	}
	public int getStartPage() {
		return getCurrentBlock() * bsize + 1;
	}
	public int getEndPage() {
		int pnum = getLastPage();
		int epage = getCurrentBlock() * bsize + bsize;
		if (epage > pnum) epage = pnum;
		return epage;
	}
	public int getPreBlockLastPage() {
		int pre = getStartPage() - 1;
		if (pre == 0)
			pre = 1;
		return pre;
	}
	public int getNextBlockFirstPage() {
		int next = getEndPage() + 1;
		int last = getLastPage();
		if (next > last)
			next = last;
		return next;
	}
	public int getLastPage() {
		return total / psize + (total % psize == 0 ? 0 : 1);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pager [offset=");
		builder.append(offset);
		builder.append(", page=");
		builder.append(page);
		builder.append(", psize=");
		builder.append(psize);
		builder.append(", total=");
		builder.append(total);
		builder.append(", bsize=");
		builder.append(bsize);
		builder.append(", curBlock=");
		builder.append(getCurrentBlock());
		builder.append(", startPage=");
		builder.append(getStartPage());
		builder.append(", endPage=");
		builder.append(getEndPage());
		builder.append(", prePage=");
		builder.append(getPreBlockLastPage());
		builder.append(", nextPage=");
		builder.append(getNextBlockFirstPage());
		builder.append(", lastPage=");
		builder.append(getLastPage());
		builder.append("]");
		return builder.toString();
	}
	
}
