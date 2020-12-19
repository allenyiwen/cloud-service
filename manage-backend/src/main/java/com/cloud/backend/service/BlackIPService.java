package com.cloud.backend.service;

import java.util.Map;

import com.cloud.backend.model.BlackIP;
import com.cloud.model.common.Page;

public interface BlackIPService {

	void save(BlackIP blackIP);

	void delete(String ip);

	Page<BlackIP> findBlackIPs(Map<String, Object> params);

}
