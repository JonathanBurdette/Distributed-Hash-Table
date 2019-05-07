
//Jonathan Burdette

public class LList<K,V> {

	private class Node {

		private K key;
		private V value;
		private Node next;
		
		private Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		private void setValue(V value) {
			this.value = value;
		}
		
		private K getKey() {
			return key;
		}
		
		private V getValue() {
			return value;
		}
	}

	private Node head;

	//constructor
	public LList() {
		head = null;
	}
	
	//adds new entry to linked list
	public void add(K key, V value) {
		Node n = new Node(key, value);
		
		if(head != null)  {
			Node current = head;  
			while (current.next != null) {
				current = current.next; 
			}
			current.next = n;
		} else {
			head = n;
		}
	}

	//checks if there is a matching key in list
	public boolean checkKey(K key) {
		Node current = head;  
		while (current != null) {
			if(current.getKey().equals(key)) {
				return true;
			}
			current = current.next; 
		}
		return false;
	}
	
	//finds matching key in linked list
	public void replaceValue(K key, V value) {
		Node current = head;  
		while (current != null) {
			if(current.getKey().equals(key)) {
				current.setValue(value);
			}
			current = current.next; 
		}
	}
	
	//gets the right value based on key
	public V getValue(K key) {
		Node current = head;  
		while (current != null) {
			if(current.getKey().equals(key)) {
				return current.getValue();
			}
			current = current.next; 
		}
		return null;
	}
	
	//gets head
	public Node getHead() {
		return head;
	}
}
