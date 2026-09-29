/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Excepciones.OperacionDeMenorImposible;
import Recursos.ConstructorFamilias;
import Recursos.conjuntos.OperConj;
import Tested.Main;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frausto Tamayo Diego
 * @version 1.43
 */
public class DeltaMatroide implements Comparable<DeltaMatroide>, Serializable,Cloneable{
    
    private LinkedList<SubMatriz> factibles;
    private LinkedList <LinkedList <Integer> > Familia;
    private Generador generador=new  Generador();
    private String nombreFam;
    private String [] etiquetas;
    private int[] tablaFrecuencias;
     
    private byte M[][];
    //private boolean modoInicio=false;
    private int campoDeOperacion=-1;/* 0= GF2, 1=GF3*/
    private Huella huella;
    private int contador;
    private boolean modoNotificacion;
    private boolean ordenar=false; //// 
    private int modoFactibles=1;/* 0 = modo factible por linea
                                   1 = modo factibles de cardinalidades iguales por linea
                                   2 = modo factibles en una sola linea*/
    public int cardiConjuntoV;

    
  
  
    
    /**
     * Constructor de Objeto DeltaMatroide
     *@author Diego Frausto
     *@version 1.0
     *@param M Matriz que se analizara 
     *@param GF2oGF3 true para analizar en GF2 false para analizar en GF3 
     */
    public DeltaMatroide(byte M[][], int GF2oGF3) {
        generador=new Generador();
        factibles=new LinkedList();
        this.M=M;
        etiquetas=new String[M.length];
        for(int i=0;i<M.length;i++) etiquetas[i]=(i+1)+"";
        
        this.campoDeOperacion=GF2oGF3;
        generaDeltaMatroide(M);
        
        nombreFam="";
        construyeFamilia();
        cardiConjuntoV=M.length;
        this.huella=new Huella(Familia, cardiConjuntoV);
        construyeTablaDeFrecuencias();
        
    }
    
    public DeltaMatroide(byte M[][],int GF2oGF3,String fam) {
        generador=new Generador();
        factibles=new LinkedList();
        this.M=M;
        this.campoDeOperacion=GF2oGF3;
        generaDeltaMatroide(M);
        nombreFam=fam;
        construyeFamilia();
        etiquetas=new String[M.length];
        for(int i=0;i<M.length;i++) etiquetas[i]=(i+1)+"";
        cardiConjuntoV=M.length;
        this.huella=new Huella(Familia, cardiConjuntoV);
        construyeTablaDeFrecuencias();
    }
    
    public DeltaMatroide(LinkedList <LinkedList <Integer> > f, String nom, String[] eti){
        this.Familia=f;
        this.nombreFam=nom;
        generador=new Generador();
        
        factibles=new LinkedList();
        
         for(int i=0;i<f.size();i++){
             SubMatriz aux=new SubMatriz();
             aux.setSubF(f.get(i));
             factibles.add(aux);
             
         }
         
         this.etiquetas=eti;
        
        calculaCardi();
        this.huella=new Huella(Familia, cardiConjuntoV);
        construyeTablaDeFrecuencias();
        
    }

    public int[] getTablaFrecuencias() {
        return tablaFrecuencias;
    }
    
    
    
    public void reconstruyeHuella(){
        this.huella=new Huella(Familia, cardiConjuntoV);

    }

    public int getModoFactibles() {
        return modoFactibles;
    }
    
    public String getNombreFam() {
        return nombreFam;
    }

    public int getCampoDeOperacion() {
        return campoDeOperacion;
    }

    public void setCampoDeOperacion(int campoDeOperacion) {
        this.campoDeOperacion = campoDeOperacion;
    }
//
//    public Huella getHuella() {
//        return huella;
//    }
//
//    public void setHuella(Huella huella) {
//        this.huella = huella;
//    }

    
    public void setNombreFam(String nombreFam) {
        this.nombreFam="";
        this.nombreFam += nombreFam;
    }
    
    
    
