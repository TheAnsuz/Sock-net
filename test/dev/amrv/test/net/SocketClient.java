package dev.amrv.test.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class SocketClient {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //System.out.print("Port: ");
        //int port = Integer.parseInt(scanner.nextLine());
        String address = "83.58.147.118";
        String fallback = "localhost";
        int port = 42069;
        //System.out.println("Address: ");
        //String address = scanner.nextLine();

        System.out.println("Trying connection...");
        Socket socket = null;
        try {
            socket = new Socket(address, port);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Retrying...");
            socket = new Socket(fallback, port);
        }
        ObjectOutputStream ous = null;

//        socket.close();
//        socket = new Socket();
//        socket.connect(new InetSocketAddress("192.168.1.46", port));
        final String binding = socket.getLocalAddress().toString() + ":" + socket
                .getLocalPort();
        final String connection = socket.getInetAddress().toString() + ":" + socket
                .getPort();

        System.out.println("Client " + binding + " connected to " + connection);

        String text = "";

        do {
            text = scanner.nextLine();
            socket.getOutputStream().write(text.getBytes());
        } while (!text.equalsIgnoreCase("exit"));
    }

}
