package com.pairliu.learning.nio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {
    private final SocketChannel socket;
    final SelectionKey sk;    
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0;
    static final int SENDING = 1;
    
    int state = READING;
    
    public Handler(Selector sel, SocketChannel c) throws IOException {
        socket = c;
        c.configureBlocking(false);
        //Optionally try first read now
        sk = socket.register(sel, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        sel.wakeup();
    }

    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            } 
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }   
    }
    
    boolean inputIsComplete() {return false;}
    boolean outputIsComplete() {return false;}
    void process() {}
    
    void read() throws IOException {
        socket.read(input);
        if (inputIsComplete()) {
            process();
            state = SENDING;
            //Normally also do first write now
            sk.interestOps(SelectionKey.OP_WRITE);
        }
        
    }
    
    void send() throws IOException {
        socket.write(output);
        if (outputIsComplete()) sk.cancel();
    }
    

}
