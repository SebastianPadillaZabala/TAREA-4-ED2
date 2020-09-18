/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arbol;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Sebastian Padilla
 * @param <K>
 * @param <V>
 */
public class ArbolBinarioBusqueda<K extends Comparable<K>, V>
        implements IArbolBusqueda<K, V> {

    NodoBinario<K, V> raiz;

    @Override
    public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(clave, valor);
            return;
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            if (clave.compareTo(nodoActual.getClave()) == 0) {
                throw new ExcepcionClaveYaExiste();//nodo ya existe
            }
            if (clave.compareTo(nodoActual.getClave()) < 0) {
                nodoActual = nodoActual.getHijoIzquierdo();

            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
        NodoBinario<K, V> nuevoNodo = new NodoBinario<>(clave, valor);
        if (clave.compareTo(nodoAnterior.getClave()) > 0) {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        } else {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        }
    }

    @Override
    public V eliminar(K clave) throws ExcepcionClaveNoExiste {
        V valorARetornar = buscar(clave);
        if (valorARetornar == null) {
            throw new ExcepcionClaveNoExiste();
        }
        this.raiz = eliminar(this.raiz, clave);
        return valorARetornar;
    }

    private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodoActual, K claveAEliminar)
            throws ExcepcionClaveNoExiste {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            throw new ExcepcionClaveNoExiste();
        }
        //BUSCANDO EL NODO CON RESPECTO A SU CLAVE
        K claveActual = nodoActual.getClave();
        if (claveAEliminar.compareTo(claveActual) > 0) {
            NodoBinario<K, V> supuestoNuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return nodoActual;
        }
        if (claveAEliminar.compareTo(claveActual) < 0) {
            NodoBinario<K, V> supuestoNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return nodoActual;
        }
        //CASO 1 --- ES UN NODO HOJA
        if (nodoActual.esHoja()) {
            return (NodoBinario<K, V>) NodoBinario.nodoVacio();
        }
        //CASO 2 --- SOLO TIENE UN HIJO
        if (!nodoActual.esHijoDerechoVacio() && nodoActual.esHijoIzquiedoVacio()) {
            return nodoActual.getHijoDerecho();
        }
        if (nodoActual.esHijoDerechoVacio() && !nodoActual.esHijoIzquiedoVacio()) {
            return nodoActual.getHijoIzquierdo();
        }
        //CASO 3 --- TIENE DOS HIJOS
        //BUSCAR EL REEMPLAZO
        NodoBinario<K, V> nodoReemplazo = this.buscarNodoSucesor(nodoActual.getHijoDerecho());
        ///ELIMINAR EL NODO SUCESOR QUE TOMARA 
        NodoBinario<K, V> posibleNuevoHijo = eliminar(nodoActual.getHijoDerecho(), nodoReemplazo.getClave());
        nodoActual.setHijoDerecho(posibleNuevoHijo);
        //ENLAZAR EL NODO REEMPLAZO A SUS NUEVAS DIRECCIONES QUE TENIA EL NODOACUAL
        nodoReemplazo.setHijoDerecho(nodoActual.getHijoDerecho());
        nodoReemplazo.setHijoIzquierdo(nodoActual.getHijoIzquierdo());
        //ANULAR EL NODO A ELIMIANR NULEANDO SUS HIJOS
        nodoActual.setHijoDerecho(null);
        nodoActual.setHijoIzquierdo(null);

        return nodoReemplazo;

    }

    @Override
    public V buscar(K clave) {
        NodoBinario<K, V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)) {

            if (clave.compareTo(nodoActual.getClave()) == 0) {
                return nodoActual.getValor();
            }
            if (clave.compareTo(nodoActual.getClave()) < 0) {
                nodoActual = nodoActual.getHijoIzquierdo();

            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
        return (V) NodoBinario.nodoVacio();
    }

    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != null;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        NodoBinario<K, V> nodoActual;
        NodoBinario<K, V> nImprimir;
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual) || !pilaDeNodos.isEmpty()) {
            while (!NodoBinario.esNodoVacio(nodoActual)) {
                pilaDeNodos.push(nodoActual);
                nodoActual = nodoActual.getHijoIzquierdo();
            }
            if (NodoBinario.esNodoVacio(nodoActual) && !pilaDeNodos.isEmpty()) {
                nImprimir = pilaDeNodos.pop();
                recorrido.add(nImprimir.getClave());
                nodoActual = nImprimir.getHijoDerecho();
            }
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        NodoBinario<K, V> nodoActual;
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(this.raiz);
        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esHijoDerechoVacio()) {
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esHijoIzquiedoVacio()) {
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }
        }
        return recorrido;
    }

    private void Ayuda(Stack<NodoBinario<K, V>> pilaDeNodos, NodoBinario<K, V> Actual) {
        while (!NodoBinario.esNodoVacio(Actual)) {
            pilaDeNodos.push(Actual);
            if (!Actual.esHijoIzquiedoVacio()) {
                Actual = Actual.getHijoIzquierdo();
            } else {
                Actual = Actual.getHijoDerecho();
            }
        }
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        this.Ayuda(pilaDeNodos, nodoActual);
        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!pilaDeNodos.isEmpty()) {
                NodoBinario<K, V> Tope = pilaDeNodos.peek();
                if (!Tope.esHijoDerechoVacio() && Tope.getHijoDerecho() != nodoActual) {
                    this.Ayuda(pilaDeNodos, Tope.getHijoDerecho());
                }
            }

        }
        return recorrido;
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        NodoBinario<K, V> nodoActual;
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esHijoIzquiedoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return recorrido;
    }

    @Override
    public int size() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantidadDeNodos = 0;
        NodoBinario<K, V> nodoActual;
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            nodoActual = colaDeNodos.poll();
            cantidadDeNodos++;
            if (!nodoActual.esHijoIzquiedoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }

        }
        return cantidadDeNodos;
    }

    private int size(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidadNodoHijoIzquierdo = this.size(nodoActual.getHijoIzquierdo());
        int cantidadNodoHijoDerecho = this.size(nodoActual.getHijoDerecho());
        return cantidadNodoHijoIzquierdo + cantidadNodoHijoDerecho + 1;
    }

    @Override
    public int altura() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int alturaActual = 0;
        NodoBinario<K, V> nodoActual;
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int cantidadDeNodosDelNivelActual = colaDeNodos.size();
            while (cantidadDeNodosDelNivelActual > 0) {
                nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHijoIzquiedoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                cantidadDeNodosDelNivelActual--;
            }
            alturaActual++;
        }
        return alturaActual;
    }

    protected int altura2(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaPorDerecha = this.altura2(nodoActual.getHijoDerecho());
        int alturaPorIzquierda = this.altura2(nodoActual.getHijoIzquierdo());
        return alturaPorDerecha > alturaPorIzquierda ? alturaPorDerecha + 1
                : alturaPorIzquierda + 1;
    }

    //PRACTICO#1 ARBOLES BINARIOS
    //Implemente un método recursivo que retorne la cantidad nodos hojas que existen en un árbol binario 
    public int ejercicio1() {
        return ejercicio1(this.raiz);
    }

    private int ejercicio1(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantHojasIzquierda = ejercicio1(nodoActual.getHijoIzquierdo());
        int cantHojasDerecha = ejercicio1(nodoActual.getHijoDerecho());
        if (nodoActual.esHoja()) {
            return cantHojasIzquierda + cantHojasDerecha + 1;
        }
        return cantHojasIzquierda + cantHojasDerecha;
    }
    // Implemente un método iterativo que retorne la cantidad nodos hojas que existen en un árbol binario 

    public int ejercicio2() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantHojas = 0;
        NodoBinario<K, V> nodoActual;
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int cantidadDeNodosActuales = colaDeNodos.size();
            while (cantidadDeNodosActuales > 0) {
                nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                if (!nodoActual.esHijoIzquiedoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (nodoActual.esHoja()) {
                    cantHojas++;
                }
                cantidadDeNodosActuales--;
            }
        }
        return cantHojas;
    }

    //Implemente un método recursivo que retorne la cantidad nodos hojas que existen en un árbol binario, pero solo en el nivel N 
    public int ejercicio3(int delnivel) {
        return ejercicio3(this.raiz, delnivel, 0);
    }

    private int ejercicio3(NodoBinario nodoActual, int delNivel, int nivelActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantHojasIzquierda = ejercicio3(nodoActual.getHijoIzquierdo(), delNivel, nivelActual + 1);
        int cantHojasDerecha = ejercicio3(nodoActual.getHijoDerecho(), delNivel, nivelActual + 1);
        if (nodoActual.esHoja() && delNivel == nivelActual) {
            return cantHojasIzquierda + cantHojasDerecha + 1;
        }
        return cantHojasIzquierda + cantHojasDerecha;
    }

    //Implemente un método iterativo que retorne la cantidad nodos hojas que existen en un árbol binario, pero solo en el nivel N
    public int ejercicio4(int nivel) {
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantHojas = 0;
        int nivelActual = 0;
        NodoBinario<K, V> nodoActual;
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int cantidadDeNodosActuales = colaDeNodos.size();
            while (cantidadDeNodosActuales > 0) {
                nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                if (!nodoActual.esHijoIzquiedoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (nodoActual.esHoja() && nivel == nivelActual) {
                    cantHojas++;
                }
                cantidadDeNodosActuales--;
            }
            nivelActual++;
        }
        return cantHojas;
    }

    //Implemente un método iterativo que retorne la cantidad nodos hojas que existen en un árbol binario, pero solo antes del nivel N 
    public int ejercicio5(int nivel) {
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantHojas = 0;
        int nivelActual = 0;
        NodoBinario<K, V> nodoActual;
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int cantidadDeNodosActuales = colaDeNodos.size();
            while (cantidadDeNodosActuales > 0) {
                nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                if (!nodoActual.esHijoIzquiedoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (nodoActual.esHoja() && nivel > nivelActual) {
                    cantHojas++;
                }
                cantidadDeNodosActuales--;
            }
            nivelActual++;
        }
        return cantHojas;
    }
    //Implemente un método recursivo que retorne verdadero, si un árbol binario esta balanceado según las reglas que establece un árbol AVL, 
    //falso en caso contrario. 
    // Un árbol binario bien formado se dice que está "equilibrado en altura" si (1) está vacío, 
    //o (2) sus hijos izquierdo y derecho están equilibrados en altura y la altura del árbol izquierdo está dentro de 1 de Altura del árbol correcto.

    public boolean ejercicio6() {
        return ejercicio6(this.raiz);
    }

    private boolean ejercicio6(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return true;
        }
        return ejercicio6(nodoActual.getHijoIzquierdo()) && ejercicio6(nodoActual.getHijoDerecho())
                && Math.abs(altura2(nodoActual.getHijoIzquierdo()) - altura2(nodoActual.getHijoDerecho())) <= 1;
    }

    //Implemente un método iterativo que la lógica de un recorrido en postorden que retorne verdadero,
    //si un árbol binario esta balanceado según las reglas que establece un árbol AVL, falso en caso contrario.
    public boolean ejercicio7() {
        if (this.esArbolVacio()) {
            return true;
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        this.Ayuda(pilaDeNodos, nodoActual);
        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            if (!pilaDeNodos.isEmpty()) {
                NodoBinario<K, V> Tope = pilaDeNodos.peek();
                if (Math.abs(altura2(Tope.getHijoIzquierdo()) - altura2(Tope.getHijoDerecho())) <= 1) {
                    if (!Tope.esHijoDerechoVacio() && Tope.getHijoDerecho() != nodoActual) {
                        this.Ayuda(pilaDeNodos, Tope.getHijoDerecho());
                    }
                } else {
                    return false;
                }
            }

        }
        return true;
    }

    //Implemente un método que reciba en listas de parámetros las llaves y valores 
    //de los recorridos en preorden e inorden respectivamente y que reconstruya el árbol binario original. Su método no debe usar el método insertar. 
    private int buscarPosicionEnLaLista(List<K> recorridoInOrdenClaves, K claveABuscar) {
        int posicion = 0;
        boolean bandera = false;
        int longitud = recorridoInOrdenClaves.size();
        while (longitud >= 0 && bandera == false) {
            K claveActual = recorridoInOrdenClaves.get(posicion);
            if (claveActual.compareTo(claveABuscar) == 0) {
                bandera = true;
            }
            posicion++;
            longitud--;
        }
        return posicion;
    }

    public NodoBinario<K, V> ejercicio8(List<K> recorridoPreOrdenClaves, List<V> recorridoPreOrdenValor,
            List<K> recorridoInOrdenClaves, List<V> recorridoInOrdenValor) {
        if (!recorridoPreOrdenClaves.isEmpty()) {
            int posClaveEnPreOrden = 0;
            K claveDeNodoActual = recorridoPreOrdenClaves.get(posClaveEnPreOrden);
            V valorDeNodoActual = recorridoPreOrdenValor.get(posClaveEnPreOrden);
            int posClaveEnInOrden = this.buscarPosicionEnLaLista(recorridoInOrdenClaves, claveDeNodoActual);
            List<K> sublistaClavesPreOrdenIzq = recorridoPreOrdenClaves.subList(posClaveEnPreOrden + 1, posClaveEnInOrden);
            List<V> sublistaValorPreOrdenIzq = recorridoPreOrdenValor.subList(posClaveEnPreOrden + 1, posClaveEnInOrden);
            List<K> sublistaClavesInOrdenIzq = recorridoInOrdenClaves.subList(posClaveEnPreOrden, posClaveEnInOrden - 1);
            List<V> sublistaValorInOrdenIzq = recorridoInOrdenValor.subList(posClaveEnPreOrden, posClaveEnInOrden - 1);

            List<K> sublistaClavesPreOrdenDer = recorridoPreOrdenClaves.subList(posClaveEnPreOrden + 1, recorridoPreOrdenClaves.size() - 1);
            List<V> sublistaValorPreOrdenDer = recorridoPreOrdenValor.subList(posClaveEnPreOrden + 1, recorridoPreOrdenValor.size() - 1);
            List<K> sublistaClavesInOrdenDer = recorridoInOrdenClaves.subList(posClaveEnInOrden + 1, recorridoInOrdenClaves.size() - 1);
            List<V> sublistaValorInOrdenDer = recorridoInOrdenValor.subList(posClaveEnInOrden + 1, recorridoInOrdenValor.size() - 1);
            NodoBinario<K, V> nodoActual = new NodoBinario<K, V>(claveDeNodoActual, valorDeNodoActual);
            NodoBinario<K, V> hijoIzquierdoDelNodoActual = this.ejercicio8(sublistaClavesPreOrdenIzq, sublistaValorPreOrdenIzq, sublistaClavesInOrdenIzq, sublistaValorInOrdenIzq);
            NodoBinario<K, V> hijoDerechoDelNodoActual = this.ejercicio8(sublistaClavesPreOrdenDer, sublistaValorPreOrdenDer, sublistaClavesInOrdenDer, sublistaValorInOrdenDer);
            nodoActual.setHijoIzquierdo(hijoIzquierdoDelNodoActual);
            nodoActual.setHijoDerecho(hijoDerechoDelNodoActual);
            return nodoActual;
        }
        return NodoBinario.nodoVacio();
    }

    // Implemente un método privado que reciba un nodo binario de un árbol binario y que retorne cual sería su sucesor inorden de la clave de dicho nodo.
    public NodoBinario<K, V> buscar2(K clave) {
        NodoBinario<K, V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)) {

            if (clave.compareTo(nodoActual.getClave()) == 0) {
                return nodoActual;
            }
            if (clave.compareTo(nodoActual.getClave()) < 0) {
                nodoActual = nodoActual.getHijoIzquierdo();

            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
        return NodoBinario.nodoVacio();
    }

    public K ejercicio9(K clave) {
        NodoBinario<K, V> nodoActual = buscar2(clave);
        nodoActual = ejercicio9(nodoActual);
        return nodoActual.getClave();
    }

    private NodoBinario<K, V> ejercicio9(NodoBinario<K, V> nodoActual) {
        nodoActual = nodoActual.getHijoDerecho();
        while (!nodoActual.esHijoIzquiedoVacio()) {
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoActual;
    }

    // Implemente un método privado que reciba un nodo binario de un árbol binario y que retorne cuál sería su predecesor inorden de la clave de dicho nodo. 
    public K ejercicio10(K clave) {
        NodoBinario<K, V> nodoActual = buscar2(clave);
        nodoActual = ejercicio10(nodoActual);
        return nodoActual.getClave();
    }

    private NodoBinario<K, V> ejercicio10(NodoBinario<K, V> nodoActual) {
        nodoActual = nodoActual.getHijoIzquierdo();
        while (!nodoActual.esHijoDerechoVacio()) {
            nodoActual = nodoActual.getHijoDerecho();
        }
        return nodoActual;
    }
    // "El Ejercicio11 se encuentra en la clase AVL"
    //----------------------------------------------- 

    //Para un árbol binario implemente un método que retorne la cantidad de nodos que tienen ambos hijos luego del nivel N.  
    public int ejercicio12(int nivel) {
        if (this.esArbolVacio()) {
            return 0;
        }
        int nivelActual = 0;
        int cantNodos = 0;
        NodoBinario<K, V> nodoActual;
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);
        while (!colaDeNodos.isEmpty()) {
            int cantidadDeNodosActuales = colaDeNodos.size();
            while (cantidadDeNodosActuales > 0) {
                nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                if (!nodoActual.esHijoIzquiedoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }

                if (nivelActual > nivel && !nodoActual.esHijoDerechoVacio() && !nodoActual.esHijoIzquiedoVacio()) {
                    cantNodos++;
                }
                cantidadDeNodosActuales--;
            }
            nivelActual++;
        }
        return cantNodos;
    }

    @Override
    public void mostrarArbol() {
        if (this.esArbolVacio()) {
            System.out.println("arbol vacio");
        }
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);
        List<K> listaInOrden = this.recorridoEnInOrden();
        while (!colaDeNodos.isEmpty()) {
            int nodosDelNivel = colaDeNodos.size();
            String nivel = "";
            Queue<NodoBinario<K, V>> colaDeNodosAnterior = new LinkedList<>();
            while (nodosDelNivel > 0) {
                NodoBinario<K, V> nodoActual = colaDeNodos.poll();
                colaDeNodosAnterior.offer(nodoActual);
                if (!nodoActual.esHijoIzquiedoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                nodosDelNivel = nodosDelNivel - 1;
            }
            imprimirNivel(colaDeNodosAnterior, listaInOrden, nivel);
        }
    }

    @Override
    public void vaciar() {
        this.raiz = this.nodoVacioParaElArbol();
    }

    @Override
    public boolean esArbolVacio() {
        return this.raiz == null;
    }

    @Override
    public int nivel() {
        return this.altura2(this.raiz) - 1;
    }

    public NodoBinario<K, V> buscarNodoSucesor(NodoBinario<K, V> nodoActual) {
        if (!nodoActual.esHijoIzquiedoVacio()) {
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoActual;
    }

    public NodoBinario<K, V> buscarNodoPredecesor(NodoBinario<K, V> nodoActual) {
        if (!nodoActual.esHijoDerechoVacio()) {
            nodoActual = nodoActual.getHijoDerecho();
        }
        return nodoActual;
    }

    private void imprimirNivel(Queue<NodoBinario<K, V>> colaDeNodos, List<K> listaInOrden, String nivelAImprimir) {
        int indice = 0;
        while (!colaDeNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = colaDeNodos.peek();
            if (listaInOrden.get(indice).compareTo(nodoActual.getClave()) == 0) {
                nodoActual = colaDeNodos.poll();
                if (!nodoActual.esHoja()) {
                    if (!nodoActual.esHijoIzquiedoVacio()) {
                        nivelAImprimir = nivelAImprimir + '-';
                        nivelAImprimir = nivelAImprimir + nodoActual.getClave();
                    } else {
                        nivelAImprimir = nivelAImprimir + '-';
                    }
                    if (!nodoActual.esHijoDerechoVacio() && nodoActual.esHijoIzquiedoVacio()) {
                        nivelAImprimir = nivelAImprimir + nodoActual.getClave();
                        nivelAImprimir = nivelAImprimir + '-';
                    } else if (!nodoActual.esHijoDerechoVacio() && !nodoActual.esHijoIzquiedoVacio()) {
                        nivelAImprimir = nivelAImprimir + '-';
                    } else {
                        nivelAImprimir = nivelAImprimir + '-';
                    }
                } else {
                    nivelAImprimir = nivelAImprimir + '-';
                    nivelAImprimir = nivelAImprimir + nodoActual.getClave();
                    nivelAImprimir = nivelAImprimir + ' ';
                }
            } else {
                nivelAImprimir = nivelAImprimir + "     ";
            }
            indice++;
        }
        System.out.println(nivelAImprimir);
    }

    private NodoBinario<K, V> nodoVacioParaElArbol() {
        return (NodoBinario<K, V>) NodoBinario.nodoVacio();
    }
    
    public boolean examen(){
      if (this.esArbolVacio()) {
            return false;
        }
        NodoBinario<K, V> nodoActual;
        Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);
        while (!colaDeNodos.isEmpty()) {
            int cantidadDeNodosActuales = colaDeNodos.size();
            while (cantidadDeNodosActuales > 0) {
                nodoActual = colaDeNodos.poll();
                K clavePadre = nodoActual.getClave();
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                  K claveDerecha = nodoActual.getHijoDerecho().getClave();
                    if(clavePadre.compareTo(claveDerecha) > 0){
                      return false;
                  }               
                }
                if (!nodoActual.esHijoIzquiedoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                K claveIzq = nodoActual.getHijoIzquierdo().getClave();
                    if(clavePadre.compareTo(claveIzq) > 0){
                      return false;
                  }     
                }
                cantidadDeNodosActuales--;
            }
        }
        return true;  
    }

    @Override
    public boolean ejercicio9P3() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int ejercicio6P3() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int ejercicio12P3(int nivel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean ejercicio10P3() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int ejercicio3Ex3(int nivel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preg2(K clave, V valor) throws ExcepcionClaveYaExiste {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
