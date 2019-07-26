package Chat1;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<ChatMessage> {
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    static final AttributeKey<String> nickAttr = AttributeKey.newInstance("nickname");
    private static final NicknameProvider nicknameProvider = new NicknameProvider();

    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // Tricky: �̹� channel�� active�� ��Ȳ����
        // �������� �� �ڵ鷯�� ��ϵ� ������ channelActive�� �Ҹ����ʽ��ϴ�.
        // [�ǽ�4-2]�� ���ؼ� ���⼭�� helo�� �θ��ϴ�.

        if (ctx.channel().isActive()) {
            helo(ctx.channel());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // Ŭ���̾�Ʈ�� ����Ǹ� ����
        helo(ctx.channel());
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());
        channels.writeAndFlush(M("LEFT", nickname(ctx)));
        nicknameProvider.release(nickname(ctx));
    }

    private void helo(Channel ch) {
        if (nickname(ch) != null) return; // already done;
        String nick = nicknameProvider.reserve();
        if (nick == null) {
            ch.writeAndFlush(M("ERR", "sorry, no more names for you"))
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            bindNickname(ch, nick);
            channels.forEach(c -> ch.write(M("HAVE", nickname(c))));
            channels.writeAndFlush(M("JOIN", nick));
            channels.add(ch);
            ch.writeAndFlush(M("HELO", nick));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
        t.printStackTrace();
        if (!ctx.channel().isActive()) {
            ctx.writeAndFlush(M("ERR", null, t.getMessage()))
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        if ("PING".equals(msg.command)) {
            // TODO: [�ǽ�3-1] PING ��ɾ ���� ������ �������ϴ�

        } else if ("QUIT".equals(msg.command)) {
            // TODO: [�ǽ�3-2] QUIT ��ɾ ó���ϰ� BYE�� �����մϴ�. ���ᵵ �����ϴ�.

        } else if ("SEND".equals(msg.command)) {
            // TODO: [�ǽ�3-3] Ŭ���̾�Ʈ�κ��� ��ȭ �ؽ�Ʈ�� �Խ��ϴ�. ��� ä�ο� FROM �޽����� ����մϴ�.

        } else if ("NICK".equals(msg.command)) {
            changeNickname(ctx, msg);
        } else {
            ctx.write(M("ERR", null, "unknown command -> " + msg.command));
        }
    }

    private void changeNickname(ChannelHandlerContext ctx, ChatMessage msg) {
        String newNick = msg.text.replace(" ", "_").replace(":", "-");
        String prev = nickname(ctx);
        if (!newNick.equals(prev) && nicknameProvider.available(newNick)) {
            nicknameProvider.release(prev).reserve(newNick);
            bindNickname(ctx.channel(), newNick);
            channels.writeAndFlush(M("NICK", prev, newNick));
        } else {
            ctx.write(M("ERR", null, "couldn't change"));
        }
    }

    // ChatMessage ��ü�� ����� ��ƿ��Ƽ �޼ҵ� �Դϴ�.
    private ChatMessage M(String... args) {
        switch (args.length) {
            case 1:
                return new ChatMessage(args[0]);
            case 2:
                return new ChatMessage(args[0], args[1]);
            case 3:
                ChatMessage m = new ChatMessage(args[0], args[1]);
                m.text = args[2];
                return m;
            default:
                throw new IllegalArgumentException();
        }
    }

    // ä�ο� ��ȭ���� �����մϴ�.
    private void bindNickname(Channel c, String nickname) {
        c.attr(nickAttr).set(nickname);
    }

    // ä�ο� ������ ��ȭ���� �����ɴϴ�.
    private String nickname(Channel c) {
        return c.attr(nickAttr).get();
    }

    // nickname(Channel)�� ������ ���Ǹ� ���� �޼ҵ��Դϴ�.
    private String nickname(ChannelHandlerContext ctx) {
        return nickname(ctx.channel());
    }
}