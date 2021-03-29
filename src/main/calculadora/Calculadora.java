package main.calculadora;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Calculadora extends Thread {

	private int[][] matrix1;
	private int[][] matrix2;
	private int[][] matrixR;
	private int initialLine;
	private int finalLine;
	private String label;
	private long initialTime = System.currentTimeMillis();

	private Semaphore mutex;

	public Calculadora(int[][] matrix1, int[][] matrix2, int[][] matrixR,
					   int initialLine, int finalLine, String label, Semaphore mutex) {
		super();
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.matrixR = matrixR;
		this.initialLine = initialLine;
		this.finalLine = finalLine;
		this.label = label;
		this.mutex = mutex;
	}

	public void run() {
		long finalTime;
		double fracPart;
		double genSpeed;

		for (int i = initialLine; i < finalLine; i++) {
			multMatrix(matrix1, matrix2, matrixR, i);
		}

		try {
			mutex.acquire();
			finalTime = System.currentTimeMillis();
			Inicio.paralelTime = (finalTime - initialTime);
			initialTime = System.currentTimeMillis();


			FileManager.writeMatrix(matrixR, label);

			finalTime = System.currentTimeMillis();
			Inicio.outputTime = (finalTime - initialTime);

			System.out.println(
							"\nTempo Input: " + Inicio.inputTime / 1000 + "s\n" +
							"Tempo Sequencial: " + Inicio.sequencialTime / 1000 + "s\n" +
							"Tempo Paralelo: " + Inicio.paralelTime / 1000 + "s\n" +
							"Tempo Output: " + Inicio.outputTime / 1000 + "s\n"
			);
			fracPart = sequencialPart(Inicio.inputTime, Inicio.sequencialTime, Inicio.outputTime);
			genSpeed = Inicio.sequencialTime / Inicio.paralelTime;

			System.out.println("SpeedUp: " + genSpeed);
			System.out.println("SpeedUp Geral: " + generalSpeedUp(fracPart, genSpeed));

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void multMatrix(int[][] matrix1, int[][] matrix2, int[][] resMatrix, int line) {
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix2[0].length; j++) {
				resMatrix[line][i] = resMatrix[line][i] + (matrix1[line][j] * matrix2[j][i]);
			}
		}
	}

	/**
	 * Função que multiplica matrizes de forma NÃO paralela
	 * @param matrix1
	 * @param matrix2
	 * @param resMatrix
	 * @throws IOException
	 */
	public static void multMatrixNP(int[][] matrix1, int[][] matrix2, int[][] resMatrix) throws IOException {
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix2[0].length; j++) {
				for (int k = 0; k < matrix1.length; k++) {
					resMatrix[i][j] = resMatrix[i][j] + (matrix1[i][k] * matrix2[k][j]);
				}
			}
		}
		FileManager.writeMatrix(resMatrix, "RT1");
		System.out.println("Matrizes multiplicadas!\n");
	}

	public static double sequencialPart(double in, double sequencial, double out) {
		double r;
		r = (1 - (((in + sequencial + out) / sequencial)));
		if (r < 0)
			return r * (-1);
		else
			return r;
	}

	private static double generalSpeedUp(double fracPart, double speedupPart) {
		return (1 / ((1 - fracPart) + (fracPart / speedupPart)));
	}
}
