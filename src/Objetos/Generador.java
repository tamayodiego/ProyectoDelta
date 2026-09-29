/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Diego Frausto Tamayo
 * @version 1.3
 */
public class Generador implements  Serializable{
    
  
    int size;
    
    /**
     * Genera las combinaciones necesarias para analizar
     * @author Diego Frausto
     * @version 1.1
     * @param M tamaño de las combinaciones que generara
     * @return Lista de todas las combinaciones posibles
     */
    public LinkedList<LinkedList<Integer>> subMatrices(int M) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  
       size=M;
       LinkedList<LinkedList<Integer>> ab=new LinkedList();
          for(int i =0;i<M;i++){
              LinkedList<Integer> tem1=new LinkedList();
              tem1.add(i);
              ab.add(tem1);
            
        }
     
       LinkedList<LinkedList<Integer>> des=genera(ab,2);
//       pruebas.immprimir(des);
       return des;
        
    }
    
    /**
     * metodo recursivo auxiliar para genera todas las combinaciones
     * @author Diego Frausto
     * @version 1.7
     * @param base conjunto de numeros-simbolos con cuales generar las combinaciones
     * @param n nivel de recursion, no debe pasar del numero de |numeros-simbolos|
     * @return Lista de todas las convinaciones posibles
     */
    
    private LinkedList<LinkedList<Integer>>  genera(LinkedList<LinkedList<Integer>>  base,int n){
        
       LinkedList<LinkedList<Integer>>  lista=new LinkedList();
       
       for(int i =0;i<base.size();i++){
           
           LinkedList<Integer> b=base.get(i);

           for(int j =b.getLast() +1 ;
                   j<size;j++){
               
//               System.out.println("entre2222");
               LinkedList<Integer> Aux=new LinkedList();
               Aux=(LinkedList<Integer>) base.get(i).clone();
               Aux.add(j);
//               pruebas.immprimir2(Aux);
               lista.add(Aux);
           }
          
       }
//       pruebas.immprimir(ab);
       if(n<size) {
           lista.addAll(genera(lista,n+1));
           return lista;
       }
       else return lista;
    }
    
    /**
     * Genera y analiza cada subMatriz, decide si es valida o no
     * @author Diego Frausto
     * @version 1.1
     * @param get es la combinacion que se va ha analizar
     * @param M Matriz de la cual se va a sacar la subMatriz
     * @return Regresa la SubMatriz generada si es valida, en caso contrario regresa null
     */
    public SubMatriz analizaSubMatriz(LinkedList<Integer> get, byte[][] M,int modo) {
       int n;
       switch (modo) {
           case 0: n=2;
           break;
           case 1: n=3;
           break;
           case 2: n=4;
           break;
           case 3: n=Integer.MAX_VALUE;
           break;
           default: n=-1;
       }
       
//        System.out.println("Entre a analizaMAtriz");
//        System.out.println(JFrameInicio.generaMatrisString(M));
//        System.out.println(get);
//        System.out.println(n);
       
       byte[][] mat=generarMatriz(get,M);
//       System.out.println(JFrameInicio.generaMatrisString(mat));
       Determinante deter=new Determinante(mat);
       
       int det=(int)deter.calcDet();
//        System.out.println("Determinante: " +det);
      //  System.out.println("determinante " +det+" en modulo: " +n);
       //  System.out.println("Acabe Determinante");
         if(det<0)det=det *-1;
         if((det%n)!=0) {
            SubMatriz A=new SubMatriz();
            A.setMatriz(mat);
            A.setSubF(get);
            A.setDeterminante(det%n);
            return A;
        }
        else return null;
     
    }
    
    /**
     * generarMatriz: Genera subMatriz a partir de la Matriz original. Da soporte a analizaSubMatriz
     * @author Diego Frausto
     * @version 1.1
     * @param get es la combinacion con que se va ha generar la SubMAtriz
     * @param M Matriz de la cual se va a sacar la subMatriz
     * @return Regresa la SubMatriz generada 
     */
    
    public LinkedList <Integer> diferenciaSimetrica(LinkedList <Integer> A, LinkedList <Integer> B){
        LinkedList <Integer> resultado=new LinkedList();
        
         for(int i =0;i<A.size();i++){
             Integer a=A.get(i);
             boolean noEsta=true;
             for(int j =0;j<B.size();j++) if(a.compareTo(B.get(j))==0) noEsta=false; 
             if(noEsta) resultado.add(a);
         }
         
           for(int i =0;i<B.size();i++){
             Integer b=B.get(i);
             boolean noEsta=true;
             for(int j =0;j<A.size();j++) if(b.compareTo(A.get(j))==0) noEsta=false; 
             if(noEsta) resultado.add(b);
         }
           //resultado.sort();
           Collections.sort(resultado);
        
        return resultado;
    }
    
    public byte[][] generarMatriz(LinkedList<Integer> get, byte[][] M) {
        byte[][] mat=new byte[get.size()][get.size()];
        int I,J;
        for(int i =0;i<get.size();i++){
             I=get.get(i); 
             for(int j =0;j<get.size();j++){ 
                J=get.get(j);
                mat[i][j]=M[I][J];
            }
        }
        return mat;
       
    }
    
  

        
    public LinkedList<byte[][]> generaOrientaciones(DeltaMatroide D){
       
        byte matrizBase[][]=clonaMatriz(D.getM());
        int unos=cuentaUnos(matrizBase);
        boolean combinaciones[]=new boolean[unos];
        LinkedList<byte[][]> resultado=new LinkedList();
        
        generaCombinaciones(D,matrizBase,combinaciones,resultado,0,unos);
        
        return resultado;
        
        
        
        
        
    }

    private int cuentaUnos(byte[][] m) {
        int unos=0;
        for (int i = 0; i < m.length; i++) 
            for (int j = i; j < m.length; j++)
                if(m[i][j]!=0) unos++;
        return unos;
       
    }

    private byte[][] clonaMatriz(byte[][] m) {
       byte[][] result=new byte[m.length][m.length];
        for (int i = 0; i < result.length; i++) 
            for (int j = 0; j < result.length; j++) result[i][j]=m[i][j];
        return result;
    }

    private void generaCombinaciones(DeltaMatroide F, byte[][] matrizBase, boolean[] combinaciones, LinkedList<byte[][]> resultado, int i,int lim) {
       if(i==lim){
           if(orientacionEsValida(F,matrizBase,combinaciones)) resultado.add(clonaMatriz(matrizBase));
       }else{
           combinaciones[i]=true;
           generaCombinaciones(F, matrizBase, combinaciones, resultado, i+1, lim);
           combinaciones[i]=false;
           generaCombinaciones(F, matrizBase, combinaciones, resultado, i+1, lim);
       }
    }

    private boolean orientacionEsValida(DeltaMatroide F, byte[][] matrizBase, boolean[] combinaciones) {
        aplicaOrientacion(matrizBase,combinaciones);
        DeltaMatroide D=new DeltaMatroide(matrizBase,1);
        if(D.equalsNoEtiquetas(F)) return true;
        else return false;
        
    }

    private void aplicaOrientacion(byte[][] matrizBase, boolean[] combinaciones) {
      int k=0;
        for (int i = 0; i < matrizBase.length; i++) {
            for (int j = i; j < matrizBase.length; j++) {
                if(matrizBase[i][j]!=0){
                    if(combinaciones[k]){
                        matrizBase[i][j]=1;
                        matrizBase[j][i]=-1;
                    }else{
                        matrizBase[i][j]=-1;
                        matrizBase[j][i]=1;
                    }
                    k++;
                }    
            }            
        }
        
    }
 }

    
    

