package com.geo.rcs.modules.datasource.conf;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 类描述
 * @Author      yanghanwei
 * @Mail        yanghanwei@geotmt.com
 * @Date        2019/1/24 14:18
 * @Version     v3
 **/
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * 如配置文件中未指定数据源类型，使用该默认值
     */
    private static final Object DATASOURCE_TYPE_DEFAULT = "com.alibaba.druid.pool.DruidDataSource";
    private static final Object DATASOURCE_TYPE_KYLIN = "org.apache.tomcat.jdbc.pool.DataSource";
    private ConversionService conversionService = new DefaultConversionService();
    private PropertyValues dataSourcePropertyValues;
    private DataSource mysqlDataSource;
    private DataSource kylinDataSource;

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment environment) {
        initMysqlDataSource(environment);
        initKylinDataSource(environment);
    }

    /**
     * 加载主数据源配置.
     * @param env
     */
    private void initMysqlDataSource(Environment env) {
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        RelaxedPropertyResolver propertyResolver1 = new RelaxedPropertyResolver(env, "spring.datasource.druid.");
        Map<String, Object> dsMap = new HashMap<String, Object>(8);
        dsMap.put("type", propertyResolver.getProperty("type"));
        dsMap.put("driver-class-name", propertyResolver.getProperty("driver-class-name"));
        dsMap.put("url", propertyResolver1.getProperty("url"));
        dsMap.put("username", propertyResolver1.getProperty("username"));
        dsMap.put("password", propertyResolver1.getProperty("password"));
        mysqlDataSource = buildDataSource(dsMap,DATASOURCE_TYPE_DEFAULT);
        dataBinder(mysqlDataSource, env,"MYSQL");
    }

    /**
     * 初始化更多数据源
     * @param env
     */
    private void initKylinDataSource(Environment env) {
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "kylin.datasource.");
        Map<String, Object> dsMap = new HashMap<>(8);
        dsMap.put("type", propertyResolver.getProperty("type"));
        dsMap.put("driver-class-name", propertyResolver.getProperty("driver-class-name"));
        dsMap.put("url", propertyResolver.getProperty("url"));
        dsMap.put("username", propertyResolver.getProperty("username"));
        dsMap.put("password", propertyResolver.getProperty("password"));
        dsMap.put("maximum-pool-size", propertyResolver.getProperty("maximum-pool-size"));
        kylinDataSource = buildDataSource(dsMap,DATASOURCE_TYPE_KYLIN);
        dataBinder(kylinDataSource, env, "KYLIN");
    }

    /**
     * 创建datasource
     * @param dsMap
     * @return
     */
    public DataSource buildDataSource(Map<String, Object> dsMap,Object type) {
        if (type == null) {
            // 默认DataSource
            type = DATASOURCE_TYPE_DEFAULT;
        }
        Class<? extends DataSource> dataSourceType;
        try {
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 为DataSource绑定更多数据
     * @param dataSource
     * @param env
     */
    private void dataBinder(DataSource dataSource, Environment env, String label) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
        dataBinder.setConversionService(conversionService);
        dataBinder.setIgnoreNestedProperties(false);
        dataBinder.setIgnoreInvalidFields(false);
        dataBinder.setIgnoreUnknownFields(true);
        if (dataSourcePropertyValues == null) {
            if("KYLIN".equals(label)){
                Map<String, Object> rpr = new RelaxedPropertyResolver(env, "spring.datasource").getSubProperties(".");
                Map<String, Object> values = new HashMap<>(rpr);
                // 排除已经设置的属性
                values.remove("type");
                values.remove("driver-class-name");
                values.remove("url");
                values.remove("username");
                values.remove("password");
                dataSourcePropertyValues = new MutablePropertyValues(values);
            }
            if("MYSQL".equals(label)){
                RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
                RelaxedPropertyResolver propertyResolver1 = new RelaxedPropertyResolver(env, "spring.datasource.druid.");
                Map<String, Object> dsMap = new HashMap<String, Object>(8);
                dsMap.put("type", propertyResolver.getProperty("type"));
                dsMap.put("driver-class-name", propertyResolver.getProperty("driver-class-name"));
                dsMap.put("url", propertyResolver1.getProperty("url"));
                dsMap.put("username", propertyResolver1.getProperty("username"));
                dsMap.put("password", propertyResolver1.getProperty("password"));

                dsMap.remove("type");
                dsMap.remove("driver-class-name");
                dsMap.remove("url");
                dsMap.remove("username");
                dsMap.remove("password");
                dataSourcePropertyValues = new MutablePropertyValues(dsMap);
            }
        }
        dataBinder.bind(dataSourcePropertyValues);

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>(8);
        // 将主数据源添加到更多数据源中
        targetDataSources.put("mysql", mysqlDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("mysql");
        // 添加更多数据源
        targetDataSources.put("kylin", kylinDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("kylin");

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);

        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        //添加属性：AbstractRoutingDataSource.defaultTargetDataSource
        mpv.addPropertyValue("defaultTargetDataSource", mysqlDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
    }


}
