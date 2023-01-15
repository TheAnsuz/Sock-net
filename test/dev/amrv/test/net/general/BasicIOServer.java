package dev.amrv.test.net.general;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class BasicIOServer {

    private static final int LOCAL_PORT = 42069;
    private static final int CONNECTION_QUEUE = 5;
    private static final String LOCAL_HOST = "192.168.1.46";

    private static final Scanner SCANNER = new Scanner(System.in);
    private static boolean running = false;
    private static ServerSocket server;

    public static void main(String[] args) throws UnknownHostException,
            IOException {

        System.out.println("localhost " + InetAddress.getLocalHost().toString());
        System.out.println("loopback  " + InetAddress.getLoopbackAddress()
                .toString());

        boolean validHost = false;
        boolean validPort = checkPort(LOCAL_PORT);
        try {
            validHost = InetAddress.getByName(LOCAL_HOST).isReachable(10);
        } catch (UnknownHostException uke) {
            validHost = false;
        }
        if (validHost)
            System.out.println("Server ip " + LOCAL_HOST + " will be used");
        else
            System.err.println("Server addressing failed, using default anyAddress");

        if (validPort)
            System.out.println("Server port " + LOCAL_PORT + "will be used");
        else
            System.out.println("Server port binding failed, using default port");

        server = new ServerSocket(validPort ? LOCAL_PORT : 0, CONNECTION_QUEUE, validHost ? InetAddress
                .getByName(LOCAL_HOST) : InetAddress.getLocalHost());
        System.out.println("Connected");
        System.out.println("BIND:  " + server.getLocalSocketAddress().toString() + " : " + server.getLocalPort());
        System.out.println("EXTERNAL: " + server.getInetAddress().toString() + " : " + server.getLocalPort());
        System.out.println(server.toString());
        ServerSender sender = new ServerSender();
        sender.start();

        server.setReceiveBufferSize(5);
        
        running = true;
        System.out.println("Running");
        while (running) {
            final Socket socket = server.accept();
            System.out.println("Client connected");
            sender.addCaptor(socket);
            new ServerReciver(socket).start();
        }

    }

    public static class ServerReciver extends Thread {

        private static int INTERNAL_ID = 0;
        private final static int MAX_TRIES = 1;
        private final Socket socket;
        private final int clientIndex;
        private int tries;

        private ServerReciver(Socket socket) {
            this.socket = socket;
            clientIndex = INTERNAL_ID++;
        }

        @Override
        public void run() {
            byte[] buffer;
            InputStream stream;
            try {
                buffer = new byte[socket.getReceiveBufferSize()];
                stream = socket.getInputStream();
            } catch (IOException ex) {
                System.err
                        .println("Unable create read-connetion with client " + clientIndex);
                return;
            }
            System.out.println("Listening to client " + clientIndex);
            while (true) {
                try {
                    int read = stream.read(buffer);
                    System.out.println(clientIndex + " << " + new String(buffer, 0, read));
                    tries = 0;
                } catch (IOException ex) {
                    System.err
                            .println("Unable to read client data from " + clientIndex + " ( " + (MAX_TRIES - tries) + " tries before shutdown ) " + ex.getMessage());
                    tries++;
                    if (tries >= MAX_TRIES)
                        break;
                }
            }
            System.out.println("Finished listening to " + clientIndex);
        }

    }

    public static class ServerSender extends Thread {

        private final List<Socket> captors = new ArrayList<>();

        public synchronized void addCaptor(Socket socket) {
            captors.add(socket);
        }

        @Override
        public void run() {

            while (true) {
                String text = SCANNER.nextLine();
                if (text.equalsIgnoreCase("check")) {
                    System.out.println("LOCAL:  " + server.getLocalSocketAddress().toString() + " : " + server.getLocalPort());
                    System.out.println("REMOTE: " + server.getInetAddress().toString() + " : " + "?????");
                    System.out.println(server);
                }
                for (int i = 0; i < captors.size(); i++) {
                    Socket client = captors.get(i);
                    if (client == null)
                        continue;
                    try {
                        if (text.equalsIgnoreCase("check")) {
                            System.out.println("Client " + i + System.lineSeparator()
                                    + "machine " + client.getLocalSocketAddress().toString() + " : " + client.getLocalPort() + System.lineSeparator()
                                    + "connected " + client.getRemoteSocketAddress().toString() + " : " + client.getPort());
                            continue;
                        }
                        captors.get(i).getOutputStream().write(text.getBytes());
                        System.out.println(i + " >> " + text);
                    } catch (IOException ex) {
                        captors.set(i, null);
                    }
                }

            }

        }

    }

    public static boolean checkPort(int port) {
        if (port < 0 || port > 65535)
            return false;
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
}
