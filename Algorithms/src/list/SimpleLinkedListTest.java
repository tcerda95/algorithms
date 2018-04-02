package list;

public class SimpleLinkedListTest {

	public static void main(String[] args) {
		SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
		SimpleLinkedList<Integer> list2 = new SimpleLinkedList<>();

		list.add(3);
		list.add(6);
		list.add(5);
		list.add(3);
		list.add(8);
		
		list2.add(8);
		list2.add(4);
		list2.add(8);
		list2.add(5);
		list2.add(3);
		
		System.out.println(SimpleLinkedList.sumLists(list, list2));
		System.out.println(SimpleLinkedList.substractLists(list, list2));
	}

}
