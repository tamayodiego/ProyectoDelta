/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import Objetos.SubMatriz;
import java.util.LinkedList;

/**
 *
 * @author diego
 */
public class Impresion {
    
    public  void immprimirCombinaciones(LinkedList<LinkedList<String>> resul) {
        System.out.println("Sub F Generadas: "+resul.size());
         for(int i =0;i<resul.size();i++){
             LinkedList<String> a=resul.get(i);
             System.out.print("X={ ");
             for(int j =0;j<a.size();j++){
                 if(j==a.size()-1) System.out.print(a.get(j));
                 else System.out.print(a.get(j)+ ", ");
             
         }  
             System.out.print(" }");
             System.out.println("");
         }
    }
    public void ImprimeMatrizNxN(byte m[][]){
        for(int i =0;i<m.length;i++){
            for(int j =0;j<m.length;j++){
                System.out.print(m[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
    }
    public  void imrpimeSubMatriz(SubMatriz get) {
        System.out.print("X={ ");
       for(int i =0;i<get.getSubF().size();i++){
           if(i<get.getSubF().size()-1) System.out.print(get.getSubF().get(i)+", ");
           else  System.out.print(get.getSubF().get(i));
       }
        System.out.print(" }");
        System.out.println("");
      for(int i =0;i<get.getMatriz().length;i++){
           for(int j =0 ;j<get.getMatriz().length;j++){
               System.out.print(get.getMatriz()[i][j]+"\t");
           }
           System.out.println("");
      }
    }
}
