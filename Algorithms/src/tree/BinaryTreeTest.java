package tree;

public class BinaryTreeTest {

	public static void main(String[] args) {
		BinaryTree<Integer> tree = new BinaryTree<Integer>(20);
		tree.left = new BinaryTree<Integer>(9);
		tree.left.left = new BinaryTree<Integer>(9);
		tree.left.right = new BinaryTree<Integer>(8);
		tree.right = new BinaryTree<Integer>(-18);
		tree.right.left = new BinaryTree<Integer>(8);
		tree.right.right = new BinaryTree<Integer>(17);
		tree.right.right.right = new BinaryTree<Integer>(-8);
		tree.right.left.left = new BinaryTree<Integer>(3);
		
		System.out.println(BinaryTree.maximumPathSum(tree));
	}

}
