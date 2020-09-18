/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arbol;
import java.util.List;
/**
 *
 * @author Sebastian Padilla
 * @param <K>
 * @param <V>
 */
public interface IArbolBusqueda<K extends Comparable <K>, V> {
  void insertar(K clave, V valor) throws ExcepcionClaveYaExiste;
    V eliminar(K clave) throws ExcepcionClaveNoExiste;
    V buscar(K clave);
    boolean contiene(K clave);
    List<K> recorridoEnInOrden();
    List<K> recorridoEnPreOrden();
    List<K> recorridoEnPostOrden();
    List<K> recorridoPorNiveles();
    int size();
    int altura();
    void vaciar();
    boolean esArbolVacio();
    int nivel();  
    void mostrarArbol();
    boolean ejercicio9P3();
    int ejercicio6P3();
    int ejercicio12P3(int nivel);
    boolean ejercicio10P3();
    boolean examen();
    int ejercicio3Ex3(int nivel);
    void preg2(K clave,V valor) throws ExcepcionClaveYaExiste;
    //int ejercicio2();
    //int ejercicio3(int nivel);
    //int ejercicio4(int nivel);
    //int ejercicio5(int nivel);
    //boolean ejercicio6();
    //boolean ejercicio7();
    //int ejercicio12(int nivel);
}
