package com.geo.rcs.modules.datasource.conf;

import java.lang.annotation.*;

/**
 * @Description 类描述
 * @Author      yanghanwei
 * @Mail        yanghanwei@geotmt.com
 * @Date        2019/1/24 14:08
 * @Version     v3
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value();
}
