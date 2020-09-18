/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arbol;

import java.util.Stack;

/**
 *
 * @author Sebastian Padilla
 * @param <K>
 * @param <V>
 */
public class AVL<K extends Comparable<K>, V> extends ArbolBinarioBusqueda<K, V> {

    private static final byte DIFERENCIA_MAXIMA = 1;

    @Override
    public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
        raiz = insertar(raiz, clave, valor);
    }

    private NodoBinario<K, V> insertar(NodoBinario<K, V> nodoActual,
            K clave, V valor) throws ExcepcionClaveYaExiste {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            NodoBinario<K, V> nuevoNodo = new NodoBinario<K, V>(clave, valor);
            return nuevoNodo;
        }
        K claveActual = nodoActual.getClave();
        if (clave.compareTo(claveActual) > 0) {
            NodoBinario<K, V> supuestoNuevoHijoDerecho = insertar(nodoActual.getHijoDerecho(), clave, valor);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return balancear(nodoActual);
        }
        if (clave.compareTo(claveActual) < 0) {
            NodoBinario<K, V> supuestoNuevoHijoIzquierdo = insertar(nodoActual.getHijoIzquierdo(), clave, valor);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return balancear(nodoActual);
        }

        throw new ExcepcionClaveYaExiste();
    }

    private NodoBinario<K, V> balancear(NodoBinario<K, V> nodoActual) {
        int alturaRamaIzq = altura2(nodoActual.getHijoIzquierdo());
        int alturaRamaDer = altura2(nodoActual.getHijoDerecho());
        int diferencia = alturaRamaIzq - alturaRamaDer;
        if (diferencia > DIFERENCIA_MAXIMA) { //ROTACION POR DERECHA -- LA RAMA GRANDE ES IZQUIERDA
            NodoBinario<K, V> hijoIzquierdo = nodoActual.getHijoIzquierdo();
            alturaRamaIzq = altura2(hijoIzquierdo.getHijoIzquierdo());
            alturaRamaDer = altura2(hijoIzquierdo.getHijoDerecho());
            if (alturaRamaDer > alturaRamaIzq) {
                return rotacionDobleDerecha(nodoActual);
            } else {
                return rotacionSimpleDerecha(nodoActual);
            }
        } else if (diferencia < -DIFERENCIA_MAXIMA) { //ROTACION POR IZQUIERDA -- LA RAMA GRANDE ES DERECHA
            NodoBinario<K, V> hijoDerecho = nodoActual.getHijoDerecho();
            alturaRamaIzq = altura2(hijoDerecho.getHijoIzquierdo());
            alturaRamaDer = altura2(hijoDerecho.getHijoDerecho());
            if (alturaRamaIzq > alturaRamaDer) {
                return rotacionDobleIzquierda(nodoActual);
            } else {
                return rotacionSimpleIzquierda(nodoActual);
            }
        }

        return nodoActual;
    }

    private NodoBinario<K, V> rotacionDobleDerecha(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoARetornar = nodoActual.getHijoIzquierdo();
        nodoActual.setHijoIzquierdo(rotacionSimpleIzquierda(nodoARetornar));
        nodoARetornar = rotacionSimpleDerecha(nodoActual);
        return nodoARetornar;
    }

    private NodoBinario<K, V> rotacionSimpleDerecha(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoARetornar = nodoActual.getHijoIzquierdo();
        nodoActual.setHijoIzquierdo(nodoARetornar.getHijoDerecho());
        nodoARetornar.setHijoDerecho(nodoActual);
        return nodoARetornar;
    }

    private NodoBinario<K, V> rotacionSimpleIzquierda(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoARetornar = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoARetornar.getHijoIzquierdo());
        nodoARetornar.setHijoIzquierdo(nodoActual);
        return nodoARetornar;
    }

    private NodoBinario<K, V> rotacionDobleIzquierda(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoARetornar = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(rotacionSimpleDerecha(nodoARetornar));
        nodoARetornar = rotacionSimpleIzquierda(nodoActual);
        return nodoARetornar;
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
            return balancear(nodoActual);
        }
        if (claveAEliminar.compareTo(claveActual) < 0) {
            NodoBinario<K, V> supuestoNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return balancear(nodoActual);
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

    public void preg2(K clave, V valor) throws ExcepcionClaveYaExiste {
        NodoBinario<K, V> nodoZ = null;
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(clave, valor);
            return;
        }
        Stack<NodoBinario<K, V>> pila = new Stack<>();
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pila.push(nodoActual);
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

        while (!pila.isEmpty()) {
            NodoBinario<K, V> nodoVisitado = pila.pop();
            if (pila.size() > 0) {
                NodoBinario<K, V> nodoX = pila.peek();
                if (nodoVisitado.getClave().compareTo(nodoX.getClave()) < 0) {
                    nodoVisitado = balancear(nodoVisitado);
                    nodoX.setHijoIzquierdo(nodoVisitado);
                    nodoZ = nodoX;
                } else {
                    nodoVisitado = balancear(nodoVisitado);
                    nodoX.setHijoDerecho(nodoVisitado);
                    nodoZ = nodoX;
                }
            } else {
                nodoVisitado = balancear(nodoVisitado);
                this.raiz = nodoVisitado;
            }
        }
    }
}
