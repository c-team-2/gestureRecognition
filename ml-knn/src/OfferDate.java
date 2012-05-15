import java.util.Date;

public class OfferDate extends Feature {
	private Date date;
	private TOD tod;
	
	enum TOD {
		Morning,
		Afternoon,
		Evening,
		Night,
		Unknown
	}
	
	public OfferDate() {
		super("OfferDate");
	}
	
	public OfferDate(Date date) {
		super("OfferDate");
		this.date = date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setTod(TOD tod) {
		this.tod = tod;
	}

	public TOD getTod() {
		return tod;
	}

	public TOD determineTod(Date date) {
		if(date.getHours() >= 0 && date.getHours() < 12) {
			return TOD.Morning;
		}
		else if (date.getHours() >= 12 && date.getHours() < 17) {
			return TOD.Afternoon;
		}
		else if (date.getHours() >= 17 && date.getHours() < 21) {
			return TOD.Evening;
		}
		else if (date.getHours() >= 21 && date.getHours() < 24) {
			return TOD.Night;
		}
		else {
			return TOD.Unknown;
		}
	}
}
