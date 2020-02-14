// 네티와 RxNetty를 사용한 논블로킹 HTTP 서버

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

class HttpTcpNettyServer {

    public static void main(String[] args) throws Exception {
	EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	EventLoopGroup workerGroup = new NioEventLoopGroup();

	try {
	    new ServerBootstrap()
		    .option(ChannelOption.SO_BACKLOG, 50_000)
		    .group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)
		    .childHandler(new HttpInitializer())
		    .bind(8080)
		    .sync()
		    .channel()
		    .closeFuture()
	  	    .sync();
	} finally {
	    bossGroup.shutdownGraccefully();
	    workerGroup.shutdownGracefully();
	}
    }
}


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

class HttpInitializer extends ChannelInitializer<SocketChannel> {

    private final HttpHandler httpHandler = new HttpHandler();

    @Override
    public void initChannel(SocketChannel ch) {
	ch
	        .pipeline()
	        .addLast(new HttpServerCodec())
 	        .addLast(httpHandler);
    }
}


import io.netty.channel.*;
import io.netty.handler.codec.http.*;

@Sharable
class HttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
	ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
	if (msg instanceof HttpRequest) {
	    sendResponse(ctx);
	}
    }

    private void sendResponse(ChannelHandlerContext ctx) {
	final DefualtFullHttpResponse response =
	    new DefualtFullHttpResponse(
		HTTP_1_1,
		HttpResponseStatus.OK,
		Unpooled.wrappedBuffer("OK".getBytes(UTF_8)));
	response.headers().add("Content-length", 2);
	ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	log.error("Error", cause);
	ctx.close();
    }
}


ctx
    .writeAndFlush(response)
    .addListener(ChannelFutureListener.CLOSE);
