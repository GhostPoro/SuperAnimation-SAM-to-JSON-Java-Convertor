package classes;

import java.util.List;
import java.util.Map;

public class SuperAnim {
	
	static SuperAnimDefMgr sInstance = null;
	static int sAnimObjIndex = 0;
	static boolean sShouldStartAnimObjDrawItr = false;
	
	//typedef std::vector<SuperAnimLabel> SuperAnimLabelArray;
	

	
	static void BeginIterateAnimObjDrawInfo() {
		sAnimObjIndex = 0;
		sShouldStartAnimObjDrawItr = true;
	}
	
	static boolean IterateAnimObjDrawInfo(SuperAnimHandler theHandler, SuperAnimObjDrawInfo theOutputObjDrawInfo) {
		if (!sShouldStartAnimObjDrawItr) {
			print("Forgot to call BeginIterateAnimObjDrawInfo?");
			return false;
		}
		
		if (!theHandler.IsValid()) {
			print("The Animation handler is not valid.");
			return false;
		}
		
		SuperAnimMainDef aMainDef = null; // SuperAnimDefMgr.GetInstance().Load_GetSuperAnimMainDef(theHandler.mMainDefKey);
		if (aMainDef == null) {
			print("I can't find the Animation definition.");
			return false;
		}
		
		int aCurFrameNum = ((int) theHandler.mCurFrameNum);
		SuperAnimFrame aCurFrame = aMainDef.mFrames[aCurFrameNum];
		if (sAnimObjIndex >= aCurFrame.mObjectVector.length) {
			// we have iterated all objects in this frame
			sShouldStartAnimObjDrawItr = false;
			return false;
		}
		
		SuperAnimObject aCurObject = aCurFrame.mObjectVector[sAnimObjIndex];
		
		// find the image, fill the sprite id
		SuperAnimImage aSuperAnimImage = aMainDef.mImageVector[aCurObject.mResNum];
		theOutputObjDrawInfo.mSpriteId = aSuperAnimImage.mSpriteId;
		
		// do the interpolateion to next frame for transform & color
		if (aCurFrameNum == aMainDef.mEndFrameNum) {
			// reach the end frame, don't need to do any interpolation
			theOutputObjDrawInfo.mTransform = aCurObject.mTransform;
			theOutputObjDrawInfo.mColor = aCurObject.mColor;
		} else {
			int aNextFrameNum = aCurFrameNum + 1;
			boolean finishedInterp = false;
			SuperAnimFrame aNextFrame = aMainDef.mFrames[aNextFrameNum];
			for (int i = 0; i < aNextFrame.mObjectVector.length; ++i) {
				SuperAnimObject anObj = aNextFrame.mObjectVector[i];
				if (anObj.mObjectNum == aCurObject.mObjectNum && anObj.mResNum == aCurObject.mResNum) {
					float anInterp = theHandler.mCurFrameNum - aCurFrameNum;
					theOutputObjDrawInfo.mTransform = aCurObject.mTransform.InterpolateTo(anObj.mTransform, anInterp);
					theOutputObjDrawInfo.mColor = aCurObject.mColor.InterpolateTo(anObj.mColor, anInterp);
					finishedInterp = true;
					break;
				}
			}
			if (!finishedInterp) {
				// we miss the object in next frame?
				// never mind
				theOutputObjDrawInfo.mTransform = aCurObject.mTransform;
				theOutputObjDrawInfo.mColor = aCurObject.mColor;
			}
		}
		
		theOutputObjDrawInfo.mTransform = theOutputObjDrawInfo.mTransform.TransformSrc(aSuperAnimImage.mTransform);
		Matrix3x3f aMatrix = new Matrix3x3f();
		aMatrix.loadIdentity();
		aMatrix.m02 = aSuperAnimImage.mWidth * 0.5f;
		aMatrix.m12 = aSuperAnimImage.mHeight * 0.5f;
		theOutputObjDrawInfo.mTransform.mMatrix = theOutputObjDrawInfo.mTransform.mMatrix.mul(aMatrix);
		
		sAnimObjIndex++;
		return true;
	}
	
	static void IncAnimFrameNum(SuperAnimHandler theMainDefHandler, float theDeltaTime, boolean hitNewLabel){
		
		if (!theMainDefHandler.IsValid()) {
			return;
		}
		
		int aLastFrameNum = (int)theMainDefHandler.mCurFrameNum;
		theMainDefHandler.mCurFrameNum += theDeltaTime * theMainDefHandler.mAnimRate;
		int aCurFrame = (int)theMainDefHandler.mCurFrameNum;
		
		if (aCurFrame != aLastFrameNum) // Reach new frame
		{
			// Check whether reach a new label frame
			boolean aIsNewLabel = false;
			if (aCurFrame >= theMainDefHandler.mLastFrameNumOfCurLabel) {
				theMainDefHandler.mCurFrameNum = theMainDefHandler.mLastFrameNumOfCurLabel;
				aIsNewLabel = true;
			}
			
			hitNewLabel = aIsNewLabel;
		}
	}
	
//	static boolean HasSection(SuperAnimHandler theHandler, String theLabelName){
//		SuperAnimMainDef aMainDef = SuperAnimDefMgr.GetInstance().Load_GetSuperAnimMainDef(theHandler.mMainDefKey);
//		
//		if (aMainDef == null) {
//			return false;
//		}
//		
//		for (SuperAnimLabel it : aMainDef.mLabels) {
//			if (theLabelName.equals(it.mLabelName))
//				return true;
//		}
//		return false;
//	}
	
	static boolean PlayBySection(SuperAnimHandler theHandler, String theLabelName){	
		SuperAnimMainDef aMainDef =  null; // SuperAnimDefMgr.GetInstance().Load_GetSuperAnimMainDef(theHandler.mMainDefKey);
		if (aMainDef == null) {
			theHandler.mIsHandlerValid = false;
			return false;
		}
		for (SuperAnimLabel it : aMainDef.mLabels) {
			if (theLabelName.equals(it.mLabelName)) {
				theHandler.mCurFrameNum = it.mStartFrameNum;
				theHandler.mCurLabel = theLabelName;
				theHandler.mFirstFrameNumOfCurLabel = it.mStartFrameNum;
				theHandler.mLastFrameNumOfCurLabel = it.mEndFrameNum;
				theHandler.mIsHandlerValid = true;
				return true;
			}
		}
		
		theHandler.mIsHandlerValid = false;
		return false;
	}
	
//	static boolean LoadAnimFile(String theAbsAnimFile){
//		return SuperAnimDefMgr.GetInstance().Load_GetSuperAnimMainDef(theAbsAnimFile) != null;
//	}
//	
//	void UnloadAnimFile(String theAbsAnimFile){
//		SuperAnimDefMgr.GetInstance().UnloadSuperAnimMainDef(theAbsAnimFile);
//	}
//	
//	public void cleanup() {
//		mMainDefCache.clear();
//	}
	
	private static String print(String text) {
		System.out.println(text);
		return text;
	}
}


