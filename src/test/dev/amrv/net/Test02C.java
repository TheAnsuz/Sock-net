package dev.amrv.test.net;

import java.io.IOException;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class Test02C {

    private static final int MAX_PINGS = 10;

    public static void main(String[] args) throws IOException,
            InterruptedException {
        TestClient client = new TestClient();
        
        client.setLocalHostname("DESKTOP");
        client.setLocalPort(200);
        client.setLocalHostip(192, 168, 1, 46);

        client.setRemotePort(42069);
        //client.setRemoteHostname("83.58.147.118");
        client.setRemoteHostip(83, 58, 147, 118);

        client.setOutputBufferSize(100);
        client.connect();
        client.getSocket().setTcpNoDelay(false); // Sends the socket send queue ASAP
        client.getSocket().setKeepAlive(false); // Checks if the connection is alive
        client.getSocket().setOOBInline(false); // Allows/Disallows the recipt of urgent data
        client.getSocket().setReuseAddress(true); // The socket will be marked as closed on the net as soon as the application holding it releases its resources
        client.getSocket().setSoLinger(true, 2); // When the socket closes if will wait X seconds to try to send the queued data to the reciever
        client.getSocket().setPerformancePreferences(-10, -10, 0); // (NOT IMPLEMENTED) Connection, latency, bandwidth
        client.getSocket().setSoTimeout(100); // Miliseconds to wait on a read() before throw exception
        
        System.out.println("LOCAL:  " + client.getSocket().getLocalAddress() + " : " + client.getSocket().getLocalPort());
        System.out.println("REMOTE: " + client.getSocket().getInetAddress()+ " : " + client.getSocket().getPort());
        
        for (int i = 1; i < MAX_PINGS; i++) {
            client.send(("Ping " + i + "/" + MAX_PINGS).getBytes());
            //client.getSocket().setTcpNoDelay(!client.getSocket().getTcpNoDelay());
        }
        //Thread.sleep(1000);
    }
}
