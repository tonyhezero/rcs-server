package com.geo.rcs.modules.event.entity;

/**
 * @author wp
 * @date Created in 10:55 2019/4/19
 */
public enum BatchEventStatus {
    /**
     * 进行中
     */
    RUN(1),
    /**
     * 暂停
     */
    PAUSE(2),
    /**
     * 取消
     */
    CANCEL(3),
    /**
     * 完成
     */
    COMPLETE(4)
    ;

    private int value;

    BatchEventStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
