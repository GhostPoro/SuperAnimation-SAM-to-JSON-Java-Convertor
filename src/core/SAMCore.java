package core;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import classes.Matrix3x3f;
import classes.SuperAnim;
import classes.SuperAnimDefMgr;
import classes.SuperAnimFrame;
import classes.SuperAnimImage;
import classes.SuperAnimLabel;
import classes.SuperAnimMainDef;
import classes.SuperAnimObject;

public class SAMCore {

	public static void main(String[] args) {
		SuperAnimDefMgr manager = new SuperAnimDefMgr();
		SuperAnimMainDef data = manager.Load_GetSuperAnimMainDef("./unit_anime_810204.sam");
		String str = saveToString(data);
		
		print(str);
		
		//JSONObject jobj = new JSONObject(str);
		
		//printMeAndMy(jobj.toMap(), "root");
	}
	
	
	
	
	
	private static String saveToString(SuperAnimMainDef data) {
		
		String result = "";
	    result += ("{");
	    
	    result += ("mAnimRate:" + data.mAnimRate + ",");
	    result += ("mX:" + data.mX + ",");
	    result += ("mY:" + data.mY + ",");
	    result += ("mWidth:" + data.mWidth + ",");
	    result += ("mHeight:" + data.mHeight + ",");
	    
	    result += ("mImageVector:[");
	    for(SuperAnimImage img : data.mImageVector) {
	        result += ("{");
	        result += ("mImageName:'" + img.mImageName + "',");
	        result += ("mWidth:" + img.mWidth + ",");
	        result += ("mHeight:" + img.mHeight + ",");
	        
	        result += ("mTransform:{mMatrix:{m:[");  // "[%f,%f,%f],[%f,%f,%f],[%f,%f,%f]]}},",
    		Matrix3x3f mat = img.mTransform.mMatrix;
    		result += ("[" + fl(mat.m00) + "," + fl(mat.m01) + "," + fl(mat.m02) + "],");
    		result += ("[" + fl(mat.m10) + "," + fl(mat.m11) + "," + fl(mat.m12) + "],");
    		result += ("[" + fl(mat.m20) + "," + fl(mat.m21) + "," + fl(mat.m22) + "]]");
    		
    		result += ("}},");
    		result += ("},");
	    }
	    result += ("],");

	    result += ("mStartFrameNum:" + data.mStartFrameNum + ",");
	    result += ("mEndFrameNum:" + data.mEndFrameNum + ",");
	    
	    result += ("mFrames:[");
	    for(SuperAnimFrame frame : data.mFrames) {
	        result += ("{ mObjectVector:[");
	        for(SuperAnimObject obj : frame.mObjectVector) {
	            //result += ("{mObjectNum:%d,mResNum:%d,mTransform:{mMatrix:{m:[[%f,%f,%f],[%f,%f,%f],[%f,%f,%f]]}},mColor:{mRed:%d,mGreen:%d,mBlue:%d,mAlpha:%d}},",
        		result += ("{mObjectNum:" + obj.mObjectNum + ",");
        		result += ("mResNum:" + obj.mResNum + ",");
        		result += ("mTransform:{mMatrix:{m:[");
        		Matrix3x3f mat = obj.mTransform.mMatrix;
        		result += ("[" + fl(mat.m00) + "," + fl(mat.m01) + "," + fl(mat.m02) + "],");
        		result += ("[" + fl(mat.m10) + "," + fl(mat.m11) + "," + fl(mat.m12) + "],");
        		result += ("[" + fl(mat.m20) + "," + fl(mat.m21) + "," + fl(mat.m22) + "]]");
        		result += ("}},mColor:{mRed:" + obj.mColor.mRed + ",mGreen:" + obj.mColor.mGreen + ",mBlue:" + obj.mColor.mBlue + ",mAlpha:" + obj.mColor.mAlpha + "}},");
	        }
	        result += ("]},");
	    }
	    result += ("],");
	    
	    result += ("mLabels:[");
	    for(SuperAnimLabel label : data.mLabels) {
	        result += ("{mLabelName:'" + label.mLabelName + "',");
    		result += ("mStartFrameNum:" + label.mStartFrameNum + ",");
			result += ("mEndFrameNum:" + label.mEndFrameNum + "},");
	    }
	    result += ("],}");
		return result;
	}
	
	private static void printMeAndMy(Map<String, Object> map, String top) {
		for(String key : map.keySet()) {
			print(top + "->" + key);
			Object inObj = map.get(key);
			if(inObj != null) {
				if(inObj instanceof Map) {
					Map<String, Object> inMap = ((Map) inObj);
					printMeAndMy(inMap, top + "->" + key);
				}
				else if(inObj instanceof List) {
					List<String> inList = ((List) inObj);
					int i = 1;
					for(Object ininObj : inList) {
						if(ininObj instanceof Map) {
							Map<String, Object> inMap = ((Map) ininObj);
							//printMeAndMy(inMap, top + "-ARRAY-" + i + "->" + key);
							printMeAndMy(inMap, top + "-ARRAY->" + key);
						}
						else {
							//print(top + "->" + key + "-ARRAY-" + i + "->" + ininObj);
							print(top + "->" + key + "-ARRAY->" + ininObj);
						}
						i++;
					}
				}
				else {
					print(top + "->" + key + "->" + inObj);
				}
			}
		}
	}
	
	// typedef std::map<int, SuperAnimObject> IntToSuperAnimObjectMap;
	private static boolean SuperAnimLabelLess(SuperAnimLabel a, SuperAnimLabel b){
		if (a.mStartFrameNum != b.mStartFrameNum) {
			return a.mStartFrameNum < b.mStartFrameNum;
		}
		return true;
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
