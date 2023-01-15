package dev.amrv.net;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class Client {

    private final List<ClientConnectionEvent> connectionEvents = new ArrayList<>();
    
    private Socket socket;

    public boolean isConnected() {
        return socket != null;
    }

    public void connect() {
    }

    public void disconnect() {

    }

}
