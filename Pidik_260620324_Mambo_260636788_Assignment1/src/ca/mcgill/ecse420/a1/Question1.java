package ca.mcgill.ecse420.a1;

public class Question1 {
	
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

}
