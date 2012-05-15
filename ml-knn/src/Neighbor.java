public class Neighbor {
	private Instance instance;
	private int distance;
	
	public Neighbor() {
		setInstance(new Instance());
		setDistance(0);
	}
	
	public Neighbor(Instance instance, int distance) {
		this.setInstance(instance);
		this.setDistance(distance);
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public Instance getInstance() {
		return instance;
	}
}
