package com.aspire.webbas.portal.common.service.impl;

import com.aspire.webbas.portal.common.dao.CityMapper;
import com.aspire.webbas.portal.common.entity.City;
import com.aspire.webbas.portal.common.service.CityService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cityService")
public class CityServiceImpl implements CityService {

	@Autowired
	private CityMapper cityMapper;

	public List<City> list(City page) {
		return this.cityMapper.list(page);
	}

	public City get(Integer cityId) {
		return this.cityMapper.selectByPrimaryKey(cityId);
	}
}