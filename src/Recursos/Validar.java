/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import java.io.Serializable;

/**
 * Valida y clasifica una matriz para procesar su analisis de forma diferente para cada caso
 * @author Diego Leonardo Frausto Tamayo
 */
public class Validar implements  Serializable {
    
    private ParValidacion val=new ParValidacion();
/**
 * Este metodo hara dos cosas fundamentalmente clasificar la matriz como Simetrica o Antisimetrica y validar que sea una matriz valida segun su tipo
 * @author Diego Leonardo Frausto Tamayo
 */
    
    public ParValidacion validarMatrizAdyacencia(byte[][] mat) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      
      if(mat!=null) {
          //averiguamos que tipo de matriz Es:
          val.setTipoMatriz(descubreTipoMatriz(mat));
          //validamos que sea un tipo de matriz valido segun el tipo descubierto
          val.setValidaEnSuTipo(validaSegunTipo(val.isTipoMatriz(),mat));
      }
      else val.setValidaEnSuTipo(false);
      
      return val;
    }
    
    public ParValidacion validacionSimple(byte[][] mat){
         for(int i =0;i<mat.length;i++){
            for(int j =i;j<mat.length;j++){
                if(mat[i][j]>-2 && mat[i][j]<2){
                    val.setMatrizenGF2oGF3(false);
                    val.setParamError("Elementos Aij diferentes de -1 0 1");
                    System.out.println("Salio falso");
                    return val;
                    
                }
                
                
            }
         }
         val.setMatrizenGF2oGF3(true);
         return val;
    }
    
    
    
    //este metodo analiza si tiene terminos negativos lo que supondria una matriz Antisimetrica
    private boolean descubreTipoMatriz(byte[][] mat) {
        for(int i =0;i<mat.length;i++)
            for(int j =i;j<mat.length;j++)
                if(mat[i][j]<0) return false;
        return true;
    }
    
    //segunda validacion; si es simetrica que se cumpla que Aij=Aji. Si es antisimetrica que se cumpla que Aij=-Aji
    private boolean validaSegunTipo(boolean tipoMatriz,byte[][] mat) {
       boolean respuesta=true;
       if(tipoMatriz){
           //Validar matriz para Simetricas
           for(int i =0;i<mat.length;i++){
               for(int j =i;j<mat.length;j++){
                    //no debe haber terminos diferentes de 0 o 1
                   if(mat[i][j]>1) {
                       val.setParamError("noGrafo");
                       return false;
                   }
                   //M en ji debe ser igual a M en ij
                   if(mat[i][j]!=mat[j][i]) {
                       val.setParamError("ij!=ji");
                       return false;
                   }
                  
                   
                   
               }
           }
           
       }else{
           //Validar Matriz Antisimetrica :
           
            for(int i =0;i<mat.length;i++){
               for(int j =i;j<mat.length;j++){
                   //no debe haber terminos diferentes de 0 1 -1
                   if(mat[i][j]==0 ||mat[i][j]==1||mat[i][j]==-1);
                   else {
                       val.setParamError("noGrafo");
                       return false;
                   }
                   
                   //no debe haber algo diferente de 0 en la diagonal
                   
                   if(i==j) if(mat[i][j]!=0) {
                       val.setParamError("diagonal");
                       return false;
                   }
                   
                   // M en i,j debe ser igual a -M en j,i
                   
                   if(mat[i][j]!=-1*mat[j][i]) {
                       val.setParamError("ij!=-ji");
                       return false;
                   }
               }
            }
           
       }
       return respuesta;
    }
    
}
