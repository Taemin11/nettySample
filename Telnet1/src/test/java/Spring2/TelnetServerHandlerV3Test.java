package Spring2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.channel.embedded.EmbeddedChannel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.junit.Test;

public class TelnetServerHandlerV3Test {
    @Test
    public void testConnect() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("ȯ���մϴ�. ")
                    .append(InetAddress.getLocalHost().getHostName())
                    .append("�� �����ϼ̽��ϴ�!\r\n")
                    .append("���� �ð��� ")
                    .append(new Date().toString()).append(" �Դϴ�.\r\n");
        }
        catch (UnknownHostException e) {
            fail();
            e.printStackTrace();
        }

        EmbeddedChannel embeddedChannel = 
                new EmbeddedChannel(new TelnetServerHandlerV3());

        String expected = (String) embeddedChannel.readOutbound();
        assertNotNull(expected);

        assertEquals(builder.toString(), (String) expected);

        String request = "hello";
        expected = "�Է��Ͻ� ����� '" + request + "' �Դϱ�?\r\n";

        embeddedChannel.writeInbound(request);

        String response = (String) embeddedChannel.readOutbound();
        assertEquals(expected, response);

        embeddedChannel.finish();
    }
}