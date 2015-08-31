package com.coderpage.wubinput.model;

/**
 * @author abner-l
 * @since 2015-08-31
 */
public abstract class Wubi {

    public static final int TYPE_86 = 0x0;
    public static final int TYPE_98 = 0x1;


    public static abstract class TypingMode {

        /**
         * 单字练习模式
         */
        public static final int PRACTICE_SINGLE_WORD = 0x10;

        /**
         * 词组练习模式
         */
        public static final int PRACTICE_PHRASH = 0x11;

        /**
         * 文章练习模式
         */
        public static final int PRACTICE_ARTICLE = 0x12;

        /**
         * 单字测试模式
         */
        public static final int TEST_SINGLE_WORD = 0x13;

        /**
         * 词组测试模式
         */
        public static final int TEST_PHRASH = 0x14;

        /**
         * 文章测试模式
         */
        public static final int TEST_ARTICLE = 0x15;

    }
}
