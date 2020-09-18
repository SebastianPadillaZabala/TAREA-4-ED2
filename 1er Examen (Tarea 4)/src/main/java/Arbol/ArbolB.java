/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arbol;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Sebastian Padilla
 * @param <K>
 * @param <V>
 */
public class ArbolB<K extends Comparable<K>, V> extends ArbolMViasBusqueda<K, V> {

    private int nroMaximoDatos;
    private int nroMinimoDatos;
    private int nroMinimoHijos;

    public ArbolB() {
        super();
        this.nroMaximoDatos = 2;
        this.nroMinimoDatos = 1;
        this.nroMinimoHijos = 2;
    }

    public ArbolB(int orden) throws ExcepcionOrdenInvalido {
        super(orden);
        this.nroMaximoDatos = super.orden - 1;
        this.nroMinimoDatos = this.nroMaximoDatos / 2;
        this.nroMinimoHijos = this.nroMinimoDatos + 1;
    }

    @Override
    public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<K, V>(orden + 1, clave, valor);
            return;
        }
        Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (this.existeClaveEnNodo(nodoActual, clave)) {
                throw new ExcepcionClaveYaExiste();
            }
            if (nodoActual.esHoja()) {
                super.insertarEnOrden(nodoActual, clave, valor);
                if (nodoActual.cantidadDeDatosNoVacios() > this.nroMaximoDatos) {
                    this.dividir(nodoActual, pilaDeAncestros);
                }
                nodoActual = NodoMVias.nodoVacio();
            } else {
                int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
                pilaDeAncestros.push(nodoActual);
                nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
            }
        }//fin while

    }

    @Override
    public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
        Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K, V> nodoActual = this.buscarNodoDeClave(claveAEliminar, pilaDeAncestros);
        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new ExcepcionClaveNoExiste();
        }

        int posicionDeLaClaveDelNodo = this.porDondeBajar(nodoActual, claveAEliminar) - 1;
        V valorARetornar = nodoActual.getValor(posicionDeLaClaveDelNodo);
        if (nodoActual.esHoja()) {
            super.eliminarDatoEnNodo(nodoActual, posicionDeLaClaveDelNodo);
            if (nodoActual.cantidadDeDatosNoVacios() < this.nroMinimoDatos) {
                if (pilaDeAncestros.isEmpty()) {
                    if (nodoActual.estanDatosVacios()) {
                        super.vaciar();
                    }

                } else {
                    this.prestarOFusionar(nodoActual, pilaDeAncestros);
                }
            }
        } else { //nodoactual no es hoja
            pilaDeAncestros.push(nodoActual);
            NodoMVias<K, V> nodoDelPredecesor = this.buscarNodoDelPredecesor(pilaDeAncestros,
                    nodoActual.getHijo(posicionDeLaClaveDelNodo));
            int posicionDelPredecesor = nodoDelPredecesor.cantidadDeDatosNoVacios() - 1;
            K clavePredecesora = nodoDelPredecesor.getClave(posicionDelPredecesor);
            V valorPredecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
            super.eliminarDatoEnNodo(nodoDelPredecesor, posicionDelPredecesor);
            nodoActual.setClave(posicionDeLaClaveDelNodo, clavePredecesora);
            nodoActual.setValor(posicionDelPredecesor, valorPredecesor);
            if (nodoDelPredecesor.cantidadDeDatosNoVacios() < this.nroMinimoDatos) {
                this.prestarOFusionar(nodoDelPredecesor, pilaDeAncestros);
            }
        }
        return valorARetornar;
    }

    private void dividir(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private NodoMVias<K, V> buscarNodoDeClave(K claveABuscar, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int tamañoDelNodoActual = nodoActual.cantidadDeDatosNoVacios();
            NodoMVias<K, V> nodoAnterior = nodoActual;
            for (int i = 0; i < tamañoDelNodoActual && nodoAnterior == nodoActual; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual;
                }
                if (claveABuscar.compareTo(claveActual) < 0) {
                    if (!nodoActual.esHoja()) {
                        pilaDeAncestros.push(nodoActual);
                        nodoActual = nodoActual.getHijo(i);
                    } else {
                        nodoActual = NodoMVias.nodoVacio();
                    }
                }
            }//fin for
            if (nodoAnterior == nodoActual) {
                if (!nodoActual.esHoja()) {
                    pilaDeAncestros.push(nodoActual);
                    nodoActual = nodoActual.getHijo(tamañoDelNodoActual);
                } else {
                    nodoActual = NodoMVias.nodoVacio();
                }
            }
        }//fin while

        return NodoMVias.nodoVacio();
    }

    public int ejercicio5P3() {
        return this.ejercicio5P3(this.raiz);
    }

    private int ejercicio5P3(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (this.nroMaximoDatos > nodoActual.cantidadDeDatosNoVacios()) {
            return 1;
        }
        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + this.ejercicio5P3(nodoActual.getHijo(i));
        }

        return cantidad;
    }

    public int ejercicio7P3(int nivelObjetivo) {
        return this.ejercicio7P3(this.raiz, nivelObjetivo, 0);
    }

    private int ejercicio7P3(NodoMVias<K, V> nodoActual, int nivelObjetivo, int nivelActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        if (this.nroMaximoDatos > nodoActual.cantidadDeDatosNoVacios()
                && nivelObjetivo == nivelActual) {
            return 1;
        }

        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + this.ejercicio7P3(nodoActual.getHijo(i), nivelObjetivo, nivelActual + 1);
        }
        return cantidad;
    }

    public int ejercicio8P3(int nivelObjetivo) {
        int nivelActual = 0;
        int cantidad = 0;
        if (this.esArbolVacio()) {
            return 0;
        }
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int cantidasDelnodosNivel = colaDeNodos.size();
            while (cantidasDelnodosNivel > 0) {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {

                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                    if (this.nroMaximoDatos > nodoActual.cantidadDeDatosNoVacios()
                            && nivelObjetivo == nivelActual) {
                        cantidad++;
                    }
                }
                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
                cantidasDelnodosNivel--;
            }
            nivelActual++;
        }
        return cantidad;
    }

    private void prestarOFusionar(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private NodoMVias<K, V> buscarNodoDelPredecesor(Stack<NodoMVias<K, V>> pilaDeAncestros, NodoMVias<K, V> hijo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
