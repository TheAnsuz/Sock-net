package dev.amrv.test.net.ui;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian MRV. aka AMRV || Ansuz
 */
public class Console extends javax.swing.JFrame {

    /**
     * Creates new form Console
     */
    private StringBuilder outputBuilder = new StringBuilder();
    private StringBuilder inputBuilder = new StringBuilder();

    public Console() {
        initComponents();
        this.setLocationRelativeTo(null);
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.noddraw", "true");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spOutput = new javax.swing.JScrollPane();
        epOutput = new javax.swing.JEditorPane();
        tfInput = new javax.swing.JTextField();
        tfTip = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Console");
        setLocation(new java.awt.Point(0, 0));

        spOutput.setName("spOutput"); // NOI18N

        epOutput.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        epOutput.setFocusable(false);
        epOutput.setName("epOutput"); // NOI18N
        spOutput.setViewportView(epOutput);

        tfInput.setName("tfInput"); // NOI18N
        tfInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfInputActionPerformed(evt);
            }
        });

        tfTip.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        tfTip.setText("INPUT:");
        tfTip.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tfTip.setFocusable(false);
        tfTip.setName("tfTip"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spOutput)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tfTip, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfInput, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfInputActionPerformed
        if (evt.getID() == ActionEvent.ACTION_PERFORMED) {
            inputBuilder.append(tfInput.getText().trim());
            System.out.println("added " + tfInput.getText().trim() + " (" + inputBuilder.length() + ")");
            tfInput.setText("");
        }
    }//GEN-LAST:event_tfInputActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException,
            InterruptedException {
        Console console = new Console();

        Scanner scanner = new Scanner(console.input);

        console.setVisible(true);
        Thread.sleep(250);
        console.output.write("Started ".getBytes());

        /*
        while (console.isVisible()) {
            //System.out.println(console.input.available());
            String line = "";
            while (console.input.available() > 0) {
                line += (char)console.input.read();
            }
            if (!line.isEmpty())
                console.output.write(line.getBytes());

        }
         */
        Thread listener = new Thread(() -> {
            String line = "";
            while (true) {
                try {
                    //System.out.println(console.input.available());
                    while (console.input.available() > 0) {
                        line += (char) console.input.read();
                    }
                    if (!line.isEmpty()) {
                        console.output.write(line.getBytes());
                        System.out.println(line);
                        line = "";
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        listener.setDaemon(true);
        listener.start();
    }

    public InputStream input = new ConsoleInputStream();
    public OutputStream output = new ConsoleOutputStream();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane epOutput;
    private javax.swing.JScrollPane spOutput;
    private javax.swing.JTextField tfInput;
    private javax.swing.JTextField tfTip;
    // End of variables declaration//GEN-END:variables

    private class ConsoleOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            outputBuilder.append((char) b);
            epOutput.setText(outputBuilder.toString());
        }

    }

    private class ConsoleInputStream extends InputStream {

        @Override
        public int available() throws IOException {
            return inputBuilder.length();
        }

        @Override
        public int read() throws IOException {
            if (inputBuilder.length() <= 0)
                return -1;

            final int data = inputBuilder.charAt(0);
            inputBuilder.deleteCharAt(0);
            //System.out.println("Read " + (char) data);
            return data;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (inputBuilder.length() <= 0)
                return -1;

            if (b == null)
                throw new NullPointerException();

            int maxRead = Math.min(b.length, len);

            for (int i = 0; i < maxRead; i++) {
                b[off + i] = (byte) inputBuilder.charAt(i);
            }
            inputBuilder.delete(0, maxRead);
            return maxRead;

        }

    }
}
