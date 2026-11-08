package co.edu.uniquindio.poo.Model.Punto1;

public class Caja {
    private final String codigo;
    private final int valor;

    public Caja(String codigo, int valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    public String getCodigo() { return codigo; }
    public int getValor() { return valor; }

    @Override
    public String toString() {
        return codigo + ":" + valor;
    }
}
