package co.edu.uniquindio.poo.Vista;

import javax.swing.*;
import java.awt.*;

// DOCUMENTACIÓN
// Ventana principal de la interfaz gráfica: una pestaña por cada punto del parcial.
// Cada pestaña arranca con los datos de prueba del enunciado ya cargados.
@SuppressWarnings("serial")   // las ventanas no se serializan
public final class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        super("Parcial 2 - Estructuras de Datos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("1. Cajas y expresión", new PanelPunto1());
        pestanas.addTab("2. Historial", new PanelPunto2());
        pestanas.addTab("3. Paquetes", new PanelPunto3());
        pestanas.addTab("4. ABB", new PanelPunto4());
        pestanas.addTab("5. AVL y Rojo-Negro", new PanelPunto5());
        add(pestanas, BorderLayout.CENTER);

        setSize(1280, 820);
        setLocationRelativeTo(null);
    }
}
