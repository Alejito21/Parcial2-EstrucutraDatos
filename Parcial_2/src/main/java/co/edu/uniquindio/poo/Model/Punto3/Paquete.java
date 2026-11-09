package co.edu.uniquindio.poo.Model.Punto3;

// DOCUMENTACIÓN
// Paquete pendiente de entrega (Punto 3): código único, tipo de entrega y peso en kg.
public class Paquete {

    // Tipos de entrega posibles.
    public enum Tipo { URGENTE, NORMAL }

    private final String codigo;
    private final Tipo tipo;
    private final int peso;

    public Paquete(String codigo, Tipo tipo, int peso) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.peso = peso;
    }

    public String getCodigo() { return codigo; }
    public Tipo getTipo() { return tipo; }
    public int getPeso() { return peso; }

    // Formato "P1(NOR,4kg)" usado al imprimir la cola.
    @Override
    public String toString() {
        return codigo + "(" + (tipo == Tipo.URGENTE ? "URG" : "NOR") + "," + peso + "kg)";
    }
}
