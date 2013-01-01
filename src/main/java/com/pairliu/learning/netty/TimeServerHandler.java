package com.pairliu.learning.netty;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

//"Time" protocol is to send back a time information when connected 
//and then close the connection immediately.
public class TimeServerHandler extends SimpleChannelHandler {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        TimeServer.allChannels.add( e.getChannel() );
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        Channel c = e.getChannel();
        UnixTime time = new UnixTime( (int)(System.currentTimeMillis() / 1000L + 2208988800L) );
        
        ChannelFuture f = c.write(time);

        f.addListener( ChannelFutureListener.CLOSE );
    }

}
