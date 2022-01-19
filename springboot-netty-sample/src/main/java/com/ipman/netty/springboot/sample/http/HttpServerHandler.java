package com.ipman.netty.springboot.sample.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;


/**
 * Created by ipipman on 2022/1/19.
 *
 * @version V1.0
 * @Package com.ipman.netty.springboot.sample.http
 * @Description: (用一句话描述该文件做什么)
 * @date 2022/1/19 9:50 下午
 */
@Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final byte[] CONTEXT = "hello coding".getBytes(StandardCharsets.UTF_8);

    /**
     * 当读取完毕时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK, Unpooled.wrappedBuffer(CONTEXT));
            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .set(CONTENT_LENGTH, response.content().readableBytes());

            ChannelFuture f = ctx.write(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
