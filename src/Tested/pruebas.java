/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file>, choose Tools | Templates
 * and open the template in the editor.
 */
package Tested;

import Formas.DeterminaDeltaMatroide;
import Objetos.DeltaMatroide;
import Objetos.Generador;
import Objetos.SubMatriz;
import Recursos.ConstructorFamilias;
import Recursos.Flujos;
import Recursos.Impresion;
import Recursos.conjuntos.CreaFamiliaDeConjuntos;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class pruebas {
    public static void main(String[] args) throws Exception {
        /*
       LinkedList<LinkedList<Integer>> o1=new LinkedList();
       LinkedList<LinkedList<Integer>> o2=new LinkedList();
       //{1}
       LinkedList<Integer> a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       o1.add(a1);
       //{2}
       a1=new LinkedList();
       a1.add(0);
       a1.add(2);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(3);
       o1.add(a1);
       //{1,2}
       a1=new LinkedList();
       a1.add(0);
       a1.add(4);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(1);
       a1.add(2);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(1);
       a1.add(3);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(2);
       a1.add(3);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(2);
       a1.add(4);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(3);
       a1.add(4);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(2);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(3);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(2);
       a1.add(3);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(2);
       a1.add(4);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(3);
       a1.add(4);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(1);
       a1.add(2);
       a1.add(3);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(2);
       a1.add(3);
       a1.add(4);
       o1.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(2);
       a1.add(3);
       o1.add(a1);
       
        a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(2);
       a1.add(4);
       o1.add(a1);
       
        a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(3);
       a1.add(4);
       o1.add(a1);
       
        a1=new LinkedList();
       a1.add(0);
       a1.add(2);
       a1.add(3);
       a1.add(4);
       o1.add(a1);
       
        a1=new LinkedList();
       a1.add(1);
       a1.add(2);
       a1.add(3);
       a1.add(4);
       o1.add(a1);
       
        a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(2);
       a1.add(3);
       a1.add(4);
       o1.add(a1);
       ////////////////////////////////////////////////////////
       
      a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       o2.add(a1);
       //{2}
       a1=new LinkedList();
       a1.add(0);
       a1.add(2);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(3);
       o2.add(a1);
       //{1,2}
       a1=new LinkedList();
       a1.add(0);
       a1.add(4);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(1);
       a1.add(2);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(1);
       a1.add(3);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(1);
       a1.add(4);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(2);
       a1.add(3);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(3);
       a1.add(4);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(2);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(3);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(4);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(2);
       a1.add(3);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(3);
       a1.add(4);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(1);
       a1.add(2);
       a1.add(3);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(1);
       a1.add(3);
       a1.add(4);
       o2.add(a1);
       
       a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(2);
       a1.add(3);
       o2.add(a1);
       
        a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(2);
       a1.add(4);
       o2.add(a1);
       
        a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(3);
       a1.add(4);
       o2.add(a1);
       
        a1=new LinkedList();
       a1.add(0);
       a1.add(2);
       a1.add(3);
       a1.add(4);
       o2.add(a1);
       
        a1=new LinkedList();
       a1.add(1);
       a1.add(2);
       a1.add(3);
       a1.add(4);
       o2.add(a1);
       
        a1=new LinkedList();
       a1.add(0);
       a1.add(1);
       a1.add(2);
       a1.add(3);
       a1.add(4);
       o2.add(a1);
       
       
       String et[]=new String[5];
       et[0]="1";
       et[1]="2";
       et[2]="3";
       et[3]="4";
       et[4]="5";
       
       String et2[]=new String[5];
       et2[0]="A";
       et2[1]="B";
       et2[2]="C";
       et2[3]="D";
       et2[4]="E";
       DeltaMatroide D1=new DeltaMatroide(o1, "iso1", et);
       DeltaMatroide D2=new DeltaMatroide(o2, "iso2", et2);
       
       LinkedList<String[]> funcion=D1.isIsomorfo(D2);
       
        for (int i = 0; i < funcion.get(0).length;i++) {
            System.out.println(funcion.get(0)[i]+"-------"+funcion.get(1)[i]);           
            
        }
       */

        ejecuta1(new boolean[5],0,5);
        
    }

    public static void immprimir(LinkedList<LinkedList<String>> resul) {
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
    public static void immprimir2(LinkedList<String> resul) {
        System.out.println("emprime un string "+resul.size());
        
             for(int j =0;j<resul.size();j++){
                 System.out.print(resul.get(j)+ " ");
             
         }
             System.out.println("");
         
    }

    private static void imrpimeMatriz(SubMatriz get) {
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

    private static void ejecuta1(boolean[] b, int i, int i0) {
        if(i==i0){
            for (int j = 0; j < b.length; j++) {
                if(b[j]) System.out.print("1"); 
                else System.out.print("0");
            }
            System.out.println("");
        
        }else{
           b[i] = false;
            ejecuta1(b, i+1, i0);
           b[i] = true;
            ejecuta1(b, i+1, i0);
        
        }
    }
}
