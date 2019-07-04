package BufferTest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class UnsignedByteBufferTest {
	final String source = "hello world";
	
	@Test
	public void unsignedBufferTojavaBuffer() {
		ByteBuf buf = Unpooled.buffer(11);
	
		buf.writeShort(-1);
		assertEquals(65535, buf.getUnsignedShort(0));
		assertEquals(2, buf.readableBytes());
		buf.writeShort(-1);
		assertEquals(7, buf.writableBytes());
	}

}
