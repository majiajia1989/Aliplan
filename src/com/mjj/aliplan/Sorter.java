package com.mjj.aliplan;

import java.util.Comparator;

public abstract class Sorter<Type> {
	public abstract void sort(Type[] array, int count,
			Comparator<Type> comparator);
}
