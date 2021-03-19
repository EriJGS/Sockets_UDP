package mx.unison.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rnavarro
 */
public class ChatService implements Runnable {

    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket = null;
    private ChatServer server;
    private String user;

    public ChatService(ChatServer s, Socket c) {
        clientSocket = c;
        server = s;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // leer el nombre con el que se va a conectar el usuario
            String u = in.readLine();
            if (u != null) {
                user = u.trim();
                if (user.isEmpty()) {
                    user = null;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUser() {
        return user;
    }

    public void send(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            String mensaje;

            // recibir mensaje del cliente
            while ((mensaje = in.readLine()) != null) {
                if (mensaje.trim().equals(".")) {
                    server.remove(user);
                    break;
                }
                // enviar el mensaje a todos los clientes conectados
                server.broadCast(mensaje);
            }
            clientSocket.close();
            
        } catch (IOException ex) {
            System.out.println("Error en la conexion");
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
