package com.wonkglorg.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents a map that maintains the order of its entries by a given comparator. This differs from a TreeMap as it is not limited by key comparison alone but can sort by both key and or value.
 *
 * @param <K>
 * @param <V>
 */
public class OrderedMap<K, V> extends HashMap<K, V>{
	/**
	 * If the sorted order is dirty (needs to be updated)
	 */
	private boolean isSortedOrderDirty = false;
	
	/**
	 * The comparator to sort the map by
	 */
	private Comparator<Entry<K, V>> comparator;
	
	/**
	 * Represents the sorted order of the map
	 */
	private final ArrayList<Entry<K, V>> sortedOrder;
	
	/**
	 * Creates a new OrderedMap
	 *
	 * @param initialCapacity The initial capacity of the map
	 * @param loadFactor The load factor of the map
	 * @param comparator The comparator to sort the map by
	 */
	public OrderedMap(int initialCapacity, float loadFactor, Comparator<Entry<K, V>> comparator) {
		super(initialCapacity, loadFactor);
		this.comparator = comparator;
		sortedOrder = new ArrayList<>(initialCapacity);
	}
	
	/**
	 * Creates a new OrderedMap
	 *
	 * @param initialCapacity The initial capacity of the map
	 * @param comparator The comparator to sort the map by
	 */
	public OrderedMap(int initialCapacity, Comparator<Entry<K, V>> comparator) {
		super(initialCapacity);
		this.comparator = comparator;
		sortedOrder = new ArrayList<>(initialCapacity);
	}
	
	/**
	 * Creates a new OrderedMap
	 *
	 * @param m The map to copy
	 * @param comparator The comparator to sort the map by
	 */
	public OrderedMap(Map<? extends K, ? extends V> m, Comparator<Entry<K, V>> comparator) {
		super(m);
		this.comparator = comparator;
		sortedOrder = new ArrayList<>(this.entrySet());
	}

	/**
	 * Creates a new OrderedMap
	 *
	 * @param comparator The comparator to sort the map by
	 */
	public OrderedMap(Comparator<Entry<K, V>> comparator) {
		super();
		this.comparator = comparator;
		sortedOrder = new ArrayList<>();
	}
	
	@Override
	public V put(K key, V value) {
		markDirty();
		return super.put(key, value);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		markDirty();
		super.putAll(m);
	}
	
	@Override
	public V putIfAbsent(K key, V value) {
		markDirty();
		return super.putIfAbsent(key, value);
	}
	
	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		markDirty();
		return super.compute(key, remappingFunction);
	}
	
	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		markDirty();
		return super.computeIfPresent(key, remappingFunction);
	}
	
	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		markDirty();
		return super.computeIfAbsent(key, mappingFunction);
	}
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		ensureSortedOrder();
		return new HashSet<>(sortedOrder);
	}
	
	@Override
	public Set<K> keySet() {
		ensureSortedOrder();
		return sortedOrder.stream().map(Entry::getKey).collect(Collectors.toCollection(HashSet::new));
	}
	
	@Override
	public Collection<V> values() {
		ensureSortedOrder();
		return sortedOrder.stream().map(Entry::getValue).collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Gets the first entry in the map
	 *
	 * @return The first entry in the map
	 */
	public Entry<K, V> getFirst() {
		ensureSortedOrder();
		return sortedOrder.get(0);
	}
	
	/**
	 * Gets the last entry in the map
	 *
	 * @return The last entry in the map or null if the map is empty
	 */
	public Entry<K, V> getLast() {
		ensureSortedOrder();
		if(sortedOrder.isEmpty()){
			return null;
		}
		return sortedOrder.get(sortedOrder.size() - 1);
	}
	
	/**
	 * Gets the entry at the specified index
	 *
	 * @param index The index to get the entry from
	 * @return The entry at the specified index
	 */
	public Entry<K, V> get(int index) {
		ensureSortedOrder();
		if(index < 0 || index >= sortedOrder.size()){
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + sortedOrder.size());
		}
		return sortedOrder.get(index);
	}
	
	/**
	 * Gets a range of entries from the map beginning from the specified index until the end of the map
	 *
	 * @param fromIndex The index to start from
	 * @return The range of entries from the map
	 */
	public List<Entry<K, V>> getRange(int fromIndex) {
		ensureSortedOrder();
		if(fromIndex < 0 || fromIndex >= sortedOrder.size()){
			throw new IndexOutOfBoundsException("Index: " + fromIndex + ", Size: " + sortedOrder.size());
		}
		return sortedOrder.subList(fromIndex, sortedOrder.size());
	}
	
	/**
	 * Gets a range of entries from the map beginning from the specified index until the specified index
	 *
	 * @param fromIndex The index to start from
	 * @param toIndex The index to end at
	 * @return The range of entries from the map
	 */
	public List<Entry<K, V>> getRange(int fromIndex, int toIndex) {
		ensureSortedOrder();
		if(fromIndex < 0 || fromIndex >= sortedOrder.size()){
			throw new IndexOutOfBoundsException("From Index: " + fromIndex + ", Size: " + sortedOrder.size());
		}
		if(toIndex < 0 || toIndex >= sortedOrder.size()){
			throw new IndexOutOfBoundsException("To Index: " + toIndex + ", Size: " + sortedOrder.size());
		}
		return sortedOrder.subList(fromIndex, toIndex);
	}
	
	private void markDirty() {
		isSortedOrderDirty = true;
	}
	
	private void ensureSortedOrder() {
		if(isSortedOrderDirty){
			sortedOrder.clear();
			sortedOrder.addAll(super.entrySet());
			if(comparator != null){
				sortedOrder.sort(comparator);
			}
			isSortedOrderDirty = false;
		}
	}
	
	/**
	 * Gets the comparator used to sort the map
	 *
	 * @return The comparator used to sort the map
	 */
	public Comparator<Entry<K, V>> getComparator() {
		return comparator;
	}
	
	/**
	 * Sets the comparator used to sort the map
	 *
	 * @param comparator The comparator used to sort the map
	 */
	public void setComparator(Comparator<Entry<K, V>> comparator) {
		this.comparator = comparator;
	}
	@Override
	public String toString() {
		return "%s".formatted(sortedOrder);
	}
}
