package BufferTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class OrderedByteBufferTest {
	final String source = "Hello world";
	
	@Test
	public void pooledHeapBufferTest() {
		ByteBuf buf = Unpooled.buffer();
		assertEquals(ByteOrder.BIG_ENDIAN, buf.order());
		
		buf.writeShort(1);
		
		buf.markReaderIndex();
		assertEquals(1, buf.readShort());
		
		buf.resetReaderIndex();
		
		ByteBuf lettleEndianBuf = buf.order(ByteOrder.LITTLE_ENDIAN);
		//order 메서드는 새로운 바이트 버퍼를 생성하는 것이 아니라 
		//주어진 바이트 버퍼의 내용을 공유하는 파생 바이트 버퍼 객체를 생성하므로 유의
		assertEquals(256, lettleEndianBuf.readShort());
	}
	
	@Test
	public void convertNettyBufferToJavaBuffer() {
		ByteBuf buf = Unpooled.buffer(11);
		
		buf.writeBytes(source.getBytes());
		assertEquals(source, buf.toString(Charset.defaultCharset()));
		
		ByteBuffer nioByteBuffer = buf.nioBuffer();
		assertNotNull(nioByteBuffer);
		assertEquals(source, new String(nioByteBuffer.array(),
				nioByteBuffer.arrayOffset(), nioByteBuffer.remaining()));
	}
	
	@Test
	public void convertJavaBufferToNettyBuffer() {
		ByteBuffer byteBuffer = ByteBuffer.wrap(source.getBytes());
		ByteBuf nettyBuffer = Unpooled.wrappedBuffer(byteBuffer);
		
		assertEquals(source, nettyBuffer.toString(Charset.defaultCharset()));
	}

}
