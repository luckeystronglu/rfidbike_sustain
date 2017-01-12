package com.foxconn.rfid.theowner.model;


import java.util.Map;

public class EventBusMsg {


    private MsgType mMsgType;
    private Map value;

    public MsgType getMsgType() {
        return mMsgType;
    }

    public void setMsgType(MsgType msgType) {
        mMsgType = msgType;
    }

    public Map getValue() {
        return value;
    }

    public void setValue(Map value) {
        this.value = value;
    }

    public enum MsgType {
        MSG_COMPANY_ID,
        MSG_ADDRESS_ID
    }
}
