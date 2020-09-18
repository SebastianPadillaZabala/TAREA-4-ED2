/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arbol;

/**
 *
 * @author Sebastian Padilla
 */
public class ExcepcionClaveNoExiste extends Exception {
  public ExcepcionClaveNoExiste(){
        super("La clave a insertar No existe en el arbol");
    }
    public ExcepcionClaveNoExiste(String mensaje){
        super(mensaje);
    }   
}
