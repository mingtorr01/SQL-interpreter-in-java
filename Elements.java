package SQL;

public class Elements {
	private String node;
	
		// constructor
		public Elements() {
		}
		public Elements(String index) {
			this.node = index;
		}
		public void addElements(String node) {
			this.node = node;
		}
		public String toString() {
			return node;
		}
}
