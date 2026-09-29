package Objetos;

import java.io.Serializable;


public class Determinante implements  Serializable{
	
	private   double det=1;
	public float matriz[][];
	public  boolean flg=false;
	
//	public static boolean flg; //bandera para imprimir
//	//constructor recibe una matriz de nxn y prende o apaga bandera para imprimir
//	@SuppressWarnings("static-access")
//	public Determinante(float[][] matriz, boolean flag) {
//		super();
//		this.matriz = matriz;
//		this.flg = flag;
//	}
	
	//constructor recibe una matriz de nxn
	public Determinante(byte[][] matriz) {
		super();
		this.matriz =parseFloat(matriz);
	}
	

///////////////CALCULO DEL DETERMINANTE DE LA MATRIZ TRIANGULADA, MULTIPLICAN LOS ELEMENTOS DE SU DIAGONAL	
public  double calcDet() {
	
	if (flg) imprime(matriz);
	
	//primero triangula la matriz
	triangular(matriz);
	
	//calcula el determinante
	det=det*matriz[matriz.length-1][matriz.length-1];
	//lo redondea
	det=fijarNum(det, 0);
	
	return det;
}
	
///////////////////FUNCIONES B�SICAS PARA TRIANGULAR LA MATRIZ///////////////////////////////////////////
////////////////////INTERCAMBIA r1<-->r2////////////////////////////////////////////////////////////////	
	public   void intercambia(float mat[][],int r1, int r2){
		float aux1[]= new float[mat.length],
			aux2[]= new float[mat.length];
		
		for (int i = 0; i < mat.length; i++) {
			aux1[i]=mat[r1][i];
			aux2[i]=mat[r2][i];
		}
		
		for (int i = 0; i < mat.length; i++) {
			mat[r1][i]=aux2[i];
			mat[r2][i]=aux1[i];
		}
		
		det=-det;

	}
//////////////////SUSTITUYE c1r1 - c2r2 --> r2///////////////////////////////////////////////////////////	
	public   void sustituye(float mat[][],float c1,int r1,float c2,int r2) {
		float auxr[]=new float [mat.length],
			aux1[]= new float[mat.length],
			aux2[]= new float[mat.length];
		
		for (int i = 0; i < mat.length; i++) {
			aux1[i]=c1*mat[r1][i];
			aux2[i]=c2*mat[r2][i];
		}
		
		for (int i = 0; i < auxr.length; i++) {
			auxr[i]=aux1[i]-aux2[i];
			mat[r2][i]=auxr[i];
		}
		
	}
	
	public   void norm(float mat[][], int r,int e) {
		float c=mat[r][e];
		for (int i = 0; i < mat.length; i++) {
			mat[r][i]/=c;
		}
		det=det*c;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	
//////////////FUNCION QUE TRIANGULA LA MATRIZ
	public  void triangular(float mat[][]) {
	
			for (int i = 0; i < mat.length; i++) {
			/////se verifica que el pivote no sea cero y si lo es va intercambiando renglones hasta que deje de serlo
			//// en caso de que todos tengan cero el det=0
				for (int k = i+1; k < mat.length && mat[i][i]==0; k++) {
					intercambia(mat, i, k);
					if (flg) imprime(mat);
				}
				if(mat[i][i]==0){
						det=0;
					return;
				}
				
				
				if(mat[i][i]!=1.0 && i<mat.length-1 && mat[i][i]!=0.0){
					norm(mat, i,i);		//vuelve 1 el pivote
					if (flg) imprime(mat);
					}
				//comienza a hacer 0's debajo del pivote
				for (int j = i+1; j < mat.length; j++) {
					if(mat[j][i]!=0){
					sustituye(mat, mat[j][i], i, mat[i][i], j);
					if (flg) imprime(mat);
					}
				}
				
			}

	}	
	
//////////////FUNCION PARA IMPRIMIR LA MATRIZ
	////se deber� prender o apagar la bandera
public   void imprime(float mat[][]) {
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				System.out.print(mat[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
///redondear el determinante para numeros reales
public double fijarNum(double numero, int digitos) {
	double resultado;
	resultado=numero*Math.pow(10, digitos);
	resultado=Math.round(resultado);
	resultado=resultado/Math.pow(10, digitos);
	
	return resultado;
	
}

    private float[][] parseFloat(byte[][] matriz) {
        float [][] aux=new float[matriz.length][matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                aux[i][j]=(float)matriz[i][j];
            }
                            
        }
        return aux;
    }

}
