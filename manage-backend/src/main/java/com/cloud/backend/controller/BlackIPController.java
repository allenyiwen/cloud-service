package com.cloud.backend.controller;

import com.cloud.backend.model.BlackIP;
import com.cloud.backend.service.BlackIPService;
import com.cloud.model.common.Page;
import com.cloud.model.log.LogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class BlackIPController {

	@Autowired
	private BlackIPService blackIPService;

	/**
	 * 添加黑名单ip
	 * 
	 */
	@LogAnnotation(module = "添加黑名单")
	@PreAuthorize("hasAuthority('ip:black:save')")
	@PostMapping("/blackIPs")
	public void save(@RequestBody BlackIP blackIP) {
		blackIP.setCreateTime(new Date());

		blackIPService.save(blackIP);
	}

	/**
	 * 删除黑名单ip
	 * 
	 */
	@LogAnnotation(module = "删除黑名单")
	@PreAuthorize("hasAuthority('ip:black:delete')")
	@DeleteMapping("/blackIPs")
	public void delete(String ip) {
		blackIPService.delete(ip);
	}

	/**
	 * 查询黑名单
	 * 
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('ip:black:query')")
	@GetMapping("/blackIPs")
	public Page<BlackIP> findBlackIPs(@RequestParam Map<String, Object> params) {
		return blackIPService.findBlackIPs(params);
	}

	/**
	 * 查询黑名单<br>
	 * 可内网匿名访问
	 * 
	 * @param params
	 * @return
	 */
	@GetMapping("/backend-anon/internal/blackIPs")
	public Set<String> findAllBlackIPs(@RequestParam Map<String, Object> params) {
		Page<BlackIP> page = blackIPService.findBlackIPs(params);
		if (page.getTotal() > 0) {
			return page.getData().stream().map(BlackIP::getIp).collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}
}
