
public interface Table<K, V> {
	
	// add the value to the table at key
	void put(int hash, K key, V value);
	
	// retrieve the value associated with the key
	// if the key is not found return a null
	V get(int hash, K key);
}