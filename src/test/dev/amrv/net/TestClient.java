package dev.amrv.test.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class TestClient {

    private String localHostname;
    private final byte[] localHostipv4 = new byte[4];
    private int localport;

    private String remoteHostname;
    private final byte[] remoteHostipv4 = new byte[4];
    private int remotePort;

    private int configOutputBufferSize = 4096;
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setLocalHostname(String hostname) {
        this.localHostname = hostname;
    }

    public void setOutputBufferSize(int bytes) {
        configOutputBufferSize = bytes;
    }

    public void setLocalHostip(int... ip) {
        if (ip == null || ip.length < 4)
            return;

        for (int i = 0; i < 4; i++) {
            localHostipv4[i] = (byte) ip[i];
        }
    }

    public void setLocalPort(int port) {
        if (port < 0x0 || port > 0xFFFF)
            return;

        this.localport = port;
    }

    public void setRemoteHostname(String hostname) {
        this.remoteHostname = hostname;
    }

    public void setRemoteHostip(int... ip) {
        if (ip == null || ip.length < 4)
            return;

        for (int i = 0; i < 4; i++) {
            remoteHostipv4[i] = (byte) ip[i];
        }
    }

    public void setRemotePort(int port) {
        if (port < 0x0 || port > 0xFFFF)
            return;

        this.remotePort = port;
    }

    public void connect() throws UnknownHostException, IOException {
        // Try manually
        // Try automatic
        InetAddress localAddress = null;

        localAddress = InetAddress.getByAddress(localHostname, localHostipv4);
        InetAddress remoteAddress = InetAddress
                .getByAddress(remoteHostname, remoteHostipv4);
        socket = new Socket(remoteAddress, remotePort, localAddress, localport);
        socket.setSendBufferSize(configOutputBufferSize);
    }

    public void disconnect() throws IOException {
        socket.close();
        socket = null;
    }

    public void send(byte[] data) throws IOException {
        send(data, 0, data.length);
    }

    public void send(byte[] data, int offset, int length) throws IOException {
        socket.getOutputStream().write(data, offset, length);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Closing client");
        this.disconnect();
    }

}
