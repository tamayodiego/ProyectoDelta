/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tested;

import Formas.*;
import Formas.GeneradorDeltaMatroide;
import Formas.Menores;
import Objetos.DeltaMatroide;
import Objetos.SubMatriz;
import Recursos.DatosSerializados;
import Recursos.ElementoFolder;
import Recursos.Flujos;
import Recursos.Impresion;
import Recursos.ParValidacion;
import Recursos.Validar;
import Recursos.modeloArbol.CeldaModelo;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import persistencia.AreaDeTrabajoJson;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author diego
 */
public class Main {
    
    private static String nombresArchivos[]=new String[4];
    /**
     * Modos de la ejecución actual, activados por argumentos de línea de
     * comandos (ver determinaBanderas). No se guardan en el área de trabajo.
     */
    public static boolean flags[]=new boolean[7];
    public static DatosSerializados datos;
    public static int indexActual;
    public static ElementoFolder  deltaMatroides;
    public static GeneradorDeltaMatroide ventanaGenera;
    public static DeterminaDeltaMatroide ventanaValidacion;
    public static Menores ventanaMenores;
    public static Menu2 ventanaMenu;
    public static Twisting ventanaTwisting;
    public static SelectorMatroide ventanaSelector;
    public static DeltaMatroidePropiedades ventanaPropiedades;
    public static GenerarOrientaciones ventanaOrientaciones;
    public static Isomorfismo ventanaIsomorfismo;
    public static ListasDeltaMatroides ventanaListas;
    public static GeneradorClaseEquivalencia ventanaEquivalencias;
    public static Stack pila=new Stack();
    public static boolean colorearYa=false;
  

    private static void ordneaNodo(ElementoFolder deltaMatroides) {
        Collections.sort(deltaMatroides.folder,new Comparator<ElementoFolder>() {
            @Override               /// si o1 es mas grande que o2 positivo si no negativo
            public int compare(ElementoFolder o1, ElementoFolder o2) {
               if(o1.isIsFolder() && !o2.isIsFolder()) return -1;
               if(!o1.isIsFolder() && o2.isIsFolder()) return 1;
               return o1.getNombre().compareToIgnoreCase(o2.getNombre());
            }
        });
        
        for (ElementoFolder nodo : deltaMatroides.folder) {
            if(nodo.isIsFolder()) ordneaNodo(nodo);
        }
    }
    public double version=0.17;
    public static Stack pilaElemntos=new Stack();
    static Impresion imp=new Impresion();
    public static Flujos flujo=new Flujos();
    public static Validar validar=new Validar();
    ///atributos de control de cocurrencia
    
    public static String ms="",estado="";
    public static int cuenta,uCuenta=0;
        
