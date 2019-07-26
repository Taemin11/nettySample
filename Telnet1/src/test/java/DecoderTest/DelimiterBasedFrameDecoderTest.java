package DecoderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;

import java.nio.charset.Charset;

import org.junit.Test;

public class DelimiterBasedFrameDecoderTest {
    @Test
    public void testDecoder() {
        String writeData = "æ»≥Á«œººø‰\r\nπ›∞©Ω¿¥œ¥Ÿ\r\n";
        String firstResponse = "æ»≥Á«œººø‰\r\n";
        String secondResponse = "π›∞©Ω¿¥œ¥Ÿ\r\n";

        DelimiterBasedFrameDecoder decoder = new DelimiterBasedFrameDecoder(8192, 
                false, Delimiters.lineDelimiter());
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(decoder);

        ByteBuf request = Unpooled.wrappedBuffer(writeData.getBytes());
        boolean result = embeddedChannel.writeInbound(request);
        assertTrue(result);

        ByteBuf response = null;

        response = (ByteBuf) embeddedChannel.readInbound();
        assertEquals(firstResponse, response.toString(Charset.defaultCharset()));

        response = (ByteBuf) embeddedChannel.readInbound();
        assertEquals(secondResponse, response.toString(Charset.defaultCharset()));

        embeddedChannel.finish();
    }
}