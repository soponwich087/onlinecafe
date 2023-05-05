import java.io.*;
import java.util.*;

public class Receipt extends Utilitys {
	private ArrayList<Menu> Items = new ArrayList<>();
	public UUID Id = UUID.randomUUID();
	public int TotalMenu = 0;
	public int TotalPrice = 0;
	public String CustomerName;
	public String Contact;
	public int TableNumber;
	public Receipt(Collection<ArrayList<Menu>> Collect) {
		for (ArrayList<Menu> list : Collect) {
			Items.addAll(list);
		}
		for (int i = 0; i < Items.size(); i++) {
			Menu Item = Items.get(i);
			if (Item.Amount > 0) {
				TotalMenu++;
				TotalPrice += (Item.Amount * Item.Price());
			}
		}
	}
	public void Print(boolean RawFile) {
		String RawContent = "";
		String rTableReceipt = String.format("[Table %d]: #%s", TableNumber, Id.toString());
		if (RawFile) { RawContent += rTableReceipt + "\n"; }
		else { System.out.println(rTableReceipt); }
		for (int i = 0; i < Items.size(); i++) {
			Menu Item = Items.get(i);
			if (Item.Amount > 0) {
				String rList = String.format("   %s x%d (%s baht)", Item.Name(), Item.Amount, Comma(Item.Amount * Item.Price()));
				if (RawFile) { RawContent += rList + "\n";} 
				else { System.out.println(rList); }
			}
		}
		String rCustomer = String.format("   Customer: %s", CustomerName);
		String rContact = String.format("   Phone Number / Contact: %s", Contact);
		if (RawFile) {
			RawContent += rCustomer + "\n";
			RawContent += rContact + "\n";
			RawContent += String.format("[Total: %s baht]", Comma(TotalPrice));
			try (BufferedWriter BuffWriter = new BufferedWriter(new FileWriter(Id.toString() + ".txt"))) {
				BuffWriter.write(RawContent); // 4.2) Output
			} catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
			}
		} else {
			System.out.println(rCustomer);
			System.out.println(rContact);
		}
	}
	public void Print() {
		Print(false);
	}
	public void Finish() {
		Print(true); // Create new file receipt.
		for (int i = 0; i < Items.size(); i++) {
			Menu Item = Items.get(i); // (It's Address) so this will affect to HashMap<> CafeMenu in CafeProgram.
			Item.Amount = 0;
		}
	}
}