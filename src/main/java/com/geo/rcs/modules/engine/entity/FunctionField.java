package com.geo.rcs.modules.engine.entity;

import java.io.Serializable;

/**
 * @author wp
 * @date Created in 10:49 2019/3/21
 */
public class FunctionField extends Field implements Serializable {

    private Long id;

    private Long functionId;

    private String  fieldId;

    private String  fieldName;

    private String showName;

    private String fieldType;

    private String value;

    private String describe;

    private String valueDesc;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getFieldId() {
        return fieldId;
    }

    @Override
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldType() {
        return fieldType;
    }

    @Override
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getDescribe() {
        return describe;
    }

    @Override
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String getValueDesc() {
        return valueDesc;
    }

    @Override
    public void setValueDesc(String valueDesc) {
        this.valueDesc = valueDesc;
    }

    @Override
    public String getShowName() {
        return showName;
    }

    @Override
    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

}
