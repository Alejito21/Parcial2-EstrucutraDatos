package co.edu.uniquindio.poo.Vista;

import co.edu.uniquindio.poo.Model.Estructuras.Cola;
import co.edu.uniquindio.poo.Model.Punto3.Paquete;
import co.edu.uniquindio.poo.Model.Punto3.Ruta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// DOCUMENTACIÓN
// Pestaña del Punto 3: cola de paquetes pendientes y selección recursiva
// según tipo de entrega, orden de registro y capacidad del vehículo.
@SuppressWarnings("serial")   // las ventanas no se serializan
public final class PanelPunto3 extends JPanel {

    private static final String[] COLUMNAS = {"#", "Código", "Tipo", "Peso (kg)"};

    private Cola<Paquete> pendientes = new Cola<>();
    private Cola<Paquete> seleccionados = new Cola<>();   // última cola retornada
    private final DefaultTableModel modeloPendientes = modeloTabla();
    private final DefaultTableModel modeloSeleccionados = modeloTabla();
    private final JTextArea salida = UtilVista.areaTexto();
    private final JLabel lblCapacidad = new JLabel(" ");

    private final JTextField txtCodigo = new JTextField("P8", 5);
    private final JComboBox<Paquete.Tipo> cmbTipo = new JComboBox<>(Paquete.Tipo.values());
    private final JTextField txtPeso = new JTextField("3", 4);
    private final JTextField txtCapacidad = new JTextField("14", 4);

    public PanelPunto3() {
        super(new BorderLayout());

        JButton btnEncolar = new JButton("Encolar paquete");
        JButton btnPrueba = new JButton("Cargar datos de prueba");
        JButton btnVaciar = new JButton("Vaciar");
        JButton btnSeleccionar = new JButton("seleccionarPaquetes");

        JPanel controles = new JPanel(new GridLayout(2, 1));
        controles.add(UtilVista.fila(new JLabel("Código:"), txtCodigo, new JLabel("Tipo:"), cmbTipo,
                new JLabel("Peso:"), txtPeso, btnEncolar, btnPrueba, btnVaciar));
        controles.add(UtilVista.fila(new JLabel("Capacidad del vehículo (kg):"), txtCapacidad, btnSeleccionar, lblCapacidad));
        add(controles, BorderLayout.NORTH);

        JPanel tablas = new JPanel(new GridLayout(1, 2, 8, 0));
        tablas.add(UtilVista.conTitulo("Cola de pendientes (frente → final)", tabla(modeloPendientes)));
        tablas.add(UtilVista.conTitulo("Cola retornada: seleccionados", tabla(modeloSeleccionados)));
        JScrollPane log = UtilVista.conTitulo("Paquetes evaluados", salida);
        log.setPreferredSize(new Dimension(0, 220));
        add(tablas, BorderLayout.CENTER);
        add(log, BorderLayout.SOUTH);

        btnEncolar.addActionListener(e -> encolar());
        btnPrueba.addActionListener(e -> cargarPrueba());
        btnVaciar.addActionListener(e -> { pendientes = new Cola<>(); modeloSeleccionados.setRowCount(0); refrescar(); });
        btnSeleccionar.addActionListener(e -> seleccionar());

        cargarPrueba();
    }

    private static DefaultTableModel modeloTabla() {
        return new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
    }

    private static JTable tabla(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setRowHeight(22);
        t.getColumnModel().getColumn(0).setMaxWidth(40);
        return t;
    }

    private void encolar() {
        try {
            String codigo = txtCodigo.getText().trim();
            if (codigo.isEmpty()) throw new IllegalArgumentException("El código no puede estar vacío.");
            for (Paquete p : pendientes) {
                if (p.getCodigo().equals(codigo)) throw new IllegalArgumentException("Ya existe un paquete con código " + codigo + ".");
            }
            int peso = UtilVista.leerEntero(txtPeso, "El peso");
            if (peso <= 0) throw new IllegalArgumentException("El peso debe ser un entero positivo.");
            pendientes.encolar(new Paquete(codigo, (Paquete.Tipo) cmbTipo.getSelectedItem(), peso));
            refrescar();
        } catch (IllegalArgumentException ex) {
            UtilVista.error(this, ex.getMessage());
        }
    }

    // Datos del enunciado (P1..P7).
    private void cargarPrueba() {
        pendientes = new Cola<>();
        pendientes.encolar(new Paquete("P1", Paquete.Tipo.NORMAL, 4));
        pendientes.encolar(new Paquete("P2", Paquete.Tipo.URGENTE, 7));
        pendientes.encolar(new Paquete("P3", Paquete.Tipo.NORMAL, 3));
        pendientes.encolar(new Paquete("P4", Paquete.Tipo.URGENTE, 5));
        pendientes.encolar(new Paquete("P5", Paquete.Tipo.NORMAL, 6));
        pendientes.encolar(new Paquete("P6", Paquete.Tipo.URGENTE, 4));
        pendientes.encolar(new Paquete("P7", Paquete.Tipo.NORMAL, 2));
        modeloSeleccionados.setRowCount(0);
        lblCapacidad.setText(" ");
        refrescar();
    }

    private void seleccionar() {
        try {
            int capacidad = UtilVista.leerEntero(txtCapacidad, "La capacidad");
            String inicial = pendientes.toString();
            String traza = UtilVista.capturar(() -> {
                Ruta.traza = true;
                try {
                    seleccionados = Ruta.seleccionarPaquetes(pendientes, capacidad);
                } finally {
                    Ruta.traza = false;
                }
            });
            int restante = capacidad <= 0 ? capacidad : capacidad - Ruta.pesoTotal(seleccionados);

            StringBuilder sb = new StringBuilder();
            sb.append("Cola inicial: ").append(inicial).append('\n');
            sb.append("seleccionarPaquetes(capacidad = ").append(capacidad).append(" kg)\n");
            sb.append(capacidad <= 0 ? "  Capacidad <= 0: se retorna una cola vacía y no se modifica la cola.\n" : traza);
            sb.append("Seleccionados: ").append(seleccionados).append('\n');
            sb.append("Pendientes:    ").append(pendientes).append('\n');
            sb.append("Capacidad que quedó disponible: ").append(restante).append(" kg\n");
            UtilVista.agregar(salida, sb.toString());

            llenar(modeloSeleccionados, seleccionados);
            lblCapacidad.setText("   Capacidad restante: " + restante + " kg");
            refrescar();
        } catch (IllegalArgumentException ex) {
            UtilVista.error(this, ex.getMessage());
        }
    }

    private void refrescar() {
        llenar(modeloPendientes, pendientes);
    }

    private static void llenar(DefaultTableModel modelo, Cola<Paquete> cola) {
        modelo.setRowCount(0);
        int i = 1;
        for (Paquete p : cola) modelo.addRow(new Object[]{i++, p.getCodigo(), p.getTipo(), p.getPeso()});
    }
}
