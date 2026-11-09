package co.edu.uniquindio.poo.Vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

// DOCUMENTACIÓN
// Utilidades compartidas por los paneles de la interfaz gráfica:
// creación de componentes con el mismo estilo, captura de la salida de consola
// y mensajes de error.
public class UtilVista {

    public static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 13);

    // Algunos métodos del modelo imprimen su traza con System.out (tablas del Punto 1,
    // evaluación de paquetes del Punto 3). Este método ejecuta la acción redirigiendo
    // System.out a un buffer temporal y retorna el texto que se imprimió.
    public static String capturar(Runnable accion) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream temporal = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(temporal);
            accion.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    // Área de texto de solo lectura con letra monoespaciada (para que las tablas se alineen).
    public static JTextArea areaTexto() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(MONO);
        area.setMargin(new Insets(6, 8, 6, 8));
        return area;
    }

    // Envuelve un componente en un scroll con borde titulado.
    public static JScrollPane conTitulo(String titulo, JComponent componente) {
        JScrollPane scroll = new JScrollPane(componente);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), titulo, TitledBorder.LEFT, TitledBorder.TOP));
        return scroll;
    }

    // Panel con los componentes en una fila, alineados a la izquierda.
    public static JPanel fila(Component... componentes) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        for (Component c : componentes) p.add(c);
        return p;
    }

    // Lista con letra monoespaciada.
    public static JList<String> lista(DefaultListModel<String> modelo) {
        JList<String> l = new JList<>(modelo);
        l.setFont(MONO);
        return l;
    }

    // Lee un entero de un campo de texto; lanza excepción con un mensaje claro si no lo es.
    public static int leerEntero(JTextField campo, String nombre) {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nombre + " debe ser un número entero.");
        }
    }

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Dato inválido", JOptionPane.WARNING_MESSAGE);
    }

    // Agrega texto al final de un área y baja el scroll hasta el final.
    public static void agregar(JTextArea area, String texto) {
        area.append(texto.endsWith("\n") ? texto : texto + "\n");
        area.setCaretPosition(area.getDocument().getLength());
    }
}
