//Last revision 24.2.09
package cz.sirsi.ancestry.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Stores map of objects and its count of occurences. Provides some methods to compute the most or the least frequent
 * object(s).
 * 
 * @author msiroky
 * @param <T> Type that can be stored in map (and that can be computed for the most/least occurences)
 */
public class FrequentData<T> {
	private Map<T, Integer> frequentMap;

	private List<T> leastFrequent;
	private boolean leastFrequentComputed = false;
	private int leastFrequentCount = 0;

	private List<T> mostFrequent;
	private boolean mostFrequentComputed = false;
	private int mostFrequentCount = 0;

	/**
	 * Initializes map of object and lists (that stores most and least frequent objects)
	 */
	public FrequentData() {
		this.mostFrequent = new ArrayList<T>();
		this.leastFrequent = new ArrayList<T>();
		this.frequentMap = new HashMap<T, Integer>();
	}

	/**
	 * Adds object to map of objects
	 * 
	 * @param name Object to add to map
	 */
	public void addName(T name) {
		addName(name, 1);
	}

	/**
	 * Adds object to map of objects. It can be specified count of occurences of the object to add to map.
	 * 
	 * @param name Object to add to map
	 * @param countToAdd Count of occurences to add to map (can be zero to inicialize map values to 0)
	 */
	public void addName(T name, int countToAdd) {
		if (name != null) {
			Integer count = this.frequentMap.get(name);
			if (count == null) {
				count = 0;
			}
			count += countToAdd;
			this.frequentMap.put(name, count);
		}
	}

	/**
	 * Computes the least frequent object(s) in the map
	 */
	private void computeLeastFrequent() {
		if (this.frequentMap.size() == 0) {
			this.leastFrequentCount = 0;
			return;
		}

		int extremeCount = Integer.MAX_VALUE;

		for (Entry<T, Integer> entry : this.frequentMap.entrySet()) {
			Integer value = entry.getValue();
			if (extremeCount >= value) {
				if (extremeCount > value) {
					extremeCount = value;
					this.leastFrequent.clear();
				}

				this.leastFrequent.add(entry.getKey());
			}
		}

		this.leastFrequentCount = extremeCount;
		this.leastFrequentComputed = true;
	}

	/**
	 * Computes the most frequent object(s) in the map
	 */
	private void computeMostFrequent() {
		if (this.frequentMap.size() == 0) {
			this.mostFrequentCount = 0;
			return;
		}

		int extremeCount = Integer.MIN_VALUE;

		for (Entry<T, Integer> entry : this.frequentMap.entrySet()) {
			Integer value = entry.getValue();
			if (value >= extremeCount) {
				if (value > extremeCount) {
					extremeCount = value;
					this.mostFrequent.clear();
				}

				this.mostFrequent.add(entry.getKey());
			}
		}

		this.mostFrequentCount = extremeCount;
		this.mostFrequentComputed = true;
	}

	/**
	 * @return Computed list of the least frequent objects in the map
	 */
	public List<T> getTheLeast() {
		if (!this.leastFrequentComputed) {
			computeLeastFrequent();
		}

		return this.leastFrequent;
	}

	/**
	 * @return Count of occurences of the least frequent object(s)
	 */
	public int getTheLeastCount() {
		if (!this.leastFrequentComputed) {
			computeLeastFrequent();
		}

		return this.leastFrequentCount;
	}

	/**
	 * @return Computed list of the most frequent objects in the map
	 */
	public List<T> getTheMost() {
		if (!this.mostFrequentComputed) {
			computeMostFrequent();
		}

		return this.mostFrequent;
	}

	/**
	 * @return Count of occurences of the most frequent object(s)
	 */
	public int getTheMostCount() {
		if (!this.mostFrequentComputed) {
			computeMostFrequent();
		}

		return this.mostFrequentCount;
	}
}