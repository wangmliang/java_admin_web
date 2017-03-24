package com.aspire.webbas.portal.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtil {
	private static final Logger LOG = LoggerFactory.getLogger(CookieUtil.class);

	public static String getCookie(HttpServletRequest req, String name) {
		Cookie[] cookies = req.getCookies();

		if (cookies == null) {
			return null;
		}

		for (Cookie c : cookies) {
			if (name.equalsIgnoreCase(c.getName())) {
				try {
					return URLDecoder.decode(c.getValue(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					return c.getValue();
				}
			}
		}
		return null;
	}

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName, String cookieValue)
			throws ServletException {
		try {
			Cookie privacy = new Cookie(cookieName, URLEncoder.encode(
					cookieValue, "UTF-8"));
			privacy.setMaxAge(-1);
			privacy.setPath("/");
			response.addCookie(privacy);
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
			throw new ServletException(e);
		}
	}

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName,
			String cookieValue, int maxAge) throws ServletException {
		try {
			Cookie privacy = new Cookie(cookieName, URLEncoder.encode(
					cookieValue, "UTF-8"));

			privacy.setMaxAge(maxAge);
			privacy.setPath("/");
			response.addCookie(privacy);
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
			throw new ServletException(e);
		}
	}

	public static void delCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName)
			throws ServletException {
		Cookie privacy = new Cookie(cookieName, null);

		privacy.setMaxAge(0);
		privacy.setPath("/");
		response.addCookie(privacy);
	}
}