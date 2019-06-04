package com.geo.rcs.modules.engine.service;

public interface DroolsService {

    Object runStaticRules(Object runner);

    Object runDynamicRules(String ruleContent, String rulesFileId, Object runner);

    Object runDynamicCacheRules(String ruleContent, String rulesFileId, Object runner);

}
