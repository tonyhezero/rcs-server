package com.geo.rcs.modules.datasource.vo;

import lombok.Data;

/**
 * @Description 规则字段请求类
 * @Author      yanghanwei
 * @Mail        yanghanwei@geotmt.com
 * @Date        2019/1/25 15:33
 * @Version     v1
 **/
@Data
public class ReqRuleFieldVo {

    /**
     * pageIndex
     */
    private Integer pageIndex;

    /**
     * pageSize
     */
    private Integer pageSize;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 场景id
     */
    private Integer sceneId;

    /**
     * 来源 1：规则集  2：决策集
     */
    private Integer eventSource;

    /**
     * 规则集id
     */
    private Integer rulesId;

    /**
     * 决策集id
     */
    private Integer decisionId;

    /**
     * 规则集名称
     */
    private String rulesName;

    /**
     * user id
     */
    private Long userId;
}
