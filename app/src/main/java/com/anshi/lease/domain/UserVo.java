package com.anshi.lease.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/17 下午 3:03
 * Desc   : description
 */
public class UserVo {


    /**
     * key_user_info : {"createTime":1519805177000,"createUser":"13913398413","id":"4317772d90ed46f293cefe9f37b88570","loginName":"test","nickName":"er","orgCode":"13","orgId":"ef007062faa4468494909699681adf58","orgName":"中信集团","password":"39EE488C7696D8F3EE3456218666A3C9","updateTime":1526450459000,"updateUser":"yk","userIcBack":"","userIcFront":"","userIcGroup":"","userIcon":"1526376810986.jpg","userMobile":"13913398413","userName":"test","userPid":"340521199710251014","userRealNameAuthFlag":"AUTHORIZED","userStatus":"NORMAL","userType":"INDIVIDUAL"}
     * key_vehicle_info : [{"bizBatteries":[{"batteryBrand":"骆驼牌","batteryCode":"8","batteryName":"锂电池","batteryParameters":"12AH","batteryPn":"36V","batteryStatus":"NORMAL","bindTime":1526389839000,"mfrsName":"超威集团"}],"bizPartss":[{"bindTime":1526350210000,"createTime":1526350193000,"createUser":"admin","id":"56eb103dc1094551b28986bad222c6ab","mfrsName":"东莞市凯瑞德电瓶车有限公司","partsBrand":"17","partsCode":"17","partsName":"17","partsParameters":"17","partsPn":"17","partsStatus":"NORMAL","partsType":"SEATS","updateTime":1526350193000,"updateUser":"admin"}],"createTime":1526281221000,"createUser":"admin","id":"1894853808fe46ebbc98824136d34034","mfrsName":"五龙集团","updateTime":1526390676000,"updateUser":"admin","vehicleBrand":"111","vehicleCode":"111","vehicleMadeIn":"111","vehiclePn":"111","vehicleStatus":"NORMAL"},{"bizBatteries":[{"batteryBrand":"32","batteryCode":"32","batteryName":"32","batteryParameters":"32","batteryPn":"32","batteryStatus":"NORMAL","bindTime":1526390251000,"mfrsName":"东莞市凯瑞德电瓶车有限公司"}],"bizPartss":[{"bindTime":1526350219000,"createTime":1526350180000,"createUser":"admin","id":"657aeb44a4a942c59eba7fa0d3ccd135","mfrsName":"东莞市凯瑞德电瓶车有限公司","partsBrand":"16","partsCode":"16","partsName":"16","partsParameters":"16","partsPn":"16","partsStatus":"NORMAL","partsType":"SEATS","updateTime":1526350179000,"updateUser":"admin"}],"createTime":1526263981000,"createUser":"admin","id":"44018cecc9eb4a479b6fa52c2b1704b4","mfrsName":"新日SUNRA电动车","updateTime":1526263981000,"updateUser":"admin","vehicleBrand":"26","vehicleCode":"26","vehicleMadeIn":"26","vehiclePn":"26","vehicleStatus":"NORMAL"},{"bizBatteries":[{"batteryBrand":"骆驼牌","batteryCode":"10","batteryName":"干电池","batteryParameters":"12AH","batteryPn":"36V","batteryStatus":"NORMAL","bindTime":1526390241000,"mfrsName":"超威集团"}],"bizPartss":[{"bindTime":1524533550000,"createTime":1517282477000,"createUser":"杨昆","id":"4b6d4e39afb84fe19c7d7bc8ee46a7af","mfrsName":"雅迪科技集团有限公司","partsBrand":"无锡产","partsCode":"1","partsName":"货物  啊","partsParameters":"6*6*6","partsPn":"230","partsStatus":"NORMAL","partsType":"SEATS","updateTime":1522226808000,"updateUser":"admin"}],"createTime":1522204526000,"createUser":"admin","id":"4a34b399191f4911bedc6079b006f986","mfrsName":"超威集团","updateTime":1522204587000,"updateUser":"admin","vehicleBrand":"凤凰","vehicleCode":"15","vehicleMadeIn":"江苏南京","vehiclePn":"12581","vehicleStatus":"NORMAL"},{"bizBatteries":[{"batteryBrand":"骆驼牌","batteryCode":"13","batteryName":"蓄电池","batteryParameters":"12AH","batteryPn":"36V","batteryStatus":"NORMAL","bindTime":1525768621000,"mfrsName":"超威集团"}],"bizPartss":[{"bindTime":1523596700000,"createTime":1522720431000,"createUser":"admin","id":"4051c597f4af406a8153403827a665c5","mfrsName":"浙江绿源电动车有限公司","partsBrand":"三生三世","partsCode":"阿萨德","partsName":"三生三世","partsParameters":"阿萨德","partsPn":"三生三世","partsStatus":"NORMAL","partsType":"SEATS","updateTime":1522720479000,"updateUser":"admin"}],"createTime":1522033280000,"createUser":"admin","id":"4b53ef646cf8402b8d38913d512e7ecf","mfrsName":"超威集团","updateTime":1522204357000,"updateUser":"admin","vehicleBrand":"凤凰","vehicleCode":"6","vehicleMadeIn":"江苏南京","vehiclePn":"6","vehicleStatus":"NORMAL"},{"bizBatteries":[{"batteryBrand":"骆驼牌","batteryCode":"18","batteryName":"锂电池","batteryParameters":"12AH","batteryPn":"36V","batteryStatus":"NORMAL","bindTime":1525770319000,"mfrsName":"超威集团"}],"bizPartss":[{"bindTime":1526350421000,"createTime":1526350328000,"createUser":"admin","id":"af67a3cc7eae4d27b13a2934618ca4c3","mfrsName":"东莞市凯瑞德电瓶车有限公司","partsBrand":"19","partsCode":"19","partsName":"19","partsParameters":"19","partsPn":"19","partsStatus":"NORMAL","partsType":"HANDLEBAR","updateTime":1526350328000,"updateUser":"admin"}],"createTime":1522380363000,"createUser":"admin","id":"519ec9c42d394545bc6ed6b5dfd7a043","mfrsName":"比德文BYVIN电动车","updateTime":1522380363000,"updateUser":"admin","vehicleBrand":"凤凰","vehicleCode":"18","vehicleMadeIn":"江苏南京","vehiclePn":"12581","vehicleStatus":"NORMAL"},{"bizBatteries":[{"batteryBrand":"asd1","batteryCode":"asd1","batteryName":"asd1","batteryParameters":"asd1","batteryPn":"asd1","batteryStatus":"NORMAL","bindTime":1525768625000,"mfrsName":"浙江绿源电动车有限公司"}],"bizPartss":[{"bindTime":1524203086000,"createTime":1517282477000,"createUser":"杨昆","id":"22b6005aeedc4d5b9046a517c92d4d52","mfrsName":"雅迪科技集团有限公司","partsBrand":"1","partsCode":"3","partsName":"阿里","partsParameters":"1","partsPn":"2","partsStatus":"NORMAL","partsType":"SEATS","updateTime":1522226851000,"updateUser":"admin"}],"createTime":1522200696000,"createUser":"admin","id":"7383b24ff08c43d5a4baa48d08ce42a1","mfrsName":"BYD","updateTime":1526282613000,"updateUser":"admin","vehicleBrand":"凤凰","vehicleCode":"12","vehicleMadeIn":"江苏南京","vehiclePn":"12581","vehicleStatus":"NORMAL"},{"bizBatteries":[{"batteryBrand":"骆驼牌","batteryCode":"22","batteryName":"蓄电池","batteryParameters":"12AH","batteryPn":"36V","batteryStatus":"NORMAL","bindTime":1526350081000,"mfrsName":"爱玛AIMA电动车"}],"bizPartss":[{"bindTime":1526350083000,"createTime":1522226977000,"createUser":"admin","id":"4ea6095e5b784d52804f8856a03a49b5","mfrsName":"雅迪","partsBrand":"阿萨德","partsCode":"8","partsName":"阿萨德","partsParameters":"啊实打实","partsPn":"阿萨德","partsStatus":"NORMAL","partsType":"SEATS","updateTime":1524729445000,"updateUser":"admin"}],"createTime":1526262123000,"createUser":"admin","id":"9c80c314ec9640cdaa0464b707da1fb9","mfrsName":"新日SUNRA电动车","updateTime":1526262122000,"updateUser":"admin","vehicleBrand":"1","vehicleCode":"23","vehicleMadeIn":"1","vehiclePn":"1","vehicleStatus":"NORMAL"},{"bizBatteries":[{"batteryBrand":"骆驼牌","batteryCode":"19","batteryName":"锂电池","batteryParameters":"12AH","batteryPn":"36V","batteryStatus":"NORMAL","bindTime":1525770311000,"mfrsName":"超威集团"}],"bizPartss":[{"bindTime":1526350407000,"createTime":1526350379000,"createUser":"admin","id":"6c500a3218e644b9ac23be82ad5674f9","mfrsName":"东莞市凯瑞德电瓶车有限公司","partsBrand":"23","partsCode":"23","partsName":"23","partsParameters":"23","partsPn":"23","partsStatus":"NORMAL","partsType":"HANDLEBAR","updateTime":1526350378000,"updateUser":"admin"}],"createTime":1520837533000,"createUser":"admin","id":"c7bded1cd81c4f4194b51391cdea0e55","mfrsName":"超威集团","updateTime":1522204573000,"updateUser":"admin","vehicleBrand":"凤凰","vehicleCode":"4","vehicleMadeIn":"江苏南京","vehiclePn":"12581","vehicleStatus":"NORMAL"},{"bizBatteries":[{"batteryBrand":"26","batteryCode":"26","batteryName":"26","batteryParameters":"26","batteryPn":"26","batteryStatus":"NORMAL","bindTime":1526350821000,"mfrsName":"新日SUNRA电动车"}],"bizPartss":[{"bindTime":1526350415000,"createTime":1526350342000,"createUser":"admin","id":"2cbbc3a14e514d2688d4d6531bc47d2c","mfrsName":"东莞市凯瑞德电瓶车有限公司","partsBrand":"20","partsCode":"20","partsName":"20","partsParameters":"20","partsPn":"20","partsStatus":"NORMAL","partsType":"SEATS","updateTime":1526350342000,"updateUser":"admin"}],"createTime":1525838650000,"createUser":"admin","id":"e86afa72b45c46c49687e1a2dbfefd4f","mfrsName":"BYD","updateTime":1525838649000,"updateUser":"admin","vehicleBrand":"22","vehicleCode":"22","vehicleMadeIn":"22","vehiclePn":"22","vehicleStatus":"NORMAL"}]
     * key_login_token : 9cbf28eb3f7049278d29456661c6520c
     */

