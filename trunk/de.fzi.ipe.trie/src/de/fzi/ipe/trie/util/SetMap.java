
package de.fzi.ipe.trie.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetMap<K,V> {
	private Map<K,Set<V>> data = new HashMap<K,Set<V>>();
	private Set<V> EMPTY = Collections.unmodifiableSet(new HashSet<V>());
	
	public void put(K key, V value) {
		Set<V> values = data.get(key);
		if (values == null) {
			values = new HashSet<V>();
			data.put(key, values);
		}
		values.add(value);
	}
	
	public Set<V> get(K key) {
		Set<V> toReturn = data.get(key);
		if (toReturn == null) return EMPTY;
		else return toReturn;
	}
	
	public Set<K> keySet() {
		return data.keySet();
	}
	
}