package com.jme.common.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Created by XuJun on 2017/09/04.
 */

public class StringUtils {

    /**
     * 手机号中间四位替换为*
     *
     * @param phone
     * @return 136****7423
     */
    public static String phoneInvisibleMiddle(String phone) {
        if (!TextUtils.isEmpty(phone))
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");

        return "";
    }

    /**
     * 身份证中间替换为*
     *
     * @param cardID
     * @return 3****************6
     */
    public static String cardIDInvisibleMiddle(String cardID) {
        int length = cardID.length();

        if (!TextUtils.isEmpty(cardID))
            return cardID.substring(0, 1) + "****************" + cardID.substring(length - 1, length);

        return "";
    }

    /**
     * 银行卡卡号替换为*
     *
     * @param bankCard
     * @return **** **** **** 2021
     */
    public static String bankCardInvisible(String bankCard) {
        if (TextUtils.isEmpty(bankCard))
            return "";

        int length = bankCard.length();

        if (length < 4)
            return "";

        return "**** **** **** " + bankCard.substring(length - 4, length);
    }

    /**
     * 银行卡卡号替换为*
     *
     * @param bankCard
     * @return 6214 **** **** 2021
     */
    public static String bankCardInvisibleMiddle(String bankCard) {
        if (TextUtils.isEmpty(bankCard))
            return "";

        int length = bankCard.length();

        if (length < 8)
            return "";

        return bankCard.substring(0, 4) + getBankCardStars(bankCard) + bankCard.substring(length - 4, length);
    }

    public static String bankCardInvisibleMiddleUnified(String bankCard) {
        if (TextUtils.isEmpty(bankCard))
            return "";

        int length = bankCard.length();

        if (length < 8)
            return "";

        return bankCard.substring(0, 4) + " **** **** " + bankCard.substring(length - 4, length);
    }

    private static String getBankCardStars(String bankcard) {
        int length = bankcard.length();
        String value;

        if (length == 16)
            value = " **** **** ";
        else if (length == 17)
            value = " **** **** * ";
        else if (length == 18)
            value = " **** **** ** ";
        else if (length == 19)
            value = " **** **** *** ";
        else
            value = " **** **** ";

        return value;
    }

    public static String getBankCardLastDigitsNumber(String bankcard) {
        if (TextUtils.isEmpty(bankcard))
            return "";

        int length = bankcard.length();

        if (length < 4)
            return bankcard;

        return bankcard.substring(length - 4, length);
    }

    /**
     * 只显示名字的第一个字
     *
     * @param name
     * @return 王*
     */
    public static String nameInvisible(String name) {
        if (!TextUtils.isEmpty(name))
            return name.substring(0, 1) + "*";
        else
            return "";
    }

    /**
     * 判断密码8-16位字母和数字的组合
     *
     * @param password
     * @return 王者荣*
     */
    public static boolean isPasswordRight(String password) {
        String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$";

        return Pattern.matches(REGEX_PASSWORD, password);
    }

    public static boolean checkBankCardLength(String bankCard) {
        int length = bankCard.length();

        if (length < 16 || length > 19)
            return false;

        return true;
    }

    /**
     * 校验银行卡卡号
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public static boolean checkBankCard(String bankCard) {
        int length = bankCard.length();

        if (length < 16 || length > 19)
            return false;

        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));

        if (bit == 'N')
            return false;

        return bankCard.charAt(length - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (null == nonCheckCodeBankCard || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+"))
            return 'N';

        char[] chs = nonCheckCodeBankCard.trim().toCharArray();

        int luhmSum = 0;

        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';

            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }

            luhmSum += k;
        }

        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static boolean isPhoneNumber(String mobile) {
        String REGEX_MOBILE = "^[1][3,4,5,7,8][0-9]{9}$";

        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    public static String getAddress(String addressStr) {
        String address;

        if (TextUtils.isEmpty(addressStr))
            return "";

        if (addressStr.startsWith("北京市") || addressStr.startsWith("天津市")
                || addressStr.startsWith("上海市") || addressStr.startsWith("重庆市")) {
            if (addressStr.contains(" 市辖区 "))
                address = addressStr.replace(" 市辖区 ", "");
            else if (addressStr.contains(" 县 "))
                address = addressStr.replaceAll(" 县 ", "");
            else
                address = addressStr;
        } else {
            address = addressStr.replace(" ", "");
        }

        return address;
    }

    public static String getSimpleAddress(String addressStr, String province, String city, String area) {
        String address;

        if (TextUtils.isEmpty(addressStr) || TextUtils.isEmpty(province)
                || TextUtils.isEmpty(city) || TextUtils.isEmpty(area))
            return "";

        if (province.equals("北京市") || province.equals("天津市")
                || province.equals("上海市") || province.equals("重庆市")) {
            address = province + area;
        } else
            address = province + city + area;

        return addressStr.replace(address, "");
    }

    public static String getRegion(String province, String city, String area) {
        String address;

        if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city) || TextUtils.isEmpty(area))
            return "";

        if (province.equals("北京市") || province.equals("天津市")
                || province.equals("上海市") || province.equals("重庆市")) {
            address = province + area;
        } else
            address = province + city + area;

        return address;
    }

    public static String formatPriceEachTree(double price) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(price);
    }

    public static String formatPriceTwoPoint(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(price);
    }

}
