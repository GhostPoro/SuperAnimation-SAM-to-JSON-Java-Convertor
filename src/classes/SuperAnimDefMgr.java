package classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperAnimDefMgr {
	
	private final int SAM_VERSION = 1;
	private final float TWIPS_PER_PIXEL = 20.0f;
	private final float LONG_TO_FLOAT = 65536.0f;

	private final byte FRAMEFLAGS_REMOVES    = 0x01;
	private final byte FRAMEFLAGS_ADDS       = 0x02;
	private final byte FRAMEFLAGS_MOVES      = 0x04;
	private final byte FRAMEFLAGS_FRAME_NAME = 0x08;

	private final int MOVEFLAGS_ROTATE     = 0x4000;
	private final int MOVEFLAGS_COLOR      = 0x2000;
	private final int MOVEFLAGS_MATRIX     = 0x1000;
	private final int MOVEFLAGS_LONGCOORDS = 0x0800;
	
	
	//typedef std::map<std::string, SuperAnimMainDef> SuperAnimMainDefMap;
	// SuperAnimMainDefMap mMainDefCache;
	private Map<String,SuperAnimMainDef> mMainDefCache;

	//SuperAnimLabelArray aSuperAnimLabelArray;
	private List<SuperAnimLabel> aSuperAnimLabelArray;
	
	//typedef std::map<int, SuperAnimObject> IntToSuperAnimObjectMap;
	//private Map<Integer, SuperAnimObject> aCurObjectMap;
	private Map<Integer, SuperAnimObject> aCurObjectMap;
	
	public SuperAnimDefMgr() {
		this.aSuperAnimLabelArray = new ArrayList<SuperAnimLabel>();
		this.mMainDefCache = new HashMap<String,SuperAnimMainDef>();
	}
	
	// std::string theSuperAnimFile include the absolute path
	public boolean LoadSuperAnimMainDef(String theSuperAnimFile) {
		String aFullPath = theSuperAnimFile;
		
		String aCurDir = "";
//		int aLastSlash = max((int) theSuperAnimFile.rfind('\\'), (int) theSuperAnimFile.rfind('/'));
//		if (aLastSlash != std::string::npos){
//			aCurDir = theSuperAnimFile.substr(0, aLastSlash);
//		}
//
//		unsigned long aFileSize = 0;
//		unsigned char *aFileBuffer = GetFileData(aFullPath.c_str(), "rb", &aFileSize);
//		if (aFileBuffer == NULL)
//		{
//			assert(false && "Cannot allocate memory.");
//			return false;
//		}
		
		
		// ...(file is initialised)...
		byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(new File(theSuperAnimFile).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
//		BufferReader aBuffer;
//		aBuffer.SetData(aFileBuffer, aFileSize);
//		// free memory
//		delete[] aFileBuffer;
		
		BufferReader aBuffer = new BufferReader();
		aBuffer.SetData(fileContent, fileContent.length);
		
		if (aBuffer.ReadLong() != 0x2E53414D)
		{
			//print("Bad file format.");
			//return false;
		}
		
		int aVersion = aBuffer.readInt();
		//print("aVersion: " + aVersion);
		if (aVersion != SAM_VERSION)
		{
			//print("Wrong version.");
			return false;
		}
		
		SuperAnimMainDef aMainDef = new SuperAnimMainDef();// mMainDefCache.get(theSuperAnimFile);
		mMainDefCache.put(theSuperAnimFile, aMainDef);
		aMainDef.mAnimRate = aBuffer.ReadByte();
		
		float l1 = aBuffer.readInt();
		float l2 = aBuffer.readInt();
		float l3 = aBuffer.readInt();
		float l4 = aBuffer.readInt();
		
		//print("1:" + l1 + " 2:" + l2 + " 3:" + l3 + " 4:" + l4);
		
		aMainDef.mX =      (int) (l1 / TWIPS_PER_PIXEL);
		aMainDef.mY =      (int) (l2 / TWIPS_PER_PIXEL);
		aMainDef.mWidth =  (int) (l3 / TWIPS_PER_PIXEL);
		aMainDef.mHeight = (int) (l4 / TWIPS_PER_PIXEL);
		
		//print("Frames: " + aMainDef.mAnimRate + " Move: " + aMainDef.mX + "x" + aMainDef.mX + " Size: " + aMainDef.mWidth + "x" + aMainDef.mHeight);
		//SuperAnimLabelArray aSuperAnimLabelArray;
		
		int aNumImages = aBuffer.ReadShort();
		
		//print("Num Images: " + aNumImages);
		//aMainDef.mImageVector.resize(aNumImages);
		//aMainDef.mImageVector = new SuperAnimImage[aNumImages];
		
		aMainDef.mImageVector = new SuperAnimImage[aNumImages];
		
		for (int anImageNum = 0; anImageNum < aNumImages; ++anImageNum)
		{
			SuperAnimImage aSuperAnimImage = new SuperAnimImage();
			aSuperAnimImage.mImageName = aBuffer.ReadString();
			aSuperAnimImage.mWidth = aBuffer.ReadShort();
			aSuperAnimImage.mHeight = aBuffer.ReadShort();
			
			float a1 =  aBuffer.ReadLong()  / (LONG_TO_FLOAT * TWIPS_PER_PIXEL);
			float a2 = -aBuffer.ReadLong()  / (LONG_TO_FLOAT * TWIPS_PER_PIXEL);
			float a3 = -aBuffer.ReadLong()  / (LONG_TO_FLOAT * TWIPS_PER_PIXEL);
			float a4 =  aBuffer.ReadLong()  / (LONG_TO_FLOAT * TWIPS_PER_PIXEL);
			float a5 =  aBuffer.ReadShort() / TWIPS_PER_PIXEL;
			float a6 =  aBuffer.ReadShort() / TWIPS_PER_PIXEL;
			
			//print("AnimImage Name: " + aSuperAnimImage.mImageName + " " + aSuperAnimImage.mWidth + " " + aSuperAnimImage.mHeight);
			//print("Matrix Data: " + a1 + " " + a2 + " " + a3 + " " + a4 + " " + a5 + " " + a6);
			
			aSuperAnimImage.mTransform.mMatrix.m00 = a1;
			aSuperAnimImage.mTransform.mMatrix.m01 = a2;
			aSuperAnimImage.mTransform.mMatrix.m10 = a3;
			aSuperAnimImage.mTransform.mMatrix.m11 = a4;
			aSuperAnimImage.mTransform.mMatrix.m02 = a5;
			aSuperAnimImage.mTransform.mMatrix.m12 = a6;
			
			
			String aImagePath;
			if (aCurDir.isEmpty()) {
				aImagePath = aSuperAnimImage.mImageName;
			} else {
				aImagePath = aCurDir + '/' + aSuperAnimImage.mImageName;
			}
			
			//aSuperAnimImage.mSpriteId = LoadSuperAnimSprite(aImagePath);
			aSuperAnimImage.mSpriteId = null;
			aMainDef.mImageVector[anImageNum] = aSuperAnimImage;
		}
		
		int aNumFrames = aBuffer.ReadShort();
		
		
		if(aNumFrames > 0) {
			//print("Num Frames: " + aNumFrames);
		}
		else {
			//print("We don't have valid frames.");
			return false;
		}
		
		aMainDef.mFrames = new SuperAnimFrame[aNumFrames];
		aMainDef.mStartFrameNum = 0;
		aMainDef.mEndFrameNum = aNumFrames - 1;
		//aMainDef.mFrames.resize(aNumFrames);
		

		
		for (int aFrameNum = 0; aFrameNum < aNumFrames; ++aFrameNum)
		{
			SuperAnimFrame aFrame = new SuperAnimFrame();
			aMainDef.mFrames[aFrameNum] = aFrame;
			byte aFrameFlags = aBuffer.ReadByte();
			
			//if (aFrameFlags == FRAMEFLAGS_REMOVES)
			if ((aFrameFlags & FRAMEFLAGS_REMOVES) != 0)
			{
				int aNumRemoves = aBuffer.ReadByte();
				for (int aRemoveNum = 0; aRemoveNum < aNumRemoves; ++ aRemoveNum)
				{
					int anObjectId = aBuffer.ReadShort();
					//print("FRAMEFLAGS_REMOVES and anObjectId: " + anObjectId);
					getSuperAnimObject(anObjectId);
					aCurObjectMap.remove(anObjectId);
//					IntToSuperAnimObjectMap::iterator anIt = aCurObjectMap.get(anObjectId);
//					if (anIt != aCurObjectMap.end())
//					{
//						aCurObjectMap.erase(anIt);
//					}
					
					
				}
			}
			
			//if (aFrameFlags == FRAMEFLAGS_ADDS)
			if ((aFrameFlags & FRAMEFLAGS_ADDS) != 0)
			{
				int aNumAdds = aBuffer.ReadByte();
				for(int anAddNum = 0; anAddNum < aNumAdds; ++anAddNum)
				{
					int anObjNum = (aBuffer.ReadShort() & 0x07FF);
					//print("FRAMEFLAGS_ADDS - aNumAdds:" + aNumAdds + " - anObjNum:" + anObjNum);
					SuperAnimObject aSuperAnimObject = getSuperAnimObject(anObjNum);
					print("Key: " + anObjNum + " Object: " + aSuperAnimObject.mObjectNum + " New: " + aSuperAnimObject.isNew());
					aSuperAnimObject.mObjectNum = anObjNum;
					aSuperAnimObject.mResNum = aBuffer.ReadByte();
					aSuperAnimObject.mColor = new Color(255, 255, 255, 255);
				}
			}
			
			//if (aFrameFlags == FRAMEFLAGS_MOVES) {
			if ((aFrameFlags & FRAMEFLAGS_MOVES) != 0) {
				////print("FRAMEFLAGS_MOVES");
				int aNumMoves = aBuffer.ReadByte();
				//print("FRAMEFLAGS_MOVES aNumMoves: " + aNumMoves);
				for (int aMoveNum = 0; aMoveNum < aNumMoves; ++ aMoveNum)
				{
					short aFlagsAndObjectNum = aBuffer.ReadShort();
					int anObjectNum = aFlagsAndObjectNum & 0x03FF;
					//print("FRAMEFLAGS_MOVES anObjectNum: " + anObjectNum);
//					IntToSuperAnimObjectMap::iterator anIt = aCurObjectMap.find(anObjectNum);
//					if (anIt == aCurObjectMap.end())
//						continue;
					//SuperAnimObject aSuperAnimObject = anIt.second;
					////print("FRAMEFLAGS_MOVES anObjectNum: " + anObjectNum);
					SuperAnimObject aSuperAnimObject = getSuperAnimObject(anObjectNum);
					print("Key: " + anObjectNum + " Object: " + aSuperAnimObject.mObjectNum + " New: " + aSuperAnimObject.isNew());
					aSuperAnimObject.mTransform = new SuperAnimTransform();
					aSuperAnimObject.mTransform.mMatrix.loadIdentity();
					
					if ((aFlagsAndObjectNum & 0x1000) != 0) {
						////print("MOVEFLAGS_MATRIX");
						
						float move1 = ((float)  aBuffer.readInt()) / LONG_TO_FLOAT;
						float move2 = ((float) -aBuffer.readInt()) / LONG_TO_FLOAT;
						float move3 = ((float) -aBuffer.readInt()) / LONG_TO_FLOAT;
						float move4 = ((float)  aBuffer.readInt()) / LONG_TO_FLOAT;
						
						//print("Object: " + anObjectNum + " Matrix Move: " + move1 + " "  + move2 + " "  + move3 + " " + move4);
						
						aSuperAnimObject.mTransform.mMatrix.m00 = move1;
						aSuperAnimObject.mTransform.mMatrix.m01 = move2;
						aSuperAnimObject.mTransform.mMatrix.m10 = move3;
						aSuperAnimObject.mTransform.mMatrix.m11 = move4;
					}
					else if ((aFlagsAndObjectNum & 0x4000) != 0) {
						////print("MOVEFLAGS_ROTATE");
						
						float inRot = aBuffer.ReadShort();
						
						float aRot = inRot / 1000.0f;
						float sinRot = (float) Math.sin(aRot);
						float cosRot = (float) Math.cos(aRot);
						
						//print("Matrix Rotation: " + aRot + " " + sinRot + " " + cosRot);
						
						aSuperAnimObject.mTransform.mMatrix.m00 = cosRot;
						aSuperAnimObject.mTransform.mMatrix.m01 = sinRot;
						aSuperAnimObject.mTransform.mMatrix.m10 = -sinRot;
						aSuperAnimObject.mTransform.mMatrix.m11 = cosRot;
					}
					
					Matrix3x3f aMatrix = new Matrix3x3f();
					aMatrix.loadIdentity();
					
					// MOVEFLAGS_LONGCOORDS
					if ((aFlagsAndObjectNum & 0x0800) != 0)
					{
						float ll1 = aBuffer.readInt();
						float ll2 = aBuffer.readInt();
						
						//print("MOVEFLAGS_LONGCOORDS LONG " + ll1 + " " + ll2);
						
						//print("Flag: " + aFlagsAndObjectNum + " Num: " + anObjectNum + " Longs: " + ll1 + " " + ll2);
						
						aMatrix.m02 = ll1 / TWIPS_PER_PIXEL;
						aMatrix.m12 = ll2 / TWIPS_PER_PIXEL;
					}
					else
					{
						float ls1 = aBuffer.ReadShort();
						float ls2 = aBuffer.ReadShort();
						
						//print("MOVEFLAGS_LONGCOORDS SHORT " + ls1 + " " + ls2);
						
						//print("Flag: " + aFlagsAndObjectNum + " Num: " + anObjectNum + " Longs: " + ls1 + " " + ls2);
						
						aMatrix.m02 = ls1 / TWIPS_PER_PIXEL;
						aMatrix.m12 = ls2 / TWIPS_PER_PIXEL;
					}
					
					Matrix3x3f mA = aSuperAnimObject.mTransform.mMatrix;
					Matrix3x3f mB = aMatrix;
					Matrix3x3f mC = aMatrix.mul(mA);
					
					//print("Matrix A:[[" + fl(mA.m00) + "," + fl(mA.m01) + "," + fl(mA.m02) + "],[" + fl(mA.m10) + "," + fl(mA.m11) + "," + fl(mA.m12) + "],[" + fl(mA.m20) + "," + fl(mA.m21) + "," + fl(mA.m22) + "]]");
					//print("Matrix B:[[" + fl(mB.m00) + "," + fl(mB.m01) + "," + fl(mB.m02) + "],[" + fl(mB.m10) + "," + fl(mB.m11) + "," + fl(mB.m12) + "],[" + fl(mB.m20) + "," + fl(mB.m21) + "," + fl(mB.m22) + "]]");
					//print("Matrix C:[[" + fl(mC.m00) + "," + fl(mC.m01) + "," + fl(mC.m02) + "],[" + fl(mC.m10) + "," + fl(mC.m11) + "," + fl(mC.m12) + "],[" + fl(mC.m20) + "," + fl(mC.m21) + "," + fl(mC.m22) + "]]");
					//print("");
					
					aSuperAnimObject.mTransform.mMatrix = mC;
					
					if ((aFlagsAndObjectNum & 0x2000) != 0)
					{
						//print("MOVEFLAGS_COLOR");
						int r = (aBuffer.ReadByte() & 0xFF);
						int g = (aBuffer.ReadByte() & 0xFF);
						int b = (aBuffer.ReadByte() & 0xFF);
						int a = (aBuffer.ReadByte() & 0xFF);
						//print("Object: " + anObjectNum + " R: " + r + " G: " + g + " B: " + b + " A: " + a);
						aSuperAnimObject.mColor.mRed = r;
						aSuperAnimObject.mColor.mGreen = g;
						aSuperAnimObject.mColor.mBlue = b;
						aSuperAnimObject.mColor.mAlpha = a;
					}
				}
			}
			
			//if (aFrameFlags == FRAMEFLAGS_FRAME_NAME) {
			if ((aFrameFlags & FRAMEFLAGS_FRAME_NAME) != 0) {
				//print("FRAMEFLAGS_FRAME_NAME");
				String aFrameName = aBuffer.ReadString();
				SuperAnimLabel aLabel = new SuperAnimLabel();
				aLabel.mLabelName = aFrameName;
				aLabel.mStartFrameNum = aFrameNum;
				//aMainDef.mLabels.insert(StringToIntMap::value_type(aFrameName, aFrameNum));
				
				//aSuperAnimLabelArray.push_back(aLabel);
				aSuperAnimLabelArray.add(aLabel);
			}
			
			//aFrame.mObjectVector.resize(aCurObjectMap.size());
			//aFrame.mObjectVector.clear();
//			for (IntToSuperAnimObjectMap::iterator anIt = aCurObjectMap.begin(); anIt != aCurObjectMap.end(); ++anIt)
//			{
//				SuperAnimObject &anObject = anIt->second;
//				aFrame.mObjectVector.push_back(anObject);
//			}
			
			if(aCurObjectMap != null) {
				aFrame.mObjectVector = new SuperAnimObject[aCurObjectMap.size()];
				int keyIndex = 0;
				for(int key : aCurObjectMap.keySet()) {
					aFrame.mObjectVector[keyIndex++] = aCurObjectMap.get(key);
				}
			}

		}
		
		// sort the label array & calculate the end frame for each label
		//std::sort(aSuperAnimLabelArray.begin(), aSuperAnimLabelArray.end(), SuperAnimLabelLess);
		
		if (aSuperAnimLabelArray.size() > 1) {
			for (int i = 0; i < aSuperAnimLabelArray.size() - 1; i++) {
				SuperAnimLabel aCurLabel = aSuperAnimLabelArray.get(i);
				SuperAnimLabel aNextLabel = aSuperAnimLabelArray.get(i + 1);
				aCurLabel.mEndFrameNum = aNextLabel.mStartFrameNum - 1;
			}
			SuperAnimLabel aLastLabel = aSuperAnimLabelArray.get(aSuperAnimLabelArray.size() - 1);
			aLastLabel.mEndFrameNum = aMainDef.mEndFrameNum;
		} else {
			// only have one section
			SuperAnimLabel aLabel = aSuperAnimLabelArray.get(0);
			aLabel.mEndFrameNum = aMainDef.mEndFrameNum;
		}
		aMainDef.mLabels.clear();
		for (int i = 0; i < aSuperAnimLabelArray.size(); i++) {
			//aMainDef.mLabels.push_back(aSuperAnimLabelArray.get(i));
			aMainDef.mLabels.add(aSuperAnimLabelArray.get(i));
		}
		
		return true;
	}
	

	
	

	
	public static SuperAnimDefMgr GetInstance()
	{
//		if (sInstance == null)
//		{
//			sInstance = new SuperAnimDefMgr();
//		}
//		
//		return sInstance;
		return new SuperAnimDefMgr();
	}
	
	public SuperAnimDefMgr DestroyInstance()
	{
//		if (sInstance)
//		{
//			delete sInstance;
//			sInstance = NULL;
//		}
		return this;
	}
	
	
	private SuperAnimObject getSuperAnimObject(int key) {
		if(aCurObjectMap == null) {
			//print("Creating New Object Map");
			aCurObjectMap = new HashMap<Integer, SuperAnimObject>();
		}
		
		SuperAnimObject obj = aCurObjectMap.get(key);
		if(obj == null) {
			//print("Creating New Object for Key: " + key);
			obj = new SuperAnimObject();
			aCurObjectMap.put(key, obj);
		}
		else {
			//print("Geting Object from Map for Key: " + key + " with Num: " + obj.mObjectNum);
		}
		return obj;
	}

	
	// std::string theSuperAnimFile include the absolute path
	public SuperAnimMainDef Load_GetSuperAnimMainDef(String theSuperAnimFile) {
		if(LoadSuperAnimMainDef(theSuperAnimFile)) {
			return mMainDefCache.get(theSuperAnimFile);
		}
		return null;
	}
	
	public void UnloadSuperAnimMainDef(String theName) {
		
	}
	
	private static String fl(float in) {
		return String.format(java.util.Locale.ROOT,"%.6f", in);
		//return new String("" + in);
	}

	private static String print(String text) {
		System.out.println(text);
		return text;
	}
}
