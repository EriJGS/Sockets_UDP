package mx.unison.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 * @author rnavarro
 */
public class ChatServer {

    public static Logger logger = Logger.getLogger(ChatServer.class.getName());
    public static final int PORT = 1027;

    private HashMap<String, ChatService> clientes;

    public ChatServer() {
        clientes = new HashMap();
    }

    public void broadCast(String message) {
        // Obtiene la lista de clientes conectados
        Collection c = clientes.values();

        Iterator<ChatService> it = c.iterator();

        while (it.hasNext()) {
            ChatService cliente = it.next();
            // Enviar el mensaje a cada cliente
            cliente.send(message);
        }
    }

    // Agregar un cliente conectado al servidor
    public boolean add(String user, ChatService service) {
        boolean result = false;

        if (!clientes.containsKey(user)) { // check usuario con mismo nombre
            clientes.put(user, service);
            result = true;
        }
        return result;
    }

    // Agregar un cliente conectado al servidor
    public boolean remove(String user) {

        boolean result = false;

        if (!clientes.containsKey(user)) { // check usuario con mismo nombre
            clientes.remove(user);
            result = true;
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        
        ChatServer server = new ChatServer();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            // esperar a los clientes
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // crear el servicio para atender al cliente
                ChatService cliente = new ChatService(server, clientSocket);  // Llama al constructor de ChatService() (crea los flujos de entrada y salida, lee y guarda el nombre del usuario que será lo primero que ingrese)

                String user = cliente.getUser();                  // Obtener el nombre del cliente
                if (user != null) {
                    server.add(user, cliente);                    // agregar al cliente al servidor

                    Thread serviceProcess = new Thread(cliente);  // Crear hilo y se le manda la clase ChatService
                    serviceProcess.start();                       // Llama al run() para hacer el proceso del chat (recibe el mensaje del cliente y lo manda a cada miembro)
                    logger.info("CONNECTED: " + user);            // Informar que se unió un miembro
                } else {
                    cliente = null;
                    clientSocket.close();
                    logger.info("NOT CONNECTED: " + user);
                }
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + PORT + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}
