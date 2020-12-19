package com.cloud.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 过滤uri<br>
 * 该类uri不需要登陆，但又不允许外网通过网关调用，只允许微服务间在内网调用，<br>
 * 为了方便拦截此场景的uri，我们自己约定一个规范，及uri中含有-anon/internal<br>
 * 如在oauth登陆的时候用到根据username查询用户，<br>
 * 用户系统提供的查询接口/users-anon/internal肯定不能做登录拦截，而该接口也不能对外网暴露<br>
 * 如果有此类场景的uri，请用这种命名格式，
 * 
 * @author allen
 *
 */
@Component
public class InternalURIAccessFilter extends ZuulFilter {

	@Override
	public Object run() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		requestContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
		requestContext.setResponseBody(HttpStatus.FORBIDDEN.getReasonPhrase());
		requestContext.setSendZuulResponse(false);

		return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();

		return PatternMatchUtils.simpleMatch("*-anon/internal*", request.getRequestURI());
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

}
