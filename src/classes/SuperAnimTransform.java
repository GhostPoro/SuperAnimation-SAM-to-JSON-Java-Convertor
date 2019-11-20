package classes;

public class SuperAnimTransform {
	
	public Matrix3x3f mMatrix;
	
	public SuperAnimTransform() {
		this.mMatrix = new Matrix3x3f();
		this.mMatrix.loadIdentity();
	}
	
	public SuperAnimTransform scale(float sx, float sy) {
		mMatrix.m00 *= sx;
		mMatrix.m01 *= sx;
		mMatrix.m02 *= sx;
		mMatrix.m10 *= sy;
		mMatrix.m11 *= sy;
		mMatrix.m12 *= sy;
		return this;
	}
	
	public SuperAnimTransform TransformSrc(SuperAnimTransform theSrcTransform) {	
		SuperAnimTransform aNewTransform = new SuperAnimTransform();	
		
		aNewTransform.mMatrix.m00 = mMatrix.m00 * theSrcTransform.mMatrix.m00 + mMatrix.m01 * theSrcTransform.mMatrix.m10;
		aNewTransform.mMatrix.m01 = mMatrix.m00 * theSrcTransform.mMatrix.m01 + mMatrix.m01 * theSrcTransform.mMatrix.m11;
		aNewTransform.mMatrix.m10 = mMatrix.m10 * theSrcTransform.mMatrix.m00 + mMatrix.m11 * theSrcTransform.mMatrix.m10;
		aNewTransform.mMatrix.m11 = mMatrix.m10 * theSrcTransform.mMatrix.m01 + mMatrix.m11 * theSrcTransform.mMatrix.m11;
		aNewTransform.mMatrix.m02 = mMatrix.m02 + mMatrix.m00 * theSrcTransform.mMatrix.m02 + mMatrix.m01*theSrcTransform.mMatrix.m12;
		aNewTransform.mMatrix.m12 = mMatrix.m12 + mMatrix.m10 * theSrcTransform.mMatrix.m02 + mMatrix.m11*theSrcTransform.mMatrix.m12;
		
		return aNewTransform;
	}
	
	public SuperAnimTransform InterpolateTo(SuperAnimTransform theNextTransform, float thePct) {
		SuperAnimTransform aNewTransform = new SuperAnimTransform();	
		
		aNewTransform.mMatrix.m00 = (mMatrix.m00 * (1.0f - thePct)) + (theNextTransform.mMatrix.m00 * thePct);
		aNewTransform.mMatrix.m01 = (mMatrix.m01 * (1.0f - thePct)) + (theNextTransform.mMatrix.m01 * thePct);
		aNewTransform.mMatrix.m10 = (mMatrix.m10 * (1.0f - thePct)) + (theNextTransform.mMatrix.m10 * thePct);
		aNewTransform.mMatrix.m11 = (mMatrix.m11 * (1.0f - thePct)) + (theNextTransform.mMatrix.m11 * thePct);
		
		aNewTransform.mMatrix.m02 = (mMatrix.m02 * (1.0f - thePct)) + (theNextTransform.mMatrix.m02 * thePct);
		aNewTransform.mMatrix.m12 = (mMatrix.m12 * (1.0f - thePct)) + (theNextTransform.mMatrix.m12 * thePct);
		
		return aNewTransform;
	}

}
