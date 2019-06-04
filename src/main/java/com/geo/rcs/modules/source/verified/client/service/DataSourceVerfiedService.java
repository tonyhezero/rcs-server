package com.geo.rcs.modules.source.verified.client.service;

import java.util.Map;

/**
 * Created by geo on 2019/3/27.
 */
public interface DataSourceVerfiedService {
    public String getValidateData(int type, String interType, Long user_id, Map<String, String> params);
}
