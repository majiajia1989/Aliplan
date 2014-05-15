package com.mjj.aliplan;
/**
 * 
 */
import java.util.Arrays;
import java.util.Comparator;

public class StandardSorter<T> extends Sorter{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void sort(Object[] array, int count, Comparator comparator) {
		Arrays.sort(array, 0, count, comparator);
	}

}
