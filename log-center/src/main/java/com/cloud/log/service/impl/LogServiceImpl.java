package com.cloud.log.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cloud.common.utils.PageUtil;
import com.cloud.log.dao.LogDao;
import com.cloud.log.service.LogService;
import com.cloud.model.common.Page;
import com.cloud.model.log.Log;

/**
 * 日志存储到mysql实现
 *
 * @author allen zhangxuelun@cmsr.chinamobile.com
 */
//@Primary
@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;

	/**
	 * 将日志保存到数据库<br>
	 * 注解@Async是开启异步执行
	 *
	 * @param log
	 */
	@Async
	@Override
	public void save(Log log) {
		if (log.getCreateTime() == null) {
			log.setCreateTime(new Date());
		}
		if (log.getFlag() == null) {
			log.setFlag(Boolean.TRUE);
		}

		logDao.save(log);
	}

	@Override
	public Page<Log> findLogs(Map<String, Object> params) {
		int total = logDao.count(params);
		List<Log> list = Collections.emptyList();
		if (total > 0) {
			PageUtil.pageParamConver(params, true);

			list = logDao.findData(params);
		}
		return new Page<>(total, list);
	}
}
