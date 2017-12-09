package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatrixVectorMultiplication {
	
	static Vector multiply(Matrix matrix, Vector vector) throws InterruptedException, ExecutionException {
		
		long startParallelMultiply = System.nanoTime() / 1000000;
		
		Vector result = new Vector(vector.getDim());
				
		ExecutorService exec = Executors.newCachedThreadPool();
				
		Future<?> future = exec.submit(new MultiplyTask(matrix, vector, result, exec));
		future.get();
				
		exec.shutdown();
		while(!exec.isTerminated()){}
				

		long endParallelMultiply = System.nanoTime() / 1000000;
		long durationParallelMultiply = endParallelMultiply - startParallelMultiply;
		System.out.println("Parallel Matrix Multiply: " + durationParallelMultiply + "ms");
		
		return result;
	}

	static class MultiplyTask implements Runnable {
		ExecutorService exec;
		Matrix matrix;
		Vector vector;
		Vector lhs, rhs;
		Vector result;

		public MultiplyTask(Matrix matrix, Vector vector, Vector result, ExecutorService exec) {
			this.matrix = matrix;
			this.vector = vector;
			this.result = result;
			this.exec = exec;

			lhs = new Vector(vector.getDim());
			rhs = new Vector(vector.getDim());
		}

		public void run() {
			try {
				int n = matrix.getDim();
				if (n == 1) {
					result.set(0, matrix.get(0,0) * vector.get(0));
				} else {
					Matrix[][] subMatrices = matrix.split();
					Vector[] subVectors = vector.split();
					Vector[] subLHS = lhs.split();
					Vector[] subRHS = rhs.split();

					Future<?>[][] future = (Future<?>[][]) new Future[2][2];
					for (int i = 0; i < 2; i++) {
						future[i][0] = exec.submit(new MultiplyTask(subMatrices[i][0], subVectors[0], subLHS[i], exec));
						future[i][1] = exec.submit(new MultiplyTask(subMatrices[i][1], subVectors[1], subRHS[i], exec));

					}
					for (int i = 0; i < 2; i++) {
						for (int j = 0; j < 2; j++) {
							future[i][j].get();
						}
					}

					Future<?> done = exec.submit(new AddTask(lhs, rhs, result, exec));
					done.get();
				}

			} catch (Exception e) {
			}

		}
	}
	static class AddTask implements Runnable {
		ExecutorService exec;
		Vector v1, v2;
		Vector result;

		public AddTask(Vector v1, Vector v2, Vector result, ExecutorService exec) {
			this.v1 = v1;
			this.v2 = v2;
			this.result = result;
			this.exec = exec;
		}


		public void run() {
			try {
				int n = v1.getDim();
				if (n == 1) {
					result.set(0, v1.get(0) + v2.get(0));
				} else {
					Vector[] subv1 = v1.split();
					Vector[] subv2 = v2.split();
					Vector[] subResults = result.split();

					Future<?>[] future = (Future<?>[]) new Future[2];
					for (int i = 0; i < 2; i++) {
						future[i] = exec.submit(new AddTask(subv1[i], subv2[i], subResults[i], exec));
					}
					for (int i = 0; i < 2; i++) {
							future[i].get();
						
					}
				}

			} catch (Exception e) {
			}

		}
	}

}
