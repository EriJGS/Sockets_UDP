package mx.unison.chat.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author rnavarro
 */
public class UpdateThread implements Runnable {

    private BufferedReader in;
    private JTextArea t;

    public UpdateThread(BufferedReader in, JTextArea t) {
        this.in = in;                                     // Recibe lo que me envía el servidor
        this.t = t;
    }

    @Override
    public void run() {

        String mensaje = null;

        try {
            while ((mensaje = in.readLine()) != null) {    // Si lo que viene en el flujo in no es nulo
                //System.out.println(mensaje);             // Imprime en consola
                t.append(mensaje + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(UpdateThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
