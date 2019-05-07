
//Edited by Jonathan Burdette

public class HashTable<K, V> implements Table<K, V> {
	
	private LList<K, V> hashArray[];
	int base; //logical starting point of the array
	
	public HashTable(int size, int base) {
		this.base = base;
		hashArray = new LList[size]; //yes this creates a raw type warning
		for(int i=0; i < size; i++){
			hashArray[i] = new LList<K, V>();
		}
	}

	@Override
	public void put(int hash, K key, V value) {
		int tbl_slot = hash - base;
		hashArray[tbl_slot].add(key, value);
	}

	@Override
	public V get(int hash, K key) {
		int tbl_slot = hash - base;
		return hashArray[tbl_slot].getValue(key);
	}
}
