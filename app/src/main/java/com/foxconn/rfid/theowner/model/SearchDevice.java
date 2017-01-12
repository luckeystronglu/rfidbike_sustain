package com.foxconn.rfid.theowner.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by Administrator on 2017/1/5.
 */
@Table(name = "SearchDevice")
public class SearchDevice {


    @Id(column = "deviceIdDecimal")
    private String deviceIdDecimal;


    private long time;

    public String getDeviceIdDecimal() {
        return deviceIdDecimal;
    }

    public void setDeviceIdDecimal(String deviceIdDecimal) {
        this.deviceIdDecimal = deviceIdDecimal;
    }


    @Override
    public int hashCode() {
        return deviceIdDecimal.hashCode();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
