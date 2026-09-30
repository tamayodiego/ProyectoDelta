/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.util.LinkedList;

/**
 *
 * @author diego
 */
public class GraperFactible {
    private LinkedList<Integer> factible;
    private boolean modoInicio;
    int index;
    private String [] etiquetasReferencias;

  
    public GraperFactible(LinkedList<Integer> factible, int index,String [] etiquetas) {
        this.factible = factible;
        
        this.index=index;
        this.etiquetasReferencias=etiquetas;
    }

    public LinkedList<Integer> getFactible() {
        return factible;
    }

    public void setFactible(LinkedList<Integer> factible) {
        this.factible = factible;
    }

    public boolean isModoInicio() {
        return modoInicio;
    }

    public void setModoInicio(boolean modoInicio) {
        this.modoInicio = modoInicio;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    
    
    
     public String toString() {
       
        
        String cadena="{";
        
        for(int j=0;j<factible.size();j++){
           cadena+=(etiquetasReferencias[factible.get(j)])+"";
           if(j!=factible.size()-1)cadena+=",";
        }
        cadena+="}";
        
        return cadena;
    }
    
}
