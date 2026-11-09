package co.edu.uniquindio.poo.Vista;

import co.edu.uniquindio.poo.Model.Estructuras.Pila;
import co.edu.uniquindio.poo.Model.Punto2.Historial;

import javax.swing.*;
import java.awt.*;

// DOCUMENTACIÓN
// Pestaña del Punto 2: historial de acciones en una pila y obtención de un segmento.
@SuppressWarnings("serial")   // las ventanas no se serializan
public final class PanelPunto2 extends JPanel {

    private Pila<String> historial = new Pila<>();
    private final DefaultListModel<String> modeloOriginal = new DefaultListModel<>();
    private final DefaultListModel<String> modeloSegmento = new DefaultListModel<>();
    private final JTextArea salida = UtilVista.areaTexto();

    private final JTextField txtAccion = new JTextField("A7", 6);
    private final JTextField txtInicio = new JTextField("2", 3);
    private final JTextField txtFin = new JTextField("4", 3);

    public PanelPunto2() {
        super(new BorderLayout());

        JButton btnApilar = new JButton("Apilar acción");
        JButton btnPrueba = new JButton("Cargar A1..A6");
        JButton btnVaciar = new JButton("Vaciar");
        JButton btnSegmento = new JButton("obtenerSegmento");

        JPanel controles = new JPanel(new GridLayout(2, 1));
        controles.add(UtilVista.fila(new JLabel("Acción:"), txtAccion, btnApilar, btnPrueba, btnVaciar));
        controles.add(UtilVista.fila(new JLabel("Inicio:"), txtInicio, new JLabel("Fin:"), txtFin, btnSegmento,
                new JLabel("  (posiciones desde la cima, empezando en 1)")));
        add(controles, BorderLayout.NORTH);

        JPanel listas = new JPanel(new GridLayout(1, 2, 8, 0));
        listas.add(UtilVista.conTitulo("Pila original (tope → fondo)", UtilVista.lista(modeloOriginal)));
        listas.add(UtilVista.conTitulo("Segmento resultante (tope → fondo)", UtilVista.lista(modeloSegmento)));
        JScrollPane log = UtilVista.conTitulo("Resultados", salida);
        log.setPreferredSize(new Dimension(0, 170));
        add(listas, BorderLayout.CENTER);
        add(log, BorderLayout.SOUTH);

        btnApilar.addActionListener(e -> {
            String a = txtAccion.getText().trim();
            if (a.isEmpty()) { UtilVista.error(this, "La acción no puede estar vacía."); return; }
            historial.apilar(a);
            refrescar();
        });
        btnPrueba.addActionListener(e -> cargarPrueba());
        btnVaciar.addActionListener(e -> { historial = new Pila<>(); modeloSegmento.clear(); refrescar(); });
        btnSegmento.addActionListener(e -> segmento());

        cargarPrueba();
    }

    private void cargarPrueba() {
        historial = new Pila<>();
        for (int i = 1; i <= 6; i++) historial.apilar("A" + i);
        modeloSegmento.clear();
        refrescar();
    }

    private void segmento() {
        try {
            int inicio = UtilVista.leerEntero(txtInicio, "Inicio");
            int fin = UtilVista.leerEntero(txtFin, "Fin");
            Pila<String> resultado = Historial.obtenerSegmento(historial, inicio, fin);
            llenar(modeloSegmento, resultado);
            String nota = resultado.estaVacia()
                    ? "  (rango inválido: inicio < 1, inicio > fin o fin > " + historial.tamanio() + ")" : "";
            UtilVista.agregar(salida, "obtenerSegmento(" + inicio + ", " + fin + ") = " + resultado + nota
                    + "\n   original sin cambios: " + historial);
            refrescar();
        } catch (IllegalArgumentException ex) {
            UtilVista.error(this, ex.getMessage());
        }
    }

    private void refrescar() {
        llenar(modeloOriginal, historial);
    }

    private static void llenar(DefaultListModel<String> modelo, Pila<String> pila) {
        modelo.clear();
        int pos = 1;
        for (String s : pila) modelo.addElement(pos++ + ". " + s);
        if (pila.estaVacia()) modelo.addElement("[vacía]");
    }
}
