package com.cloud.log.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.utils.PageUtil;
import com.cloud.log.service.LogService;
import com.cloud.model.common.Page;
import com.cloud.model.log.Log;

/**
 * 日志存储到elasticsearch实现
 *
 * @author allen zhangxuelun@cmsr.chinamobile.com
 */
//@Service
public class EsLogServiceImpl implements LogService, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(EsLogServiceImpl.class);

	private static final String INDEX = "index_logs";
	private static final String TYPE = "type_logs";

	@Autowired
	private TransportClient client;

	/**
	 * 将日志保存到elasticsearch<br>
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
		logger.info("{}", log);

		String string = JSONObject.toJSONString(log);

		IndexRequestBuilder builder = client.prepareIndex(INDEX, TYPE).setSource(string, XContentType.JSON);
		builder.execute();
	}

	@Override
	public Page<Log> findLogs(Map<String, Object> params) {
		SearchRequestBuilder builder = client.prepareSearch().setIndices(INDEX).setTypes(TYPE);
		if (!CollectionUtils.isEmpty(params)) {
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

			// 用户名模糊匹配
			String username = MapUtils.getString(params, "username");
			if (StringUtils.isNoneBlank(username)) {
				queryBuilder.must(QueryBuilders.wildcardQuery("username", "*" + username + "*"));
			}

			// 模块精确匹配 2019.07.29改为模糊匹配
			String module = MapUtils.getString(params, "module");
			if (StringUtils.isNoneBlank(module)) {
//				queryBuilder.must(QueryBuilders.matchQuery("module", module));
				queryBuilder.must(QueryBuilders.wildcardQuery("module", "*" + module + "*"));
			}

			String flag = MapUtils.getString(params, "flag");
			if (StringUtils.isNoneBlank(flag)) {
				Boolean bool = Boolean.FALSE;
				if ("1".equals(flag) || "true".equalsIgnoreCase(flag)) {
					bool = Boolean.TRUE;
				}
				queryBuilder.must(QueryBuilders.matchQuery("flag", bool));
			}

			// 大于等于开始日期,格式yyyy-MM-dd
			String beginTime = MapUtils.getString(params, "beginTime");
			if (StringUtils.isNoneBlank(beginTime)) {
				// 转化为0点0分0秒
				Long timestamp = toTimestamp(beginTime + "T00:00:00");
				queryBuilder.must(QueryBuilders.rangeQuery("createTime").from(timestamp));
			}

			// 小于等于结束日期,格式yyyy-MM-dd
			String endTime = MapUtils.getString(params, "endTime");
			if (StringUtils.isNoneBlank(endTime)) {
				// 转化为23点59分59秒
				Long timestamp = toTimestamp(endTime + "T23:59:59");
				queryBuilder.must(QueryBuilders.rangeQuery("createTime").to(timestamp));
			}

			if (queryBuilder != null) {
				builder.setPostFilter(queryBuilder);
			}
		}

		builder.addSort("createTime", SortOrder.DESC);

		PageUtil.pageParamConver(params, true);
		Integer start = MapUtils.getInteger(params, PageUtil.START);
		if (start != null) {
			builder.setFrom(start);
		}

		Integer length = MapUtils.getInteger(params, PageUtil.LENGTH);
		if (length != null) {
			builder.setSize(length);
		}

		SearchResponse searchResponse = builder.get();

		SearchHits searchHits = searchResponse.getHits();
		// 总数量
		Long total = searchHits.getTotalHits();

		int size = searchHits.getHits().length;
		List<Log> list = new ArrayList<>(size);
		if (size > 0) {
			searchHits.forEach(hit -> {
				String val = hit.getSourceAsString();
				list.add(JSONObject.parseObject(val, Log.class));
			});
		}

		return new Page<>(total.intValue(), list);
	}

	private Long toTimestamp(String str) {
		LocalDateTime localDateTime = LocalDateTime.parse(str);
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		return date.getTime();
	}

	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	/**
	 * 初始化日志es索引
	 */
	@PostConstruct
	public void initIndex() {
		LogService logService = applicationContext.getBean(LogService.class);
		// 日志实现是否采用elasticsearch
		boolean flag = (logService instanceof EsLogServiceImpl);
		if (!flag) {
			return;
		}

		try {
			// 判断索引是否存在
			IndicesExistsResponse indicesExistsResponse = client.admin().indices()
					.exists(new IndicesExistsRequest(INDEX)).get();
			if (indicesExistsResponse.isExists()) {
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		CreateIndexRequestBuilder requestBuilder = client.admin().indices().prepareCreate(INDEX);

		CreateIndexResponse createIndexResponse = requestBuilder.execute().actionGet();
		if (createIndexResponse.isAcknowledged()) {
			logger.info("索引：{},创建成功", INDEX);
		} else {
			logger.error("索引：{},创建失败", INDEX);
		}
	}

}
