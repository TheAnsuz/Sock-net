package dev.amrv.test.net;

import com.sun.security.sasl.ClientFactoryImpl;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class Test01 {

    private static final int LOCAL_PORT = 42070;
    private static final String LOCAL_HOST = "192.168.1.46";
    private static final int LOCAL_HOST_MODE = 2; // 0 auto, 1 manual, 2 search
    private static final int REMOTE_PORT = 42069;
    private static final String REMOTE_HOST = "83.58.147.118";

    private static final String LOCAL_STRING = "LOCAL:  %0 : %1";
    private static final String REMOTE_STRING = "REMOTE: %0 : %1";

    public static void main(String[] args) throws UnknownHostException,
            IOException {

        InetAddress localAddress = InetAddress.getLocalHost();

        final InetAddress remoteAddress = InetAddress.getByName(REMOTE_HOST);
        Socket client = null;

        lookahead:
        if (LOCAL_HOST_MODE == 2) {
            InetAddress[] addresses = findLocalAddresses();
            System.out.println("Found " + addresses.length + " addresses");
            for (int i = addresses.length - 1; i >= 0; i--) {
                InetAddress address = addresses[i];
                try {
                    System.out.println("Trying: " + address.toString());
                    client = new Socket(remoteAddress, REMOTE_PORT, address, LOCAL_PORT);
                    break lookahead;
                } catch (SocketException se) {

                }
            }
            throw new SocketException("No local address can reach the destination");

        } else if (LOCAL_HOST_MODE == 1) {
            System.out.println("Trying default localhost");
            client = new Socket(remoteAddress, REMOTE_PORT, localAddress, LOCAL_PORT);

        } else if (LOCAL_HOST_MODE == 0) {
            System.out.println("Using automatic asignment");
            client = new Socket(remoteAddress, REMOTE_PORT);
        }

        System.err.println(LOCAL_STRING.replaceAll("%0", client
                .getLocalAddress().toString())
                .replaceAll("%1", client.getLocalPort() + ""));

        System.err.println(REMOTE_STRING.replaceAll("%0", client
                .getInetAddress().toString())
                .replaceAll("%1", client.getPort() + ""));
        System.err.println(client.toString());
        ClientListener listener = new ClientListener(client);
        listener.start();
    }

    private static final InetAddress[] findLocalAddresses() throws
            SocketException {
        final List<InetAddress> addresses = new ArrayList<>();

        for (Enumeration<NetworkInterface> en = NetworkInterface
                .getNetworkInterfaces(); en.hasMoreElements();) {
            NetworkInterface network = en.nextElement();

            for (Enumeration<InetAddress> ipAddress = network.getInetAddresses(); ipAddress
                    .hasMoreElements();) {
                addresses.add(ipAddress.nextElement());
            }
        }
        return addresses.toArray(new InetAddress[0]);
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
                    System.out.println(dis.readUTF());
                }
            } catch (IOException ex) {
                Logger.getLogger(Test01.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
