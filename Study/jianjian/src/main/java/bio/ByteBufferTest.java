package bio;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * ByteBuffer
 * 三个重要的元素：
 *      容量 capcity 指的是创建缓冲区大小的上限
 *      限制 limit
 *      位置 position 通过位置灵活操作缓冲区里的数据
 *          limit和position结合使用position的位置不会超过limit的位置，常用于避免取到空数据避免取到空数据
 * Created by gao on 2016/11/28.
 */
public class ByteBufferTest {
    @Test
    public void testByteBuffer1(){
        //创建缓冲区并且制定初始容量，limit和capcity的值都为100
        ByteBuffer buffer=ByteBuffer.allocate(100);
        //填充数据，每调用一次put方法，position+1
        buffer.put("z".getBytes());
        buffer.put("a".getBytes());
        //get到的数据为0，get得到当前position上的数据
        //get,limit方法也会使得position+1
//        buffer.limit(1);
//        buffer.position(0);
        byte b=buffer.get();
        System.out.println(b);
    }
    @Test
    public void testWarp(){
        //wrap,相当于第一步创建了十字街的缓冲区，和字节数组是相等的
        //第二部，调用了flip方法
        ByteBuffer buffer=ByteBuffer.wrap("200".getBytes());
    }

    /**
     * clear方法，不是真正的清楚目的
     * 会把position的值设置为0，limit位置设置为capacity位置，原来缓冲区的数据不清楚
     * 所以，要真正的clear，需要和flip方法配合使用
     * flip() 把limit的当前position，position=0
     */
    @Test
    public void testClear(){
        ByteBuffer buffer=ByteBuffer.wrap("nishiwode".getBytes());
        //把position=0
        buffer.clear();
        buffer.put("nishi".getBytes());
        buffer.put("ha".getBytes());
        //limit=position,position=0
        buffer.flip();
        System.out.println(buffer.get());
        System.out.println(buffer.get());
    }
}
