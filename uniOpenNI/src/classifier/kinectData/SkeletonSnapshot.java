package classifier.kinectData;

import java.util.Date;

import org.OpenNI.Point3D;

public class SkeletonSnapshot {
	public Date timestamp;
	
	public Point3D headPosition;
	public Point3D neckPosition;
	public Point3D leftShoulderPosition;
	public Point3D leftElbowPosition;
	public Point3D leftHandPosition;

	public Point3D rightShoulderPosition;
	public Point3D rightElbowPosition;
	public Point3D rightHandPosition;

	public Point3D torsoPosition;
	public Point3D leftHipPosition;
	public Point3D leftKneePosition;
	public Point3D leftFootPosition;
	public Point3D rightHipPosition;
	public Point3D rightKneePosition;
	public Point3D rightFootPosition;
	
	public SkeletonSnapshot(){
		this.timestamp = new Date();
	}

	public SkeletonSnapshot(Point3D headPosition,
			Point3D neckPosition, Point3D leftShoulderPosition,
			Point3D leftElbowPosition, Point3D leftHandPosition,
			Point3D rightShoulderPosition, Point3D rightElbowPosition,
			Point3D rightHandPosition,  Point3D torsoPosition,
			Point3D leftHipPosition, Point3D leftKneePosition,
			Point3D leftFootPosition, Point3D rightHipPosition,
			Point3D rightKneePosition, Point3D rightFootPosition) {
		this.timestamp = new Date();
		
		this.headPosition = headPosition;
		this.neckPosition = neckPosition;
		
		this.leftShoulderPosition = leftShoulderPosition;
		this.leftElbowPosition = leftElbowPosition;
		this.leftHandPosition = leftHandPosition;
		
		this.rightShoulderPosition = rightShoulderPosition;
		this.rightHandPosition = rightHandPosition;
		this.rightElbowPosition = rightElbowPosition;

		this.torsoPosition = torsoPosition;
		
		this.leftHipPosition = leftHipPosition;
		this.leftKneePosition = leftKneePosition;
		this.leftFootPosition = leftFootPosition;
		
		this.rightHipPosition = rightHipPosition;
		this.rightKneePosition = rightKneePosition;
		this.rightFootPosition = rightFootPosition;
	}
	
	public String toString(){
		String out = "SkeletonSnapshot: ";
		out += "timestamp: " + timestamp;
		out += "; rightShoulderPosition: " + rightShoulderPosition.getX();
		return out;
	}

	
}
