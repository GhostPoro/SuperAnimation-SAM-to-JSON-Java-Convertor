package classes;

public class SuperAnimHandler {
	
	public String mMainDefKey;
	public String mCurLabel;
	public int mFirstFrameNumOfCurLabel;
	public int mLastFrameNumOfCurLabel;
	public float mCurFrameNum;
	public float mAnimRate;
	public float mWidth;
	public float mHeight;
	public boolean mIsHandlerValid;

	public SuperAnimHandler() {
		mFirstFrameNumOfCurLabel = 0;
		mLastFrameNumOfCurLabel = 0;
		mCurFrameNum = 0.0f;
		mAnimRate = 0.0f;
		mWidth = 0.0f;
		mHeight = 0.0f;
		mIsHandlerValid = false;
	}
		
	public boolean IsValid() {
		return mIsHandlerValid;
	}
	
	// implement extern functions MAYBE NOT STATIC
	public static SuperAnimHandler GetSuperAnimHandler(String theAbsAnimFile){
		SuperAnimHandler aSuperAnimHander = new SuperAnimHandler(); // <--OOOOOOOOOOOOO
		SuperAnimMainDef  aMainDef = SuperAnimDefMgr.GetInstance().Load_GetSuperAnimMainDef(theAbsAnimFile);
		if (aMainDef != null) {
			aSuperAnimHander.mMainDefKey = theAbsAnimFile; // right now just use the animation file name as the key
			aSuperAnimHander.mAnimRate = aMainDef.mAnimRate;
			aSuperAnimHander.mWidth = aMainDef.mWidth;
			aSuperAnimHander.mHeight = aMainDef.mHeight;
			aSuperAnimHander.mCurFrameNum = aMainDef.mStartFrameNum;
			aSuperAnimHander.mIsHandlerValid = true;
		} else {
			aSuperAnimHander.mIsHandlerValid = false;
			System.out.println("SuperAnimHandler.GetSuperAnimHandler -> aMainDef is NULL");
		}
		
		return aSuperAnimHander;
	}
}
