package Recursos.conjuntos;
import Formas.DeterminaDeltaMatroide;
import Objetos.Generador;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;



public class OperConj {

	public Conjunto union(Conjunto c1, Conjunto c2) {

		Conjunto aux = new Conjunto();

		for (Integer elem : c1.getElemConj()) {
			aux.agregar(elem);
		}

		for (Integer elem : c2.getElemConj()) {
			aux.agregar(elem);
		}

		return aux;

	}

	public Conjunto interseccion(Conjunto c1, Conjunto c2) {

		Conjunto aux = new Conjunto();

		for (Integer elem : c1.getElemConj()) {
			if (c2.getElemConj().contains(elem))
				aux.agregar(elem);
		}

		return aux;

	}

	public Conjunto difSim(Conjunto c1, Conjunto c2) {

		Conjunto auxi, aux;
		aux = union(c1, c2);
		auxi = interseccion(c1, c2);

		for (Integer elem : auxi.getElemConj()) {
			aux.eliminar(elem);
		}

		return aux;

	}

	public boolean equal(Conjunto c1, Conjunto c2) {

		if (c1.getElemConj().equals(c2.getElemConj()))
			return true;

		return false;

	}
        
        public static boolean isMatroide(LinkedList<LinkedList<Integer>> familia) {
        for (int i = 0; i < familia.size(); i++) {
            LinkedList<Integer> F1 = familia.get(i);
            for (int j = 0; j < familia.size(); j++) {
                if(i!=j){
                    LinkedList<Integer> F2 = familia.get(j);
                    if(!axiomaSimetrico(F1,F2,familia)){
                        return false;
                    }
                }
                
            }
            
        }   
       return true;
    }
    
    private static boolean axiomaSimetrico(LinkedList<Integer> F1, LinkedList<Integer> F2,LinkedList<LinkedList<Integer>> F) {
        
        LinkedList<Integer> dif=(new Generador()).diferenciaSimetrica(F1,F2);
        for (Integer x : dif) {
            boolean seCumpleX=false;
            for (Integer y : dif) {
                LinkedList<Integer> aux=new LinkedList();
                aux.add(x);
                if(x.compareTo(y)!=0) aux.add(y);
                LinkedList<Integer> difEnF=(new Generador()).diferenciaSimetrica(F1,aux);
                if(buscaFactibleEn(difEnF,F)){
                    seCumpleX=true;
                    break;
                    
                }
            }
            if(!seCumpleX) return false;
        }
        return true;
    }

    private static boolean buscaFactibleEn(LinkedList<Integer> difEnF, LinkedList<LinkedList<Integer>> F) {
        
        for (LinkedList<Integer> Fi : F) {
            /// solo en factibles del mismo tamaño
            if(Fi.size()==difEnF.size()){
              //ordenamos los factibles
              boolean seEncontro=true;
              if(Fi.size()==0) return true;
              Collections.sort(Fi);
              Collections.sort(difEnF);
              //buscamos en cada indice debe de corresponder 
                for (int i = 0; i < Fi.size(); i++) {
                    if(Fi.get(i).compareTo(difEnF.get(i))!=0) {
                        seEncontro=false;
                        break;
                    }
                }
                /// si ninguno rompio el ciclo significa que los
                // factibles son iguales
                if(seEncontro)return true;    
                
            }
        }
        // si recorrimos todoslos factibles 
        //y ningunos activo entonces el factible no esta en F
        return false;
    }
        public LinkedList <LinkedList <Integer> > operacionMenorBorrado(LinkedList <LinkedList <Integer> > F, int x){
            LinkedList <LinkedList <Integer> > resultado=new LinkedList();
            Integer X=new Integer(x);
            boolean noEsta=true;
            for (int i = 0; i < F.size(); i++) {
                noEsta=true;
                LinkedList <Integer> tem=F.get(i);
                for (int j = 0; j < tem.size(); j++) {
                    if(X.compareTo(tem.get(j))==0) {
                        noEsta=false;
                        break;
                    }
                    
                    
                }
                if(noEsta) resultado.add(tem);
                
            }
            
            
            return resultado;
        }
        
        public LinkedList <LinkedList <Integer> > operacionMenorContraccion(LinkedList <LinkedList <Integer> > F,int x){
            LinkedList <LinkedList <Integer> > resultado=new LinkedList();
            
            Integer X=new Integer(x);
            boolean Esta;
            for (int i = 0; i < F.size(); i++) {
                Esta=false;
                LinkedList <Integer> tem=F.get(i);
                int j;
                for ( j = 0; j < tem.size(); j++) {
                    if(X.compareTo(tem.get(j))==0) {
                        Esta=true;
                        break;
                    }
                    
                    
                }
                if(Esta) {
                    LinkedList <Integer> tem2=new LinkedList();
                    for ( j = 0; j < tem.size(); j++) {
                    if(! (X.compareTo(tem.get(j))==0)) 
                       tem2.add(tem.get(j));
                    
                     
                    }
                    resultado.add(tem2);
                
                
            }
            
            
            
        }
            return resultado;

    }
        public LinkedList<LinkedList<Integer>> ordenaLista(LinkedList<LinkedList<Integer>> listaDes) {

        //Esto ordena lexicográficamente los elementos de los factibes
        for (LinkedList<Integer> lista : listaDes) {
            lista.sort(null);

        }
        //Ésto ordena la familia
        Collections.sort(listaDes, new Comparator<LinkedList<Integer>>() {

            @Override
            public int compare(LinkedList<Integer> l1, LinkedList<Integer> l2) {
                //si los factibles son del mismo tamaño se fijará en el primer elemento para así ordenarlos
                //lexicográficamente a partir de éste
                if (l1.size() == l2.size()) {
                    return l1.get(0) - l2.get(0);
                } else {
                    //Si no son del mismo tamaño los ordenará de cardinalidad menor al mayor
                    return l1.size() - l2.size();
                }
            }
        });

        return listaDes;
    }
        
        public String[] obtentenEtiquetas(LinkedList<LinkedList<Integer>> j){
            String[] ab = null;
            return ab;
        }

}
