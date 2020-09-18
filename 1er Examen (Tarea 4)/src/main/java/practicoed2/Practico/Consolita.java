/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicoed2.Practico;

import Arbol.AVL;
import Arbol.ArbolB;
import Arbol.ArbolBinarioBusqueda;
import Arbol.ArbolMViasBusqueda;
import Arbol.ExcepcionClaveNoExiste;
import Arbol.ExcepcionClaveYaExiste;
import Arbol.IArbolBusqueda;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Sebastian Padilla
 */
public class Consolita {

    /**
     * @param args the command line arguments
     * @throws Arbol.ExcepcionClaveYaExiste
     * @throws Arbol.ExcepcionClaveNoExiste
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws ExcepcionClaveYaExiste, ExcepcionClaveNoExiste, IOException {
        // TODO code application logic here
        IArbolBusqueda<Integer, String> arbolMVias = new ArbolMViasBusqueda<>();
        IArbolBusqueda<Integer, String> arbolBinario = new ArbolBinarioBusqueda<>();
        IArbolBusqueda<Integer, String> arbolBinario2 = new ArbolBinarioBusqueda<>();
        IArbolBusqueda<Integer, String> AVL = new AVL<>();
        IArbolBusqueda<Integer, String> AVL2 = new AVL<>();

        arbolMVias.insertar(50, "diego");
        arbolMVias.insertar(60, "oscar");
        arbolMVias.insertar(40, "astete");
        arbolMVias.insertar(62, "enzo");
        arbolMVias.insertar(63, "julio");


        System.out.println("1) Pregunta 1");
        System.out.println("2) Pregunta 2 - (YA ESTAN INSERTADOS POR CONSOLA)");
        System.out.println("3) Pregunta 3 - (YA ESTA EL ARBOLMVIAS INSERTADO POR CONSOLA)");
        int num = - 1;
        Scanner entrada = new Scanner(System.in);
        while (num != 0) {
            num = entrada.nextInt();
            switch (num) {
                case 1:
                    arbolBinario.insertar(50, "diego");
                    arbolBinario.insertar(60, "oscar");
                    arbolBinario.insertar(40, "astete");
                    arbolBinario.insertar(62, "enzo");
                    arbolBinario.insertar(63, "julio");
                    System.out.println("ARBOL BINARIO DE BUSQUEDA QUE NO CUMPLE: ");
                    ((ArbolBinarioBusqueda) arbolBinario).mostrarArbol();
                    boolean d = ((ArbolBinarioBusqueda) arbolBinario).examen();
                    System.out.println("El arbol binario es monticulo? (Prueba falsa): " + d);
                    
                    arbolBinario2.insertar(50, "diego");
                    arbolBinario2.insertar(60, "oscar");
                    System.out.println("ARBOL BINARIO DE BUSQUEDA QUE CUMPLE: ");
                    ((ArbolBinarioBusqueda) arbolBinario2).mostrarArbol();
                    boolean w = ((ArbolBinarioBusqueda) arbolBinario2).examen();
                    System.out.println("El arbol binario es monticulo? (Prueba Verdadera): " + w);
                    break;

                case 2:

                    AVL.preg2(50, "diego");
                    AVL.preg2(60, "oscar");
                    AVL.preg2(40, "astete");
                    AVL.preg2(62, "enzo");
                    AVL.preg2(63, "julio");

                    AVL2.insertar(50, "diego");
                    AVL2.insertar(60, "oscar");
                    AVL2.insertar(40, "astete");
                    AVL2.insertar(62, "enzo");
                    AVL2.insertar(63, "julio");

                    System.out.println("ARBOL AVL INSERTAR ITERATIVO: ");
                    ((AVL) AVL).mostrarArbol();
                    System.out.println("ARBOL AVL INSERTAR RECURSIVO: ");
                    ((AVL) AVL2).mostrarArbol();

                    break;

                case 3:
                    ((ArbolMViasBusqueda) arbolMVias).verArbol();
                    int z = arbolMVias.ejercicio3Ex3(0);
                    System.out.println("La Cantidad de nodos Padres fuera del nivel insertado por consola es : " + z);
                    break;

            }
        }
    }

}
