package tree;

public class BinarySearchTreeTest {
	public static void main(String[] args) {
		BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
		tree.insert(10);
		tree.insert(6);
		tree.insert(15);
		tree.insert(0);
		tree.insert(8);
		tree.insert(20);
		tree.insert(12);
		
		System.out.println(tree.getInRange(-20, 100));
	}
}
