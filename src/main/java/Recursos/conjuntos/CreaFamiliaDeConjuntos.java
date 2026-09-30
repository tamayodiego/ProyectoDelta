package Recursos.conjuntos;

import Recursos.ConstructorFamilias;
import java.util.LinkedList;
import java.util.Scanner;

public class CreaFamiliaDeConjuntos {

    @SuppressWarnings("empty-statement")
    public ConjFam creaFamilia(String cad) throws Exception {
        
      
        
        
        int i = 0;
        int j;
        for (; i < cad.length() && cad.charAt(i) != '{'; i++)
			;

        for (j = cad.length() - 1; j <= 0 && cad.charAt(j) != '}'; j--)
			;

        String subcad = cad.substring(i + 1, j);

        LinkedList<String> lista = subs(subcad);

        ConjFam fam = creaSub(lista);

        return fam;

    }

    private ConjFam creaSub(LinkedList<String> lista) {
        ConjFam famAux = new ConjFam();
        Scanner sc;

        for (String str : lista) {
            sc = new Scanner(str);
            sc.useDelimiter(",");
            Conjunto conjAux = new Conjunto();

            while (sc.hasNext()) {
                conjAux.agregar(sc.nextInt());
            }
            famAux.agregar(conjAux);
        }
        
        return famAux;
    }

    private LinkedList<String> subs(String subcad) throws Exception {
        LinkedList<String> lista = new LinkedList();

        boolean flag = false;
        int i = 0;
        int j = 0;

        while (i < subcad.length()) {
            if (subcad.charAt(i) == '{') {
                if (flag) {
                    throw new Exception();

                } else {
                    flag = true;
                    j = i;
                }
            }

            if (subcad.charAt(i) == '}') {
                if (flag) {
                    lista.add(subcad.substring(j + 1, i));
                    flag = false;
                } else {
                    throw new Exception();

                }
            }
            i++;

        }
        if (flag) {
            throw new Exception();
        }
        return lista;

    }
}
