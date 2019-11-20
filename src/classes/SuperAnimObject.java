package classes;

public class SuperAnimObject {
	
	public int mObjectNum;
	public int mResNum;
	public SuperAnimTransform mTransform;
	public Color mColor;
	
	private boolean neew = true;
	
	public SuperAnimObject() {
		
	}

	public boolean isNew() {
		if (neew) {
			neew = false;
			return true;
		}
		return false;
	}
}
