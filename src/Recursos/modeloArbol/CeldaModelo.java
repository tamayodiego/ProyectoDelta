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
package Recursos.modeloArbol;

import Recursos.ElementoFolder;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author diego
 */
public class CeldaModelo  extends DefaultTreeCellRenderer{
    
    @Override
    public   Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus) {
        DefaultMutableTreeNode au=(DefaultMutableTreeNode) value;
        
          super.getTreeCellRendererComponent(tree, value, selected,expanded, leaf, row, hasFocus);
        //altura de cada nodo
        tree.setRowHeight(26);

        //setOpaque(true);     
        //color de texto
        setForeground( Color.black );
         if( selected )
            setForeground( Color.red );        
        //-- Asigna iconos
        // si value es la raiz
        if ( tree.getModel().getRoot().equals( (DefaultMutableTreeNode) value ) ) {
            setIcon(  new ImageIcon(getClass().getResource("/Imagenes/carpeta.png")) );
        } 
        else if(au.getUserObject() instanceof ElementoFolder){
            ElementoFolder aux=(ElementoFolder) au.getUserObject();
            if(aux.isIsFolder()) setIcon(  new ImageIcon(getClass().getResource("/Imagenes/carpeta.png")) );   
            else setIcon(  new ImageIcon(getClass().getResource("/Imagenes/molecula.png")) );
        }
        return this;
    }
    
    
    
}
