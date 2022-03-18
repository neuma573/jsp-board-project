package com.lguplus.medialog.project.common.dto;

import org.joda.time.DateTime;

import com.lguplus.medialog.project.common.utils.DateUtils;



public class Search<T> extends Pager {
	private T where;
	private String bgn, end;
	private String sort;
	public T getWhere() {
		return where;
	}
	public void setWhere(T where) {
		this.where = where;
	}
	public String getBgn() {
		return bgn;
	}
	public void setBgn(String bgn) {
		this.bgn = bgn;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public DateTime getBgnDate() {
		if (bgn == null || "".equals(bgn))
			return null;
		return DateUtils.str2dateYMD(bgn);
	}
	public DateTime getEndDate() {
		if (end == null || "".equals(end))
			return null;
		return DateUtils.str2dateYMDHMS(end + "235959");
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Search [where=");
		builder.append(where);
		builder.append(", bgn=");
		builder.append(bgn);
		builder.append(", end=");
		builder.append(end);
		builder.append(", sort=");
		builder.append(sort);
		builder.append(", offset=");
		builder.append(offset);
		builder.append(", page=");
		builder.append(page);
		builder.append(", psize=");
		builder.append(psize);
		builder.append(", total=");
		builder.append(total);
		builder.append("]");
		return builder.toString();
	}
}
