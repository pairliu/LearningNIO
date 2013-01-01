package com.pairliu.learning.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class TimeClient {
    public static void main(String[] args) {
        ChannelFactory factory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(
                        new TimeDecoder(), 
                        new TimeClientHandler() );
            }
        });

        //Looks not good with hard-coded String
        //But if the options are very flexible, then it is ok.
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        ChannelFuture future = bootstrap.connect(new InetSocketAddress( 8080 ));
        future.awaitUninterruptibly();
        if ( !future.isSuccess() ) {
            future.getCause().printStackTrace();
        }
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        factory.releaseExternalResources();
    }
}
