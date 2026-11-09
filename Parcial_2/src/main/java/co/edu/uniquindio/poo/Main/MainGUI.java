package co.edu.uniquindio.poo.Main;

import co.edu.uniquindio.poo.Vista.VentanaPrincipal;

import javax.swing.*;

// DOCUMENTACIÓN
// Punto de entrada de la interfaz gráfica (Swing).
// Para la versión de consola con verificaciones automáticas, ejecutar Main.
public class MainGUI {

    public static void main(String[] args) {
        // La interfaz se crea en el hilo de eventos de Swing, como recomienda la librería.
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
                // Si falla, se usa el aspecto por defecto de Java
            }
            new VentanaPrincipal().setVisible(true);
        });
    }
}
