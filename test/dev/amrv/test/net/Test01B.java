package dev.amrv.test.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class Test01B {

    private static final int LOCAL_PORT = 42069;
    private static final int CONNECTION_QUEUE = 5;
    private static final String LOCAL_HOST = "192.168.1.46";
    private static final boolean LOCAL_HOST_AUTO = true;
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) throws UnknownHostException,
            IOException {

        final InetAddress localAddress = LOCAL_HOST_AUTO ? InetAddress
                .getLocalHost() : InetAddress.getByName(LOCAL_HOST);
        ServerSocket server = new ServerSocket(LOCAL_PORT, CONNECTION_QUEUE, localAddress);
        System.err.println("LOCAL:  " + server.getLocalSocketAddress().toString() + " : " + server.getLocalPort());
        System.err.println("REMOTE: " + server.getInetAddress().toString() + " : " + "?????");
        System.out.println(server);
        Thread acceptListener = new Thread(() -> {
            for (;;) {
                try {
                    final Socket client = server.accept();
                    System.err.println("Client connected " + client.toString());
                    ClientListener clientListener = new ClientListener(client);
                    clientListener.start();
                    client.getOutputStream().write("Conectado al servidor".getBytes());
                    clients.add(client);
                } catch (IOException ex) {
                    Logger.getLogger(Test01B.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
        });
        acceptListener.start();
        Charset charset = Charset.forName("UTF-8");
        String text = "";
        
        do {
            text = scanner.nextLine();
            
            for (Socket client : clients) {
                client.getOutputStream().write(text.getBytes(charset));
            }
            
        } while (!text.equalsIgnoreCase("exit"));
    }

    private static class ClientListener extends Thread {

        private final Socket socket;

        ClientListener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(socket
                        .getInputStream());

                for (;;) {
                    if (dis.available() > 0)
                    System.out.println(dis.readUTF());
                }
            } catch (IOException ex) {
                Logger.getLogger(Test01.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

}
