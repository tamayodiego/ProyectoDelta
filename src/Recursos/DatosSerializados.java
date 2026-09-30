/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import Objetos.DeltaMatroide;
import java.util.LinkedList;

/**
 *
 * @author diego
 */
public class DatosSerializados {
    private ElementoFolder
            folder;
    private boolean flags[];


    public DatosSerializados(){
        folder=new ElementoFolder(true, "Delta-Matroides", new LinkedList<ElementoFolder>(), null);
        folder.folder.add(new ElementoFolder(true,"Twst's",new LinkedList<ElementoFolder>(),folder));


        flags=new boolean[7];
        for(int i =0;i<7;i++) flags[i]=false;
    }

    public ElementoFolder getMatroides() {
        return folder;
    }
    
    
    public int cuantosMatroides(){
        return folder.folder.size();
    }
    public boolean[] getFlags() {
        return flags;
    }

    public void setMatroides(ElementoFolder matroides) {
        this.folder = matroides;
    }
    

    public void setFlags(boolean[] flags) {
        this.flags = flags;
    }
    
    public void agregarDeltaMatroide(ElementoFolder matroide){
        folder.folder.add(matroide);
    }
    
    public void eliminaDeltaMatroide(int index){
        folder.folder.remove(index);
    }
    
    
    
}
