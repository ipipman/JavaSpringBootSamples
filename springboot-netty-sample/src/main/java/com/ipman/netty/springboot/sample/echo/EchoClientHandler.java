package com.ipman.netty.springboot.sample.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Created by ipipman on 2022/1/19.
 *
 * @version V1.0
 * @Package com.ipman.netty.springboot.sample.echo
 * @Description: (用一句话描述该文件做什么)
 * @date 2022/1/19 9:26 下午
 */
@Sharable
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMsg;

    public EchoClientHandler() {
        firstMsg = Unpooled.wrappedBuffer("Hello Codeing".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 通道活跃时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 第一次传输
        ctx.writeAndFlush(firstMsg);
    }

    /**
     * 读取数据时
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);
    }

    /**
     * 读取完毕时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 延迟3秒
        TimeUnit.SECONDS.sleep(3);
        ctx.flush();
    }

    /**
     * 捕获到异常时
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
