package com.geo.rcs.modules.datasource.vo;

import lombok.Data;

/**
 * @Description 类描述
 * @Author      yanghanwei
 * @Mail        yanghanwei@geotmt.com
 * @Date        2019/1/25 14:56
 * @Version     v1
 **/
@Data
public class RepRuleFieldVo {

    /**
     * 字段id
     */
    private Integer fieldId;

    /**
     * 规则字段中文描述
     */
    private String fieldName;

    /**
     * 调用量
     */
    private Integer num;

    /**
     * 日期
     */
    private String recentDate;
    /**
     * 城市
     */
    private String province;
    /**
     *通过数量by城市
     */
    private Integer passNum;
    /**
     * 人工审核数量by城市
     */
    private Integer manNum;
    /**
     * 拒绝数量by城市
     */
    private Integer refuseNum;
    /**
     * 响应时间
     */
    private Integer expendEventAvg;
}
