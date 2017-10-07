package ca.mcgill.ecse420.a1;

public class Question1 {
	
	public static void main(String[] args) {
		
		double[][] a = generateMatrix(2000, 2000);
		double[][] b = generateMatrix(2000, 2000);
		
		long startSequentialMultiply = System.nanoTime()/1000000;
		double[][] c = sequentialMultiplyMatrix(a, b);
		long endSequentialMultiply = System.nanoTime()/1000000;
         
		long durationSequentialMultiply = endSequentialMultiply - startSequentialMultiply;
		
//		System.out.println("\nPrinting matrix a:");
//		printMatrix(a);

//		System.out.println("\nPrinting matrix b:");
//		printMatrix(b);
		
//		System.out.println("\nPrinting matrix c:");
//		printMatrix(c);
		
		System.out.println("Sequential Matrix Multiply: " + durationSequentialMultiply + "ms");
		
	}
	
	public static double[][] sequentialMultiplyMatrix(double a[][], double b[][]) {
		
		double[][] c = new double[a.length][b[0].length];
		
		/*     A            B           C
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
		for(int i=0; i<a.length; i++) {
			for(int j=0; j<b[0].length; j++) {
				for(int k=0; k<a[0].length; k++) {
					c[i][j] += a[i][k]*b[k][j];
				}	
			}	
		}	
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

}
