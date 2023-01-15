package dev.amrv.test.net.address;

import dev.amrv.net.Address;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class ValidateIP2 {

    public static void main(String[] args) throws UnknownHostException,
            IOException {
        Address address = new Address();
        address.setName("fe80::3b76:536f:aca9:e5e7%14");
        address.setPort(52342);
        //address.setIpv4Silently("34.0.0.23.5");
        //address.setIpv6("fe80::3b76:536f:aca9:e5e7%14");
//
//        for (int c = 40; c < 100; c++) {
//            System.out.println(c + " >> " + (char) c);
//        }

        System.out.println(InetAddress.getByName("fe80::3b76:536f:aca9:e5e7%24").isReachable(10));

        System.out.println("null name: " + InetAddress.getByName(null));
        System.out.println("localhost: " + InetAddress.getLocalHost().toString());
        System.out.print(address + " >> ");
        
        System.out.println(address.build().isReachable(10));

    }

}
