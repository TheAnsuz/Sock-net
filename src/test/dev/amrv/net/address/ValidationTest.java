package dev.amrv.test.net.address;

import dev.amrv.net.Address;
import java.util.Scanner;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class ValidationTest {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Type 'exit' to exit");
        String text = "";
        do {
            System.out.print("Hostname: ");
            text = scanner.nextLine();
            check(text);
            
        } while (!text.equalsIgnoreCase("exit"));
    }
    private static void check(String name) {
        boolean valid = false;

        System.out.println((valid ? "Verificado " : "Invalido ") + name);
    }
}
