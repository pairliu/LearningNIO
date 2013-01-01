// $Id$

package com.pairliu.learning.nio;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class CopyFile {
    static public void main(String args[]) throws Exception {
        long begin = System.currentTimeMillis();
        
        if (args.length < 2) {
            System.err.println("Usage: java CopyFile infile outfile");
            System.exit(1);
        }

        String infile = args[0];
        String outfile = args[1];

        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();

            //no need to record how many bytes have been read, 
            //because buffer knows its own capacity            
            int r = fcin.read(buffer);

            if (r == -1) {
                break;
            }

            buffer.flip();

            fcout.write(buffer);
        }
        
        System.out.println( "Operation time: " + ((System.currentTimeMillis() - begin) ) );
    }
}
