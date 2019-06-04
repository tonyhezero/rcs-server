package com.geo.rcs.modules.datasource.conf;//package com.geo.rcs.modules.datasource.conf;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.annotation.Order;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.lang.annotation.*;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.Map;
//import java.util.Set;
///**
// * @author wp
// * @date Created in 11:03 2019/5/21
// */
//public class MultiDataSource extends AbstractRoutingDataSource {
//    private final static ThreadLocal<String> DATA_SOURCE_KEY = new ThreadLocal<>(); //保存当前线程的数据源对应的key
//
//    private Set<Object> keySet;  //所有数据源的key集合
//
//    private static void switchSource(String key) {
//        DATA_SOURCE_KEY.set(key); //切换当先线程的key
//    }
//
//    private static void clear() {
//        DATA_SOURCE_KEY.remove(); //移除key值
//    }
//
//    public static Object execute(String ds, Run run) throws Throwable {
//        switchSource(ds);
//        try {
//            return run.run();
//        } finally {
//            clear();
//        }
//    }
//
//    //AbstractRoutingDataSource抽象类实现方法，即获取当前线程数据源的key
//    @Override
//    protected Object determineCurrentLookupKey() {
//        String key = DATA_SOURCE_KEY.get();
//        return key;
//    }
//
//    /**
//     * 在获取key的集合，目的只是为了添加一些告警日志
//     */
//    @Override
//    public void afterPropertiesSet() {
//        super.afterPropertiesSet();
//        try {
//            Field sourceMapField = AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
//            sourceMapField.setAccessible(true);
//            Map<Object, javax.sql.DataSource> sourceMap = (Map<Object, javax.sql.DataSource>) sourceMapField.get(this);
//            this.keySet = sourceMap.keySet();
//            sourceMapField.setAccessible(false);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public interface Run {
//        Object run() throws Throwable;
//    }
//
//    /**
//     * 用于获取AOP切点及数据源key的注解
//     */
//    @Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
//    @Retention(RetentionPolicy.RUNTIME)
//    @Documented
//    public @interface DataSource {
//        String value() default ""; //该值即key值
//    }
//
//    /**
//     * 声明切面
//     */
//    @Component
//    @Aspect
//    @Order(-10)  //使该切面在事务之前执行
//    public static class DataSourceSwitchInterceptor {
//
//        /**
//         * 扫描所有含有@MultiDataSource$DataSource注解的类
//         */
//        @Pointcut("@within(top.ivan.demo.springboot.mapper.MultiDataSource.DataSource)")
//        public void switchDataSource() {
//        }
//
//        /**
//         * 使用around方式监控
//         * @param point
//         * @return
//         * @throws Throwable
//         */
//        @Around("switchDataSource()")
//        public Object switchByMethod(ProceedingJoinPoint point) throws Throwable {
//            Method method = getMethodByPoint(point); //获取执行方法
//            Parameter[] params = method.getParameters(); //获取执行参数
//            Parameter parameter;
//            String source = null;
//            boolean isDynamic = false;
//            for (int i = params.length - 1; i >= 0; i--) { //扫描是否有参数带有@DataSource注解
//                parameter = params[i];
//                if (parameter.getAnnotation(DataSource.class) != null && point.getArgs()[i] instanceof String) {
//                    source = (String) point.getArgs()[i]; //key值即该参数的值，要求该参数必须为String类型
//                    isDynamic = true;
//                    break;
//                }
//            }
//            if (!isDynamic) { //不存在参数带有Datasource注解
//                DataSource dataSource = method.getAnnotation(DataSource.class); //获取方法的@DataSource注解
//                if (null == dataSource || !StringUtils.hasLength(dataSource.value())) { //方法不含有注解
//                    dataSource = method.getDeclaringClass().getAnnotation(DataSource.class); //获取类级别的@DataSource注解
//                }
//                if (null != dataSource) {
//                    source = dataSource.value(); //设置key值
//                }
//            }
//            return persistBySource(source, point); //继续执行该方法
//        }
//
//
//        private Object persistBySource(String source, ProceedingJoinPoint point) throws Throwable {
//            try {
//                switchSource(source); //切换数据源
//                return point.proceed(); //执行
//            } finally {
//                clear(); //清空key值
//            }
//        }
//
//        private Method getMethodByPoint(ProceedingJoinPoint point) {
//            MethodSignature methodSignature = (MethodSignature) point.getSignature();
//            return methodSignature.getMethod();
//        }
//    }
//}