    /**
     *Metodo generador del deltaMatroide
     *@author Diego Frausto
     *@version 1.7
     *@param M Matriz que se analizara 
     */
    private void generaDeltaMatroide(byte[][] M) {
//       
//        System.out.println("entre a genera delta conM:");
//        System.out.println(JFrameInicio.generaMatrisString(M));
            Main.estado="Generando Combinaciones....";
       
        LinkedList <LinkedList <Integer>> lista=generador.subMatrices(M.length);
//        //System.out.println("Ya Acabe de Generar Combinaciones");
//        System.out.println("Tamaño de lista:");
//        System.out.println(lista.size());
//        
            Main.estado="Analizando Combinaciones...";
            Main.cuenta=lista.size();
            
        
        analizaLoops();
//        System.out.println(GF2oGF3);
        for(int i=0;i<lista.size();i++,Main.uCuenta++){
//            System.out.println(lista.get(i));
          SubMatriz matrizAux=generador.analizaSubMatriz(lista.get(i),M,campoDeOperacion);
//            System.out.println(matrizAux);
          if(matrizAux!=null) factibles.add(matrizAux);  
        } 
        Main.estado="Terminado";
        Main.uCuenta=0;
//        System.out.println("Tamaño deltas:");
//        System.out.println(deltas.size());
//        
        
    }
    
    
    
    public void setModeFactibles0(){
        modoFactibles=0;
    }
    
     public void setModeFactibles1(){
        modoFactibles=1;
    }
      public void setModeFactibles2(){
        modoFactibles=2;
    }
    
    public LinkedList<SubMatriz> getDeltas() {
        return factibles;
    }

   

    public byte[][] getM() {
        return M;
    }
    
     public int getCampo() {
        return campoDeOperacion;
    }
   

