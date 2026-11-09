package co.edu.uniquindio.poo.Model.Punto1;

import co.edu.uniquindio.poo.Model.Estructuras.Pila;

// DOCUMENTACIÓN
// Punto 1: retiro de una caja de la pila y reorganización de las restantes
// según la paridad del valor de la caja retirada.
public class GestionarCajas {

    // Busca la caja con el código dado, la retira y reorganiza la pila:
    //  - valor retirado PAR   -> arriba las posiciones impares, luego las pares.
    //  - valor retirado IMPAR -> arriba las posiciones pares, luego las impares.
    // Las posiciones se cuentan desde la cima (1, 2, 3, ...) DESPUÉS de retirar la caja.
    // Cada grupo conserva el orden relativo que tenía.
    // Retorna true si la encontró; false si la pila está vacía o el código no existe
    // (en ese caso la pila queda exactamente igual).
    public static boolean retirarCaja(Pila<Caja> pila, String codigo) {
        if (pila.estaVacia()) return false;

        // 1. Buscar: se pasan a 'aux' las cajas que están encima de la buscada.
        Pila<Caja> aux = new Pila<>();
        while (!pila.estaVacia() && !pila.tope().getCodigo().equals(codigo)) {
            aux.apilar(pila.desapilar());
        }

        // 2. No se encontró: se devuelven todas las cajas (el orden queda igual).
        if (pila.estaVacia()) {
            while (!aux.estaVacia()) pila.apilar(aux.desapilar());
            return false;
        }

        // 3. Se encontró: está en la cima, se retira.
        Caja retirada = pila.desapilar();

        // 4. Se devuelven las cajas que estaban encima, quedando en su orden original.
        while (!aux.estaVacia()) pila.apilar(aux.desapilar());

        // 5. Se separan por posición (desde la cima). Al desapilar de 'pila' y apilar
        //    en 'impares'/'pares', cada grupo queda invertido (la última en entrar arriba).
        Pila<Caja> impares = new Pila<>();
        Pila<Caja> pares = new Pila<>();
        int posicion = 1;
        while (!pila.estaVacia()) {
            Caja c = pila.desapilar();
            if (posicion % 2 == 1) impares.apilar(c); else pares.apilar(c);
            posicion++;
        }

        // 6. Se reconstruye: primero el grupo que va al FONDO y luego el que va ARRIBA.
        //    Al pasar de nuevo de una pila a otra se invierte otra vez, recuperando
        //    el orden relativo original dentro de cada grupo.
        boolean valorPar = retirada.getValor() % 2 == 0;
        Pila<Caja> abajo = valorPar ? pares : impares;
        Pila<Caja> arriba = valorPar ? impares : pares;
        while (!abajo.estaVacia()) pila.apilar(abajo.desapilar());
        while (!arriba.estaVacia()) pila.apilar(arriba.desapilar());
        return true;
    }
}
