package com.cloud.log.controller;

import com.cloud.log.service.LogService;
import com.cloud.model.common.Page;
import com.cloud.model.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LogController {

	@Autowired
	private LogService logService;

	@PostMapping("/logs-anon/internal")
	public void save(@RequestBody Log log) {
		logService.save(log);
	}

	/**
	 * 日志模块<br>
	 * 2019.07.29作废
	 */
	@Deprecated
	@PreAuthorize("hasAuthority('log:query')")
	@GetMapping("/logs-modules")
	public Map<String, String> logModule() {
		return com.cloud.model.log.constants.LogModule.MODULES;
	}

	/**
	 * 日志查询
	 * 
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('log:query')")
	@GetMapping("/logs")
	public Page<Log> findLogs(@RequestParam Map<String, Object> params) {
		return logService.findLogs(params);
	}

}
