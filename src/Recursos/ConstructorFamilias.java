/*
 * Copyright (C) 2017 vonn_0132
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package Recursos;

import Excepciones.NoEntradaValida;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author vonn_0132
 */
public class ConstructorFamilias {
    LinkedList<LinkedList<Integer>> familia;
   public  LinkedList<String> lista;
   private int tamanioCard;
    LinkedList<String> conjuntoV;
    public ConstructorFamilias(String cad) throws NoEntradaValida,StringIndexOutOfBoundsException{
        cad=cad.replaceAll("\\s","");
        tamanioCard=0;
        conjuntoV=new LinkedList();
        familia=new LinkedList();
        int i = 0;
        int j;
        //demilitamos las llaves superiores
        for (; i < cad.length() && cad.charAt(i) != '{'; i++)
			;

        for (j = cad.length() - 1; j >= 0 && cad.charAt(j) != '}'; j--)
			;
        System.out.println(i);
        System.out.println(j);
         String subcad="" ;
        if(i+1 <j) 
             subcad = cad.substring(i + 1, j);
        else throw new NoEntradaValida("Los Delimitadores \"{\" y \"}\" no concuerdan");
        /////
        //System.out.println(subcad);
      
        lista=new LinkedList();
        System.out.println(subcad);
        lista = subs(subcad);
         for (int k = 0; k < lista.size(); k++) {
            String temp=lista.get(k);
            String a[]=temp.split(",");
            for (int l = 0; l < a.length ; l++) {
               if(!a[l].equals("")) {
                   if(buscaEtiqueta(a[l])<0){
                       conjuntoV.add(a[l]);
                       tamanioCard++;
                   }
               }
            }            
        }
        Collections.sort(conjuntoV);
        System.out.println(conjuntoV);
         
        for (int k = 0; k < lista.size(); k++) {
            String temp=lista.get(k);
            String a[]=temp.split(",");
            LinkedList<Integer> aux=new LinkedList();
            for (int l = 0; l < a.length ; l++) {
               if(!a[l].equals("")) {
                   int elementoV=buscaEtiqueta(a[l]);
                   if(elementoV<0){
                       conjuntoV.add(a[l]);
                       aux.add(tamanioCard);
                       tamanioCard++;
                   }else aux.add(elementoV);
               }
                
            }
            familia.add(aux);
            
        }
    }
    
    public String[] getConjuntoV(){
        Object[] con=conjuntoV.toArray();
        String[] resul=new String[con.length];
        for (int i = 0; i < resul.length; i++) resul[i]=(String) con[i];
        return resul;
    }
    public LinkedList<LinkedList<Integer>> getFamilia() {
        return familia;
    }
     
    
    private LinkedList<String> subs(String subcad) throws NoEntradaValida {
        // System.out.println(subcad);
        LinkedList<String> lista = new LinkedList();

        boolean flag = false;
        int i = 0;
        int j = 0;

        while (i < subcad.length()) {
//             System.out.println(subcad.charAt(i));
            if (subcad.charAt(i) == '{') {
                if (flag) {
                    throw new NoEntradaValida("Una \"{\", no tiene su \"}\" correspondiente");

                } else {
                    flag = true;
                    j = i;
                }
            }

            if (subcad.charAt(i) == '}') {
                if (flag) {
                    lista.add(subcad.substring(j + 1, i));
                    flag = false;
                } else {
                    throw new NoEntradaValida("Una \"}\", no tiene su \"{\" correspondiente");

                }
            }
            i++;

        }
        if (flag) {
            throw new NoEntradaValida("hay llaves {,} sin cerrar");
        }
        return lista;

    }

    private int buscaEtiqueta(String string) {
        for (int i = 0; i < conjuntoV.size();i++) 
            if(conjuntoV.get(i).compareTo(string)==0) return i;
      return -1;
    }
}
