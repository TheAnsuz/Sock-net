package dev.amrv.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class Address {

    public static final int IPV4_MAX_SEGMENT = 0xff;
    public static final int IPV6_MAX_SEGMENT = 0xffff;
    public static final int PORT_MIN = 0;
    public static final int PORT_MAX = 65535;
    public static final int NAME_LABEL_MAX = 63;
    public static final int NAME_MAX = 255;

    private String name;
    private boolean useMachine = false;
    private boolean hasIpv6 = false;
    private boolean hasIpv4 = false;
    private final byte[] ipv4 = new byte[4];
    private final byte[] ipv6 = new byte[16];
    private int port;

    public void setUseMachineAddress(boolean useMachineAddress) {
        this.useMachine = useMachineAddress;
    }

    public boolean useMachineAddress() {
        return this.useMachine;
    }

    public void clearName() {
        this.name = null;
    }

    public boolean setNameSilently(String name) {
        try {
            this.setName(name);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    public void setName(String name) {
        if (name == null) {
            this.name = null;
            return;
        }

        int labelIndex = 0;
        char prevChar = ' ';

        for (char c : name.toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '-') {

                if (labelIndex++ > NAME_LABEL_MAX)
                    throw new IllegalArgumentException("Hostname's label must not exceed " + NAME_LABEL_MAX + " characters");

            } else if (c == '.') {
                if (prevChar == '-')
                    throw new IllegalArgumentException("Hostname's must not contain hyphens before label's separator (.)");

                else if (prevChar == '.')
                    throw new IllegalArgumentException("Hostname's can't have empty bytes, use 0 instead");

                labelIndex = 0;

            } else
                throw new IllegalArgumentException("Invalid character (" + c + ") only alphanumeric and hyphen characters allowed ");

            prevChar = c;
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean setIpv4Silently(String literal) {
        try {
            setIpv4(literal);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public void setIpv4(String literal) {
        int[] segments = new int[4];
        int index = 0;

        for (char c : literal.toCharArray()) {
            if (c == '.') {
                index++;

                if (index + 1 > segments.length)
                    throw new IllegalArgumentException("IPv4 should only contain 4 bytes of data");

            } else if (c >= '0' && c <= '9') {
                segments[index] = segments[index] * 10 + Character.getNumericValue(c);

                if (segments[index] > IPV4_MAX_SEGMENT)
                    throw new IllegalArgumentException("IPv4's byte at index " + index + "(" + segments[index] + ") exceeds " + IPV4_MAX_SEGMENT);
            } else
                throw new IllegalArgumentException("IPv4 literal must be provided as <byte>.<byte>.<byte>.<byte>");
        }

        setIpv4(segments);
    }

    public boolean setIpv4Silently(int... segments) {
        try {
            setIpv4(segments);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    public void setIpv4(int... segments) {
        if (segments == null)
            throw new NullPointerException();

        if (segments.length != 4)
            throw new IllegalArgumentException("IPv4 provider must be 4 parameters long");

        for (int i = 0; i < ipv4.length; i++) {

            if (segments[i] < 0x0 || segments[i] > IPV4_MAX_SEGMENT)
                throw new IllegalArgumentException("IPv4's byte at index " + i + "(" + segments[i] + ")" + " exceeds " + IPV4_MAX_SEGMENT);

            ipv4[i] = (byte) segments[i];
        }

        hasIpv4 = true;
    }

    public void clearIpv4() {
        for (int i = 0; i < ipv4.length; i++)
            ipv4[i] = 0;

        hasIpv4 = false;
    }

    public boolean setIpv6Silently(String literal) {
        try {
            setIpv6(literal);
            return true;
        } catch (InputMismatchException ime) {
            return false;
        }
    }

    public void setIpv6(String literal) {
        int[] segments = new int[8];
        int index = 0;

    }

    public boolean setIpv6Silently(int... segments) {
        try {
            setIpv6(segments);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    public void setIpv6(int... segments) {
        if (segments == null)
            throw new NullPointerException();

        if (segments.length != 8)
            throw new IllegalArgumentException("IPv6 privder must be 8 parameters long");

        int currentByte;
        for (int i = 0; i < ipv6.length; i++) {
            currentByte = segments[i / 2] >> 8 - i % 2 * 8;

            if (currentByte < 0x0 || currentByte > IPV6_MAX_SEGMENT)
                throw new IllegalArgumentException("IPv6's byte at index " + i / 2 + "(" + segments[i / 2] + ")" + " exceeds " + IPV6_MAX_SEGMENT);

            ipv6[i] = (byte) currentByte;
        }

        hasIpv6 = false;

    }

    public void clearIpv6() {
        for (int i = 0; i < ipv6.length; i++)
            ipv6[i] = 0;

        hasIpv6 = false;
    }

    public boolean setPortSilently(int port) {
        try {
            setPort(port);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    public void setPort(int port) {
        if (port < PORT_MIN || port > PORT_MAX)
            throw new IllegalArgumentException("Port must be in between " + PORT_MIN + " and " + PORT_MAX);

        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void clearPort() {
        this.port = 0;
    }

    public InetAddress build() throws UnknownHostException {
        if (useMachine)
            return InetAddress.getLocalHost();
        else if (hasIpv4)
            return InetAddress.getByAddress(name, ipv4);

        else if (hasIpv6)
            return InetAddress.getByAddress(name, ipv6);

        else
            return InetAddress.getByName(name);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        if (hasIpv4)
            for (int i = 0; i < ipv4.length; i++)
                builder.append(Integer.toString(Byte.toUnsignedInt(ipv4[i]), 10))
                        .append(i == ipv4.length - 1 ? "" : ".");

        else
            for (int i = 0; i < ipv4.length; i++)
                builder.append(Integer.toUnsignedString(Byte.toUnsignedInt(ipv4[i]), 16))
                        .append(i == ipv4.length - 1 ? "" : ":");

        return "Address[" + name + "/" + builder.toString() + "/" + port + ']';
    }

}
