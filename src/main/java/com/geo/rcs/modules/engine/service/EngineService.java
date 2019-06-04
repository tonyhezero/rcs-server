package com.geo.rcs.modules.engine.service;

/**
 * Author:  yongmingz
 * Created on : 2018.1.11
 */
public interface EngineService {

    String getRulesRes(String rulesConfig, String rulesKey) throws Exception;

    String updateResStatusToPass(String rulesConfig) throws Exception;

    String updateResStatusToHuman(String rulesConfig) throws Exception;

    String updateResStatusToReject(String rulesConfig) throws Exception;

    String updateResStatusToInvalid(String rulesConfig) throws Exception;

    String runner(String rulesConfig, String rulesKey);

}
