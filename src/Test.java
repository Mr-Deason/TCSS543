import java.io.InputStreamReader;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(new InputStreamReader(System.in));
		while (in.hasNext()) {
			String string = in.nextLine();
			System.out.println(string);
		}
	}
}
