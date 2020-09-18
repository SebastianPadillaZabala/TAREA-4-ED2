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
class ExcepcionOrdenInvalido extends Exception {
    public ExcepcionOrdenInvalido(){
        super("El orden de su arbol deber ser mayor o igual a 3");
    }
    public ExcepcionOrdenInvalido(String mensaje){
        super(mensaje);
    } 
}
