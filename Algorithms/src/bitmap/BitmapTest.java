package bitmap;

public class BitmapTest {
	private static int SIZE = 223100;
	
	
	public static void main(String[] args) {
		Bitmap map = new Bitmap(SIZE);
		
		for (int i = 0; i < SIZE; i++) {
			map.set(i, false);
			map.set(i, true);
			if (map.get(i) == false)
				System.out.println("ERROR");
		}
	}

}
