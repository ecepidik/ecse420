package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ca.mcgill.ecse420.a3.MatrixVectorMultiplication.MultiplyTask;

public class Question4 {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		Vector v = new Vector(128);
		v.generateVector();
//		System.out.println("Vector:");
//		for(int j = 0; j < v.getDim(); j++) {
//			System.out.println(v.get(j));
//		}
		
		Matrix m = new Matrix(128);
		m.generateMatrix();
//		System.out.println("Matrix:");
//		for(int i = 0; i < m.getDim(); i++) {
//			for(int j = 0; j < m.getDim(); j++) {
//				System.out.print(m.get(i, j) + " ");
//			}
//			System.out.println(" ");
//		}
		
		Vector paralelMV = ca.mcgill.ecse420.a3.MatrixVectorMultiplication.multiply(m, v);
//		
//		System.out.println("Result Parallel:");
//		for(int i = 0; i < paralelMV.getDim(); i++) 
//			System.out.println(paralelMV.get(i));

		Vector sequentialMV = sequentialMultiply(m, v);
//		System.out.println("Result Sequential:");
//		for(int i = 0; i < sequentialMV.getDim(); i++) 
//			System.out.println(sequentialMV.get(i));
	}
	

	public static Vector sequentialMultiply(Matrix matrix, Vector vector) {

		// initializes a matrix n x m where n = row number of a & m = column
		// number of b
		Vector result = new Vector(vector.getDim());

		// starts the timer
		long startSequentialMultiply = System.nanoTime() / 1000000;

		// Iterative process to calculate the sequential matrix multiplication
		for (int i = 0; i < vector.getDim(); i++) {
			int temp = 0;
			for (int k = 0; k < matrix.getDim(); k++) {
				temp += matrix.get(i,k) * vector.get(k);
			}
			result.set(i, temp);
		}

		// stops the timer
		long endSequentialMultiply = System.nanoTime() / 1000000;
		long durationSequentialMultiply = endSequentialMultiply - startSequentialMultiply;
		System.out.println("Sequential Matrix Multiply: " + durationSequentialMultiply + "ms");

		return result;
	}

	public static double[][] generateMatrix(int rowSize, int columnSize) {
		double[][] matrix = new double[rowSize][columnSize];

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < columnSize; j++) {
				matrix[i][j] = (int) (Math.random() * 10);
			}
		}
		return matrix;
	}

}