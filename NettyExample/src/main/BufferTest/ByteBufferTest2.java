package BufferTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;


public class ByteBufferTest2 {
    public static void main(String[] args) {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("초기 상태 : " + firstBuffer);


        //2
        /*
        byte[] source = "Hello world!".getBytes();

        for (byte item : source) {
            firstBuffer.put(item);
            System.out.println("현재 상태 : " + firstBuffer);
        }
        */
        //3
        /*
        firstBuffer.put((byte)1);
        System.out.println(firstBuffer.get());
        System.out.println(firstBuffer);
        */
        //4
        firstBuffer.put((byte) 1);
        firstBuffer.put((byte) 2);
        assertEquals(2, firstBuffer.position());
        
        firstBuffer.rewind();
        assertEquals(0, firstBuffer.position());
        
        assertEquals(1, firstBuffer.get());
        assertEquals(1, firstBuffer.position());
        
        System.out.println(firstBuffer);
    }
}