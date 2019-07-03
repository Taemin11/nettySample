package BufferTest;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

import io.netty.buffer.*;

import org.junit.Test;

public class CreateByteBufferNettyTest {

	@Test
	public void createUnpooledHeapBufferTest() {
		ByteBuf buf = Unpooled.buffer(11);
		
		testBuffer(buf, false);
	}

	@Test
	public void createUnpooledDirectBufferTest() {
		ByteBuf buf = Unpooled.directBuffer(11);
		
		testBuffer(buf, true);
	}
	
	@Test
	public void createpooledHeapBufferTest() {
		ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
		
		testBuffer(buf, false);
	}
	
	@Test
	public void createpooledDirectBufferTest() {
		ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);
		
		testBuffer(buf, true);
	}
	
	
	
	private void testBuffer(ByteBuf buf, boolean isDirect) {
		assertEquals(11, buf.capacity());
		
		assertEquals(isDirect, buf.isDirect());
		
		assertEquals(0, buf.readableBytes());
		assertEquals(11, buf.writableBytes());
	}
}
