package com.cblue.store.common.pojo;

import java.util.List;

public class EasyUIDataGrid implements java.io.Serializable {
	
	private Long total;
	private List rows; //不同泛型
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	

}
