package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;

public class Question1 {
	
	public static int THREAD_AMOUNT = 1;
	public static int ROW_AMOUNT_PER_THREAD;

	public static void main(String[] args) {

		double[][] a = generateMatrix(2000, 2000);
		double[][] b = generateMatrix(2000, 2000);

	//	double[][] c = sequentialMultiplyMatrix(a, b);
	//	double[][] d = parallelMultiplyMatrix(a, b);
		
	}

	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {

		double[][] c = new double[a.length][b[0].length];

		/* 
		 *     A            B           C
		 * | 1 2 3 |     | 5 6 |    | 11 24 |
		 * | 4 5 6 |  x  | 1 3 | =  | 37 63 |
		 * | 7 8 9 |     | 2 4 |    | 61 102|
		 * 
		 * when i = 0;
		 * 	c[0][0] = a[0][0]*b[0][0] + a[0][1]*b[1][0] + a[0][2]*b[2][0]
		 * 	c[0][1] = a[0][0]*b[0][1] + a[0][1]*b[1][1] + a[0][2]*b[2][1]
		 * 
		 * when i = 1;
		 * 	c[1][0] = a[1][0]*b[0][0] + a[1][1]*b[1][0] + a[1][2]*b[2][0]
		 * 	c[1][1] = a[1][0]*b[0][1] + a[1][1]*b[1][1] + a[1][2]*b[2][1]
		 *
		 * when i = 2;
		 * 	c[2][0] = a[2][0]*b[0][0] + a[2][1]*b[1][0] + a[2][2]*b[2][0]
		 * 	c[2][1] = a[2][0]*b[0][1] + a[2][1]*b[1][1] + a[2][2]*b[2][1]
		 *
		 */
		
		long startSequentialMultiply = System.nanoTime()/1000000;
		for(int i=0; i < a.length; i++) {
			for(int j=0; j < b[0].length; j++) {
				for(int k=0; k < a[0].length; k++) {
					c[i][j] += a[i][k]*b[k][j];
				}	
			}	
		}	
		
		long endSequentialMultiply = System.nanoTime()/1000000;
		long durationSequentialMultiply = endSequentialMultiply - startSequentialMultiply;
		System.out.println("Sequential Matrix Multiply: " + durationSequentialMultiply + "ms");

		return c;
	}

	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
		double[][] c = new double[a.length][b[0].length];
		
		ROW_AMOUNT_PER_THREAD = a.length/THREAD_AMOUNT;
		
		long startParallelMultiply = System.nanoTime()/1000000;
		
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_AMOUNT);

		for (int rowCIndex = 0; rowCIndex < a.length;) {
			executor.execute(new ParallelMultiplyMatrix(a, b, c, rowCIndex));
			rowCIndex += ROW_AMOUNT_PER_THREAD;
		}
		executor.shutdown();
		
		while(!executor.isTerminated()) {}
		
		long endParallelMultiply = System.nanoTime()/1000000;
		long durationParallelMultiply = endParallelMultiply - startParallelMultiply;
		System.out.println("Parallel Matrix Multiply: " + durationParallelMultiply + "ms");
		
		return c;
	}

	public static double[][] generateMatrix(int rowSize, int columnSize) {
		double[][] matrix = new double[rowSize][columnSize];

		for (int i=0; i<rowSize; i++) {
			for (int j=0; j<columnSize; j++) {
				matrix[i][j] = (int) (Math.random()*10);
			}           
		}
		return matrix;
	}

	public static void printMatrix(double[][] matrix) {
		for (int i=0; i<matrix.length; i++) {
			for (int j=0; j<matrix[0].length; j++) {
				System.out.print(matrix[i][j] + "  ");
			}
			System.out.println();
		}
	}

	public static boolean compareMatrices(double[][] a, double[][] b) {
		if (Arrays.deepEquals(a, b)){
			return true;
		}
		return false;
	}
	
	public static class ParallelMultiplyMatrix implements Runnable {

		private double[][] a;
		private double[][] b;
		private double[][] c;

		private int rowCIndex;

		ParallelMultiplyMatrix(double[][] a, double[][] b, double[][] c, int rowCIndex) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.rowCIndex = rowCIndex;
		}

		@Override
		public void run() {
			for (int k = rowCIndex; k < (rowCIndex + ROW_AMOUNT_PER_THREAD) && k < a.length; k++) {
				for (int i = 0; i < b[0].length; i++) {
					for (int j = 0; j < a[0].length; j++) {
						c[k][i] += a[k][j] * b[j][i];
					}
				}
			}
		}
	}
}
