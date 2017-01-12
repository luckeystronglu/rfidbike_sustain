package com.foxconn.rfid.theowner.model;

import java.io.Serializable;

/**
 * Created by appadmin on 2017/1/5.
 */

public class ReadCardMsgEntity implements Serializable {
    private long deviceId;
    private String title;
    private long dateTime;
    private String content;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReadCardMsgEntity() {
    }

    public ReadCardMsgEntity(String title, long dateTime, String content) {
        this.deviceId = deviceId;
        this.title = title;
        this.dateTime = dateTime;
        this.content = content;
    }
}
