/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * SubMatriz: es el Obejto que contiene una SubMatriz su Combinacion y su determinante
 * @author Diego Frausto
 * @version 1.2
 * 
 */
public class SubMatriz implements  Serializable {
    
    private byte [][] matriz;
    private LinkedList<Integer> subF;
    private int determinante;
    private boolean modoInicio=false;

   
    public int getDeterminante() {
        return determinante;
    }

    public void setModoInicio(boolean modoInicio) {
        this.modoInicio = modoInicio;
    }
    

    public void setDeterminante(int determinante) {
        this.determinante = determinante;
    }
    

    public byte[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(byte[][] matriz) {
        this.matriz = matriz;
    }

    public LinkedList<Integer> getSubF() {
        return subF;
    }

    public void setSubF(LinkedList<Integer> subF) {
        this.subF = subF;
    }

    @Override
    public String toString() {
        int desface;
        if(modoInicio)desface=0;
        else desface=1;
        String cadena="{";
        
        for(int j=0;j<subF.size();j++){
           cadena+=(subF.get(j)+desface)+"";
           if(j!=subF.size()-1)cadena+=",";
        }
        cadena+="}";
        
        return cadena;
    }
    
    
    
    
}
