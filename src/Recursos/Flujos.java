/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import Objetos.DeltaMatroide;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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

   
    
     public boolean escribirMatroide(String nombreArchivo,DeltaMatroide matroide) throws IOException {
        throw new IOException();//To change body of generated methods, choose Tools | Templates.
    }
     /**
     * Des Serializa un objeto DeltaMatroide desde un fichero
     *@author Diego Frausto
     *@version 1.0
     *@param nombre es el nombre del Fichero
     * @return es la DeltaMatroide almacenada en el Fichero
     */
     
     public DeltaMatroide desSerializarMatroide(String nombre) throws FileNotFoundException, IOException, ClassNotFoundException{
         
         DeltaMatroide matroide=null;
         FileInputStream entrada=new FileInputStream(nombre+".dmc");
         ObjectInputStream desSerializador=new ObjectInputStream(entrada);
         matroide=(DeltaMatroide) desSerializador.readObject();
         System.out.println(nombre);
         entrada.close();
         desSerializador.close();
         return matroide;
         
     }
    /**
     * Serializa un objeto DeltaMatroide a un fichero
     *@author Diego Frausto
     *@version 1.0
     *@param nombre es el nombre del Fichero
     * @param matroide es el deltaMatroide que sera serializada
     * @return True si La operacion se realiza con Exito, False si ocurrio un error o excepcion
     */
     public boolean SerializarMatroide(String nombre,DeltaMatroide matroide){
         
        FileOutputStream Archivo = null;
        ObjectOutputStream Serializador = null;
        try {
            Archivo=new FileOutputStream(nombre+".dmc");
            Serializador=new  ObjectOutputStream(Archivo);
            Serializador.writeObject(matroide);
            
            
            
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
           return false;
        }
        finally{
           
            try {
                 if(Archivo!=null) Archivo.close();
                 if(Serializador!=null) Serializador.close();
            } catch (IOException ex) {
                Logger.getLogger(Flujos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
     }
     
     
      public boolean SerializaDatos(DatosSerializados datos,File ruta){
         
         try{
            // Serializar un objeto de datos a un archivo
            ObjectOutputStream out = new ObjectOutputStream(new
            FileOutputStream(ruta));
            out.writeObject(datos);
            out.close();
           
            
          
            } catch (IOException e) {
                return false;
            }

                     return true;
                 }
    
     
     public boolean SerializaDatos(DatosSerializados datos){
         
         try{
            // Serializar un objeto de datos a un archivo
            ObjectOutputStream out = new ObjectOutputStream(new
            FileOutputStream("data.00"));
            out.writeObject(datos);
            out.close();
           
            
          
            } catch (IOException e) {
                return false;
            }

                     return true;
                 }
     
     public DatosSerializados desSerializaDatos(){
         DatosSerializados datos = null;
         
         try{
             ObjectInputStream entrada=new ObjectInputStream(new FileInputStream("data.00"));
             datos=(DatosSerializados) entrada.readObject();

         } catch (Exception ex) {
             System.out.println(ex.getMessage());
            return null;
        }
         return datos;
     }
     public DatosSerializados desSerializaDatos(File ruta){
         DatosSerializados datos = null;
         
         try{
             ObjectInputStream entrada=new ObjectInputStream(new FileInputStream(ruta));
             datos=(DatosSerializados) entrada.readObject();

         } catch (Exception ex) {
             System.out.println(ex.getMessage());
            return null;
        }
         return datos;
     }
}
