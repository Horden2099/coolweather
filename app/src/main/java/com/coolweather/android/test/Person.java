package com.coolweather.android.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来做Powermock的测试.
 * 方法名/类定义/魔数都是为了简单随意写的，仅仅只是用来介绍每种mock手段而已
 */
public class Person {
    private int mInvaildParam; // 仅仅是用来做私有构造方法的说明。私有构造方法常用于单例设计模式
    private String mName;
    private final List<String> mAddressList = new ArrayList<>();
    private School mInnerSchoolObj;

    private Person(int invaildParam) {
        mInvaildParam = invaildParam;
        System.out.println("Just test, param = " + invaildParam);
    }

    public Person(String name) {
        mName = name;
    }

    public void modifyName(String name) {
        modifyInnerName(name);
    }

    public String getName() {
        return mName;
    }

    public void addAddressList(String address) {
        mAddressList.add(address);
    }

    public void addInnerAddressList(String innerAddr) {
        mAddressList.add(innerAddr);
    }

    public List<String> getAllAddress() {
        return mAddressList;
    }

    public String getSchoolNo() {
        return mInnerSchoolObj.getNo();
    }

    public int getModifyInfoTimes(String info) {
        return getInnerModifyInfoTimes(info) + 2;
    }

    private int getInnerModifyInfoTimes(String info) {
        return 1;
    }
    private void modifyInnerName(String name) {
        mName = name;
    }

    public static class School {
        private String no;// 学号
        public void setNo() {
        }
        public String getNo() {
            return no;
        }
    }
}