     public static Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("Imagenes/LogoBueno.png"));


        return retValue;
    
    }
    
    /** Archivo donde se guarda el área de trabajo entre ejecuciones. */
    public static final Path ARCHIVO_AREA_DE_TRABAJO = Paths.get("areaDeTrabajo.json");

    /** Guarda el área de trabajo actual. Es el único punto de guardado de la app. */
    public static void guardarAreaDeTrabajo() {
        guardarAreaDeTrabajo(ARCHIVO_AREA_DE_TRABAJO);
    }

    /**
     * Pregunta si se desea cerrar ProyectoDelta; si la respuesta es sí,
     * guarda el área de trabajo y termina. Si no, regresa sin hacer nada.
     * Lo usan todas las ventanas al cerrarse con la X.
     */
    public static void confirmarYSalir() {
        if (JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas cerrar ProyectoDelta?",
                "¿Estás seguro?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            guardarAreaDeTrabajo();
            System.exit(0);
        }
    }

    /** Termina la aplicación sin guardar ("Cerrar ProyectoDelta sin guardar..."). */
    public static void salirSinGuardar() {
        System.exit(0);
    }

    private static boolean guardarAreaDeTrabajo(Path ruta) {
        datos.setMatroides(deltaMatroides);
        try {
            AreaDeTrabajoJson.guardar(datos, ruta);
            return true;
        } catch (IOException e) {
            String mensaje = "No se pudo guardar " + ruta + ":\n" + e.getMessage();
            System.err.println(mensaje);
            if (!flags[4]) {
                JOptionPane.showMessageDialog(null, mensaje, "Error al guardar", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    /**
     * Carga el área de trabajo guardada. Si el archivo existe pero no se
     * puede leer, lo respalda como .bak (en lugar de sobrescribirlo al
     * cerrar) y arranca con un área vacía.
     *
     * @return un aviso para el usuario, o null si todo salió bien
     */
    private static String cargarAreaDeTrabajo() {
        Path ruta = ARCHIVO_AREA_DE_TRABAJO;
        String aviso = null;
        datos = null;
        if (Files.exists(ruta)) {
            try {
                datos = AreaDeTrabajoJson.cargar(ruta);
            } catch (IOException | AreaDeTrabajoJson.FormatoInvalidoException e) {
                Path respaldo = ruta.resolveSibling(ruta.getFileName() + ".bak");
                aviso = "No se pudo leer " + ruta + ":\n" + e.getMessage()
                        + "\n\nSe inicia con un área de trabajo vacía";
                try {
                    Files.move(ruta, respaldo, StandardCopyOption.REPLACE_EXISTING);
                    aviso += " y el archivo original se guardó como " + respaldo + ".";
                } catch (IOException errorRespaldo) {
                    aviso += ". Tampoco se pudo respaldar: " + errorRespaldo.getMessage();
                }
            }
        }
        if (datos == null) {
            datos = new DatosSerializados();
        }
        return aviso;
    }

    /** @return true si se importó un área de trabajo */
    public static boolean menubarImportar(JFrame pancho){
         JFileChooser explo=new JFileChooser();
        explo.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Área de trabajo (*.dmj)", "dmj","DMJ");
        explo.setFileFilter(filtro);
        if(JFileChooser.APPROVE_OPTION!=explo.showOpenDialog(pancho)) return false;
        try {
            datos = AreaDeTrabajoJson.cargar(explo.getSelectedFile().toPath());
        } catch (IOException | AreaDeTrabajoJson.FormatoInvalidoException e) {
            JOptionPane.showMessageDialog(pancho, "No se pudo importar el archivo:\n" + e.getMessage(),
                    "Importar", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        deltaMatroides=datos.getMatroides();
        return true;
    }

    public static void menubarExportar(JFrame pancho){
     JFileChooser explo=new JFileChooser();
        explo.setFileSelectionMode(JFileChooser.FILES_ONLY);
        explo.setSelectedFile(new File("AreaTrabajo"));
        File fichero=null;
        boolean continua=true,pushoAceptar = false;
        while(continua){
            pushoAceptar=false;
            int seleccion = explo.showSaveDialog(pancho);
            if (seleccion == JFileChooser.APPROVE_OPTION){
                pushoAceptar=true;
                fichero = explo.getSelectedFile();
                String filePath = fichero.getPath();
                if(!filePath.toLowerCase().endsWith(".dmj"))
                {
                    fichero = new File(filePath + ".dmj");
                }
                boolean decicion;
                if(fichero.exists()) { 
                   
                  if( JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(pancho,"El fichero existe,deseas reemplazarlo?","Titulo",JOptionPane.YES_NO_OPTION))
                  continua=false;
                }else continua=false;
            }else {
                pushoAceptar=false;
                break;
            }
        } 
        if(pushoAceptar){
            guardarAreaDeTrabajo(fichero.toPath());
        }
}
    
     /*
        Parametros
        -A  0 modo matriz en Archivo
        -V  1 modo sin Validar Matriz
     
        -I  3 modo incio en 0 
        -G  4 modo Interfaz Grafica Desactivada
        -E  6 modo Solo Lectura de Datos Serializados
        Nombre de Archivos:
        0 archivo de texto Matriz
        1 nombre de Obejeto Serializado/Entrada
        2 Nombre Archivo Salida txt
        3 nombre Archivo Serializar/Salida
         */
    
    public static void main(String[] args) {
       
        String aviso=cargarAreaDeTrabajo();
        deltaMatroides=datos.getMatroides();

        determinaBanderas(true,args);
        if(aviso!=null){
            System.err.println(aviso);
            if(!flags[4]) JOptionPane.showMessageDialog(null, aviso, "Área de trabajo", JOptionPane.WARNING_MESSAGE);
        }
        if(flags[4]){
            byte[][] mat;
            boolean continua=true;
            while(continua){

                if(!flags[6]){
                    mat=obtenMatriz();
                    ParValidacion ok=validar.validarMatrizAdyacencia(mat);
                    if((ok.isValidaEnSuTipo() && mat!=null) || (flags[1] && mat!=null)){
                        DeltaMatroide matroide=iniciEjecucionConsola(mat);
                       // datos.agregarDeltaMatroide(matroide);
                    }else {
                        System.out.println("Erro, matriz invalida");
                    }
                   
                }
               continua= despliegaMenu();
            }
            
        }else iniciaInterfazGrafica();
        guardarAreaDeTrabajo();
        System.out.println("Terminando ejecucion .....");
        
        
    }
    static public void cargaListaMatroides(JTree listaMatroides,int a) {
//        
//        DefaultMutableTreeNode abuelo = new DefaultMutableTreeNode("DeltaMatroides");
//        DefaultMutableTreeNode tws = new DefaultMutableTreeNode("Twist's");
//        DefaultTreeModel modelo = new DefaultTreeModel(abuelo);
//        modelo.insertNodeInto(tws, abuelo, 0);
//        listaMatroides.setModel(modelo);
        listaMatroides.setCellRenderer(new CeldaModelo());

        ElementoFolder aux = Main.deltaMatroides;

        DefaultMutableTreeNode carpetaRaiz = new DefaultMutableTreeNode(aux);
        /**
         * Definimos el modelo donde se agregaran los nodos
         */
       // System.out.println("Se invoco el 2");
        DefaultTreeModel modelo = new DefaultTreeModel(carpetaRaiz);
        listaMatroides.setModel(modelo);
        crearArbol2(aux, modelo, carpetaRaiz);
        Main.setTreeState(listaMatroides, true);

        // ListaMatroides.setModel(model);
    }

    static   void crearArbol2(ElementoFolder Folder, DefaultTreeModel modelo, DefaultMutableTreeNode carpetaRaiz) {
        int index = 0;
        // System.out.println("Se invoco el 2");
        for (index = 0; index < Folder.folder.size(); index++) {
            ElementoFolder get = Folder.folder.get(index);
            DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(get);
            if (get.isIsFolder()) {
                modelo.insertNodeInto(nodo, carpetaRaiz, index);
                crearArbol2(get, modelo, nodo);
            }

        }
    }
    
    public static void cargaListaMatroides(JTree listaMatroides) {
        ordenaArbol();
        listaMatroides.setCellRenderer(new CeldaModelo());

        ElementoFolder aux = Main.deltaMatroides;

        DefaultMutableTreeNode carpetaRaiz = new DefaultMutableTreeNode(aux);
        /**
         * Definimos el modelo donde se agregaran los nodos
         */
        DefaultTreeModel modelo = new DefaultTreeModel(carpetaRaiz);
        listaMatroides.setModel(modelo);
        crearArbol(aux, modelo, carpetaRaiz);
        Main.setTreeState(listaMatroides, true);
     //    System.out.println("Se invoco el 1");
        // ListaMatroides.setModel(model);
    }

    static void crearArbol(ElementoFolder Folder, DefaultTreeModel modelo, DefaultMutableTreeNode carpetaRaiz) {
        int index = 0;
        for (index = 0; index < Folder.folder.size(); index++) {
            ElementoFolder get = Folder.folder.get(index);
            DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(get);
            modelo.insertNodeInto(nodo, carpetaRaiz, index);
            if (get.isIsFolder()) {
                crearArbol(get, modelo, nodo);
            }
        }
    }
    public static void ordenaArbol(){
        
        ordneaNodo(deltaMatroides);
        
    }

    public static void setTreeState(JTree tree, boolean expanded) {
    Object root = tree.getModel().getRoot();
    setTreeState(tree, new TreePath(root),expanded,deltaMatroides);
  }
  
  public static void setTreeState(JTree tree, TreePath path, boolean expanded,ElementoFolder a) {
    Object lastNode = path.getLastPathComponent();
    for (int i = 0; i < tree.getModel().getChildCount(lastNode); i++) {
      Object child = tree.getModel().getChild(lastNode,i);
      TreePath pathToChild = path.pathByAddingChild(child);
      setTreeState(tree,pathToChild,expanded,a.folder.get(i));
    }
    if (a.expandContra) 
      tree.expandPath(path);
    else
      tree.collapsePath(path);
      
    
  }
    private static void determinaBanderas(boolean full,String [] args) {
        
 boolean parametros[]=new boolean[7];
     //   for(int i =0;i<5;i++) parametros[i]=false;
        if(0<args.length){
            for(int i =0;i<args.length;i++) {
                switch(args[i]){
                    case "-A":  
                        flags[0]=true;
                        nombresArchivos[0]=args[i+1];
                        System.out.println("Modo Lectura de Matriz en Archivo Activado");
                        i++;
                    break;
                    case "-V":
                        flags[1]=true;
                        System.out.println("Modo sin Validacion de Matriz simetrica Activado");
                    break;
                    case "-I":
                        parametros[3]=true;
                        System.out.println("Modo inicio En 0 Activado");
                    break;
                      case "-G":
                          if(full){
                            flags[4]=true;
                            System.out.println("Interfaz Grafica Desactivada...\nIniciando Ejecucion");
                          }
                    break;
                    
                    
                    case "-E":
                        if(full){
                            flags[6]=true;
                            System.out.println("Modo solo Lectura de Datos Activado");
                           
                        }
                    break;
                    default:
                        System.out.println("Parametro "+args[i]+" Desconocido");
                    break;
                }
            }
        }
        
     
   
        
       
    }
    private static byte[][] obtenMatriz() {
        byte mat[][] = null;
      if(flags[0]){
                if(0<nombresArchivos[0].length()){
                    try {
                        mat=flujo.leerMatriz(nombresArchivos[0]);
                    } catch (IOException ex) {
                        System.out.println("Error Al Abrir Archivo");
                    }
               }else System.out.println("No introduciste el nombre del Archivo");
               
           }else {
          
               System.out.println("Introduce el tamaño N de la matriz seguido de los NxN terminos");
               Scanner leer=new Scanner(System.in);
                int n=leer.nextInt();
                mat=new byte[n][n];
                for(int i=0;i<n;i++) for(int j=0;j<n;j++) mat[i][j]=leer.nextByte();  
           }
      
      return mat;
    }

    private static void iniciaInterfazGrafica() {
         try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        PantallaCarga p=new PantallaCarga();
        p.setVisible(true);
        
     
      
    }

    private static DeltaMatroide iniciEjecucionConsola(byte[][] mat) {
       System.out.print("\nAnalizar matriz en GF2 o GF3?\nTeclea 2 para GF2 o 3 para GF3: ");
        Scanner leer =new Scanner (System.in);
        int res=leer.nextInt();
        while(res!=2 && res!=3){
            System.out.print("Parametro incorrecto\nTeclea 2 para GF2 o 3 para GF3: ");
            res=leer.nextInt();
        }
        
        System.out.println("la matriz Introducida es:");
        imp.ImprimeMatrizNxN(mat);
        boolean tipo;
        if(res==2) {
            System.out.println("La Matriz Se Analizara en Modulo 2");
            tipo=true;
        }
        else {
            System.out.println("La Matriz se Analizara en Modulo 3");
            tipo=false;
        }
        
        
                
        DeltaMatroide deltaMat=new DeltaMatroide(mat,res-2);
       // if(flags[3]) deltaMat.setModo(false);
       
        System.out.println("Elige una Letra para identificar a la Familia de Factibles: ");
        String respuesta;
        respuesta=leer.nextLine();
        
        deltaMat.setNombreFam(respuesta);
        presentaResultados(deltaMat);
        return deltaMat;
    }

    private static boolean despliegaMenu() {
        
        int respuesta;
        while(true) {
      
        System.out.println("/////////////////////////Menu/////////////////////////////////////////");
        System.out.println("//                                                                  //");
        System.out.println("//  Delta-Matroides en la Lista: "+datos.cuantosMatroides()+"       //");
        System.out.println("//                                                                  //");
        System.out.println("// Acciones:                                                        //");
        System.out.println("//       1  Ver un Delta-Matroide en la lista                       //");
        System.out.println("//       2  Introducir otra matriz                                  //");
        System.out.println("//       3  Salir                                                   //");
        System.out.println("//                                                                  //");
        System.out.println("//////////////////////////////////////////////////////////////////////");
        
         Scanner leer =new Scanner (System.in);
         
         respuesta=leer.nextInt();
         
         switch(respuesta){
             case 1:
                 presentaDeltaMatroide();
             break;
             
             case 2:
                 return true;
             
             case 3:
                return false;
         }
        
    }

        
    }
    
    private static String imrpimeMatriz(SubMatriz get, boolean modo) {
        String cadena="X={ ";
        int desface;
        if(modo) desface=1;
        else desface=0;
       for(int i =0;i<get.getSubF().size();i++){
           if(i<get.getSubF().size()-1) cadena+=(get.getSubF().get(i)+desface)+", ";
           else  cadena+=get.getSubF().get(i)+desface;
       }
        cadena+=" }";
        cadena+="\tDeterminante: "+get.getDeterminante();
        cadena+="\n";
      for(int i =0;i<get.getMatriz().length;i++){
           for(int j =0 ;j<get.getMatriz().length;j++){
               cadena+=get.getMatriz()[i][j]+"\t";
           }
           cadena+="\n";
      }
        System.out.println(cadena);
        return cadena;
    }


    private static void presentaResultados(DeltaMatroide deltaMat) {
         LinkedList<SubMatriz> A=deltaMat.getDeltas();
        String Cadena=deltaMat.getNombreFam() +"={";        
        Cadena+=deltaMat+"}";
        
        System.out.println(Cadena);
        
        Scanner leer=new Scanner(System.in);
        System.out.println("¿Quieres Ver Los Determinantes? S/N");
        String respuesta;
        
         while(true){
            respuesta=leer.nextLine();
            if(respuesta.equalsIgnoreCase("S")||respuesta.equalsIgnoreCase("N")) break;
            else System.out.println("Seleccion "+respuesta+" Invalida\nS=si N=no");
        }
        String Cadena2="";
        if(respuesta.equalsIgnoreCase("S"))
            for(int k=0;k<A.size();k++) Cadena2+=imrpimeMatriz(A.get(k),true);
        
        
        System.out.println("¿Quieres Escribir los Resultados en un Archivo de Texto? S/N");
   
        while(true){
            respuesta=leer.nextLine();
            if(respuesta.equalsIgnoreCase("S")||respuesta.equalsIgnoreCase("N")) break;
            else System.out.println("Seleccion "+respuesta+" Invalida\nS=si N=no");
        }   
        if(respuesta.equalsIgnoreCase("S")){
          flujo=new Flujos();
            System.out.print("Teclea el nombre del Archivo de texto: ");
            String nombreArchivo=leer.nextLine();
            flujo.escribirMatriz(Cadena +"\n"+Cadena2,nombreArchivo );
            System.out.println("Los Resultados fueron Escritos en el Archivo de Texto \""+nombreArchivo+"\"");
        }
        
        
    }

    private static void presentaDeltaMatroide() {
        System.out.println("Indice\tMatroide");
       // LinkedList<DeltaMatroide> matroides=datos.getMatroides();
       // for(int i =0;i<matroides.size();i++) System.out.println(i+"\t"+matroides.get(i).getNombreFam());
        
        System.out.println("Teclea el Indice del Delta-Matroide que quieres ver: ");
        Scanner leer =new Scanner (System.in);
        int index=leer.nextInt();
        
     //   if(index<matroides.size() && 0<=index) presentaResultados(datos.getMatroides().get(index));
      //  else System.out.println("Elemento Invalido");
        
    }
    
}
