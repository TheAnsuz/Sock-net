package dev.amrv.net;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public interface ClientConnectionEvent {
    
    public void beforeConnect();
    
    public void afterConnect();
    
    public void failConnect();
    
    public void onDisconnect();
    
}
