package com.aspire.webbas.portal.modules.authapi;

import java.io.Serializable;

public class IfmStyle implements Serializable {
	private static final long serialVersionUID = 3240244812398914237L;
	private Integer winW;
	private Integer winH;
	private Integer ifmW;
	private Integer ifmH;
	private Integer ifmLeft;
	private Integer ifmTop;
	private Integer scrollTop;
	private Integer scrollLeft;

	public Integer getWinW() {
		return this.winW;
	}

	public void setWinW(Integer winW) {
		this.winW = winW;
	}

	public Integer getWinH() {
		return this.winH;
	}

	public void setWinH(Integer winH) {
		this.winH = winH;
	}

	public Integer getIfmW() {
		return this.ifmW;
	}

	public void setIfmW(Integer ifmW) {
		this.ifmW = ifmW;
	}

	public Integer getIfmH() {
		return this.ifmH;
	}

	public void setIfmH(Integer ifmH) {
		this.ifmH = ifmH;
	}

	public Integer getIfmLeft() {
		return this.ifmLeft;
	}

	public void setIfmLeft(Integer ifmLeft) {
		this.ifmLeft = ifmLeft;
	}

	public Integer getIfmTop() {
		return this.ifmTop;
	}

	public void setIfmTop(Integer ifmTop) {
		this.ifmTop = ifmTop;
	}

	public Integer getScrollTop() {
		return this.scrollTop;
	}

	public void setScrollTop(Integer scrollTop) {
		this.scrollTop = scrollTop;
	}

	public Integer getScrollLeft() {
		return this.scrollLeft;
	}

	public void setScrollLeft(Integer scrollLeft) {
		this.scrollLeft = scrollLeft;
	}
}