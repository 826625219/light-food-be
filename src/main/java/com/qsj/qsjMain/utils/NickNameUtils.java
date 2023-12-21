package com.qsj.qsjMain.utils;

import java.util.Random;


public class NickNameUtils {

    private static final String[] NickNameList ={"抱猫软卧","老猫念诗","青眼猫","抚尾猫","布偶猫","别摸我的猫尾巴","那只折耳猫","九命猫","走失的猫","猫姬",
            "馋猫","枕上猫","夹心猫咪酒","彼得熊猫","三月折耳猫","猫九","猫癖","布丁猫妹","猫猫吃泡面","骑鱼的猫","躲猫猫","懒猫","思猫","小奶猫","偷腥的猫","青丝与白猫",
            "你的猫跑掉了","抱走小奶猫","老酒馆的猫"};

    public static String getName() {
        return NickNameList[new Random().nextInt(NickNameList.length)];
    }

}
