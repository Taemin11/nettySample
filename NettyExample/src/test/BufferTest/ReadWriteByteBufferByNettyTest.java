package BufferTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class ReadWriteByteBufferByNettyTest {
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
		
		buf.writeInt(65537);
		assertEquals(4, buf.readableBytes());
		assertEquals(7, buf.writableBytes());
		
		assertEquals(1, buf.readShort()); // 앞 두 바이트를 읽고 1인지 검사
		assertEquals(2, buf.readableBytes());
		assertEquals(7, buf.writableBytes());
		
		assertEquals(true, buf.isReadable());
		
		buf.clear();
		
		assertEquals(0, buf.readableBytes());
		assertEquals(11, buf.writableBytes());
	}
}
