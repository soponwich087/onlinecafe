import java.text.DecimalFormat;

public class Utilitys {
	protected String SafeGet(String[] T, int INDEX) {
		try {
			return T[INDEX];
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}
	protected int Clamp(int x, int min, int max) {
		return Math.max(min, Math.min(x, max)); // Reference from Lua, limit x by a range min and max.
	}
	protected String Comma(int number) {
	    DecimalFormat formatter = new DecimalFormat("#,###");
	    return formatter.format(number);
	}
}