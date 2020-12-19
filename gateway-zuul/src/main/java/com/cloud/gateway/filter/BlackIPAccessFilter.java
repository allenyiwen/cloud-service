package com.cloud.gateway.filter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cloud.gateway.feign.BackendClient;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 黑名单IP拦截<br>
 * 黑名单ip变化不会太频繁，<br>
 * 考虑到性能，我们不实时掉接口从别的服务获取了，<br>
 * 而是定时把黑名单ip列表同步到网关层,
 * 
 * @author allen
 *
 */
@Component
public class BlackIPAccessFilter extends ZuulFilter {

	/**
	 * 黑名单列表
	 */
	private Set<String> blackIPs = new HashSet<>();

	@Override
	public boolean shouldFilter() {
		if (blackIPs.isEmpty()) {
			return false;
		}

		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		String ip = getIpAddress(request);

		return blackIPs.contains(ip);// 判断ip是否在黑名单列表里
	}

	@Override
	public Object run() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		requestContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
		requestContext.setResponseBody("black ip");
		requestContext.setSendZuulResponse(false);

		return null;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Autowired
	private BackendClient backendClient;

	/**
	 * 定时同步黑名单IP
	 */
	@Scheduled(cron = "${cron.black-ip}")
	public void syncBlackIPList() {
		try {
			Set<String> list = backendClient.findAllBlackIPs(Collections.emptyMap());
			blackIPs = list;
		} catch (Exception e) {
			// do nothing
		}
	}

	/**
	 * 获取请求的真实ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
