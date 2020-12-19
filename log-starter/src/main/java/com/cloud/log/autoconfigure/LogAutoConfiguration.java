package com.cloud.log.autoconfigure;

import com.cloud.model.log.constants.LogQueue;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author allen zhangxuelun@cmsr.chinamobile.com
 */
@Configuration
public class LogAutoConfiguration {

    /**
     * 声明队列<br>
     * 如果日志系统已启动，或者mq上已存在队列 LogQueue.LOG_QUEUE，此处不用声明此队列<br>
     * 此处声明只是为了防止日志系统启动前，并且没有队列 LogQueue.LOG_QUEUE的情况下丢失消息
     *
     * @return
     */
    @Bean
    public Queue logQueue() {
        return new Queue(LogQueue.LOG_QUEUE);
    }

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 将LogMqClient声明成Bean
     * 2019.07.29添加
     */
    @Bean
    public LogMqClient logMqClient() {
        return new LogMqClient(amqpTemplate);
    }

}
