package com.pairliu.learning.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NBTestPractice {
    public static void main( String[] args ) throws Exception {
        Selector s = Selector.open();
        ByteBuffer buffer = ByteBuffer.allocate( 1024 );
        
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind( new InetSocketAddress( 8080 ));
        
        //1st thing needed to remember
        ssc.configureBlocking( false );
        
        //2nd thing needed to remember
        ssc.register( s, SelectionKey.OP_ACCEPT );
        
        //So the double loop is inevitable
        while ( true ) {
            s.select(); //This call is a block call
            
            Set<SelectionKey> keys = s.selectedKeys(); //It is better to directly return the iterator
            Iterator<SelectionKey> it = keys.iterator();
            
            while ( it.hasNext() ) {
                SelectionKey key = it.next();
                
                //Or can use other methods like isAcceptable(). Those are better...
                if ( ( key.readyOps() & SelectionKey.OP_ACCEPT ) == SelectionKey.OP_ACCEPT ) {
                    
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();

                    SocketChannel acceptedChannel = channel.accept();
                    //5th thing need to remember
                    acceptedChannel.configureBlocking( false );
                    acceptedChannel.register( s, SelectionKey.OP_READ );
                    
                    //3rd thing needed to remember
                    it.remove();
                    
                } else if ( ( key.readyOps() & SelectionKey.OP_READ ) == SelectionKey.OP_READ ) {                    
                    SocketChannel channel = (SocketChannel)key.channel();
                    
                    buffer.clear();
                    channel.read( buffer );
                    
                    //4th thing to remember
                    buffer.flip();
                    channel.write( buffer );
                    
                    //again
                    it.remove();
                }
            }
        }
    }
}
