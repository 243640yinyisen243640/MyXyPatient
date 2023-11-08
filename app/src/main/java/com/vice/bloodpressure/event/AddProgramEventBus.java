package com.vice.bloodpressure.event;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class AddProgramEventBus {
        private String message;

    public AddProgramEventBus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
