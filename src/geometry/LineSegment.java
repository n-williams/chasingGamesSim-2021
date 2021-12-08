package geometry;

//example of composition, as it contains two cartesian coordinates
public class LineSegment {
	CartesianCoordinate startPoint;
	CartesianCoordinate endPoint;
	
	public LineSegment(CartesianCoordinate c1, CartesianCoordinate c2) {
		startPoint = c1;
		endPoint = c2;
	}

	public CartesianCoordinate getStartPoint() {
		return startPoint;
	}

	public CartesianCoordinate getEndPoint() {
		return endPoint;
	}
	
	public String toString() {
		return startPoint.getX() + ", " + startPoint.getY() + " to " + endPoint.getX() + ", " + endPoint.getY();
	}
	
	public double length() {
		double xSquare = Math.pow((endPoint.getX() - startPoint.getX()),2);
		double ySquare = Math.pow((endPoint.getY() - startPoint.getY()),2);
		double length = Math.sqrt(xSquare + ySquare);
		
		return length;
	}
	
	public double angle() {
		double xDiff = endPoint.getX() - startPoint.getX();
		double yDiff = endPoint.getY() - startPoint.getY();
		
		double angleInRad = Math.atan2(yDiff,xDiff);
		double angleInDeg = Math.toDegrees(angleInRad);
		
		return angleInDeg;
	}
}
