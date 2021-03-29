package main.calculadora;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Caio K. Brasil
 * Multiplicador de Matrizes com Paralelismo
 *
 */

public class Inicio {

	static double inputTime = 0;
	static double outputTime = 0;
	static double sequencialTime = 0;
	static double paralelTime = 0;

	public static void main(String[] args) throws IOException {

		long t_Inicial, t_Final;
		int counterLines;

		int processors = Runtime.getRuntime().availableProcessors();
		System.out.println("Threads being used: " + processors + "\n");

		Semaphore mutex = new Semaphore(1);

		Scanner sc = new Scanner(System.in);

		List<Calculadora> calculadoras = new ArrayList<>();
		int[][] result;
		int[][] a0 , b0, a1, b1, a2, b2, a3, b3;
		int menu = 0, number, resto, resp;

		a0 = Gerador.createMatrix(1000,1000);
		a1 = Gerador.createMatrix(2000,2000);
		a2 = Gerador.createMatrix(1000,2000);
		a3 = Gerador.createMatrix(2000,4000);

		b0 = Gerador.createMatrix(1000,1000);
		b1 = Gerador.createMatrix(2000,2000);
		b2 = Gerador.createMatrix(2000,1000);
		b3 = Gerador.createMatrix(4000,2000);

		System.out.println("Gerar novas matrizes?");
		System.out.println("0. Sim\n1. Não");
		resp = sc.nextInt();
			if (resp == 0) {
				generateNewMatrix();
			}
			else{
				t_Inicial = System.currentTimeMillis();
				FileManager.readMatrix(a0, "A0");
				FileManager.readMatrix(b0, "B0");
				FileManager.readMatrix(a1, "A1");
				FileManager.readMatrix(b1, "B1");
				FileManager.readMatrix(a2, "A2");
				FileManager.readMatrix(b2, "B2");
				FileManager.readMatrix(a3, "A3");
				FileManager.readMatrix(b3, "B3");
				t_Final = System.currentTimeMillis();
				inputTime = (t_Final-t_Inicial);
			}

		while(menu == 0) {
			System.out.println("Escolha as matrizes a serem multiplicadas: ");
			System.out.println("" +
					"1: A0(1000x1000) X B0(1000x1000)\n" +
					"2: A1(2000x2000) X B1(2000x2000)\n" +
					"3: A2(1000x2000) X B2(2000x1000)\n" +
					"4: A3(2000x4000) X B3(4000x2000)\n" +
					"5: Exit");
			menu = sc.nextInt();

			switch (menu) {
				case 1:
					result = Gerador.generateResultMatrix(a0, b0);
					t_Inicial = System.currentTimeMillis();
					Calculadora.multMatrixNP(a0,b0, result);
					t_Final = System.currentTimeMillis();
					sequencialTime = (t_Final-t_Inicial);
					counterLines = a0.length;
					number = counterLines / processors;
					resto = counterLines % processors;
					Gerador.createCalculator(number, resto, processors, a0, b0, result, calculadoras, "C0", mutex);
					break;
				case 2:
					result = Gerador.generateResultMatrix(a1, b1);
					t_Inicial = System.currentTimeMillis();
					Calculadora.multMatrixNP(a1,b1, result);
					t_Final = System.currentTimeMillis();
					sequencialTime = (t_Final-t_Inicial);
					counterLines = a1.length;
					number = counterLines / processors;
					resto = counterLines % processors;
					Gerador.createCalculator(number, resto, processors, a1, b1, result, calculadoras, "C1", mutex);
					break;

				case 3:
					result = Gerador.generateResultMatrix(a2, b2);
					t_Inicial = System.currentTimeMillis();
					Calculadora.multMatrixNP(a2,b2, result);
					t_Final = System.currentTimeMillis();
					sequencialTime = (t_Final-t_Inicial);
					counterLines = a2.length;
					number = counterLines / processors;
					resto = counterLines % processors;
					Gerador.createCalculator(number, resto, processors, a2, b2, result, calculadoras, "C2", mutex);
					break;

				case 4:
					result = Gerador.generateResultMatrix(a3, b3);
					t_Inicial = System.currentTimeMillis();
					Calculadora.multMatrixNP(a3,b3, result);
					t_Final = System.currentTimeMillis();
					sequencialTime = (t_Final-t_Inicial);
					counterLines = a3.length;
					number = counterLines / processors;
					resto = counterLines % processors;
					Gerador.createCalculator(number, resto, processors, a3, b3, result, calculadoras, "C3", mutex);
					break;

				case 5:
					System.exit(0);
			}
		}
	sc.close();

		for (Calculadora c : calculadoras)
			c.start();
	}

	/**
	 * Chamada da função para criar as matrizes
	 * @throws IOException
	 */
	private static void generateNewMatrix() throws IOException {
		Gerador.generateRandomMatrix(1000, 1000, "A0");
		Gerador.generateRandomMatrix(2000, 2000, "A1");
		Gerador.generateRandomMatrix(1000, 2000, "A2");
		Gerador.generateRandomMatrix(2000, 4000, "A3");

		Gerador.generateRandomMatrix(1000, 1000, "B0");
		Gerador.generateRandomMatrix(2000, 2000, "B1");
		Gerador.generateRandomMatrix(2000, 1000, "B2");
		Gerador.generateRandomMatrix(4000, 2000, "B3");
	}
}