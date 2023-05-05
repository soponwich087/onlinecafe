import java.io.*;
import java.util.*;

// 1.Classes, 2.Inheritance, 3.Try / catch, 4.Input & Output, or 5.Collections.

public class CafeProgram extends Utilitys { // 2) Inheritance
	private String[][] CafeTables = new String[5][2];
	private HashMap<String, ArrayList<Menu>> CafeMenu = new HashMap<String, ArrayList<Menu>>(); // 5) Collections
	private Terminal Console = new Terminal();
	public CafeProgram() {
		File MyCafeMenuFile = new File("MenuCafe.dat");
		try (BufferedReader BuffReader = new BufferedReader(new FileReader(MyCafeMenuFile))) { // 4.1) Input
			String Line = null;
			while ((Line = BuffReader.readLine()) != null) {
				String[] Item = Line.split(", ");
				String ItemName = SafeGet(Item, 0);
				String ItemCategory = SafeGet(Item, 1);
				int ItemPrice = Integer.parseInt(SafeGet(Item, 2));
				ArrayList<Menu> Container = CafeMenu.getOrDefault(ItemCategory, null);
				Container = (Container == null) ? new ArrayList<Menu>() : Container;
				CafeMenu.put(ItemCategory, Container); // Update new category.
				Menu v = new Menu(ItemName, ItemPrice);
				Container.add(v);
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	public int PrintCafeMenu() {
		int Choice = 0;
		for (Map.Entry<String, ?> Set : CafeMenu.entrySet()) {
			System.out.println(String.format("%d. %s", (Choice++)+1, Set.getKey()));
		}
		return Choice; // Elements (category)
	}
	public void Start() {
		do {
			Console.Clear();
			System.out.println("====== Online Cafe ======");
			System.out.println("1. Table select");
			System.out.println("2. Menu book");
			System.out.println("3. EXIT");
			switch (Console.PromptInteger("// Select action", 1, 3)) {
				case 1:
					boolean StillInTableSelectPage = true;
					do {
						Console.Clear();
						System.out.println("====== Online Cafe / Table select ======");
						for (int i = 0; i < CafeTables.length; ++i) {
							System.out.print("  " + String.format("{%s} [Table %d] {%s}", CafeTables[i][0] != null ? "-" : "1", i + 1, CafeTables[i][1] != null ? "-" : "2") + (((i) % 2 == 0) ? "" : "\n"));
						}
						System.out.println();
						System.out.println("====== ========================== ======");
						for (int i = 0; i < CafeTables.length; ++i)
							System.out.println(String.format("%d. Table %d", i + 1, i + 1));
						int BackToMain = CafeTables.length + 1;
						System.out.println(String.format("%d. BACK", BackToMain));
						int TableNumber = Console.PromptInteger("// Select your table number", 1, BackToMain);
						if (TableNumber == BackToMain) {
							StillInTableSelectPage = false;
						} else {
							try {
								String[] Table = CafeTables[TableNumber - 1];
								int TablePosition = Console.PromptInteger("// Select your position", 1, Table.length);
								if (Table[TablePosition - 1] == null) {
									Console.Clear();
									Receipt PurchaseReceipt = new Receipt(CafeMenu.values());
									if (PurchaseReceipt.TotalMenu > 0) {
										System.out.println(String.format("[Table %d]:", TableNumber));
										String cName = Console.PromptString("// Customer name", "any");
										String cContact = Console.PromptString("// Customer phone number or contact", "any");
										PurchaseReceipt.CustomerName = cName.length() == 0 ? "-" : cName;
										PurchaseReceipt.Contact = cContact.length() == 0 ? "-" : cContact;
										PurchaseReceipt.TableNumber = TableNumber;
										Console.Clear();
										PurchaseReceipt.Print();
										if (Console.PromptBoolean(String.format("// Confirm this purchase (%s baht)", Comma(PurchaseReceipt.TotalPrice)))) {
											PurchaseReceipt.Finish();
											Table[TablePosition - 1] = PurchaseReceipt.Id.toString();
											Console.Pause();
										} else {
											PurchaseReceipt = null;
											System.gc(); // call the garbage collector to clean up (explicitly)
										}
									} else {
										StillInTableSelectPage = false;
										Console.Clear();
										System.out.println("[WARN]: Please select some menu before selecting a table");
										Console.Pause();
									}
									
								} else {
									System.out.println("[ERROR]: Someone took your position, please look for another seat");
									Console.Pause();
								}
							} catch (IndexOutOfBoundsException e) {
								System.out.println("[ERROR]: No table on that number");
								Console.Pause();
							}
						}
					} while (StillInTableSelectPage);
					break;
				case 2:
					boolean StillInCategoryPage = true;
					do {
						Console.Clear();
						System.out.println("====== Online Cafe / Categories ======");
						int Count = PrintCafeMenu();
						int BackMenu = Count + 1;
						System.out.println(String.format("%d. EXIT", BackMenu));
						int Choice = Console.PromptInteger("// Select menu", 1, BackMenu);
						if (Choice == BackMenu) {
							StillInCategoryPage = false;
						} else {
							boolean StillInMenuPage = true;
							do {
								Set<String> Categories = CafeMenu.keySet();
								String[] CategoriesIdx = Categories.toArray(new String[0]);
								String CategoryName = CategoriesIdx[Choice - 1];
								ArrayList<Menu> Container = CafeMenu.get(CategoryName); // No IndexOutOfBoundsException (PromptInteger is already limit)
								Console.Clear();
								System.out.println(String.format("====== Online Cafe / Categories / %s ======", CategoryName));
								for (int MenuIdx = 0; MenuIdx < Container.size(); MenuIdx++) {
									Menu Item = Container.get(MenuIdx);
									System.out.println(String.format("%d. %s, %d baht %s", MenuIdx + 1, Item.Name(), Item.Price(), Item.Amount > 0 ? String.format("(x%d)", Item.Amount) : ""));
								}
								int BackToMain = Container.size() + 1;
								System.out.println(String.format("%d. BACK", BackToMain));
								int ChooseMenu = Console.PromptInteger("// Select menu", 1, BackToMain);
								if (ChooseMenu != BackToMain) {
									Menu Item = Container.get(ChooseMenu - 1);
									int SetAmount;
									try { // 3) Try / catch
										SetAmount = Clamp(Integer.parseInt(Console.PromptString("// Set input amount", "int <limit: 0-99>")), 0, 99);
										Item.Amount = SetAmount;
									} catch(NumberFormatException e) {
										System.out.println("[ERROR]: Input cannot convert to integer. (Skipped Process)");
										Console.Pause();
									}
								} else {
									StillInMenuPage = false;
								}
							} while (StillInMenuPage);
						}
					} while (StillInCategoryPage);
					break;
				case 3:
					Console.Close();
					break;
			}
		} while (!Console.Suspended);
	}
}
