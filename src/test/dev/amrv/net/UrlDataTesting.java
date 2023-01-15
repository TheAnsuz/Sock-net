package dev.amrv.test.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class UrlDataTesting {

    public static void main(String[] args) throws MalformedURLException,
            IOException {
        URL url = new URL("https://mrdoob.com/projects/chromeexperiments/google-gravity/");

        int read = 0;
        byte[] data = new byte[2048];
        StringBuilder builder = new StringBuilder();
        InputStream stream = url.openStream();

        while (stream.available() > 0) {

            read = stream.read(data);
            System.err.println("read " + read);
            builder.append(new String(data, 0, read, Charset.forName("UTF-8")));

        }

        System.out.println(builder.toString());
    }

}
