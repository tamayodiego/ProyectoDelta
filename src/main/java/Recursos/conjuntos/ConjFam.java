package Recursos.conjuntos;

import java.util.Collections;
import java.util.LinkedList;

public class ConjFam {

	LinkedList<Conjunto> conj = new LinkedList<>();

	public void agregar(Conjunto elem) {

		if (!is(elem)) {

			conj.add(elem);
			Collections.sort(conj);
		}

	}

	public void eliminar(Integer elem) {

		if (conj.contains(elem)) {

			conj.remove(elem);
		}

	}

	public String impElem() {

		String ca = "{";
		for (Conjunto conjunto : conj) {
			if (conj.indexOf(conjunto) < conj.size() - 1) {
				ca += conjunto.impElem() + ",";
			} else {
				ca += conjunto.impElem();
			}
		}
		ca += "}";

		return ca;

	}

	public boolean is(Conjunto c) {

		for (Conjunto conjunto : conj) {
			if (conjunto.getElemConj().equals(c.getElemConj()))
				return true;
		}
		return false;

	}

	public boolean axiomaInt(Conjunto F1, Conjunto F2, boolean flag) {

		OperConj opAux = new OperConj();
		Conjunto auxF1XY = null, auxDif, auxXY = new Conjunto();
		int x = 0, y = 0;

		auxDif = opAux.difSim(F1, F2);

		for (int i = 0; i < auxDif.getElemConj().size(); i++) {
			x = auxDif.getElemConj().get(i);
			for (int j = 0; j < auxDif.getElemConj().size(); j++) {
				y = auxDif.getElemConj().get(j);
				auxXY.agregar(x);
				auxXY.agregar(y);

				auxF1XY = opAux.difSim(F1, auxXY);

				if (is(auxF1XY) && !flag)
					return true;

				if (is(auxF1XY) && flag) {
                                        Formas.DeterminaDeltaMatroide.anadirLinea("x=" + x + " y=" + y);
                                        
                                        Formas.DeterminaDeltaMatroide.anadirLinea("{x,y}=" + auxXY.impElem());
                                        
                                        Formas.DeterminaDeltaMatroide.anadirLinea(F1.impElem() + "^{x,y}=" + auxF1XY.impElem());
					return true;
				}

				auxXY.limpiar();
			}

		}

		return false;

	}
        
        public LinkedList<LinkedList<Integer>> devuelveLista(){
            
            LinkedList<LinkedList<Integer>> lista = new LinkedList<LinkedList<Integer>>();
            
            for (Conjunto conjunto : conj) {
                lista.add(conjunto.getElemConj());
            }
            
            return lista;
        }

}
