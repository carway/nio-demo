package test.buffer;

import java.nio.CharBuffer;

/**
 * 1.3.4 剩余空间大小获取
 * int remaining()的作用：返回“当前位置”position与limit之间的元素数
 *
 * @author carway
 * @date 2019-04-02 20:52
 */
public class Test4 {

    public static void main(String[] args) {
        char[] charArray = new char[]{'a', 'b', 'c', 'd', 'e'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        System.out.println("A capacity()=" + charBuffer.capacity() + " limit()=" + charBuffer.limit() + " position()=" + charBuffer.position());
        charBuffer.position(2);
        System.out.println("B capacity()=" + charBuffer.capacity() + " limit()=" + charBuffer.limit() + " position()=" + charBuffer.position());
        System.out.println("C remaining()=" + charBuffer.remaining());
    }
/*

    A capacity()=5 limit()=5 position()=0
    B capacity()=5 limit()=5 position()=2
    C remaining()=3

    public final int remaining(){
        return limit-position;
    }
*/

}
