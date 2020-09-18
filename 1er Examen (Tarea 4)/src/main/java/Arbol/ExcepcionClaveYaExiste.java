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
public class ExcepcionClaveYaExiste extends Exception {
   public ExcepcionClaveYaExiste(){
        super("La clave a insertar ya existe en el arbol");
    }
    public ExcepcionClaveYaExiste(String mensaje){
        super(mensaje);
    }   
}
