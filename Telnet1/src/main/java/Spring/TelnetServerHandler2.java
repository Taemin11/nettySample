package Spring;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;

@Sharable
public class TelnetServerHandler2 extends SimpleChannelInboundHandler<String> {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
		ctx.write("ȯ���մϴ�2. "
					+ InetAddress.getLocalHost().getHostName() + "�� �����ϼ̽��ϴ�!\r\n");
		ctx.write("���� �ð��� " + new Date() + " �Դϴ�2.\r\n");
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request)
			throws Exception {
		String response;
		boolean close = false;

		if (request.isEmpty()) {
			response = "����� �Է��� �ּ���2.\r\n";
		}
		else if ("bye".equals(request.toLowerCase())) {
			response = "���� �Ϸ� �Ǽ���2!\r\n";
			close = true;
		}
		else {
			response = "�Է��Ͻ� ����� '" + request + "' �Դϱ�2?\r\n";
		}

		ChannelFuture future = ctx.write(response);

		if (close) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}