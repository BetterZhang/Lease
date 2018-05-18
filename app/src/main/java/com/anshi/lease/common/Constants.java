package com.anshi.lease.common;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/17 下午 1:33
 * Desc   : Constants常量配置
 */
public final class Constants {

    private Constants() {

    }

    // Http常量配置
    public static final class HttpConst {

        public static final int Test_LAN = 1; // 局域网内测试环境
        public static final int Test_WAN = 2; // 广域网内测试环境(外部可访问)
        public static final int Produce = 3;  // 正式生产环境

        public static int Envi = Test_LAN;

        public static String URL_BASE;
        public static String URL_BASE_IMG;

        private HttpConst() {

        }

        static {
            if (Envi == Test_LAN) {
                URL_BASE = "http://106.14.172.38:8081";
                URL_BASE_IMG = "http://106.14.172.38:8990/leaseupload/usericon/";
            } else if (Envi == Test_WAN) {
                URL_BASE = "http://106.14.172.38:8081";
                URL_BASE_IMG = "http://106.14.172.38:8990/leaseupload/usericon/";
            } else if (Envi == Produce) {
                URL_BASE = "http://106.14.172.38:8081";
                URL_BASE_IMG = "http://106.14.172.38:8990/leaseupload/usericon/";
            }
        }
    }

    public static final class IntentConst {

        private IntentConst() {

        }

        public static final int CODE_REQUEST_LOGIN = 10000;
    }

    // SP常量配置
    public static final class SPConst {

        private SPConst() {

        }

        public static final String SP_KEY_TOKENID = "tokenId";

    }

}
