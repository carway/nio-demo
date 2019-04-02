package test.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.InvalidMarkException;

/**
 * 1.3.6知识点细化测试
 * 前面介绍了缓冲区4个核心技术点: capacity、 limit、 position 和mark,根据这4个技术点，可以设计出以下7个实验。
 * 1)缓冲区的capacity不能为负数，缓冲区的limit不能为负数，缓冲区的position不能为负数。
 * 2) position不能大于其limit。
 * 3) limit不能大于其capacity。
 * 4)如果定义了mark,则在将position或limit调整为小于该mark的值时，该mark被丢存。
 * 5)如果未定义mark,那么调用reset()方法将导致抛出InvalidMarkException异常。
 * 6)如果position大于新的limit,则position的值就是新limit的值。
 * 7)当limit和position值一样时， 在指定的position写人数据时会出现异常，因为此位置是被限制的。
 *
 * @author carway
 * @date 2019-04-02 21:22
 */
public class DetailTest6 {
    public static void main(String[] args) {
        //test4_1();
        //test4_3();
//        test4_4();
//        test6();
        test7();
    }

    //验证第1条：缓冲区的capacity不能为负数，缓冲区的limit不能为负数，缓冲区的position不能为负数。

    /**
     * 验证“缓冲区的capacity不能为负数”
     * allocate(int capacity）分配一个新的缓存区
     */
    public static void test1_1() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("byteBuffer的容量capacity大小不能为负数");
        }
        /**
         * 运行结果：
         * byteBuffer的容量capacity大小不能为负数
         */
    }

    /**
     * 验证“缓冲区的limit不能为负数”
     */
    public static void test1_2() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        try {
            byteBuffer = (ByteBuffer) byteBuffer.limit(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("byteBuffer限制limit大小不能为负数");
        }
        /**
         * 运行结果：
         * byteBuffer限制limit大小不能为负数
         */
    }

    /**
     * 验证“缓冲区的position不能为负数”
     * allocate(int capacity）分配一个新的缓存区
     */
    public static void test1_3() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        try {
            byteBuffer = (ByteBuffer) byteBuffer.position(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("byteBuffer位置position大小不能为负数");
        }
        /**
         * 运行结果：
         * byteBuffer位置position大小不能为负数
         */
    }


    //验证第2条：position不能大于其limit

    /**
     * 验证“position不能大于其limit”
     */
    public static void test2() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        byteBuffer.limit(2);
        try {
            byteBuffer.position(3);
        } catch (IllegalArgumentException e) {
            System.out.println("byteBuffer的位置position大小不能大于其限制limit");
        }
        /**
         * 运行结果：
         * byteBuffer的位置position大小不能大于其限制limit
         */

