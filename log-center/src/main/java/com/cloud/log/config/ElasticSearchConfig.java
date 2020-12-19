package com.cloud.log.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.cloud.log.service.impl.EsLogServiceImpl;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchConfig {

	private String clusterName;

	private String clusterNodes;

    /**
     * 使用elasticsearch实现类时才触发
     *
     * @return
     */
	@Bean
    @ConditionalOnBean(value = EsLogServiceImpl.class)
	public TransportClient getESClient() {
		// 设置集群名字
		Settings settings = Settings.builder().put("cluster.name", this.clusterName).build();
		TransportClient client = new PreBuiltTransportClient(settings);
		try {
			// 读取的ip列表是以逗号分隔的
			for (String clusterNode : this.clusterNodes.split(",")) {
				String ip = clusterNode.split(":")[0];
				String port = clusterNode.split(":")[1];
				((TransportClient) client)
						.addTransportAddress(new TransportAddress(InetAddress.getByName(ip), Integer.parseInt(port)));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return client;
	}
}
