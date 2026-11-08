package co.edu.uniquindio.poo.Model.Punto1;
import co.edu.uniquindio.poo.Model.Estructuras.Pila;

public class GestionarCajas {

    public static boolean retirarCaja(Pila<Caja> pila, String codigo) {
        if (pila.estaVacia()) return false;

        Pila<Caja> aux = new Pila<>();
        while (!pila.estaVacia() && !pila.tope().getCodigo().equals(codigo)) {
            aux.apilar(pila.desapilar());
        }
        if (pila.estaVacia()) {
            while (!aux.estaVacia()) pila.apilar(aux.desapilar());
            return false;
        }
        Caja retirada = pila.desapilar();


        while (!aux.estaVacia()) pila.apilar(aux.desapilar());


        Pila<Caja> impares = new Pila<>();
        Pila<Caja> pares = new Pila<>();
        int posicion = 1;
        while (!pila.estaVacia()) {
            Caja c = pila.desapilar();
            if (posicion % 2 == 1) impares.apilar(c); else pares.apilar(c);
            posicion++;
        }


        boolean valorPar = retirada.getValor() % 2 == 0;
        Pila<Caja> abajo = valorPar ? pares : impares;
        Pila<Caja> arriba = valorPar ? impares : pares;
        while (!abajo.estaVacia()) pila.apilar(abajo.desapilar());
        while (!arriba.estaVacia()) pila.apilar(arriba.desapilar());
        return true;
    }
}

