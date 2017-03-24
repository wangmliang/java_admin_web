package com.aspire.webbas.portal.common.service.impl;

import com.aspire.webbas.portal.common.cache.AuthCache;
import com.aspire.webbas.portal.common.dao.OperationAddressDao;
import com.aspire.webbas.portal.common.dao.OperationDao;
import com.aspire.webbas.portal.common.entity.Operation;
import com.aspire.webbas.portal.common.entity.OperationAddress;
import com.aspire.webbas.portal.common.service.AuthService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthServiceImpl implements AuthService {
	private static final Logger logger = LoggerFactory
			.getLogger(AuthServiceImpl.class);

	@Autowired
	private OperationDao operationDao;

	@Autowired
	private OperationAddressDao operationAddressDao;

	// @Autowired
	// private ResourceDao resourceDao;

	@SuppressWarnings("unused")
	private static final String REG_SEP = "{*}";

	public Map<String, String> listAddressUrlByLoginName(String loginName)
			throws Exception {
		List<OperationAddress> list = this.operationAddressDao.listOperationAddressByLoginName(loginName);

		return createMap(list);
	}

	public boolean authorizeSuccess(String addressUrl,
			Map<String, String> urlMap) {
		String[] strs = addressUrl.split("\\?");

		if (!isContains(urlMap.keySet(), strs[0])) {
			return false;
		}
		String neededAuth = (String) urlMap.get(strs[0]);

		if ((neededAuth == null) || (strs.length < 2)) {
			return true;
		}

		Set<String> urlParams = splitParam(strs[1]);

		Set<String> neededAuthParams = splitParam(neededAuth);

		return urlParams.containsAll(neededAuthParams);
	}

	public boolean authorizeFail(String addressUrl, Map<String, String> urlMap) {
		return !authorizeSuccess(addressUrl, urlMap);
	}

	public boolean authExclude(String addressUrl) {
		initCache();

		Map<String, String> excludeAuthMap = AuthCache.getCache().getUnauthMap();

		if (excludeAuthMap != null) {
			return isContains(excludeAuthMap.keySet(), addressUrl);
		}

		return false;
	}

	public boolean notNeedAuthAfterLogin(String addressUrl) {
		initCache();

		Map<String, String> loginAuthMap = AuthCache.getCache()
				.getLoginAuthMap();

		if (loginAuthMap != null) {
			return isContains(loginAuthMap.keySet(), addressUrl);
		}

		return false;
	}

	public boolean authorize(Long staffId, String url) {
		initCache();

		String[] strs = url.split("\\?");
		String addressUrl = strs[0];
		String addressParam = strs.length > 1 ? strs[1] : null;

		Map<String, List<OperationAddress>> authMap = AuthCache.getCache()
				.getAuthMap();

		StringBuilder sb = new StringBuilder();
		if (!isContains(authMap.keySet(), addressUrl, sb)) {
			logger.debug(new StringBuilder()
					.append("method[authorize],addressUrl=").append(addressUrl)
					.append(",失败").toString());
			return false;
		}

		Set<String> userOperationList = getResourceAndOperationKey(staffId);
		String addressUrlTmp = addressUrl;
		if ((null != sb) && (!"".equals(sb.toString()))) {
			addressUrlTmp = sb.toString();
		}
		List<OperationAddress> neededAuthList = (List<OperationAddress>) authMap
				.get(addressUrlTmp);

		if ((neededAuthList == null) || (neededAuthList.size() == 0)) {
			return false;
		}

		for (OperationAddress address : neededAuthList) {
			logger.debug(new StringBuilder().append(address.getResourceId())
					.append("-").append(address.getOperationKey()).toString());

			if (userOperationList.contains(new StringBuilder()
					.append(address.getResourceId()).append("-")
					.append(address.getOperationKey()).toString())) {
				if ((addressParam == null) || ("".equals(addressParam))) {
					return true;
				}

				if (containsAuthParam(addressParam, buildParamString(address))) {
					return true;
				}
			}
		}

		return false;
	}

	private Set<String> splitParam(String params) {
		Set<String> set = new HashSet<String>();

		if (params == null) {
			return set;
		}

		String[] strs = params.split("\\&");

		for (String s : strs) {
			set.add(s);
		}

		return set;
	}

	private boolean containsAuthParam(String targetParam, String neededParam) {
		Set<String> urlParams = splitParam(targetParam);

		Set<String> neededAuthParams = splitParam(neededParam);

		return urlParams.containsAll(neededAuthParams);
	}

	private synchronized void initCache() {
		if (AuthCache.getCache().getUnauthMap() == null) {
			List<OperationAddress> unAuthList = this.operationAddressDao
					.listOperationAddressByAuthType("UNAUTH");

			List<OperationAddress> loginAuthList = this.operationAddressDao
					.listOperationAddressByAuthType("LOGIN_AUTH");

			List<OperationAddress> authList = this.operationAddressDao
					.listOperationAddressByAuthType("AUTH");

			AuthCache.getCache().init(createMap(unAuthList),
					createMap(loginAuthList), createAuthMap(authList));
		}
	}

	private Set<String> getResourceAndOperationKey(Long staffId) {
		List<Operation> list = this.operationDao
				.listResourceOperationByStaffId(staffId);

		Set<String> set = new HashSet<String>();

		for (Operation record : list) {
			set.add(new StringBuilder().append(record.getResourceId())
					.append("-").append(record.getOperationKey()).toString());
		}

		return set;
	}

	private Set<String> getResourceKeyAndOperationKey(Long staffId) {
		List<Operation> list = this.operationDao
				.listResourceKeyAndOperationKeyStaffId(staffId);

		Set<String> set = new HashSet<String>();

		for (Operation record : list) {
			set.add(new StringBuilder().append(record.getResourceKey())
					.append("-").append(record.getOperationKey()).toString());
		}
		return set;
	}

	private Map<String, String> createMap(List<OperationAddress> list) {
		Map<String, String> urlMap = new HashMap<String, String>();

		for (OperationAddress address : list) {
			urlMap.put(address.getOperationAddressUrl(),
					buildParamString(address));
		}

		return urlMap;
	}

	private Map<String, List<OperationAddress>> createAuthMap(
			List<OperationAddress> list) {
		Map<String, List<OperationAddress>> authMap = new HashMap<String, List<OperationAddress>>();

		List<OperationAddress> tempList = null;
		for (OperationAddress address : list) {
			if (authMap.containsKey(address.getOperationAddressUrl())) {
				tempList = (List<OperationAddress>) authMap.get(address
						.getOperationAddressUrl());

				tempList.add(address);
			} else {
				tempList = new ArrayList<OperationAddress>();

				tempList.add(address);
				authMap.put(address.getOperationAddressUrl(), tempList);
			}
		}

		return authMap;
	}

	private String buildParamString(OperationAddress address) {
		StringBuffer paramStr = new StringBuffer();

		if (!StringUtils.isEmpty(address.getParameterName1())) {
			paramStr.append(address.getParameterName1()).append("=")
					.append(address.getParameterValue1());
		}

		if (!StringUtils.isEmpty(address.getParameterName2())) {
			paramStr.append("&").append(address.getParameterName2())
					.append("=").append(address.getParameterValue2());
		}

		if (!StringUtils.isEmpty(address.getParameterName3())) {
			paramStr.append("&").append(address.getParameterName3())
					.append("=").append(address.getParameterValue3());
		}

		return paramStr.toString();
	}

	public List<Map<String, Boolean>> authorize(Long staffId, String[] resKeys,
			String[] operKeys) {
		List<Map<String, Boolean>> results = new ArrayList<Map<String, Boolean>>();

		Set<String> userOperationList = getResourceKeyAndOperationKey(staffId);
		for (int i = 0; i < resKeys.length; i++) {
			Map<String, Boolean> result = new HashMap<String, Boolean>();
			if (userOperationList.contains(new StringBuilder()
					.append(resKeys[i]).append("-").append(operKeys[i])
					.toString()))
				result.put("result", Boolean.valueOf(true));
			else {
				result.put("result", Boolean.valueOf(false));
			}
			results.add(result);
		}
		return results;
	}

	private boolean isMatch(String reg, String str) {
		String[] regs = reg.split("\\{\\*\\}");
		int len = regs.length;
		if (len == 1) {
			if (str.startsWith(regs[0])) {
				return true;
			}
			return false;
		}
		if (len == 2) {
			int start = str.indexOf(regs[0]);
			int end = str.lastIndexOf(regs[1]);
			if ((start + regs[0].length() <= end) && (str.startsWith(regs[0]))
					&& (str.endsWith(regs[1]))) {
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean isContains(Set<String> keys, String url, StringBuilder sb) {
		if (keys.contains(url)) {
			return true;
		}
		for (String key : keys) {
			if ((key.indexOf("{*}") > -1) && (isMatch(key, url))) {
				if (null != sb) {
					sb.append(key);
				}
				return true;
			}

		}

		return false;
	}

	private boolean isContains(Set<String> keys, String url) {
		return isContains(keys, url, null);
	}
}