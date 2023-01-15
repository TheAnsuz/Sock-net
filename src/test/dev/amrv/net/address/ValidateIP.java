package dev.amrv.test.net.address;

import dev.amrv.net.Address;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class ValidateIP {

    public static void main(String[] args) throws UnknownHostException,
            IOException {

        byte[] sample = new byte[6];
                
        System.out.println(Arrays.toString(sample));
        
        /*
        System.out.println("Type 'exit' to exit");
        
        System.out.println(4 * BPB / 4);

        // IPv4
        check(127, 0, 0, 1);
        check(0127, 0, 0, 1);
        check(127, 256, 0, 1);
        check(83, 58, 147, 118);
        check(-123, 0, 0, 1);

        //Ipv6
        check(2001, 0xdb8, 0x3333, 4444, 5555, 6666, 7777, 8888);
        check(12, 23, 43, 213, 23, 21, 23, 23, 12, 2, 23, 32, 123, 23, 32, 43);
        check(2001, 0xdb8, 3333, 4444);
        check(1273);

        //Create IPv4
        compose(83, 58, 147, 118);
        System.out.println((byte) 83);
        System.out.println((byte) 174);
        compose2(83, 58, 147, 118);
        compose(83, 58);
        compose2(83, 58);
        compose(21073);
        compose2(21073);
         */
        //Create IPv6
        //compose6(2001, 0xdb8, 0x3333, 4444, 5555, 6666, 7777, 8888);
        // Tru create ipv6
        byte[] ipv6 = compose6(0xfe80, 0, 0, 0, 0x3b76, 0x536f, 0xaca9, 0xe5e7);
        System.out.println(Arrays.toString(ipv6) + " Can connect: " + InetAddress.getByAddress("localhost", ipv6).isReachable(10));

        //Try create ipv4
        byte[] ip = new byte[]{(byte) 83, (byte) 58, (byte) 147, (byte) 118};
        System.out.println(Arrays.toString(ip) + " Can connect: " + InetAddress.getByAddress("localhost", ip).isReachable(10));
    }

//    private static void check(int... buffer) {
//        boolean valid = Address.checkIp(buffer);
//
//        System.out.println((valid ? "Verificado " : "Invalido ") + Arrays.toString(buffer));
//    }

    private static byte[] compose(int... segments) {
        byte[] ipv4 = new byte[4];
        if (segments.length == 1) {
            ipv4[0] = (byte) (segments[0] >> 24);
            ipv4[1] = (byte) (segments[0] >> 16);
            ipv4[2] = (byte) (segments[0] >> 8);
            ipv4[3] = (byte) (segments[0]);
            System.out.println("Check 1 " + Arrays.toString(segments) + " >> " + Arrays.toString(ipv4));

        } else if (segments.length == 2) {
            ipv4[0] = (byte) (segments[0] >> 8);
            ipv4[1] = (byte) segments[0];
            ipv4[2] = (byte) (segments[1] >> 8);
            ipv4[3] = (byte) segments[1];
            System.out.println("Check 2 " + Arrays.toString(segments) + " >> " + Arrays.toString(ipv4));

        } else if (segments.length == 4) {
            ipv4[0] = (byte) segments[0];
            ipv4[1] = (byte) segments[1];
            ipv4[2] = (byte) segments[2];
            ipv4[3] = (byte) segments[3];
            System.out.println("Check 4 " + Arrays.toString(segments) + " >> " + Arrays.toString(ipv4));
        }
        return ipv4;
    }

    private static byte[] compose6(int... segments) {
        byte[] ipv6 = new byte[16];

        if (segments.length == 8) {
            for (int i = 0; i < ipv6.length; i++) {
                ipv6[i] = (byte) (segments[i / 2] >> 8 - i % 2 * 8);
                System.out.println(i + ": " + ipv6[i] + " (" + Integer.toHexString(segments[i / 2]) + ") = " + segments[i / 2] + " >> " + (8 - (i % 2) * 8) + " = " + ((byte) (segments[i / 2] >> 8 - (i % 2) * 8)));
            }
        } else if (segments.length % 2 == 0){
            int bytesToSplit = ipv6.length / segments.length;
            int bitsToSplit = segments.length / bytesToSplit;

            for (int i = 0; i < ipv6.length; i++)
                ipv6[i] = (byte) (segments[i / bytesToSplit] >> (bitsToSplit - (i % bytesToSplit) * bitsToSplit));

        }
        return ipv6;
    }

    private static void compose2(int... segments) {
        ByteBuffer buffer = ByteBuffer.allocate(segments.length * 4);
        for (int segment : segments)
            buffer.putInt(segment);
        System.out.println(Arrays.toString(segments) + " >> " + Arrays.toString(buffer.array()));
    }

    public static byte[] convertIntToByteArray2(int value) {
        return new byte[]{
            (byte) (value >> 24),
            (byte) (value >> 16),
            (byte) (value >> 8),
            (byte) value};
    }
}
