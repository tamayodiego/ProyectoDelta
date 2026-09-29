package Recursos.conjuntos;

import java.util.LinkedList;

public class Conjunto implements Comparable<Conjunto> {

	private LinkedList<Integer> elemConj = new LinkedList<>();

	public void agregar(Integer elem) {

		if (!elemConj.contains(elem)) {

			elemConj.add(elem);
			elemConj.sort(null);
		}

	}

	public void eliminar(Integer elem) {

		if (elemConj.contains(elem)) {

			elemConj.remove(elem);
		}

	}

	public void limpiar() {

		elemConj.clear();

	}

	public void sustituir(Integer e1, Integer e2) {
		eliminar(e1);
		agregar(e2);
	}

	public String impElem() {
		
		if(elemConj.isEmpty()) return "Ø";

		String elm = "{";

		for (Integer num : elemConj) {

			if (elemConj.indexOf(num) < elemConj.size() - 1) {
				elm += num + ",";
			} else {
				elm += num;
			}
		}
		elm += "}";
		// System.out.println(elemConj.toString());
		// System.out.println(elm);

		return elm;

	}

	public LinkedList<Integer> getElemConj() {
		return elemConj;
	}

	public void setElemConj(LinkedList<Integer> elemConj) {
		this.elemConj = elemConj;
	}

	@Override
	public int compareTo(Conjunto o) {
		int comparedSize = o.elemConj.size();
		if (this.elemConj.size() > comparedSize) {
			return 1;
		} else if (this.elemConj.size() == comparedSize) {
			return 0;
		} else {
			return -1;
		}
	}

}