/*
        public final Buffer position(int newPosition) {
            if ((newPosition > limit) || (newPosition < 0)) //关键代码
                throw new IllegalArgumentException();
            position = newPosition;
            if (mark > position) mark = -1;
            return this;
        }

        */
    }

    //验证第3条：limit不能大于其capacity

    /**
     * 验证“limit不能大于其capacity”
     */
    public static void test3() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        try {
            byteBuffer.limit(100);
        } catch (IllegalArgumentException e) {
            System.out.println("byteBuffer的limit大小不能大于其capacity容量");
        }
        /**
         * 运行结果：
         * byteBuffer的limit大小不能大于其capacity容量
         */

 /*
        public final Buffer limit(int newLimit) {
            if ((newLimit > capacity) || (newLimit < 0))    //关键代码
                throw new IllegalArgumentException();
            limit = newLimit;
            if (position > limit) position = limit;
            if (mark > limit) mark = -1;
            return this;
        }

        */
    }


    //验证第4条：如果定义了mark,则在将position或limit调整为小于该mark的值时，该mark被丢存。
    //这里将第4条拆分成4点来分别进行验证
    //1)如果定义了mark,则在将position调整为不小于该mark的值时，该mark不丢弃。
    //2)如果定义了mark,则在将position调整为小于该mark的值时，该mark被丢弃。
    //3)如果定义了mark,则在将limit 调整为不小于该mark的值时，该mark不丢弃。
    //4)如果定义了mark,则在将limit调整为小于该mark的值时，该mark被丢弃。
    //该mark被舍弃，丢弃后mark的值为-1


    /*
    public final Buffer position(int newPosition) {
        if ((newPosition > limit) || (newPosition < 0))
            throw new IllegalArgumentException();
        position = newPosition;
        if (mark > position) mark = -1;  //关键代码
        return this;
    }

    public final Buffer limit(int newLimit) {
        if ((newLimit > capacity) || (newLimit < 0))
            throw new IllegalArgumentException();
        limit = newLimit;
        if (position > limit) position = limit;
        if (mark > limit) mark = -1;    //关键代码
        return this;
    }

    从源码可知，不管是position或者是limit方法都和mark比较，如果mark大于它们，就设置为-1丢弃。

     */


    /**
     * 验证“如果定义了mark,则在将position调整为不小于该mark的值时，该mark不丢弃”
     */
    public static void test4_1() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        byteBuffer.position(1);
        byteBuffer.mark();

        System.out.println("byteBuffer在" + byteBuffer.position() + "位置设置mark标记");

        byteBuffer.position(2);

        byteBuffer.reset();

        System.out.println();

        System.out.println("byteBuffer回到" + byteBuffer.position() + "位置设置mark标记");

        /**
         * 运行结果：
         * byteBuffer位置position大小不能为负数
         */
    }

    /**
     * 验证“如果定义了mark,则在将position调整为小于该mark的值时，该mark被丢弃。”
     */
    public static void test4_2() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        byteBuffer.position(2);
        byteBuffer.mark();

        System.out.println("byteBuffer在" + byteBuffer.position() + "位置设置mark标记");

        byteBuffer.position(1);

        try {
            byteBuffer.reset();
        } catch (InvalidMarkException e) {
            System.out.println("byteBuffer的mark标记无效");

        }
        /**
         * 运行结果：
         * byteBuffer的mark标记无效
         */
    }

    /**
     * 验证“如果定义了mark,则在将limit 调整为不小于该mark的值时，该mark不丢弃。”
     */
    public static void test4_3() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        System.out.println("A byteBuffer position=" + byteBuffer.position() + " limit=" + byteBuffer.limit());

        byteBuffer.position(2);
        byteBuffer.mark();

        System.out.println("B byteBuffer position=" + byteBuffer.position() + " limit=" + byteBuffer.limit() + " 设置mark=" + byteBuffer.position());

        byteBuffer.position(3);
        byteBuffer.limit(3);

        System.out.println("C byteBuffer position=" + byteBuffer.position() + " limit=" + byteBuffer.limit());

        byteBuffer.reset();

        System.out.println("D byteBuffer position=" + byteBuffer.position() + " limit=" + byteBuffer.limit());

        /**
         * 运行结果：
         * A byteBuffer position=0 limit=3
         * B byteBuffer position=2 limit=3 设置mark=2
         * C byteBuffer position=3 limit=3
         * D byteBuffer position=2 limit=3
         */
    }

    /**
     * 验证“如果定义了mark,则在将limit调整为小于该mark的值时，该mark被丢弃。”
     */
    public static void test4_4() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        System.out.println("A byteBuffer position=" + byteBuffer.position() + " limit=" + byteBuffer.limit());

        byteBuffer.position(2);
        byteBuffer.mark();

        System.out.println("B byteBuffer position=" + byteBuffer.position() + " limit=" + byteBuffer.limit() + " 设置mark=" + byteBuffer.position());

        byteBuffer.limit(1);

        System.out.println("C byteBuffer position=" + byteBuffer.position() + " limit=" + byteBuffer.limit());

        try {
            byteBuffer.reset();
        } catch (InvalidMarkException e) {
            System.out.println("byteBuffer mark丢失");
        }

        /**
         * 运行结果：
         * byteBuffer mark丢失
         */
    }

    //验证第5条：如果未定义mark,那么调用reset()方法将导致抛出InvalidMarkException异常。

    /**
     * 验证“如果未定义mark,那么调用reset()方法将导致抛出InvalidMarkException异常。”
     * <p>
     * 实际上mark默认初始化就-1
     */
    public static void test5() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        try {
            byteBuffer.reset();
        } catch (InvalidMarkException e) {
            System.out.println("byteBuffer的mark标记无效");
        }
        /**
         * 运行结果：
         * byteBuffer的mark标记无效
         */
    }

    //验证第6条：如果position大于新的limit,则position的值就是新limit的值。

    /**
     * 验证“如果position大于新的limit,则position的值就是新limit的值。”
     */
    public static void test6() {
        byte[] byteArray = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        byteBuffer.position(3);
        System.out.println("byteBuffer limit(2)之前的位置：" + byteBuffer.position());
        byteBuffer.limit(2);
        System.out.println("byteBuffer limit(2)之后的位置：" + byteBuffer.position());
        /**
         * 运行结果：
         * byteBuffer limit(2)之前的位置：3
         * byteBuffer limit(2)之后的位置：2
         */


/*
        public final Buffer limit(int newLimit) {
            if ((newLimit > capacity) || (newLimit < 0))
                throw new IllegalArgumentException();
            limit = newLimit;
            if (position > limit) position = limit; //这句就是关键
            if (mark > limit) mark = -1;
            return this;
        }

        */
    }


    //验证第7条：当limit和position值一样时， 在指定的position写人数据时会出现异常，因为此位置是被限制的。。

    /**
     * 验证“当limit和position值一样时， 在指定的position写人数据时会出现异常，因为此位置是被限制的。”
     */
    public static void test7() {
        char[] charArray = new char[]{'a', 'b', 'c', 'd'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        System.out.println("A capacity()=" + charBuffer.capacity() + " limit()=" + charBuffer.limit() + " position()=" + charBuffer.position());

        charBuffer.position(1);
        charBuffer.limit(1);
        charBuffer.put("z");

        /**
         * 运行结果：
         * A capacity()=4 limit()=4 position()=0
         * 	at java.nio.CharBuffer.put(CharBuffer.java:922)
         * 	at java.nio.CharBuffer.put(CharBuffer.java:950)
         * 	at test.buffer.DetailTest6.test7(DetailTest6.java:302)
         * 	at test.buffer.DetailTest6.main(DetailTest6.java:27)
         */

        //直接贴源码更加一目了然
/*
        public CharBuffer put(String src, int start, int end) {
            checkBounds(start, end - start, src.length());
            if (isReadOnly())
                throw new ReadOnlyBufferException();
            if (end - start > remaining())
                throw new BufferOverflowException();  //这句就是关键
            for (int i = start; i < end; i++)
                this.put(src.charAt(i));
            return this;
        }
*/
    }
}
