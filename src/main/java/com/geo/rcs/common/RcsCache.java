package com.geo.rcs.common;

/**
 * @author wp
 * @date Created in 14:36 2019/5/21
 */
public enum RcsCache {
    /**
     * 规则集缓存HEADER
     */
    RULESET_ONLINE("RULESETCACHE:ONLINE:","线上版规则集对象缓存"),
    RULESET_CONF("RCS24:RULESETCACHE:CONF:","配置版规则集对象缓存"),
    RULESET_MODIFY("RCS24:RULESETCACHE:MODIFY:FLAG:","规则集修改标识"),

    /**
     * 异步任务队列HEADER
     */
    JOY_SEND_COUNT("TASK:SEND:COUNT:","分发器发送任务数"),
    JOY_DISPATCH_SEND_COUNT("TASK:SEND:COUNT:","分发器发送任务数"),
    JOY_CONSUME_SUCCESS_COUNT("TASK:CONSUME:SUCCESS:COUNT：","执行器成功处理任务数"),
    JOY_CONSUME_ERROR_COUNT("TASK:CONSUME:ERROR:COUNT：","执行器处理失败任务数"),
    JOY_CONSUME_OTHER_COUNT("TASK:CONSUME:OTHER:COUNT：","执行器处理任务数-其他"),
    JOY_CONSUME_EXCEPTION_COUNT("TASK:CONSUME:EXCEPTION:COUNT：","执行器处理任务异常数"),
    JOY_CONSUME_ROLLBACK_COUNT("TASK:CONSUME:ROLLBACK:COUNT：","执行器处理任务回退数"),
    JOY_WORKER_KEEP_ALIVE("TASK:WORKER:","执行者注册"),

    /**
     * 批量进件HEADER
     */
    JOY_EVENT_PASS("TASK:EVENT:PASS:","批量进件通过数量"),
    JOY_EVENT_AUDIT("TASK:EVENT:AUDIT:","批量进件审批数量"),
    JOY_EVENT_REFUSE("TASK:EVENT:REFUSE:","批量进件拒绝数量"),
    JOY_EVENT_ERROR("TASK:EVENT:ERROR:","批量进件错误数量"),
    /**
     * 数据源接口查询缓存HEADER
     */
    SOURCE_INTER_CACHE("SOUECE:INTER:CACHE:","数据源接口缓存"),
    ;

    static final String VERSION = "RCS3:";

    private String header;

    private String message;

    RcsCache(String header,String message){
        this.header = header;
        this.message = message;
    }

    public String getHeader() {
        return VERSION +header;
    }

    public String getMessage() {
        return message;
    }

}
