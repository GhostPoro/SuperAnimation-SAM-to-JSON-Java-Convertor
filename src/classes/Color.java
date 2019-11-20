package classes;

public class Color {
	
	public int mRed = 0;
	public int mGreen = 0;
	public int mBlue = 0;
	public int mAlpha = 255;
	
	public Color() { }
	
	public Color(int theRed, int theGreen, int theBlue, int theAlpha) {
		this.mRed = theRed;
		this.mGreen = theGreen;
		this.mBlue = theBlue;
		this.mAlpha = theAlpha;
	}
	
	public Color InterpolateTo(Color theNextColor, float thePct){
		return new Color((int)(mRed   * (1.0f - thePct) + theNextColor.mRed   * thePct),
					     (int)(mGreen * (1.0f - thePct) + theNextColor.mGreen * thePct),
					     (int)(mBlue  * (1.0f - thePct) + theNextColor.mBlue  * thePct),
					     (int)(mAlpha * (1.0f - thePct) + theNextColor.mAlpha * thePct));
	}
}
