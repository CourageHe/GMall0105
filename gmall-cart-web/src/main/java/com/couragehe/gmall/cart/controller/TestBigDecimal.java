package com.couragehe.gmall.cart.controller;

import java.math.BigDecimal;

/**
 * @PackageName:com.couragehe.gmall.cart.controller
 * @ClassName:TestBigDecimal
 * @Description:测试
 * @Autor:CourageHe
 * @Date: 2020/1/15 11:04
 */
public class TestBigDecimal {
    public static void main(String[]args){
        //初始化
        BigDecimal b1 = new BigDecimal(0.01f);
        BigDecimal b2 = new BigDecimal(0.01d);
        BigDecimal b3 = new BigDecimal("0.01");

        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);

        //比较
        int i = b1.compareTo(b2);// 1 0 -1
        System.out.println(i);
        //运算
        BigDecimal add = b1.add(b2);
        System.out.println(add);

        BigDecimal subtract = b2.subtract(b1);//减
        BigDecimal subtract1 = subtract.setScale(3,BigDecimal.ROUND_HALF_DOWN);
        System.out.println(subtract1);

        BigDecimal b4 = new BigDecimal("6");
        BigDecimal b5 = new BigDecimal("7");
        BigDecimal multiply = b4.multiply(b5);
        System.out.println(multiply);

        //保留三位有效数字，四舍五入
        BigDecimal divide = b4.divide(b5,3,BigDecimal.ROUND_HALF_DOWN);
        System.out.println(divide);

        //约数

    }
}
