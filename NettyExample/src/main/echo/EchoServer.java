package echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//1
        EventLoopGroup workerGroup = new NioEventLoopGroup();//2
        try {
            ServerBootstrap b = new ServerBootstrap();//3
            b.group(bossGroup, workerGroup)//4
             .channel(NioServerSocketChannel.class)//5
             .childHandler(new ChannelInitializer<SocketChannel>() {//6
                @Override
                public void initChannel(SocketChannel ch) {//7
                    ChannelPipeline p = ch.pipeline();//8
                    p.addLast(new EchoServerHandler());//9
                }
            });

            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}