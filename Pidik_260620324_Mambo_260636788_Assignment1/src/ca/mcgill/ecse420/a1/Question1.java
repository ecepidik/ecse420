package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;

public class Question1 {
	
	// Change the thread amount by changing the value
	public static int THREAD_AMOUNT = 16;
	
	public static int ROW_AMOUNT_PER_THREAD;

	public static void main(String[] args) {

		// Change the sizes of the matrices that are being multiplied
		double[][] a = generateMatrix(500, 500);
		double[][] b = generateMatrix(500, 500);

		double[][] c = sequentialMultiplyMatrix(a, b);
		double[][] d = parallelMultiplyMatrix(a, b);
		
	}

	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {

		// initializes a matrix n x m where n = row number of a & m = column number of b
		double[][] c = new double[a.length][b[0].length];
		
		// starts the timer
		long startSequentialMultiply = System.nanoTime()/1000000;
		
		// Iterative process to calculate the sequential matrix multiplication
		for(int i=0; i < c.length; i++) {
			for(int j=0; j < b[0].length; j++) {
				for(int k=0; k < a[0].length; k++) {
					c[i][j] += a[i][k]*b[k][j];
				}	
			}	
		}	
		
		// stops the timer
		long endSequentialMultiply = System.nanoTime()/1000000;
		long durationSequentialMultiply = endSequentialMultiply - startSequentialMultiply;
		System.out.println("Sequential Matrix Multiply: " + durationSequentialMultiply + "ms");

		return c;
	}

	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {
		double[][] c = new double[a.length][b[0].length];
		
		// Divides the rows for the resulting matrix c by the amount of threads used 
		// This is the ROW_AMOUNT_PER_THREAD number 
		// Each thread deals with ROW_AMOUNT_PER_THREAD many rows instead 
		ROW_AMOUNT_PER_THREAD = (int) Math.ceil((double) a.length/THREAD_AMOUNT);
		
		//starts the timer
		long startParallelMultiply = System.nanoTime()/1000000;
		
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_AMOUNT);

		// creates and runs the threads in the executor service
		for (int rowCIndex = 0; rowCIndex < c.length;) {
			executor.execute(new ParallelMultiplyMatrix(a, b, c, rowCIndex));
			rowCIndex += ROW_AMOUNT_PER_THREAD;
		}
		
		executor.shutdown();
		
		// required for timing purposes
		while(!executor.isTerminated()) {}
		
		// stops the timer
		long endParallelMultiply = System.nanoTime()/1000000;
		long durationParallelMultiply = endParallelMultiply - startParallelMultiply;
		System.out.println("Parallel Matrix Multiply using " + THREAD_AMOUNT + " threads: "  + durationParallelMultiply + "ms");
		
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
	
	public static class ParallelMultiplyMatrix implements Runnable {

		private double[][] a;
		private double[][] b;
		private double[][] c;

		// rowCIndex is the index of the first row in the result matrix c that is computed by the thread
		// e.g. Computing a 100 X 100 matrix with 4 threads:
		// ROW_AMOUNT_PER_THREAD = 25
		// thread 1 computes rows 0-24 (rowCIndex = 0), thread 2 computes rows 25-49 (rowCIndex = 25), so on
		private int rowCIndex;

		ParallelMultiplyMatrix(double[][] a, double[][] b, double[][] c, int rowCIndex) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.rowCIndex = rowCIndex;
		}

		@Override
		public void run() {
			// Same logic as sequential number
			// Only difference is the first for loop is a lot smaller
			for (int k = rowCIndex; k < (rowCIndex + ROW_AMOUNT_PER_THREAD) && k < c.length; k++) {
				for (int i = 0; i < b[0].length; i++) {
					for (int j = 0; j < a[0].length; j++) {
						c[k][i] += a[k][j] * b[j][i];
					}
				}
			}
		}
	}
}
