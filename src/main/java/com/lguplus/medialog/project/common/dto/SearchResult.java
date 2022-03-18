package com.lguplus.medialog.project.common.dto;



public class SearchResult<S,T> extends Result<T> {
	private Search<S> search;
	public Search<S> getSearch() {
		return search;
	}
	public void setSearch(Search<S> search) {
		this.search = search;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchResult [search=");
		builder.append(search);
		builder.append(", code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}
}
