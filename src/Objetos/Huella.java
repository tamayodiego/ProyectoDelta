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
package Objetos;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author diego
 */
public class Huella implements Comparable<Huella>,  Serializable{
    private int huella[];

    public Huella(LinkedList<LinkedList<Integer> > deltaMatrode,int size) {
        huella=new int[size+1];
        for (int i = 0; i < huella.length; i++) huella[i]=0;
        calculaHuella(deltaMatrode);
    }

    private void calculaHuella(LinkedList<LinkedList<Integer>> deltaMatrode) {
        for (int i = 0; i < deltaMatrode.size(); i++) {
            LinkedList<Integer> aux=deltaMatrode.get(i);
            huella[aux.size()]++;         
        
        }
       
    }

    @Override
    public String toString() {
        String cad="(";
        for (int i = 0; i < huella.length; i++) {
            cad+=huella[i];
            if(i<huella.length-1)cad+=",";
        }
        cad+=")";
        return cad;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Huella other = (Huella) obj;
        if(this.huella.length!=other.huella.length) return false;
        else{
            for (int i = 0; i < huella.length; i++) {
                if(this.huella[i]!=other.huella[i]) return false;
                
            }
        }
        return true;
    }

    @Override
    public int compareTo(Huella o) {
       if(this.huella.length!=o.huella.length) return this.huella.length -o.huella.length;
       
        for (int i = 0; i < huella.length; i++) if(huella[i]!=o.huella[i]) return huella[i]-o.huella[i];
        
        return 0;
    }
    
    
    
    
    
    
}
