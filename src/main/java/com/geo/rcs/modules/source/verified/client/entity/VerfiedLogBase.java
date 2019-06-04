package com.geo.rcs.modules.source.verified.client.entity;

/**
 * Created by geo on 2019/4/1.
 */
public class VerfiedLogBase {
    private String token ;

    private String  exportType;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }


}
