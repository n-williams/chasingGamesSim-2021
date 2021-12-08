package geometry;

public class CartesianCoordinate {
	private double xPosition;
	private double yPosition;
	
	public CartesianCoordinate(double x, double y) {
		xPosition = x;
		yPosition = y;
	}

	public double getX() {
		return xPosition;
	}
	
	public void setX(double xPosition) {
		this.xPosition = xPosition;
	}

	public double getY() {
		return yPosition;
	}
	
		public void setY(double yPosition) {
		this.yPosition = yPosition;
	}

	public String toString() {
		return xPosition + " " + yPosition;
	}
}
