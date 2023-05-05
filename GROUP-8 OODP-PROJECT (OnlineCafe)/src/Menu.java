public class Menu { // 1) Classes
	private String __Name__;
	private int __Price__;
	public int Amount = 0;
	public Menu(String ItemName, int ItemPrice) {
		this.__Name__ = ItemName;
		this.__Price__ = ItemPrice;
	}
	public String Name() {
		return __Name__;
	}
	public int Price() {
		return __Price__;
	}
}