    private KeyUserInfoBean key_user_info;
    private String key_login_token;
    private List<KeyVehicleInfoBean> key_vehicle_info;

    public KeyUserInfoBean getKey_user_info() {
        return key_user_info;
    }

    public void setKey_user_info(KeyUserInfoBean key_user_info) {
        this.key_user_info = key_user_info;
    }

    public String getKey_login_token() {
        return key_login_token;
    }

    public void setKey_login_token(String key_login_token) {
        this.key_login_token = key_login_token;
    }

    public List<KeyVehicleInfoBean> getKey_vehicle_info() {
        return key_vehicle_info;
    }

    public void setKey_vehicle_info(List<KeyVehicleInfoBean> key_vehicle_info) {
        this.key_vehicle_info = key_vehicle_info;
    }

    public static class KeyUserInfoBean {
        /**
         * createTime : 1519805177000
         * createUser : 13913398413
         * id : 4317772d90ed46f293cefe9f37b88570
         * loginName : test
         * nickName : er
         * orgCode : 13
         * orgId : ef007062faa4468494909699681adf58
         * orgName : 中信集团
         * password : 39EE488C7696D8F3EE3456218666A3C9
         * updateTime : 1526450459000
         * updateUser : yk
         * userIcBack :
         * userIcFront :
         * userIcGroup :
         * userIcon : 1526376810986.jpg
         * userMobile : 13913398413
         * userName : test
         * userPid : 340521199710251014
         * userRealNameAuthFlag : AUTHORIZED
         * userStatus : NORMAL
         * userType : INDIVIDUAL
         */

