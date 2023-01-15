package dev.amrv.test.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class ClientListener {

    private static final String LOCAL_STRING = "LOCAL:  %0 : %1";
    private static final String REMOTE_STRING = "REMOTE: %0 : %1";
    
    private enum Connection {
        DISCORD("35.186.224.47", 443),
        OPERA("173.194.76.188", 5228),
        ;
        
        public final String ip;
        public final int port;
        
        Connection(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }
        
    }
    
    public static void main(String[] args) throws IOException {

        Connection connection = Connection.OPERA;
        
        System.out.print("Starting...");
        InetAddress address = InetAddress.getByAddress("localmachine", new byte[] {(byte)83,(byte)58,(byte)147,(byte)118});
        Socket socket = new Socket(address, 42069);
//Socket socket = new Socket(connection.ip, connection.port);
        System.out.println("DONE");
        System.err.println(LOCAL_STRING.replaceAll("%0", socket
                .getLocalAddress().toString())
                .replaceAll("%1", socket.getLocalPort() + ""));

        System.err.println(REMOTE_STRING.replaceAll("%0", socket
                .getInetAddress().toString())
                .replaceAll("%1", socket.getPort() + ""));
        System.err.println(socket.toString());
        InputStream stream = socket.getInputStream();
        byte[] buffer = new byte[socket.getReceiveBufferSize()];
        int read = 0;
        for (;;) {

            System.out.print("Reading (" + stream.available() + ") ");
            read = stream.read(buffer);
            System.out.println(read + " bytes");
            if (read > 0) {
                System.out.println(new String(buffer, 0, read, Charset.forName("UTF-8")));
            }

        }
    }

}
