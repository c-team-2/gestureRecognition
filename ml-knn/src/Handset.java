
public class Handset extends Feature {

	enum Device {
		iPhone,
		iPod,
		G1,
		Droid,
		MyTouch,
		Hero,
		Unknown
	}
	
	enum OS {
		Android,
		iPhone,
		Unknown
	}
	
	private Device device;
	private OS os;
	
	public Handset() {
		super("Handset");
	}
	
	public Handset(Device device, OS os) {
		super("Handset");
		this.os = os;
		this.device = device;
	}

	public void setOs(OS os) {
		this.os = os;
	}

	public OS getOs() {
		return os;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}
	
	public static OS determineOS(String os) {
		if(os.toLowerCase().contains("iphoneos")) {
			return OS.iPhone;
		}
		else if(os.toLowerCase().contains("android")) {
			return OS.Android;
		}
		else {
			return OS.Unknown;
		}
	}
	
	public static Device determineDevice(String device) {
		if(device.toLowerCase().contains("iphone")) {
			return Device.iPhone;
		}
		else if(device.toLowerCase().contains("ipod")) {
			return Device.iPod;
		}
		else if(device.toLowerCase().contains("droid")) {
			return Device.Droid;
		}
		else if(device.toLowerCase().contains("g1")) {
			return Device.G1;
		}
		else if(device.toLowerCase().contains("mytouch")) {
			return Device.MyTouch;
		}
		else if(device.toLowerCase().contains("hero")) {
			return Device.Hero;
		}
		else {
			return Device.Unknown;
		}
	}

}
