package com.qsj.qsjMain.utils;

import com.qsj.qsjMain.config.EnvConf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


public class SerialUtils {
    public final static Integer MODE_ORDER = 1;
    public final static Integer MODE_COUPON_SN = 0;

    public final static String PREFIX_COUPON_SN = "Q";
    public final static String PREFIX_INTEGRAL_COUPON_SN = "C";
    public final static String PREFIX_ORDER = "O";
    public final static String PREFIX_RECHARGE = "R";

    public final static String PREFIX_VIP_RECHARGE = "V";

    public final static String PREFIX_VIP_INCREASE_RECHARGE = "I";
    private final static Map<String, Integer> internalCounterMap = new ConcurrentHashMap<>();

    protected static String generate(String prefix, Integer mode) {
        // 券码
        // 前缀+三十六进制时间戳+协调节点ID+两位三十六进制随机数+四位三十六进制内部计数器
        if (!internalCounterMap.containsKey(prefix)) {
            internalCounterMap.put(prefix, new Random().nextInt(1679616));
        }
        internalCounterMap.put(prefix, internalCounterMap.get(prefix) + 1);
        String sn = prefix +
                getTimestampPart(mode) +
                EnvConf.coordinateNodeId +
                padLeftZero(Integer.toString(new Random().nextInt(1296), 36), 2) +
                padLeftZero(Integer.toString(internalCounterMap.get(prefix) % 1679616, 36), 4);
        return sn.toUpperCase();
    }

    public static String generateSN() {
        return generate(PREFIX_COUPON_SN, MODE_COUPON_SN);
    }

    public static String generateIntegralSN() {
        return generate(PREFIX_INTEGRAL_COUPON_SN, MODE_COUPON_SN);
    }

    public static String generateOrder() {
        return generate(PREFIX_ORDER, MODE_ORDER);
    }

    public static String generateRecharge() {
        return generate(PREFIX_RECHARGE, MODE_ORDER);
    }

    public static String generateVipRecharge() {
        return generate(PREFIX_VIP_RECHARGE, MODE_ORDER);
    }

    public static String generateVipIncrease() {
        return generate(PREFIX_VIP_INCREASE_RECHARGE, MODE_ORDER);
    }
    public static String padLeftZero(String str, int length) {
        if (str.length() >= length) {
            return str;
        }
        return "0".repeat(length - str.length()) + str;
    }

    public static String generateNickName(Long userId) {
        return "QS" + padLeftZero(userId.toString(), 8);
    }


    /**
     * 获得时间戳部分
     * @param mode 0: 三十六禁止时间戳, 1: YYYYMMDDHHmmss
     * @return 时间戳部分
     */
    private static String getTimestampPart(Integer mode) {
        if (mode == 0) {
            return padLeftZero(Long.toString(new Date().getTime() / 1000, 36), 6);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return sdf.format(new Date());
        }
    }
}
