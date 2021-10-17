/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DANIYAL ZAFAR
 */
public class InvalidQuantity extends Exception{

    public InvalidQuantity() {
        super("\n-------------------------\nPlease enter quantity greater than 0 !\n-------------------------\n");
    }
    
}
