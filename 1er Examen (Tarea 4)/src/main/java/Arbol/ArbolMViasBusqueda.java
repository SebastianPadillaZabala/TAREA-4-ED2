/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arbol;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Sebastian Padilla
 * @param <K>
 * @param <V>
 */
public class ArbolMViasBusqueda<K extends Comparable<K>, V>
        implements IArbolBusqueda<K, V> {

    protected NodoMVias<K, V> raiz;
    protected int orden;

    public ArbolMViasBusqueda() {
        this.orden = 3;
    }

    public ArbolMViasBusqueda(int orden) throws ExcepcionOrdenInvalido {
        if (orden < 3) {
            throw new ExcepcionOrdenInvalido();
        }
        this.orden = 3;
    }

    protected NodoMVias<K, V> nodoVacioParaElArbol() {
        return (NodoMVias<K, V>) NodoMVias.nodoVacio();
    }

    @Override
    public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<K, V>(orden, clave, valor);
            return;
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (this.existeClaveEnNodo(nodoActual, clave)) {
                throw new ExcepcionClaveYaExiste();
            }
            //si llegamos a este punto la clave no esta en el nodoactual
            if (nodoActual.esHoja()) {
                if (nodoActual.estanDatosLlenos()) {
                    //no hay campo para la clave-valor en el nodoActual
                    int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
                    NodoMVias<K, V> nuevoHijo = new NodoMVias<>(orden, clave, valor);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                } else {
                    //si hay campo para la clave-valor en el nodoactual  
                    this.insertarEnOrden(nodoActual, clave, valor);
                }
                nodoActual = NodoMVias.nodoVacio();
            } else {
                int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
                if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                    NodoMVias<K, V> nuevoHijo = new NodoMVias<>(orden, clave, valor);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }
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

    @Override
    public V buscar(K claveABuscar) {
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            NodoMVias<K, V> nodoAnterior = nodoActual;
            for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios() && nodoActual == nodoAnterior; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual.getValor(i);
                }
                if (claveABuscar.compareTo(claveActual) < 0) {
                    if (nodoActual.esHijoVacio(i)) {
                        return (V) NodoMVias.datoVacio();
                    }
                    nodoActual = nodoActual.getHijo(i);
                }
            }
            if (nodoActual == nodoAnterior) {
                nodoActual = nodoActual.getHijo(orden - 1);
            }
        }
        return (V) NodoMVias.datoVacio();
    }

    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != NodoMVias.datoVacio();
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoInOrdenAmigo(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoInOrdenAmigo(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            recorridoInOrdenAmigo(nodoActual.getHijo(i), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
        recorridoInOrdenAmigo(nodoActual.getHijo(orden - 1), recorrido);
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoPreOrdenAmigo(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoPreOrdenAmigo(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoPreOrdenAmigo(nodoActual.getHijo(i), recorrido);
        }
        recorridoPreOrdenAmigo(nodoActual.getHijo(orden - 1), recorrido);

    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoPostOrdenAmigo(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoPostOrdenAmigo(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoPostOrdenAmigo(nodoActual.getHijo(0), recorrido);
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            recorridoPostOrdenAmigo(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
                recorrido.add(nodoActual.getClave(i));
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
            if (!nodoActual.esHijoVacio(orden - 1)) {
                colaDeNodos.offer(nodoActual.getHijo(orden - 1));
            }
        }
        return recorrido;
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    private int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < orden; i++) {
            int alturaDeHijo = altura(nodoActual.getHijo(i));
            if (alturaDeHijo > alturaMayor) {
                alturaMayor = alturaDeHijo;
            }
        }
        return alturaMayor + 1;
    }

    @Override
    public void vaciar() {
        this.raiz = this.nodoVacioParaElArbol();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(raiz);
    }

    @Override
    public int nivel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mostrarArbol() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean existeClaveEnNodo(NodoMVias<K, V> nodoActual, K clave) {
        for (int i = 0; i < orden - 1; i++) {
            if (!nodoActual.esDatoVacio(i)) { //preguntando si la clave en la posicion i no es vacia
                K claveEnTurno = nodoActual.getClave(i);
                if (claveEnTurno.compareTo(clave) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int porDondeBajar(NodoMVias<K, V> nodoActual, K clave) {
        int posicion = 0;
        while (posicion < nodoActual.cantidadDeDatosNoVacios()) {
            K claveActual = nodoActual.getClave(posicion);
            if (clave.compareTo(claveActual) < 0) {
                return posicion;
            }
            posicion++;
        }
        return posicion;
    }

    public void insertarEnOrden(NodoMVias<K, V> nodoActual, K clave, V valor) {
        int i = porDondeBajar(nodoActual, clave);
        int longitud = nodoActual.cantidadDeDatosNoVacios();
        while (longitud > i) {
            K claveRecorrer = nodoActual.getClave(longitud - 1);
            V valorRecorrer = nodoActual.getValor(longitud - 1);
            nodoActual.setClave(longitud, claveRecorrer);
            nodoActual.setValor(longitud, valorRecorrer);
            longitud--;
        }
        nodoActual.setClave(i, clave);
        nodoActual.setValor(i, valor);
    }

    private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveActual) == 0) {
                if (nodoActual.esHoja()) {
                    this.eliminarDatoEnNodo(nodoActual, i);
                    if (nodoActual.estanDatosVacios()) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }
                K claveReemplazo;
                if (this.hayHijosMasAdelante(nodoActual, i)) {
                    claveReemplazo = this.buscarSucesorInOrden(nodoActual, claveAEliminar);
                } else {
                    claveReemplazo = this.buscarPredecesorInOrden(nodoActual, claveAEliminar);
                }
                V valorReemplazo = this.buscar(claveReemplazo);
                nodoActual = this.eliminar(nodoActual, claveReemplazo);
                nodoActual.setClave(i, claveReemplazo);
                nodoActual.setValor(i, valorReemplazo);
                return nodoActual;
            }

            if (claveAEliminar.compareTo(claveActual) < 0) {
                NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }
        NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(orden - 1), claveAEliminar);
        nodoActual.setHijo(orden - 1, supuestoNuevoHijo);
        return nodoActual;
    }

    public void eliminarDatoEnNodo(NodoMVias<K, V> nodoActual, int i) {
        int longitud = nodoActual.cantidadDeDatosNoVacios();
        int posicion = 0;
        for (posicion = 0; posicion < longitud; posicion++) {
            if (posicion == i) {
                while (posicion < longitud - 1) {
                    K claveRecorrer = nodoActual.getClave(posicion + 1);
                    V valorRecorrer = nodoActual.getValor(posicion + 1);
                    nodoActual.setClave(posicion, claveRecorrer);
                    nodoActual.setValor(posicion, valorRecorrer);
                    posicion++;
                }

                nodoActual.setClave(longitud - 1, null);
                nodoActual.setValor(longitud - 1, null);
            }
        }
    }

    private boolean hayHijosMasAdelante(NodoMVias<K, V> nodoActual, int i) {
        i = i + 1;
        for (int x = i; x < nodoActual.cantidadDeDatosNoVacios() + 1; x++) {
            if (!nodoActual.esHijoVacio(x)) {
                return true;
            }
        }
        return false;
    }

    private int posicionClave(NodoMVias<K, V> nodoActual, K clave) {
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveActual.compareTo(clave) == 0) {
                return i;
            }
        }
        return 0;
    }

    private K buscarSucesorInOrden(NodoMVias<K, V> nodoActual, K clave) {
        int posicionClave = this.posicionClave(nodoActual, clave);
        NodoMVias<K, V> nodoAux = nodoActual.getHijo(posicionClave + 1);
        if (!NodoMVias.esNodoVacio(nodoAux)) {
            while (!nodoAux.esHijoVacio(0)) {
                nodoAux = nodoAux.getHijo(0);
            }
            return nodoAux.getClave(0);
        } else {
            return nodoActual.getClave(posicionClave + 1);
        }

    }

    private K buscarPredecesorInOrden(NodoMVias<K, V> nodoActual, K clave) {
        int posicionClave = this.posicionClave(nodoActual, clave);
        NodoMVias<K, V> nodoAux = nodoActual.getHijo(posicionClave);
        if (!NodoMVias.esNodoVacio(nodoAux)) {

            while (!nodoAux.esHijoVacio(nodoAux.cantidadDeDatosNoVacios())) {
                nodoAux = nodoAux.getHijo(nodoAux.cantidadDeDatosNoVacios());
                if (!nodoAux.esHijoVacio(nodoAux.cantidadDeDatosNoVacios())) {
                    return nodoAux.getClave(0);
                }
            }
            return nodoAux.getClave(nodoAux.cantidadDeDatosNoVacios() - 1);

        } else {
            return nodoActual.getClave(posicionClave - 1);
        }
    }

    public void mostrar() {
        if (this.esArbolVacio()) {
            System.out.println("Nodo Vacio");
        }
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int nodosDelNivel = colaDeNodos.size();

            Queue<NodoMVias<K, V>> colaDeNodosAnterior = new LinkedList<>();
            while (nodosDelNivel > 0) {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
                    colaDeNodosAnterior.offer(nodoActual);
                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                }
                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
                nodosDelNivel--;
            }
            this.imprimirNivel(colaDeNodosAnterior);
        }
    }

    private void imprimirNivel(Queue<NodoMVias<K, V>> colaDeNodos) {
        List<K> listaDeClaves = new LinkedList<>();
        while (!colaDeNodos.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < orden - 1; i++) {
                listaDeClaves.add(nodoActual.getClave(i));
            }
            System.out.print(listaDeClaves);
            System.out.print("              ");
            listaDeClaves.clear();
        }
        System.out.println(" ");
    }

    private void imprimirLineas(String linea, NodoMVias<K, V> nodoActual) {
        List<K> listaClaves = new LinkedList<>();
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            ponerClaves(listaClaves, nodoActual);
            System.out.println(linea + "--" + listaClaves);
            String lineaDelHijo = linea + " |";
            for (int i = 0; i < this.orden - 1; i++) {
                if (!nodoActual.esHijoVacio(i)) {
                    imprimirLineas(lineaDelHijo, nodoActual.getHijo(i));
                } else {
                    System.out.println(lineaDelHijo + "--|");
                }
            }
            String ultimaLinea = linea + " »";
            imprimirLineas(ultimaLinea, nodoActual.getHijo(this.orden - 1));
        } else {
            System.out.println(linea + "--|");
        }
    }

    private void ponerClaves(List<K> listaClaves, NodoMVias<K, V> nodoActual) {
        for (int i = 0; i < this.orden - 1; i++) {
            listaClaves.add(nodoActual.getClave(i));
        }
    }

    public void verArbol() {
        String linea = "";
        if (!NodoMVias.esNodoVacio(this.raiz)) {
            imprimirLineas(linea, this.raiz);
        } else {
            System.out.println(linea + "El Arbol Esta Vacio");
        }
    }

    @Override
    public int ejercicio6P3() {
        return this.ejercicio6P3(this.raiz);
    }

    private int ejercicio6P3(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        if (nodoActual.cantidadDeDatosNoVacios() < orden - 1) {
            return 1;
        }
        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + this.ejercicio6P3(nodoActual.getHijo(i));
        }

        return cantidad;
    }

    @Override
    public boolean ejercicio9P3() {
        boolean b = true;
        int nivelActual = 0;
        if (this.esArbolVacio()) {
            return true;
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
                }
                if (nodoActual.esHoja() && nivelActual != this.altura() - 1) {
                    b = false;
                }

                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
                cantidasDelnodosNivel--;
            }
            nivelActual++;
        }
        return b;
    }

    @Override
    public boolean ejercicio10P3() {
        int nivel = 0;
        int maximoDatos = orden - 1;
        int minimoDatos = maximoDatos / 2;
        int minimoHijos = minimoDatos + 1;
        if (this.esArbolVacio()) {
            return true;
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
                }
                if (!nodoActual.esHoja()) {
                    if (nodoActual.cantidadDeDatosNoVacios() < minimoDatos
                            || nodoActual.cantidadDeHijosNoVacios() < minimoHijos) {
                        return false;
                    }
                } else {
                    if (nivel != this.altura() - 1) {
                        return false;
                    }
                }

                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
                cantidasDelnodosNivel--;
            }
            nivel++;
        }

        return true;
    }

    @Override
    public int ejercicio12P3(int nivelObjetivo) {
        int cantidad = 0;
        int nivel = 0;
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
                }

                if (nivel > nivelObjetivo && nodoActual.cantidadDeHijosNoVacios() != 0
                        && !nodoActual.esHoja()) {
                    cantidad++;
                }
                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
                cantidasDelnodosNivel--;
            }
            nivel++;
        }
        return cantidad;
    }

    private int ejercicio3amigo(NodoMVias<K, V> nodoActual, int nivelObjetivo, int nivel) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (!nodoActual.esHoja() && nivel != nivelObjetivo) {
            return 1;
        }
        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + this.ejercicio3amigo(nodoActual.getHijo(i), nivelObjetivo, nivel + 1);
        }
        return cantidad;
    }
    public int ejercicio3Ex3(int nivelObjetivo){
       int cantidad = 0;
        int nivel = 0;
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
                }

                if (nivel != nivelObjetivo
                        && !nodoActual.esHoja()) {
                    cantidad++;
                }
                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
                cantidasDelnodosNivel--;
            }
            nivel++;
        }
        return cantidad;
     }
    

    @Override
    public boolean examen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preg2(K clave, V valor) throws ExcepcionClaveYaExiste {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
