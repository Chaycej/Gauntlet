package gauntlet;

public class Camera {

	private float xOffset;
	private float yOffset;
	
	public Camera(float x, float y) {
		this.xOffset = x;
		this.yOffset = y;
	}
	
	public void update(float x, float y) {
		this.xOffset = x;
		this.yOffset = y;
	}
	
	public float getXoffset() {
		return xOffset;
	}
	
	public float getYoffset() {
		return yOffset;
	}	

}