package nova.core.util.transform.matrix;

import nova.core.util.transform.vector.Transformer;
import nova.core.util.transform.vector.Vector3;
import nova.core.util.transform.vector.Vector3d;

import java.util.Arrays;

/**
 *  Matrix4x4 for 3D Vector transforms. It is immutable.
 */
public class Matrix4x4 extends Matrix implements Cloneable, Transformer {

	/**
	 * Identity matrix.
	 */
	public static Matrix4x4 IDENTITY = new Matrix4x4(new double[][] {
		{ 1, 0, 0, 0 },
		{ 0, 1, 0, 0 },
		{ 0, 0, 1, 0 },
		{ 0, 0, 0, 1 } });
	/**
	 * 4x4 array [row][column]
	 */

	/**
	 * Creates Matrix4x4 form 2D double array creating defensive copy of it.
	 * @param mat 4x4 array to create Matrix4x4 form.
	 */
	public Matrix4x4(double[][] mat) {
		super(mat);
		assert mat.length == 4;
		for (int i = 0; i < mat.length; i++) {
			assert mat[i].length == 4;
		}
	}

	/**
	 * Copy constructor
	 * @param old matrix to copy.
	 */
	public Matrix4x4(Matrix4x4 old) {
		this(old.mat);
	}

	/**
	 * Use Matrix4x4.IDENTITY instead.
	 */
	@Deprecated()
	public Matrix4x4() {
		this(Matrix4x4.IDENTITY);
	}

	/**
	 *  Cross multiples matrices.
	 * @param other matrix to multiply by.
	 * @return this x other
	 */
	public Matrix4x4 multiply(Matrix4x4 other) {
		double[][] res = new double[4][4];

		//Let JIT and loop unrolling do their work.
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j <4; j++) {
				for (int k = 0; k < 4; k++) {
					res[i][j] += this.mat[i][k] * other.mat[k][j];
				}
			}
		}
		return new Matrix4x4(res);
	}

	/**
	 *  Reverse cross multiples matrices..
	 * @param other matrix to be multiplied by.
	 * @return other x this
	 */
	public Matrix4x4 rightMultiply(Matrix4x4 other) {
		return other.multiply(this);
	}

	/**
	 * Transposes matrix.
	 * @return transposed matrix
	 */
	public Matrix4x4 transpose() {
		double[][] res = new double[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				res[j][i] = mat[i][j];
			}
		}
		return new Matrix4x4(res);
	}

	/**
	 * Transform vector by this matrix.
	 * @param vector to be transformed.
	 * @return transformed vector.
	 */
	@Override
	public Vector3d transform(Vector3<?> vector) {
		double x,y,z,w;
		x = mat[0][0]*vector.xd() + mat[1][0]*vector.yd() + mat[2][0]*vector.zd() + mat[3][0];
		y = mat[0][1]*vector.xd() + mat[1][1]*vector.yd() + mat[2][1]*vector.zd() + mat[3][1];
		z = mat[0][2]*vector.xd() + mat[1][2]*vector.yd() + mat[2][2]*vector.zd() + mat[3][2];
		w = mat[0][3]*vector.xd() + mat[1][3]*vector.yd() + mat[2][3]*vector.zd() + mat[3][3];
		return new Vector3d(x / w, y / w, z / w);
	}
	
	@Override
	public Matrix4x4 clone() {
		return new Matrix4x4(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName()).append('\n');
		sb.append(Arrays.toString(mat[0])).append('\n');
		sb.append(Arrays.toString(mat[1])).append('\n');
		sb.append(Arrays.toString(mat[2])).append('\n');
		sb.append(Arrays.toString(mat[3])).append('\n');

		return sb.toString();
	}
}
