package com.mjj.aliplan;
/**
 * 
 * @author jiajia
 *
 * @param <T>
 */
public abstract class TObjectPool<T> extends ObjectPool {

	public TObjectPool() {
		super();
	}

	public TObjectPool(int size) {
		super(size);
	}

	public T allocate() {
		@SuppressWarnings("unchecked")
		T object = (T) super.allocate();
		return object;
	}

}
