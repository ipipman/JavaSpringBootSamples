package com.ipman.netty.springboot.sample.echo;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by ipipman on 2022/1/19.
 *
 * @version V1.0
 * @Package com.ipman.netty.springboot.sample.http
 * @Description: (用一句话描述该文件做什么)
 * @date 2022/1/19 9:11 下午
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读取
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
        ctx.flush();
    }

    /**
     * 抓住异常
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
