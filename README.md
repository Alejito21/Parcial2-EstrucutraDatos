# Parcial 2 - Estructuras de Datos

Universidad del Quindío · Ingeniería de Sistemas y Computación.

Proyecto Java (Maven) con las soluciones del parcial. Todas las estructuras son propias
(`Pila` y `Cola` con nodos enlazados); no se usan colecciones de Java en los ejercicios.

## Estructura

```
Parcial_2/src/main/java/co/edu/uniquindio/poo/
├── Main/Main.java                 Consola: ejecuta los datos de prueba y verifica resultados
├── Main/MainGUI.java              Interfaz gráfica (Swing)
├── Vista/                         Ventana con una pestaña por punto + PanelArbol (dibujo de árboles)
└── Model/
    ├── Estructuras/  Pila, Cola   Estructuras propias
    │                 NodoArbol, DibujoArbol (dibujo en texto de cualquier árbol)
    ├── Punto1/       Caja, GestionarCajas (retirarCaja), Expresiones (infija→posfija, evaluación)
    ├── Punto2/       Historial (obtenerSegmento)
    ├── Punto3/       Paquete, Ruta (seleccionarPaquetes, recursivo sin ciclos)
    ├── Punto4/       NodoABB, ArbolABB (contarEquiposEnRango, eliminar, recorridos)
    └── Punto5/       NodoAVL, ArbolAVL (inserción/eliminación paso a paso con FE y rotaciones)
                      NodoRN, ArbolRN (inserción paso a paso con recoloreos y rotaciones), Paso
```

## Ejecución

Desde IntelliJ:
- `co.edu.uniquindio.poo.Main.MainGUI`: interfaz gráfica.
- `co.edu.uniquindio.poo.Main.Main`: consola con verificaciones automáticas.

Desde la terminal (requiere JDK 24 o superior):

```bash
cd Parcial_2
javac -encoding UTF-8 -d out $(find src -name '*.java')
java -cp out co.edu.uniquindio.poo.Main.MainGUI   # interfaz gráfica
java -cp out co.edu.uniquindio.poo.Main.Main      # consola
```

## Interfaz gráfica

Cada pestaña arranca con los datos del enunciado cargados y permite probar otros casos:

| Pestaña | Qué permite |
|---|---|
| 1. Cajas y expresión | Apilar cajas, `retirarCaja`, prueba completa C3 → C4 → C7 y tablas de conversión/evaluación de la expresión |
| 2. Historial | Apilar acciones y `obtenerSegmento(inicio, fin)` mostrando la pila original y el resultado |
| 3. Paquetes | Encolar paquetes, `seleccionarPaquetes(capacidad)`: evaluación de cada paquete, seleccionados, pendientes y capacidad restante |
| 4. ABB | Árbol dibujado, insertar/eliminar, `contarEquiposEnRango` y los cuatro recorridos |
| 5. AVL y Rojo-Negro | Construcción, eliminación y Rojo-Negro paso a paso: cada paso con su descripción y el árbol dibujado (FE en cada nodo del AVL, colores en el Rojo-Negro) |

## Punto 5: convenciones

- Factor de equilibrio: **FE = altura(derecho) − altura(izquierdo)**.
- Las rotaciones dobles se muestran en sus dos pasos (dos rotaciones simples).
- Al eliminar un nodo con dos hijos se reemplaza por el **mayor de su subárbol izquierdo**.
- El árbol Rojo-Negro se construye con el **postorden del AVL después de eliminar "cine"**.
- Los árboles se imprimen acostados: la raíz a la izquierda, el subárbol derecho arriba.

Cada resultado se compara con el valor esperado y se marca `[OK]` o `[FALLA]`.