        private long createTime;
        private String createUser;
        private String id;
        private String loginName;
        private String nickName;
        private String orgCode;
        private String orgId;
        private String orgName;
        private String password;
        private long updateTime;
        private String updateUser;
        private String userIcBack;
        private String userIcFront;
        private String userIcGroup;
        private String userIcon;
        private String userMobile;
        private String userName;
        private String userPid;
        private String userRealNameAuthFlag;
        private String userStatus;
        private String userType;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getUserIcBack() {
            return userIcBack;
        }

        public void setUserIcBack(String userIcBack) {
            this.userIcBack = userIcBack;
        }

        public String getUserIcFront() {
            return userIcFront;
        }

        public void setUserIcFront(String userIcFront) {
            this.userIcFront = userIcFront;
        }

        public String getUserIcGroup() {
            return userIcGroup;
        }

        public void setUserIcGroup(String userIcGroup) {
            this.userIcGroup = userIcGroup;
        }

        public String getUserIcon() {
            return userIcon;
        }

        public void setUserIcon(String userIcon) {
            this.userIcon = userIcon;
        }

        public String getUserMobile() {
            return userMobile;
        }

        public void setUserMobile(String userMobile) {
            this.userMobile = userMobile;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPid() {
            return userPid;
        }

        public void setUserPid(String userPid) {
            this.userPid = userPid;
        }

        public String getUserRealNameAuthFlag() {
            return userRealNameAuthFlag;
        }

        public void setUserRealNameAuthFlag(String userRealNameAuthFlag) {
            this.userRealNameAuthFlag = userRealNameAuthFlag;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }

    public static class KeyVehicleInfoBean implements Serializable {
        /**
         * bizBatteries : [{"batteryBrand":"骆驼牌","batteryCode":"8","batteryName":"锂电池","batteryParameters":"12AH","batteryPn":"36V","batteryStatus":"NORMAL","bindTime":1526389839000,"mfrsName":"超威集团"}]
         * bizPartss : [{"bindTime":1526350210000,"createTime":1526350193000,"createUser":"admin","id":"56eb103dc1094551b28986bad222c6ab","mfrsName":"东莞市凯瑞德电瓶车有限公司","partsBrand":"17","partsCode":"17","partsName":"17","partsParameters":"17","partsPn":"17","partsStatus":"NORMAL","partsType":"SEATS","updateTime":1526350193000,"updateUser":"admin"}]
         * createTime : 1526281221000
         * createUser : admin
         * id : 1894853808fe46ebbc98824136d34034
         * mfrsName : 五龙集团
         * updateTime : 1526390676000
         * updateUser : admin
         * vehicleBrand : 111
         * vehicleCode : 111
         * vehicleMadeIn : 111
         * vehiclePn : 111
         * vehicleStatus : NORMAL
         */

        private long createTime;
        private String createUser;
        private String id;
        private String mfrsName;
        private long updateTime;
        private String updateUser;
        private String vehicleBrand;
        private String vehicleCode;
        private String vehicleMadeIn;
        private String vehiclePn;
        private String vehicleStatus;
        private List<BizBatteriesBean> bizBatteries;
        private List<BizPartssBean> bizPartss;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMfrsName() {
            return mfrsName;
        }

        public void setMfrsName(String mfrsName) {
            this.mfrsName = mfrsName;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getVehicleBrand() {
            return vehicleBrand;
        }

        public void setVehicleBrand(String vehicleBrand) {
            this.vehicleBrand = vehicleBrand;
        }

        public String getVehicleCode() {
            return vehicleCode;
        }

        public void setVehicleCode(String vehicleCode) {
            this.vehicleCode = vehicleCode;
        }

        public String getVehicleMadeIn() {
            return vehicleMadeIn;
        }

        public void setVehicleMadeIn(String vehicleMadeIn) {
            this.vehicleMadeIn = vehicleMadeIn;
        }

        public String getVehiclePn() {
            return vehiclePn;
        }

        public void setVehiclePn(String vehiclePn) {
            this.vehiclePn = vehiclePn;
        }

        public String getVehicleStatus() {
            return vehicleStatus;
        }

        public void setVehicleStatus(String vehicleStatus) {
            this.vehicleStatus = vehicleStatus;
        }

        public List<BizBatteriesBean> getBizBatteries() {
            return bizBatteries;
        }

        public void setBizBatteries(List<BizBatteriesBean> bizBatteries) {
            this.bizBatteries = bizBatteries;
        }

        public List<BizPartssBean> getBizPartss() {
            return bizPartss;
        }

        public void setBizPartss(List<BizPartssBean> bizPartss) {
            this.bizPartss = bizPartss;
        }

        public static class BizBatteriesBean {
            /**
             * batteryBrand : 骆驼牌
             * batteryCode : 8
             * batteryName : 锂电池
             * batteryParameters : 12AH
             * batteryPn : 36V
             * batteryStatus : NORMAL
             * bindTime : 1526389839000
             * mfrsName : 超威集团
             */

            private String batteryBrand;
            private String batteryCode;
            private String batteryName;
            private String batteryParameters;
            private String batteryPn;
            private String batteryStatus;
            private long bindTime;
            private String mfrsName;

            public String getBatteryBrand() {
                return batteryBrand;
            }

            public void setBatteryBrand(String batteryBrand) {
                this.batteryBrand = batteryBrand;
            }

            public String getBatteryCode() {
                return batteryCode;
            }

            public void setBatteryCode(String batteryCode) {
                this.batteryCode = batteryCode;
            }

            public String getBatteryName() {
                return batteryName;
            }

            public void setBatteryName(String batteryName) {
                this.batteryName = batteryName;
            }

            public String getBatteryParameters() {
                return batteryParameters;
            }

            public void setBatteryParameters(String batteryParameters) {
                this.batteryParameters = batteryParameters;
            }

            public String getBatteryPn() {
                return batteryPn;
            }

            public void setBatteryPn(String batteryPn) {
                this.batteryPn = batteryPn;
            }

            public String getBatteryStatus() {
                return batteryStatus;
            }

            public void setBatteryStatus(String batteryStatus) {
                this.batteryStatus = batteryStatus;
            }

            public long getBindTime() {
                return bindTime;
            }

            public void setBindTime(long bindTime) {
                this.bindTime = bindTime;
            }

            public String getMfrsName() {
                return mfrsName;
            }

            public void setMfrsName(String mfrsName) {
                this.mfrsName = mfrsName;
            }
        }

        public static class BizPartssBean {
            /**
             * bindTime : 1526350210000
             * createTime : 1526350193000
             * createUser : admin
             * id : 56eb103dc1094551b28986bad222c6ab
             * mfrsName : 东莞市凯瑞德电瓶车有限公司
             * partsBrand : 17
             * partsCode : 17
             * partsName : 17
             * partsParameters : 17
             * partsPn : 17
             * partsStatus : NORMAL
             * partsType : SEATS
             * updateTime : 1526350193000
             * updateUser : admin
             */

            private long bindTime;
            private long createTime;
            private String createUser;
            private String id;
            private String mfrsName;
            private String partsBrand;
            private String partsCode;
            private String partsName;
            private String partsParameters;
            private String partsPn;
            private String partsStatus;
            private String partsType;
            private long updateTime;
            private String updateUser;

            public long getBindTime() {
                return bindTime;
            }

            public void setBindTime(long bindTime) {
                this.bindTime = bindTime;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getCreateUser() {
                return createUser;
            }

            public void setCreateUser(String createUser) {
                this.createUser = createUser;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMfrsName() {
                return mfrsName;
            }

            public void setMfrsName(String mfrsName) {
                this.mfrsName = mfrsName;
            }

            public String getPartsBrand() {
                return partsBrand;
            }

            public void setPartsBrand(String partsBrand) {
                this.partsBrand = partsBrand;
            }

            public String getPartsCode() {
                return partsCode;
            }

            public void setPartsCode(String partsCode) {
                this.partsCode = partsCode;
            }

            public String getPartsName() {
                return partsName;
            }

            public void setPartsName(String partsName) {
                this.partsName = partsName;
            }

            public String getPartsParameters() {
                return partsParameters;
            }

            public void setPartsParameters(String partsParameters) {
                this.partsParameters = partsParameters;
            }

            public String getPartsPn() {
                return partsPn;
            }

            public void setPartsPn(String partsPn) {
                this.partsPn = partsPn;
            }

            public String getPartsStatus() {
                return partsStatus;
            }

            public void setPartsStatus(String partsStatus) {
                this.partsStatus = partsStatus;
            }

            public String getPartsType() {
                return partsType;
            }

            public void setPartsType(String partsType) {
                this.partsType = partsType;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public String getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(String updateUser) {
                this.updateUser = updateUser;
            }
        }
    }
}
