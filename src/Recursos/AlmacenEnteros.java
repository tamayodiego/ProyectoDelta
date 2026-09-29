/*
 * Copyright (C) 2017 Diego
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

/**
 *
 * @author Diego
 */
public class AlmacenEnteros {
    private String cadena;
    private int indexActual;
    private int fin;
    
    public AlmacenEnteros(String cad){
        cadena=cad;
        indexActual=0;
        fin=cad.length();
    }
    
    public int nextInt(){
        int result=-2;
        int begin,end;
        if(indexActual<fin){
            String B=encuentraDesde();
            if(B!=null) result=Integer.parseInt(B);
        }
        return result;
    }

   
    private String encuentraDesde() {
      int j,i;
      
        for (i = indexActual; i < fin && (cadena.charAt(i)<'0' || cadena.charAt(i)>'9') && cadena.charAt(i)!='-'; i++);
        
        if(i==fin) return null;
        
        for (j = i+1; j < fin && (cadena.charAt(j)>='0' && cadena.charAt(j)<='9') ; j++);
        
        String re=cadena.substring(i, j);
        
        indexActual=j;
        return re;
        
        
    }
    
}
