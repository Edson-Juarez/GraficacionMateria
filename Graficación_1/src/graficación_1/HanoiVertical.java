/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graficación_1;

/**
 *
 * @author edson
 */
import java.util.LinkedList;
import java.util.Queue;

public class HanoiVertical {

    Queue<Integer>[] torres;

    public HanoiVertical(int numDiscos) {
        torres = new LinkedList[3];
        for (int i = 0; i < 3; i++) {
            torres[i] = new LinkedList<>();
        }

        for (int i = numDiscos; i >= 1; i--) {
            torres[0].add(i); // Inicialmente, todos los discos están en la torre 0.
        }
    }

    public void resolverHanoi(int numDiscos, int origen, int auxiliar, int destino) {
        if (numDiscos == 1) {
            int disco = torres[origen].remove(); // Mueve el disco superior del origen al destino.
            torres[destino].add(disco);
            imprimirTorres(); // Imprime el estado actual de las torres.
            return;
        }
        if (!torres[origen].isEmpty()) {
            resolverHanoi(numDiscos - 1, origen, destino, auxiliar); // Mueve n - 1 discos a la torre auxiliar.
            int disco = torres[origen].remove(); // Mueve el disco restante del origen al destino.
            torres[destino].add(disco);
            imprimirTorres(); // Imprime el estado actual de las torres.
            resolverHanoi(numDiscos - 1, auxiliar, origen, destino); // Mueve n - 1 discos de la torre auxiliar al destino.
        } else {
            resolverHanoi(numDiscos - 1, auxiliar, origen, destino); // Mueve n - 1 discos de la torre auxiliar al destino.
        }
    }

    public void imprimirTorres() {
        int numDiscos = torres[0].size() + torres[1].size() + torres[2].size();
        int maxDiscos = numDiscos;
        // Recorremos verticalmente cada nivel de las torres.
        for (int nivel = 1; nivel <= maxDiscos; nivel++) {
            for (int i = 0; i < 3; i++) {
                if (!torres[i].isEmpty()) {
                    if (torres[i].size() >= nivel) {
                        int disco = torres[i].peek();
                        String discoStr = String.valueOf(disco);
                        int relleno = numDiscos - discoStr.length();
                        // Llenamos con espacios en blanco a la izquierda para centrar el disco.
                        for (int j = 0; j < relleno; j++) {
                            System.out.print(" ");
                        }
                        System.out.print(discoStr);
                    } else {
                        int relleno = numDiscos;
                        // Si no hay disco en este nivel, imprimimos espacios en blanco.
                        for (int j = 0; j < relleno; j++) {
                            System.out.print(" ");
                        }
                    }
                } else {
                    int relleno = numDiscos;
                    // Si no hay disco en esta torre, imprimimos espacios en blanco.
                    for (int j = 0; j < relleno; j++) {
                        System.out.print(" ");
                    }
                }
                if (i < 2) {
                    System.out.print(" | "); // Separador entre torres.
                }
            }
            System.out.println(); // Salto de línea al final de cada nivel.
        }
        // Línea de separación entre torres.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < numDiscos; j++) {
                System.out.print("-");
            }
            if (i < 2) {
                System.out.print("|"); // Separador vertical al final de cada torre.
            }
        }
        System.out.println(); // Salto de línea al final de la representación de las torres.
        System.out.println(); // Línea adicional para separar las representaciones de las torres.
    }

    public static void main(String[] args) {
        HanoiVertical hanoi = new HanoiVertical(6);
        hanoi.resolverHanoi(6, 0, 1, 2);
    }
}
