package com.aspire.webbas.portal.common.entity;

/**
 * 城市
 * <pre>
 * <b>Title：</b>City.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月9日 - 上午9:08:45<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class City {
	/**
	 * 市ID
	 */
	private Integer cityId;
	
	/**
	 * 城市名称
	 */
	private String cityName;
	
	/**
	 * 省份ID
	 */
	private Integer provinceId;
	
	/**
	 * 区号
	 */
	private String areaCode;

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = (cityName == null ? null : cityName.trim());
	}

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = (areaCode == null ? null : areaCode.trim());
	}

	@Override
	public String toString() {
		return "City [cityId=" + cityId + ", cityName=" + cityName
				+ ", provinceId=" + provinceId + ", areaCode=" + areaCode + "]";
	}
	
}