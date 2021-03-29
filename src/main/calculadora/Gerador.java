package main.calculadora;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Gerador {

	/**
	 * Função para gerar matriz (m,n) com números aleatórios entre 0-9
	 * @param m
	 * @param n
	 * @param label
	 * @throws IOException
	 */
		public static void generateRandomMatrix(int m, int n, String label) throws IOException {
			Random rand = new Random();
			int [][] matriz = new int [m][n];
			int x;
			
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					x = rand.nextInt(9);
					matriz[i][j] = x;
				}
			}
			System.out.println("Matriz " + label + " criada com sucesso!");

			FileManager.writeMatrix(matriz, label);

		}

	/**
	 * Função que cria a matriz resultado da multiplicação da matriz a x b
	 * @param a
	 * @param b
	 * @return
	 */
		public static int [][] generateResultMatrix(int [][] a, int [][] b){
			checkMatrix(a, b);
			int linA,colB;
			linA = a.length; //número de linhas da matriz a
			colB = b[0].length; //número de colunas da matriz b 	
			int [][] result = new int [linA][colB]; //matriz para armazenar resultado
			
			return result;
		}
	/**
	 * Funcção para verificar se duas matrizes a e b são multiplicáveis
	 * @param a
	 * @param b
	 */
	public static void checkMatrix(int a [][], int b [][]) {
		if( a[0].length != b.length) {
			System.out.println("O número de colunas da matriz A deve \nser igual ao número de linhas da \nmatriz B!");
			System.exit(0);
		}
	}

	/**
	 * Função para dividir entre as threads quantas linhas da matriz cada uma calcula
	 * @param number
	 * @param resto
	 * @param processors
	 * @param a
	 * @param b
	 * @param c
	 * @param calculadoras
	 * @param label
	 * @param mutex
	 */
	public static void createCalculator(int number, int resto, int processors, int [][] a, int [][] b, int[][]c,
										List<Calculadora> calculadoras, String label, Semaphore mutex){

		int initialLine, finalLine = 0, nextLine = 0;

		for (int i = 0; i < processors; i++) {
			initialLine = nextLine;
			finalLine = initialLine + (number - 1);
			if (resto > 0) {
				finalLine++;
				resto--;
			}
			nextLine = finalLine + 1;

			System.out.println("Linhas da Thread " + i + ": " + initialLine + "-" + finalLine);
			calculadoras.add(new Calculadora(a, b, c, initialLine, finalLine, label, mutex));
		}
	}

	public static int [][] createMatrix(int lines, int columns){
		return new int [lines][columns];
	}
}
