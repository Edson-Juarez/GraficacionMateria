/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.*;

public class PuzzleProfundidad {
    // Definimos las direcciones posibles de movimiento en el tablero
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    // Usamos un HashSet para almacenar los estados visitados
    Set<String> visitados = new HashSet<>();

    // Usamos una pila para almacenar los nodos a visitar
    Stack<Nodo> pila = new Stack<>();

    public static void main(String[] args) {
        // Definimos el estado inicial del tablero
        int[][] tableroInicial = {
            {5, 6, 7, 8},
            {9, 1, 2, 10},
            {11, 3, 4, 12},
            {13, 0, 14, 15}
        };

        // Llamamos al método resolver con el estado inicial
        new PuzzleProfundidad().resolver(tableroInicial);
    }

    void resolver(int[][] tableroInicial) {
        // Creamos un nodo con el estado inicial y lo añadimos a la pila
        Nodo nodoInicial = new Nodo(tableroInicial, "", 0);
        pila.push(nodoInicial);

        // Mientras la pila no esté vacía
        while (!pila.isEmpty()) {
            // Sacamos el primer nodo de la pila
            Nodo nodoActual = pila.pop();

            // Añadimos su estado a los estados visitados
            visitados.add(nodoActual.estado);

            // Si el nodo actual es el estado meta
            if (nodoActual.esMeta()) {
                // Imprimimos los movimientos necesarios para llegar a la solución y terminamos
                System.out.println("Se resolvió el rompecabezas en " + nodoActual.costo + " pasos.");
                return;
            }

            // Para cada dirección posible de movimiento
            for (int i = 0; i < 4; i++) {
                int nx = nodoActual.x + dx[i];
                int ny = nodoActual.y + dy[i];

                // Si el movimiento es válido (no sale del tablero)
                if (nx >= 0 && ny >= 0 && nx < 4 && ny < 4) {
                    // Creamos un nuevo nodo copiando el estado actual y realizamos el movimiento
                    Nodo adyacente = new Nodo(nodoActual.tablero, nodoActual.movimientos, nodoActual.costo + 1);
                    adyacente.intercambiar(nx, ny);

                    // Si el nuevo estado no ha sido visitado antes, lo añadimos a la pila
                    if (!visitados.contains(adyacente.estado)) {
                        pila.push(adyacente);
                        System.out.println("Paso " + adyacente.costo + ":");
                        adyacente.imprimirTablero(); // Imprime el tablero después de cada movimiento
                    }
                }
            }
        }
    }

    class Nodo {
        int[][] tablero;
        String movimientos;
        int costo;
        String estado;
        int x, y;

        Nodo(int[][] tablero, String movimientos, int costo) {
            this.tablero = new int[4][4];

            // Copiamos el tablero y encontramos la posición del espacio vacío (0)
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    this.tablero[i][j] = tablero[i][j];
                    if (tablero[i][j] == 0) {
                        x = i;
                        y = j;
                    }
                }
            }

            this.movimientos = movimientos;
            this.costo = costo;

            // Convertimos el estado del tablero a una cadena para poder almacenarlo en el HashSet de estados visitados
            this.estado = Arrays.deepToString(this.tablero);
        }

        void intercambiar(int nx, int ny) {
            // Realizamos el movimiento intercambiando el espacio vacío con la celda adyacente
            int temp = tablero[x][y];
            tablero[x][y] = tablero[nx][ny];
            tablero[nx][ny] = temp;

            x = nx;
            y = ny;

            // Actualizamos la cadena que representa el estado después del movimiento
            estado = Arrays.deepToString(tablero);
        }

        boolean esMeta() {
            // Comprobamos si el estado actual es el estado meta
            return estado.equals("[[1 ,2 ,3 ,4], [5 ,6 ,7 ,8], [9 ,10 ,11 ,12], [13 ,14 ,15 ,0]]");
        }

        void imprimirTablero() { // Función para imprimir el tablero
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero[0].length; j++) {
                    System.out.printf("%2d ", tablero[i][j]);
                }
                System.out.println();
            }
            System.out.println(); // Agrega una línea en blanco entre cada paso para facilitar la lectura
        }
    }
}
