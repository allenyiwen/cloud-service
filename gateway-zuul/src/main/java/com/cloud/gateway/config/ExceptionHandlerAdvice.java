package com.cloud.gateway.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.netflix.client.ClientException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.fastjson.JSONObject;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

	/**
	 * feignClient调用异常，将服务的异常和http状态码解析
	 * 
	 * @param exception
	 * @param response
	 * @return
	 */
	@ExceptionHandler({ FeignException.class })
	public Map<String, Object> feignException(FeignException exception, HttpServletResponse response) {
		int httpStatus = exception.status();
		if (httpStatus >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			log.error("feignClient调用异常", exception);
		}

		Map<String, Object> data = new HashMap<>();

		String msg = exception.getMessage();

		if (!StringUtils.isEmpty(msg)) {
			int index = msg.indexOf("\n");
			if (index > 0) {
				String string = msg.substring(index);
				if (!StringUtils.isEmpty(string)) {
					JSONObject json = JSONObject.parseObject(string.trim());
					data.putAll(json.getInnerMap());
				}
			}
		}
		if (data.isEmpty()) {
			data.put("message", msg);
		}

		data.put("code", httpStatus + "");

		response.setStatus(httpStatus);

		return data;
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> badRequestException(IllegalArgumentException exception) {
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.BAD_REQUEST.value());
		data.put("message", exception.getMessage());

		return data;
	}

	@ExceptionHandler({ClientException.class, Throwable.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> serverException(Throwable throwable) {
		log.error("服务端异常", throwable);
		Map<String, Object> data = new HashMap<>();
		data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		data.put("message", "服务端异常，请联系管理员");

		return data;
	}

}
