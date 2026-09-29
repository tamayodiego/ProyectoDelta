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
package Recursos;

import Objetos.DeltaMatroide;
import java.util.LinkedList;

/**
 *
 * @author diego
 */
public class OperacionesListas {

    public LinkedList<DeltaMatroide> getOperacion(int operacion, LinkedList<DeltaMatroide> A, LinkedList<DeltaMatroide> B) {
            LinkedList<DeltaMatroide> resultado=null;
        switch(operacion){
            case 0:
                resultado=union(A,B);
            break;
            case 1:
                resultado=interseccion(A,B);
            break;
            case 2:
                resultado=diferencia(A,B);
            break;
            case 3:
                resultado=diferencia(B,A);
            break;
            case 4:
                resultado=diferenciaSimetrica(A,B);
            break;
        }
        return resultado;
    }

    private LinkedList<DeltaMatroide> interseccion(LinkedList<DeltaMatroide> A, LinkedList<DeltaMatroide> B) {
        LinkedList<DeltaMatroide> resultado=new LinkedList<>();
        
        for (DeltaMatroide D : A) {
            for (DeltaMatroide E : B) {
                if(D.compareTo(E)==0) {
                    resultado.add(D);
                    break;
                }
            }
            
        }
        return resultado;
    }

    private LinkedList<DeltaMatroide> diferencia(LinkedList<DeltaMatroide> A, LinkedList<DeltaMatroide> B) {
        LinkedList<DeltaMatroide> resultado=new LinkedList<>();
        boolean noEsta;
        for (DeltaMatroide D : A) {
            noEsta=true;
            for (DeltaMatroide E : B) {
                if(D.compareTo(E)==0) {
                    noEsta=false;
                    break;
                }
                
            }
            if(noEsta) resultado.add(D);
            
        }
        return resultado;
    }

    private LinkedList<DeltaMatroide> diferenciaSimetrica(LinkedList<DeltaMatroide> A, LinkedList<DeltaMatroide> B) {
       LinkedList<DeltaMatroide> union=union(A,B);
        return diferencia(union, interseccion(A, B));
       
    }

    private LinkedList<DeltaMatroide> union(LinkedList<DeltaMatroide> A, LinkedList<DeltaMatroide> B) {
        LinkedList<DeltaMatroide> resultado=new LinkedList<>();
        resultado.addAll(A);
        for (DeltaMatroide D:B){
            if(!resultado.contains(D)) resultado.add(D);
        }
        return resultado;
    }
    
    
    public LinkedList<DeltaMatroide> depuracionIsomorfica(LinkedList<DeltaMatroide> L1){
        LinkedList<DeltaMatroide> L2=new LinkedList<>();
         int indices[]=new int[L1.size()];//={-1};


        for(int i=0; i < L1.size() ; ++i){
            indices[i]=-1;
            for(int j=i+1; j < L1.size() ; ++j){

                if(L1.get(i).isIsomorfo(L1.get(j))!= null){
                    indices[i]=j;
                    break;
                } 

            }

        }

        boolean usados[]=new boolean[L1.size()];
        for(int i=0;i<L1.size();++i) usados[i]=true;

        for(int i=0;i<L1.size();++i){
            if(indices[i]!=-1) usados[indices[i]]=false;
            if(usados[i]) L2.add(L1.get(i));

        }
        
        return L2;
    }
    
    
}
