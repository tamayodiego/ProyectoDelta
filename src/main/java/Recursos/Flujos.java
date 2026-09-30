/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Lectura y escritura de matrices en archivos de texto.
 *
 * El guardado del área de trabajo se movió a persistencia.AreaDeTrabajoJson.
 *
 * @author Diego Frausto
 */
public class Flujos {

    public byte[][] leerMatriz(String archivo) throws IOException {
       FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        int n=Integer.parseInt(b.readLine());

        byte[][] mat=new byte[n][n];
        try{
            for(int i =0;i<n;i++){
                String Aux=b.readLine();
                String[] a;
                if(Aux!=null){
                    a=Aux.split(" ");
                    for(int j =0;j<n;j++){
                        mat[i][j]=Byte.parseByte(a[j]);
                    }
                }else return null;

            }
        }catch(ArrayIndexOutOfBoundsException ex){
            return null;
        }
//        catch(NumberFormatException ex){
//            System.err.println("Puede faltar el tamaño de la matriz");
//            return null;
//        }
        return mat;

    }

    public boolean escribirMatriz(String impresion,String ruta){
          FileWriter fichero = null;
        PrintWriter pw = null;
        boolean result=true;
        try
        {
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);
                pw.println(impresion);

        } catch (Exception e) {
            result=false;
        } finally {
           try {
           // Nuevamente aprovechamos el finally para
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        return result;
    }
}
