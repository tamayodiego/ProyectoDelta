package Recursos.conjuntos;

import java.util.LinkedList;


public class pruebaMain {
    
    public static void main(String[] args) throws Exception {
        CreaFamiliaDeConjuntos a = new CreaFamiliaDeConjuntos();
        ConjFam familia = new ConjFam();
        
       
      familia= a.creaFamilia("F={{123,234,7727},{345,456},{567,678}}");
      
//      System.out.println("F="+familia.impElem());
      LinkedList<LinkedList<Integer>> lista=familia.devuelveLista();
      
        for (LinkedList<Integer> list : lista) {
            System.out.println(list.toString());
        }
      


    }
    
}
