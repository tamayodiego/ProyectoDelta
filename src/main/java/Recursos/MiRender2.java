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

import java.awt.Color;
import java.awt.Component;
import java.util.function.IntPredicate;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Diego
 */
public class MiRender2 extends DefaultTableCellRenderer
{
   /** Indica si el renglón va resaltado. */
   private final IntPredicate resaltado;

   public MiRender2(IntPredicate resaltado) {
      this.resaltado = resaltado;
   }

   public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column)
   {
    super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
      
      if ( resaltado.test(row) )
      {
          
         this.setOpaque(true);
         this.setBackground(Color.orange);
         this.setForeground(Color.BLACK);
      } else {
          //
          this.setOpaque(false);
        
        
         // this.setOpaque(false);
      }

      return this;
   }

   
}
