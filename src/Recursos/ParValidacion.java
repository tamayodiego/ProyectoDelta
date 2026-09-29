/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recursos;

import java.io.Serializable;

/**
 * Este objeto funciona mas como una estructura de datos de control mas que como objeto, su objetivo es almacenar el tipo de matriz y su estado valido
 * @author Diego Tamayo
 * @version 1.0
 */
public class ParValidacion implements  Serializable{
    
    private boolean tipoMatriz;/* True: Simetrica False:Antisimetrica*/
    private boolean validaEnSuTipo;
    private String paramError;
    private boolean matrizenGF2oGF3;

    public boolean isMatrizenGF2oGF3() {
        return matrizenGF2oGF3;
    }

    public void setMatrizenGF2oGF3(boolean matrizenGF2oGF3) {
        this.matrizenGF2oGF3 = matrizenGF2oGF3;
    }
    
    

    public String getParamError() {
        return paramError;
    }

    public void setParamError(String paramError) {
        this.paramError = paramError;
    }
    
    

    public boolean isValidaEnSuTipo() {
        return validaEnSuTipo;
    }

    public void setValidaEnSuTipo(boolean validaEnSuTipo) {
        this.validaEnSuTipo = validaEnSuTipo;
    }
    
    
    public boolean isTipoMatriz() {
        return tipoMatriz;
    }

    public void setTipoMatriz(boolean tipoMatriz) {
        this.tipoMatriz = tipoMatriz;
    }

      
    
}
