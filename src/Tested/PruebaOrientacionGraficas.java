/*
 * Copyright (C) 2017 diego
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
package Tested;

import Objetos.DeltaMatroide;
import java.util.LinkedList;

/**
 *
 * @author diego
 */
public class PruebaOrientacionGraficas {
    public static void main(String[] args) {
        byte a[][]={
            {0,1,1,0,0,0,0},
            {1,0,1,0,0,1,1},
            {1,1,0,1,0,1,0},
            {0,0,1,0,1,0,0},
            {0,0,0,1,0,1,0},
            {0,1,1,0,1,0,1},
            {0,1,0,0,0,1,0}
        };
        DeltaMatroide mat=new DeltaMatroide(a, 0);
        
        LinkedList<byte[][]> matrices=generaConbinaciones(a);
        LinkedList<DeltaMatroide> matroides=new LinkedList();
        for (int i = 0; i < matrices.size(); i++) {
            System.out.println("Generando Conbinacion "+i);
            DeltaMatroide temp=new DeltaMatroide(matrices.get(i),1);
            if(sonIgueles(mat.getFamilia(),temp.getFamilia())) matroides.add(temp);
            
        }
        System.out.println(matroides.size());
        for (int i = 0; i < matroides.size(); i++) {
            imprimeMatriz(matroides.get(i).getM());
            System.out.println("");
            
        }
        
    }

    private static LinkedList<byte[][]> generaConbinaciones(byte[][] a) {
        LinkedList<byte[][]> temp=new LinkedList();
        
        int unos=cuentaUnos(a);
        int matrices=(int) Math.pow(2, unos);
        byte matrizBase[][]=generaMatriz(a);
        imprimeMatriz(matrizBase);
        for (int i = 0; i < matrices; i++) {
            String conbi=Integer.toBinaryString(i);
            if(conbi.length()<unos) conbi=concadenaCeros(conbi,unos);
            temp.add(GeneraMatrizCon(matrizBase,conbi));
            
        }
        return temp;
    }

    private static int cuentaUnos(byte[][] a) {
       return 10;
    }

    private static String concadenaCeros(String conbi,int ceros) {
    String result="";
    
        for (int i = 0; i < ceros-conbi.length(); i++) {
            result+="0";
            
        }
          result+=conbi;
          
          return result;
    
    }

    private static byte[][] generaMatriz(byte[][] a) {
        byte matriz[][]=clona(a);
        for (int i = 0; i < matriz.length; i++) {
            for (int j = i; j < matriz[i].length; j++) {
                if(matriz[i][j]==1) matriz[j][i]*=-1;
                        
            }
            
        }
        return matriz;
    }
    
    private static void imprimeMatriz(byte[][] a){
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                System.out.print(a[i][j]+"  ");
                
            }
            System.out.println("");
            
        }
    }

    private static byte[][] GeneraMatrizCon(byte[][] matrizBase, String conbi) {
       
      byte a[][]=clona(matrizBase);
      int cont=0;
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a[i].length; j++) {
                
                if(a[i][j]==1) {
                  
                    if(conbi.charAt(cont)=='1') {
                    a[i][j]*=-1;
                    a[j][i]*=-1;
                    
                    }
                    cont++;
                }
                    
                
            }
            
        }
        return a;
    }

    private static byte[][] clona(byte[][] a) {
        byte clon[][]=new byte[a.length][a.length];
        for (int i = 0; i < clon.length; i++) {
            for (int j = 0; j < clon.length; j++) {
                clon[i][j]=a[i][j];
                
            }
            
        }
        return clon;
    }

    private static boolean sonIgueles(LinkedList<LinkedList<Integer>> familia, LinkedList<LinkedList<Integer>> familia0) {
         System.out.println(familia);
         System.out.println(familia0);
        if(familia.size()!=familia0.size()) {
            System.out.println("las familias son de cardinalidad diferente");
           
            return false;
            
        }
        
        for (int i = 0; i < familia0.size(); i++) {
            LinkedList<Integer> f1=familia.get(i);
            LinkedList<Integer> f2=familia0.get(i);
            if(f1.size()!=f2.size()) {
                System.out.println("factible "+f1+" es diferente de "+f2);
                return false;
            }
            for (int j = 0; j < f2.size(); j++) {
                if(!f1.get(j).equals(f2.get(j))) {
                     System.out.println("factible "+f1+" es diferente de "+f2);
                    return false;
                }
                
            }
            
        }
        return true;
    }
}
