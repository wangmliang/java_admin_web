package com.aspire.webbas.portal.common.filter;

import com.aspire.webbas.core.util.SpringContextHolder;
import com.aspire.webbas.portal.common.auth.session.SessionContext;
import com.aspire.webbas.portal.common.authapi.AuthResult;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.service.AuthService;
import com.aspire.webbas.portal.common.service.impl.AuthServiceImpl;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ��Ȩ������
 * <pre>
 * <b>Title��</b>AuthFilter.java<br/>
 * <b>@author��</b>WML<br/>
 * <b>@date��</b>2016��11��8�� - ����5:54:26<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class AuthFilter implements Filter {
	public static final String JSP_SUFFIX = "jsp";
	public static final String AJAX_SUFFIX = "ajax";
	private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);
	private AuthService authService;

	public AuthService getAuthService() {
		return this.authService;
	}

	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}

	/**
	 * ��ʼ��
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * ���˲���
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String contextPath = httpRequest.getContextPath();
		String servletPath = httpRequest.getServletPath();

		servletPath = servletPath.replaceAll("\\//", "/");

		System.out.println(new StringBuilder().append("contextPath[")
				.append(contextPath).append("],  servletPath[")
				.append(servletPath).append("]").toString());

		this.authService = ((AuthService) SpringContextHolder.getBean(AuthServiceImpl.class));
		
		/**
		 * ���������Ȩ����
		 */
		if ((this.authService.authExclude(servletPath)) || (servletPath.equals("/pages/login.shtml"))) {
			System.out.println(new StringBuilder().append("url[").append(servletPath).append("]���������Ȩ��").toString());
			chain.doFilter(request, response);
			return;
		}
		HttpSession session = SessionContext.getContext().getSession(httpRequest.getSession(true).getId());
		Staff staff = session == null ? null : (Staff) session.getAttribute("LOGIN_STAFF");
		/**
		 * ��β��"/"����servletPathΪ""����ת���ص�¼
		 */
		if (("/".equals(servletPath)) || ("".equals(servletPath))
				|| (servletPath.equals("/pages/login.shtml"))) {
			/**
			 * �ѵ�¼�������ֱ����ת��ҳ��
			 */
			if (null != staff) {
				httpResponse.sendRedirect(new StringBuilder().append(contextPath).append("/pages/main.shtml").toString());
				return;
			}
		}
		/**
		 * δ��¼������ת����¼ҳ
		 */
		if (staff == null) {
			forceToLogin(httpRequest, httpResponse, contextPath, servletPath);
			return;
		}
		System.out.println(new StringBuilder().append("�Ƿ��session����û���").append(staff != null).toString());
		if (this.authService.notNeedAuthAfterLogin(servletPath)) {
			System.out.println(new StringBuilder().append("url[").append(servletPath).append("]���ڵ�¼�󲻼�Ȩ��").toString());
			chain.doFilter(request, response);
			return;
		}
		System.out.println(new StringBuilder().append("***********�û�[").append(staff.getStaffId()).append("]�ѵ�¼*************").toString());
		/**
		 * ��Ȩ����
		 */
		if (!this.authService.authorize(staff.getStaffId(), servletPath)) {
			System.out.println(new StringBuilder().append("***�û�[").append(staff.getStaffId()).append("]��Ȩʧ��*****").toString());
			if (isAjaxRequest(httpRequest)) {
				sendError(httpResponse, new AuthResult(AuthResult.FAIL, "�û�û��Ȩ��", new StringBuilder().append(contextPath)
				.append("/pages/noAuthority.shtml").toString()));
			} else {
				httpResponse.sendRedirect(new StringBuilder().append(contextPath).append("/pages/noAuthority.shtml").toString());
			}
			return;
		}
		chain.doFilter(request, response);
	}
	
	/**
	 * ����
	 */
	public void destroy() {
	}

	/**
	 * ��ת��¼ҳ
	 * @param request
	 * @param response
	 * @param contextPath
	 * @param servletPath
	 * @throws IOException
	 * @author WML
	 * 2016��11��8�� - ����5:55:57
	 */
	private void forceToLogin(HttpServletRequest request, HttpServletResponse response, String contextPath, String servletPath)
			throws IOException {
		System.out.println("forceToLogin....");
		if (isAjaxRequest(request)) {
			sendError(response, new AuthResult(AuthResult.NOT_LOGIN, "�û�δ��¼", new StringBuilder().append(contextPath).append("/pages/login.shtml").toString()));
		} else {
			System.out.println("************�ض��򵽵�¼ҳ��*****************");
			response.sendRedirect(new StringBuilder().append(contextPath).append("/pages/login.shtml").toString());
		}
	}

	/**
	 * ��ת����ҳ
	 * @param response
	 * @param result
	 * @author WML
	 * 2016��11��8�� - ����5:55:32
	 */
	@SuppressWarnings("deprecation")
	private void sendError(HttpServletResponse response, AuthResult result) {
		try {
			JsonGenerator jsonGenerator = null;
			ObjectMapper objectMapper = new ObjectMapper();
			jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(), JsonEncoding.UTF8);
			jsonGenerator.writeObject(result);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * �Ƿ�Ajax����
	 * @param request
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����5:55:19
	 */
	public boolean isAjaxRequest(HttpServletRequest request) {
		String xRequestedWith = request.getHeader("x-requested-with");
		return (null != xRequestedWith) && ("XMLHttpRequest".equalsIgnoreCase(xRequestedWith));
	}
}