package dev.amrv.test.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class SocketServer {

    private static final int SERVER_PORT = 42059;
    private static final int SERVER_MAX_CONNECTION_QUEUE = 50;
    private static final byte[] SERVER_BIND_IP = new byte[]{(byte) 192,
        (byte) 168, (byte) 1, (byte) 46};

    private static final List<Socket> listeners = new ArrayList();

    public static void main(String[] args) throws IOException {
        //ServerSocket server = new ServerSocket(SERVER_PORT, SERVER_MAX_CONNECTION_QUEUE, Inet4Address
        //        .getByAddress(SERVER_BIND_IP));
        
        ServerSocket server= new ServerSocket(SERVER_PORT);
        
        System.out.println("Server " + server.toString());

        Thread serverAccepting = new Thread(() -> {
            while (server.isBound()) {
                System.out.println("awaiting...");
                try {
                    final Socket socket = server.accept();
                    listeners.add(socket);
                    Thread lectura = new Thread(() -> {
                        try {
                            DataInputStream input = new DataInputStream(socket.getInputStream());
                            
                            while (true) {
                                System.out.println(input.readUTF());
                            }
                            
                        } catch (IOException ex) {
                            Logger.getLogger(SocketServer.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    });
                    lectura.start();
                    System.out.println("Client connected");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        serverAccepting.start();
    }

}
