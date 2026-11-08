package co.edu.uniquindio.poo.Model.Punto3;

public class Paquete {
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

    @Override
    public String toString() {
        return codigo + "(" + (tipo == Tipo.URGENTE ? "URG" : "NOR") + "," + peso + "kg)";
    }
}
