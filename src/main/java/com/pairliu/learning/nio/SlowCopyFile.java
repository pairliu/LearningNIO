package com.pairliu.learning.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SlowCopyFile {
    public static void main( String[] args ) throws Exception {
        byte[] bytes = new byte[1024];
        
        long begin = System.currentTimeMillis();

        if (args.length < 2) {
            System.err.println("Usage: java FastCopyFile infile outfile");
            System.exit(1);
        }

        String infile = args[0];
        String outfile = args[1];

        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);
        
        while ( true ) {
            int read = fin.read( bytes );
            if ( read == -1 ) break;
            
            fout.write( bytes, 0, read );
        }
        fin.close();
        
        fout.close();
        
        System.out.println( "Operation time: " + ((System.currentTimeMillis() - begin) ) );
    }
    

}
