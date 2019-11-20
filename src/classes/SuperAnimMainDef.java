package classes;

import java.util.ArrayList;
import java.util.List;

public class SuperAnimMainDef {
	
	// typedef std::map<std::string, SuperAnimMainDef> SuperAnimMainDefMap;
	// SuperAnimMainDefMap mMainDefCache;
	
	
	// typedef std::vector<SuperAnimFrame> SuperAnimFrameVector;
	// public SuperAnimFrameVector mFrames;
	public SuperAnimFrame[] mFrames;
	
	public int mStartFrameNum;
	public int mEndFrameNum;
	public int mAnimRate;
	
	// typedef std::vector<SuperAnimLabel> SuperAnimLabelArray;
	// public SuperAnimLabelArray mLabels;
	public List<SuperAnimLabel> mLabels;
	
	
	public int mX;
	public int mY;
	public int mWidth;
	public int mHeight;
	
	// typedef std::vector<SuperAnimImage> SuperAnimImageVector;
	// public SuperAnimImageVector mImageVector;
	public SuperAnimImage[] mImageVector;
	
	public SuperAnimMainDef() {
		this.mLabels = new ArrayList();
	}

}
