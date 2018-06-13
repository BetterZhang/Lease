package com.anshi.lease.domain;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/13 下午10:19
 * Desc   : description
 */
public class VehicleInfoVo {


    /**
     * DeviceType : BATTERY
     * LocTime : 1522231578199
     * DeviceID : 19
     * BatteryID : 40c7e58b649f49da88787c380655b8e0
     * VehicleID : c7bded1cd81c4f4194b51391cdea0e55
     * LON : 118.75454723
     * LAT : 31.99366459
     */

    private String DeviceType;
    private long LocTime;
    private String DeviceID;
    private String BatteryID;
    private String VehicleID;
    private double LON;
    private double LAT;

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String DeviceType) {
        this.DeviceType = DeviceType;
    }

    public long getLocTime() {
        return LocTime;
    }

    public void setLocTime(long LocTime) {
        this.LocTime = LocTime;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String DeviceID) {
        this.DeviceID = DeviceID;
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

    public double getLON() {
        return LON;
    }

    public void setLON(double LON) {
        this.LON = LON;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }
}