    public String[] getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String[] etiquetas) {
        this.etiquetas = etiquetas;
    }
    
    

   
    public String getResultado() {
        /* 0 = modo factible por linea
            1 = modo factibles de cardinalidades iguales por linea
            2 = modo factibles en una sola linea*/
        int desface;
        
        String saltoCardinalidad,saltoSencillo;
        switch(modoFactibles){
            case 1: 
                saltoCardinalidad="\n";
                saltoSencillo="";
            break;
            case 2: 
                saltoCardinalidad="";
                saltoSencillo="\n";
            break;
            default:
                saltoCardinalidad="";
                saltoSencillo="";
            break;
                
                
        }
       
        
        String Cadena="";
        int cardinalidad=Familia.get(0).size();
        
        for(int i=0;i<Familia.size();i++){
        
            LinkedList <Integer> tempFactible=Familia.get(i);
            String subCadena="";

            if(cardinalidad!=tempFactible.size()){
                subCadena+=saltoCardinalidad;    
                cardinalidad=tempFactible.size();
            }

            subCadena+="{";
             for(int j=0;j<tempFactible.size();j++){
                
                  subCadena+=(etiquetas[tempFactible.get(j)])+"";
                  if(j<tempFactible.size()-1)  subCadena+=",";
             }
             subCadena+="}";
             if(i<Familia.size()-1) subCadena+=",";
             subCadena+=saltoSencillo;
             Cadena+=subCadena;
        }
        Cadena+=saltoCardinalidad+saltoSencillo;
      
        return Cadena;
    }

    public Huella getHuella() {
        return huella;
    }

    public void setHuella(Huella huella) {
        this.huella = huella;
    }
    

    private void analizaLoops() {
        for(int j=0;j<M.length;j++){
            if(M[j][j]!=0){
                byte[][] aux1=new byte[1][1];
                aux1[0][0]=1;
                SubMatriz A=new SubMatriz();
                LinkedList<Integer> subF=new LinkedList();
                subF.add(j);
                A.setDeterminante(1);
                A.setMatriz(aux1);
                A.setSubF(subF);
                factibles.add(A);
            }
        }
    }

    public void setOrdenar(boolean ordenar) {
        this.ordenar = ordenar;
    }
    
    
    
    @Override
    public String toString() {
        String Cad="";
        Cad+= nombreFam;
        Cad+= " "+Familia.size()+" Factibles en ";
        switch(campoDeOperacion) {
            case 0:Cad+="GF(2)";
            break;
            case 1: Cad+="GF(3)";
            break;
            case 2: Cad+="GF(3)";
            break;
            case 3: Cad+="R";
            break;
            default: Cad+="Campo desconocido";
        }
        return Cad;
    }

    public void construyeFamilia() {
        Familia=new LinkedList();
        Familia.add(new LinkedList <Integer>());
        for(int i =0;i<factibles.size();i++){
            Familia.add(factibles.get(i).getSubF());
            
        }
       
    }
    public void twist(LinkedList<Integer> factible) {
        for(int i =0;i<Familia.size();i++){
            Familia.set(i, generador.diferenciaSimetrica(Familia.get(i), factible));
        }
        construyeTablaDeFrecuencias();
        reconstruyeHuella();
    }
    
    
    public LinkedList <LinkedList <Integer> > twist(LinkedList <Integer> factible, boolean arg[]){
       // System.out.println(factible + " " +arg );
     //   System.out.println(Familia.size()+" "+arg.length+" ");
        for(int i =0;i<Familia.size();i++){
//            System.out.println(i+ " de " + Familia.size() );
//            Familia.get(i);
//            System.out.println(arg[i]);
//            System.out.println(factible);
//            generador.toString();
            if(arg[i]) Familia.set(i, generador.diferenciaSimetrica(Familia.get(i), factible));
        }
       if(ordenar) ordenaFamilia();
        this.construyeTablaDeFrecuencias();
        reconstruyeHuella();
        return Familia;
        
    }

    public LinkedList <LinkedList <Integer> > getFamilia() {
        return Familia;
    }

    public void setFamilia(LinkedList<LinkedList<Integer>> Familia) {
        this.Familia = Familia;
    }
    
    
    
     public Object clone()    {
//        
//          private LinkedList<SubMatriz> factibles;
//    private LinkedList <LinkedList <Integer> > Familia;
//    private Generador generador=new  Generador();
//    private String nombreFam;
//    private String [] etiquetas;
        Object clone = null;
        try
        {
            clone = super.clone();
        } 
        catch(CloneNotSupportedException e)
        {
            // No deberia ocurrir
        }
        
        
        ((DeltaMatroide)clone).setFamilia((LinkedList<LinkedList<Integer>>) Familia.clone());
        
        return clone;
    }

    private void calculaCardi() {
    cardiConjuntoV= etiquetas.length;   
    }


    public LinkedList <String[] >  isIsomorfo(DeltaMatroide e){
    
        if(!this.huella.equals(e.getHuella())) return null;
        if(evaluaTablaFrecuencias(e.tablaFrecuencias)) return null;
        return construyeBiyeccion(e);       
    }

    private void construyeTablaDeFrecuencias() {
        tablaFrecuencias=new int[etiquetas.length];
        for (int i = 0; i < tablaFrecuencias.length; i++) tablaFrecuencias[i]=0;
        
        for (int i = 0; i < Familia.size(); i++) {
            LinkedList<Integer>  tem=Familia.get(i);
            for (int j = 0; j < tem.size(); j++) {
                tablaFrecuencias[tem.get(j)]++;
                
            }
           
            
        }
            
        
       
    }

    private boolean evaluaTablaFrecuencias(int[] tabla) {
       boolean usados[]=new boolean[tabla.length];
        for (int i = 0; i < usados.length; i++) usados[i]=true;
            
        for (int i = 0; i < usados.length; i++) {
            boolean noSeEncontro=true;
            for (int j = 0; j < usados.length; j++) {
                if(usados[j]){
                    if(this.tablaFrecuencias[i]==tabla[j]){
                        usados[j]=false;
                        noSeEncontro=false;  
                        break;
                    }
                }
                
            }
            if(noSeEncontro) return true;
            
        }
        return false;
    }

    public void ordenaFamilia() {
        for (LinkedList<Integer> Fi : Familia) Collections.sort(Fi);
        Collections.sort(Familia, new Comparator<LinkedList<Integer>>() {
            @Override
            public int compare(LinkedList<Integer> o1, LinkedList<Integer> o2) {
                if(o1.size()!=o2.size())
                    return o1.size()-o2.size();
                else{
                    Collections.sort(o1);
                    Collections.sort(o2);
                    for (int i = 0; i < o2.size(); i++) {
                        if(o1.get(i).compareTo(o2.get(i))!=0) 
                            return o1.get(i)-o2.get(i);
                        
                    }
                    return 0;
                            
                }
            }
        });
    }

    boolean compareToNoEtiquetas(DeltaMatroide F) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int compareTo(DeltaMatroide D){
        
        if(this.Familia.size()!=D.Familia.size())  return this.Familia.size()-D.Familia.size();
        
        if(this.huella.compareTo(D.huella)!=0) return this.huella.compareTo(D.huella);
        this.ordenaFamilia();
        D.ordenaFamilia();
        int mod1=modoFactibles,mod2=D.modoFactibles;
        this.setModeFactibles0();
        D.setModeFactibles0();
        int comp=this.getResultado().compareTo(D.getResultado());
        this.modoFactibles=mod1;
        D.modoFactibles=mod2;
        return comp;
        
    }

    public boolean equalsNoEtiquetas(DeltaMatroide F) {
        ordenaFamilia();
        F.ordenaFamilia();
        if(!this.getHuella().equals(F.getHuella())) return false;
        else{
            for (int i = 0; i < Familia.size(); i++) {
                LinkedList<Integer> F1=Familia.get(i);
                LinkedList<Integer> F2=F.getFamilia().get(i);
                if(!equelsFactibles(F1,F2)) return false;
            }
            
            
        }
        return true;
        
        
    }

    private boolean equelsFactibles(LinkedList<Integer> F1, LinkedList<Integer> F2) {
        if(F1.size()!=F2.size()) return false;
        Collections.sort(F2);
         Collections.sort(F1);
         for (int i = 0; i < F2.size(); i++) 
            if(F1.get(i).compareTo(F2.get(i))!=0) return false;
         return true;        
    }

    
    
    private class GeneradorBiyeccion{
        LinkedList<String[]> permutaciones;
        LinkedList<String[]> funcionBiyectiva;
        int limite;
        public GeneradorBiyeccion(DeltaMatroide F1,DeltaMatroide F2) {
            permutaciones=new LinkedList();
            funcionBiyectiva=new LinkedList();
            String[] etiq=new String[F1.getEtiquetas().length];
            boolean usados[]=new boolean[F1.getEtiquetas().length];
            for (int i = 0; i < usados.length; i++) usados[i]=false;
            funcionBiyectiva.add(F1.getEtiquetas());
            generaBiyecciones(0,F1,F2,etiq,usados);
            buscaBiyecionValida(F1,F2);
        }

        public LinkedList<String[]> getFuncionBiyectiva() {
            for (int i = 0; i < permutaciones.size(); i++) {
                System.out.println(Imprime(permutaciones.get(i)));
                
            }
            return funcionBiyectiva;
        }
        
        
        
         private boolean buscaBiyecionValida(DeltaMatroide F1,DeltaMatroide F2) {
             for (int i = 0; i < permutaciones.size(); i++) {
                 String[] integeres = permutaciones.get(i);
                 boolean funciono=true;
                 for (int j = 0; j < F1.getFamilia().size(); j++) {
                   
                    LinkedList<Integer> factible=F1.getFamilia().get(j);
                   //  System.out.println(factible);
                    if(!busaFactibleEn(factible,F2,integeres)) {
                        funciono=false;
                        break;
                    }
                     
                 }
                 if(funciono) {
                     System.out.println(integeres[0]);
                     funcionBiyectiva.add(integeres);
                     return true;
                 }
                 
                 
             }
             funcionBiyectiva=null;
             return false;
             
             
            
        }
        private void generaBiyecciones(int i, DeltaMatroide F1, DeltaMatroide F2, String[] etiq, boolean[] usados) {
            if(i<F1.getEtiquetas().length){
                for (int j = 0; j < F1.getEtiquetas().length; j++) {
                    if(F1.getTablaFrecuencias()[i]==F2.getTablaFrecuencias()[j]// puedo ir de etiqueta de F1 en i a la etiqueta F2 en j?
                            && !usados[j]){// y ademas no he usado a etiqueta en j
                        etiq[i]=F2.getEtiquetas()[j];
                        usados[j]=true;
                        generaBiyecciones(i+1, F1, F2, clonaEtiquetas(etiq), clonaUsados(usados));
                        etiq[i]=null;
                        usados[j]=false;
                    }
                }
            }else 
            {
                for (int j = 0; j < etiq.length; j++)  System.out.print(etiq[j]);
                System.out.println("");
                permutaciones.add(etiq);
            }
            
        }
        private String[] clonaEtiquetas(String [] et){
            String a[]=new String[et.length];
            System.out.println(a.length);
            System.out.println(et[0]);
            for (int i = 0; i < a.length; i++) a[i]=et[i];
            return a;
        }
        private boolean[] clonaUsados(boolean[] a){
            boolean[] b=new boolean[a.length];
            for (int i = 0; i < b.length; i++) b[i]=a[i];
            return b;
        
    }

        private Integer[] conjuntoBaseDeF(DeltaMatroide F1) {
            Integer a[]=new Integer[F1.getEtiquetas().length];
            for (int i = 0; i < a.length; i++) a[i]=i;
            return a;
        }

        private boolean busaFactibleEn(LinkedList<Integer> factible, DeltaMatroide F2, String[] integeres) {
            LinkedList<Integer> fac=(LinkedList<Integer>) factible.clone();
           // for (int i = 0; i < fac.size(); i++) fac.set(i,integeres[fac.get(i)]);
            
            for (int i = 0; i < F2.Familia.size(); i++) 
                if(equelsFactible(factible,integeres,F2.Familia.get(i),F2.getEtiquetas())) return true;
                
            return false;
        }

        private boolean equelsFactible(LinkedList<Integer> fac, String[] permutacion, LinkedList<Integer> get, String[] etiquetas1) {
            if(fac.size()!=get.size()) return false;
            else{
                for (int i = 0; i < get.size(); i++) {
                    boolean noEsta=true;
                    Integer n1= fac.get(i);
                    for (int j = 0; j < get.size(); j++) {
                        Integer n2 = get.get(j);
                        if(  permutacion[n1].compareTo(etiquetas1[n2])==0 ) {
                            noEsta=false;
                            break;
                        }
                        
                    }
                    if(noEsta) return false;
                    
                }
                return true;
            }
                
        }

        private String Imprime(String[] get) {
           String cad="[";
            for (int i = 0; i < get.length; i++) {
                String integer = get[i];
                cad+=integer+",";
                
            }
            cad+="]";
            return cad;
        }
    }
        
    private LinkedList<String[]> construyeBiyeccion(DeltaMatroide e) {
        GeneradorBiyeccion gen=new GeneradorBiyeccion(this, e);
        return gen.getFuncionBiyectiva();
    }

    
    public DeltaMatroide getMenorBorrado(String x) throws OperacionDeMenorImposible{
        boolean noEcontrado=true;
        int i;
        for ( i = 0; i < etiquetas.length; i++) {
            if(etiquetas[i].compareTo(x)==0){
                noEcontrado=false;
                break;
            }
        }
        
        if(noEcontrado) throw new OperacionDeMenorImposible("Elemento "+x+" no pertenece al DM");
        
        OperConj op=new OperConj();
        LinkedList<LinkedList<Integer>> fam=op.operacionMenorBorrado(Familia, i);
        String nombre=this.nombreFam+"/"+x;
        
        DeltaMatroide a=new DeltaMatroide(fam, nombre, etiquetas);
        a.setModeFactibles0();
        try {
            ConstructorFamilias o = new ConstructorFamilias("D={" +a.getResultado()+"}");
            a=new DeltaMatroide(o.getFamilia(), nombre, o.getConjuntoV());
            
        } catch (Exception ex) {
            Logger.getLogger(DeltaMatroide.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a;
        
    }
    
    public DeltaMatroide getMenorContraccion(String x) throws OperacionDeMenorImposible{
        boolean noEcontrado=true;
        int i;
        for ( i = 0; i < etiquetas.length; i++) {
            if(etiquetas[i].compareTo(x)==0){
                noEcontrado=false;
                break;
            }
        }
        
        if(noEcontrado) throw new OperacionDeMenorImposible("Elemento "+x+" no pertenece al DM");
        
        OperConj op=new OperConj();
        LinkedList<LinkedList<Integer>> fam=op.operacionMenorContraccion(Familia, i);
        String nombre=this.nombreFam+"*"+x;
        
         DeltaMatroide a=new DeltaMatroide(fam, nombre, etiquetas);
        a.setModeFactibles0();
        try {
            ConstructorFamilias o = new ConstructorFamilias("D={" +a.getResultado()+"}");
            a=new DeltaMatroide(o.getFamilia(), nombre, o.getConjuntoV());
            
        } catch (Exception ex) {
            Logger.getLogger(DeltaMatroide.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a;
    }
    
}
