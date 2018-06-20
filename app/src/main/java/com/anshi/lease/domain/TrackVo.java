package com.anshi.lease.domain;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/20 下午5:40
 * Desc   : description
 */
public class TrackVo {


    /**
     * LocTime : 1522205489441
     * LON : 118.7694618791042
     * StayTime : 29814
     * LAT : 31.981473655869156
     */

    private long LocTime;
    private double LON;
    private long StayTime;
    private double LAT;

    public long getLocTime() {
        return LocTime;
    }

    public void setLocTime(long LocTime) {
        this.LocTime = LocTime;
    }

    public double getLON() {
        return LON;
    }

    public void setLON(double LON) {
        this.LON = LON;
    }

    public long getStayTime() {
        return StayTime;
    }

    public void setStayTime(long StayTime) {
        this.StayTime = StayTime;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }
}
