package classes;

public class Matrix3x3f {

	public float m00 = 0;
	public float m01 = 0;
	public float m02 = 0;
	
	public float m10 = 0;
	public float m11 = 0;
	public float m12 = 0;
	
	public float m20 = 0;
	public float m21 = 0;
	public float m22 = 0;
	
	public Matrix3x3f() {
		
	}
	
	public Matrix3x3f loadIdentity() {
		m01	= m02 = m10 = m12 = m20 = m21 = 0;
		m00 = m11 = m22 = 1;
		return this;
	}
	
	public Matrix3x3f mul(Matrix3x3f theMat) {
		Matrix3x3f aResult = new Matrix3x3f();
		aResult.m00 = m00 * theMat.m00 + m01 * theMat.m10 + m02 * theMat.m20;
		aResult.m01 = m00 * theMat.m01 + m01 * theMat.m11 + m02 * theMat.m21;
		aResult.m02 = m00 * theMat.m02 + m01 * theMat.m12 + m02 * theMat.m22;
		aResult.m10 = m10 * theMat.m00 + m11 * theMat.m10 + m12 * theMat.m20;
		aResult.m11 = m10 * theMat.m01 + m11 * theMat.m11 + m12 * theMat.m21;
		aResult.m12 = m10 * theMat.m02 + m11 * theMat.m12 + m12 * theMat.m22;
		aResult.m20 = m20 * theMat.m00 + m21 * theMat.m10 + m22 * theMat.m20;
		aResult.m21 = m20 * theMat.m01 + m21 * theMat.m11 + m22 * theMat.m21;
		aResult.m22 = m20 * theMat.m02 + m21 * theMat.m12 + m22 * theMat.m22;
		return aResult;
	}
	
}
