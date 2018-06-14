package com.anshi.lease.domain;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/14 下午11:46
 * Desc   : description
 */
public class VehiclePowerVo {


    /**
     * DeviceType : BATTERY
     * RSOC : 80
     * PS : 0
     * DeviceID : 19
     * Quanity : 2Kwh
     * BatteryID : 40c7e58b649f49da88787c380655b8e0
     * VehicleID : c7bded1cd81c4f4194b51391cdea0e55
     */

    private String DeviceType;
    private String RSOC;
    private String PS;
    private String DeviceID;
    private String Quanity;
    private String BatteryID;
    private String VehicleID;

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String DeviceType) {
        this.DeviceType = DeviceType;
    }

    public String getRSOC() {
        return RSOC;
    }

    public void setRSOC(String RSOC) {
        this.RSOC = RSOC;
    }

    public String getPS() {
        return PS;
    }

    public void setPS(String PS) {
        this.PS = PS;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String DeviceID) {
        this.DeviceID = DeviceID;
    }

    public String getQuanity() {
        return Quanity;
    }

    public void setQuanity(String Quanity) {
        this.Quanity = Quanity;
    }

    public String getBatteryID() {
        return BatteryID;
    }

    public void setBatteryID(String BatteryID) {
        this.BatteryID = BatteryID;
    }

    public String getVehicleID() {
        return VehicleID;
    }

    public void setVehicleID(String VehicleID) {
        this.VehicleID = VehicleID;
    }
}
