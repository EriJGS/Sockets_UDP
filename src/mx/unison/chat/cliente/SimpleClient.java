/* Desarrollo de Sistemas III (19 de marzo de 2021) - Tarea: "Trabajando con Sockets UDP" */

package mx.unison.chat.cliente;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class SimpleClient extends javax.swing.JFrame {

    String userName;
    String hostName;
    final int portNumber = 1027;
    PrintWriter mensajeAlServidor;

    public SimpleClient() {
        initComponents();
        this.getContentPane().setBackground(Color.PINK);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        editorArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuSalir = new javax.swing.JMenu();
        jMenuItemSalir = new javax.swing.JMenuItem();
        jChat = new javax.swing.JMenu();
        jMenuItemConectar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 153, 153));
        setResizable(false);

        messageArea.setEditable(false);
        messageArea.setColumns(20);
        messageArea.setRows(5);
        jScrollPane1.setViewportView(messageArea);
        messageArea.getAccessibleContext().setAccessibleParent(messageArea);

        editorArea.setColumns(20);
        editorArea.setRows(5);
        jScrollPane2.setViewportView(editorArea);

        sendButton.setText("Enviar");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        jMenuSalir.setText("File");

        jMenuItemSalir.setText("Salir");
        jMenuItemSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItemSalirMouseClicked(evt);
            }
        });
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalirActionPerformed(evt);
            }
        });
        jMenuSalir.add(jMenuItemSalir);

        jMenuBar1.add(jMenuSalir);

        jChat.setText("Chat");

        jMenuItemConectar.setText("Conectar");
        jMenuItemConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConectarActionPerformed(evt);
            }
        });
        jChat.add(jMenuItemConectar);

        jMenuBar1.add(jChat);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sendButton)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        sendMessage();
    }//GEN-LAST:event_sendButtonActionPerformed

    private void jMenuItemSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItemSalirMouseClicked
        
    }//GEN-LAST:event_jMenuItemSalirMouseClicked

    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemSalirActionPerformed

    private void jMenuItemConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConectarActionPerformed
        String inputValue = JOptionPane.showInputDialog(null, "Ingrese el nombre de usuario");

        if ((!inputValue.trim().isEmpty()) && (inputValue != null)) {
            userName = inputValue.trim();
            connect(userName);
            
            setTitle(userName.toUpperCase());
            jMenuItemConectar.setEnabled(false);
        }
    }//GEN-LAST:event_jMenuItemConectarActionPerformed

    // Método para conectar al cliente al servidor
    private void connect(String user) {        
        try {
            Socket socket = new Socket(hostName, portNumber);                                       // Solicitar conexión al servidor, se abre el socket
            mensajeAlServidor = new PrintWriter(socket.getOutputStream(), true);                    // Obtener el canal para envíar datos al servidor
            mensajeAlServidor.println(user);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Crear flujo de entrada para leer lo que envíe el servidor
            
            Thread thread = new Thread(new UpdateThread(in, messageArea));                          // Crea thread y le mando la clase MenssajeThread (esta clase va a recibir lo que mande el servidor y lo imprimirá)
            thread.start();
        } catch (IOException ex) {
            System.err.println("No se pudo conectar el cliente" + ex.getMessage());
        }
    }
    
    private void sendMessage() {
        String mensaje = editorArea.getText().trim();
        
        if (mensaje.trim().equals(".")) {
            mensajeAlServidor.println(mensaje);                   // Si es un punto se lo mando al servidor y termina
            System.exit(0);
        } else if (!mensaje.isEmpty()){
            mensajeAlServidor.println(userName + ": " + mensaje); // Le envío el mensaje al servidor
            editorArea.setText("");
        }
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SimpleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SimpleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SimpleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimpleClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SimpleClient().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea editorArea;
    private javax.swing.JMenu jChat;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemConectar;
    private javax.swing.JMenuItem jMenuItemSalir;
    private javax.swing.JMenu jMenuSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables

}
