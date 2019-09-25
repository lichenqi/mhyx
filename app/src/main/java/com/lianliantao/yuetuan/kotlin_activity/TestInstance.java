package com.lianliantao.yuetuan.kotlin_activity;

/*单列模式 双重锁模式*/
public class TestInstance {

    private static volatile TestInstance testInstance;

    public static TestInstance getInstance() {
        if (testInstance == null) {
            synchronized (TestInstance.class) {
                if (testInstance == null) {
                    testInstance = new TestInstance();
                }
            }
        }
        return testInstance;
    }

}
