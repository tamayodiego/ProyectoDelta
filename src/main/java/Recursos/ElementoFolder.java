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
public class ElementoFolder {

    private boolean isFolder;
    public boolean expandContra;
    private String nombre;
    private DeltaMatroide matrode;
    public LinkedList<ElementoFolder> folder;
    private ElementoFolder nodoPadre;

    public ElementoFolder(boolean isFolder, String nombre, Object objeto, ElementoFolder padre) {
        this.isFolder = isFolder;
        this.nombre = nombre;
        nodoPadre = padre;
        if (this.isFolder) {
            folder = (LinkedList<ElementoFolder>) objeto;
        } else {
            matrode = (DeltaMatroide) objeto;
        }
        expandContra=true;
    }

    public String getRuta() {
        String ruta = "";
        if (nodoPadre == null) {
            ruta += "\\" + nombre;
        } else {
            ruta = nodoPadre.getRuta() + "\\" + nombre;
        }

        return ruta;
    }

    public ElementoFolder getNodoPadre() {
        return nodoPadre;
    }

    public void setNodoPadre(ElementoFolder nodoPadre) {
        this.nodoPadre = nodoPadre;
    }

    
    public boolean isIsFolder() {
        return isFolder;
    }

    public void setIsFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DeltaMatroide getMatrode() {
        return matrode;
    }

    public void setMatrode(DeltaMatroide matrode) {
        this.matrode = matrode;
    }

    public LinkedList<ElementoFolder> getFolder() {
        return folder;
    }

    public void setFolder(LinkedList<ElementoFolder> folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return nombre; //To change body of generated methods, choose Tools | Templates.
    }

    
}
