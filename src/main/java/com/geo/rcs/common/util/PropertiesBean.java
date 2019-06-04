package com.geo.rcs.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置文件属性对象
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/11/23 20:50
 */
@Component
public class PropertiesBean {

    @Value("${spring.datasource.druid.url}")
    private String url;
    @Value("${spring.datasource.driverClassName}")
    private String driver;
    @Value("${spring.datasource.druid.username}")
    private String username;
    @Value("${spring.datasource.druid.password}")
    private String password;

    @Value("${kylin.datasource.username}")
    private String kylinAccount;
    @Value("${kylin.datasource.password}")
    private String kylinPwd;

    @Value("${spring.kafka.topic.apiEventTopic}")
    private String ApiEventTopic;
    @Value("${spring.kafka.topic.decisionEventTopic}")
    private String decisionEventTopic;

    public String getApiEventTopic() {
        return ApiEventTopic;
    }

    public String getDecisionEventTopic() {
        return decisionEventTopic;
    }

    public String getKylinAccount(){
        return kylinAccount;
    }
    public String getKylinPwd(){
        return kylinPwd;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
