package bitmap;

public class Bitmap {
	private final static int VAR_SIZE = Integer.SIZE;
	private final static int MASK = 1 << (Integer.SIZE-1);
	
	private int[] bitmap;
	private int bits;
	
	public void set(int i, boolean value) {
		if (i >= bits)
			throw new IndexOutOfBoundsException("Bitmap size is of " + bits + " bits. " + i + " is out of bounds.");
		
		int index = i / VAR_SIZE;
		int offset = i % VAR_SIZE;
		int mask = MASK >>> offset;
		
		if (value)
			bitmap[index] |= mask;
		else
			bitmap[index] &= (-1 ^ mask);
	}
	
	public boolean get(int i) {
		if (i >= bits)
			throw new IndexOutOfBoundsException("Bitmap size is of " + bits + " bits. " + i + " is out of bounds.");
		
		int index = i / VAR_SIZE;
		int offset = i % VAR_SIZE;

		return (bitmap[index] & (MASK >>> offset)) != 0 ? true : false;
	}
	
	public int size() {
		return bits;
	}
	
	public Bitmap(int bits) {
		this.bitmap = new int[bits/VAR_SIZE + 1];
		this.bits = bits;
	}
}
