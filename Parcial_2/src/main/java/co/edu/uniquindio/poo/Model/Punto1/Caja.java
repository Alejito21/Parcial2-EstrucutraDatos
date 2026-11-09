package co.edu.uniquindio.poo.Model.Punto1;

// DOCUMENTACIÓN
// Caja del depósito (Punto 1): tiene un código único y un valor entero.
// Es inmutable: una vez creada no cambia.
public class Caja {
    private final String codigo;
    private final int valor;

    public Caja(String codigo, int valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    public String getCodigo() { return codigo; }
    public int getValor() { return valor; }

    // Formato "C1:2" (código:valor), usado al imprimir la pila.
    @Override
    public String toString() {
        return codigo + ":" + valor;
    }
}
