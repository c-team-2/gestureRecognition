public class WSAction extends Feature {

		enum Action {
			Hit,
			Redeem,
			Unknown
		}
		
		private Action action;
	
		public WSAction() {
			super("WSAction");
		}
		
		public WSAction(Action action) {
			super("WSAction");
			this.action = action;
		}

		public void setAction(Action action) {
			this.action = action;
		}

		public Action getAction() {
			return action;
		}
		
		public static Action determineAction(String action) {
			if(action.toLowerCase().contains("hit")) {
				return Action.Hit;
			}
			else if(action.toLowerCase().contains("redemption")) {
				return Action.Redeem;
			}
			else {
				return Action.Unknown;
			}
		}
}
