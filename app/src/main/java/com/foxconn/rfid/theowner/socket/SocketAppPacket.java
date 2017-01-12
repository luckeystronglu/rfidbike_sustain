package com.foxconn.rfid.theowner.socket;

import android.annotation.SuppressLint;

import org.apache.mina.core.session.IoSession;

@SuppressLint("DefaultLocale")
public class SocketAppPacket {
    // 最新版本检查
    public static final int COMMAND_ID_GET_APP_VERSION = 0x61033;
    // 用户登录
    public static final int COMMAND_ID_USER_LOGIIN = 0x61013;

    //密码更新
    public static final int COMMAND_ID_UPDATE_PASSWORD = 0x61023;

    //获取公司列表
    public static final int GET_COMPANY_LIST = 0x62013;
    //根据公司id获取设备列表
    public static final int GET_DEVICE_LIST_DEPEND_COMPANY_ID = 0x62023;
    //根据设备id获取设备列表
    public static final int GET_SEARCH_DEVICE_LIST_DEPEND_DEVICE_ID = 0x62043;
    //根据设备id搜索设备列表
    public static final int GET_DEVICE_LIST_DEPEND_DEVICE_ID = 0x62033;
    // 配置RF参数
    public static final int COMMAND_ID_CONFIG_RF_PARAMS = 0x30041;

    //获取设备状态
    public static final int COMMAND_ID_GET_DEVICE_STATUS = 0x62033;

    //重启设备
    public static final int COMMAND_ID_REBOOT_DEVICE = 0x30061;

    //新增基站
    public static final int COMMAND_ID_ADD_BASESTATION = 0x63013;

    //获取设备类型  0x63023
    public static final int COMMAND_ID_ADD_BASESTATION_TYPE = 0x63023;

    //获取新增设备状态  0x63023
    public static final int COMMAND_ID_ADD_BASESTATION_STATUS = 0x63033;

    private IoSession fromClient = null;

    /**
     * @return the fromClient
     */
    public IoSession getFromClient() {
        return fromClient;
    }

    public SocketAppPacket(IoSession channel) {
        this.fromClient = channel;
    }

    String packetType;

    public String getPacketType() {
        return packetType;
    }

    /**
     * @param packetType the packetType to set
     */
    private void setPacketType(String packetType) {
        this.packetType = packetType;
    }


    private int commandId = 0;

    /**
     * @return the commandId
     */
    public int getCommandId() {
        return commandId;
    }

    /**
     * @param commandId the commandId to set
     */
    public void setCommandId(int commandId) {
        this.commandId = commandId;

        String typeString = "0x" + Integer.toHexString(commandId).toUpperCase() + "_";
        switch (commandId) {
            // 最新版本检查
            case SocketAppPacket.COMMAND_ID_GET_APP_VERSION:
                typeString += "GET_APP_VERSION";
                break;
            // 用户登录
            case SocketAppPacket.COMMAND_ID_USER_LOGIIN:
                typeString += "USER_LOGIN";
                break;
            // 获取公司列表
            case SocketAppPacket.GET_COMPANY_LIST:
                typeString += "GET_COMPANY_LIST";
                break;
            default:
                typeString += "UNKNOWN";
                break;
        }

        setPacketType(typeString);
    }

    private byte[] commandData = null;

    /**
     * @return the commandData
     */
    public byte[] getCommandData() {
        return commandData;
    }

    /**
     * @param commandData the commandData to set
     */
    public void setCommandData(byte[] commandData) {
        this.commandData = commandData;
    }

    long receiveTime = 0;

    /**
     * @return the receiveTime
     */
    public long getReceiveTime() {
        return receiveTime;
    }

    /**
     * @param receiveTime the receiveTime to set
     */
    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

}
