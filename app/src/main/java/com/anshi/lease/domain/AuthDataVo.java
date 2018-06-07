package com.anshi.lease.domain;

import java.io.Serializable;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/07 下午11:02
 * Desc   : 实名认证数据信息类
 */
public class AuthDataVo implements Serializable {

//    id:ID,
//    userPid:用户身份证号
//    userIcFront:身份证正面照片BASE64码,
//    userIcBack:身份证背面照片BASE64码,
//    userIcGroup:用户手举身份证合照,
//    updateUser:更新人

    private String id;
    private String userPid;
    private String userIcFront;
    private String userIcBack;
    private String userIcGroup;
    private String updateUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPid() {
        return userPid;
    }

    public void setUserPid(String userPid) {
        this.userPid = userPid;
    }

    public String getUserIcFront() {
        return userIcFront;
    }

    public void setUserIcFront(String userIcFront) {
        this.userIcFront = userIcFront;
    }

    public String getUserIcBack() {
        return userIcBack;
    }

    public void setUserIcBack(String userIcBack) {
        this.userIcBack = userIcBack;
    }

    public String getUserIcGroup() {
        return userIcGroup;
    }

    public void setUserIcGroup(String userIcGroup) {
        this.userIcGroup = userIcGroup;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
