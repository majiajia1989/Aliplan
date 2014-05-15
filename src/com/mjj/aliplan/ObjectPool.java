
package com.mjj.aliplan;
/**
 * 
 * @author jiajia
 *
 */
public abstract class ObjectPool extends BaseObject {
	private FixedSizeArray<Object> mAvailable;
	private int mSize;

	private static final int DEFAULT_SIZE = 32;

	public ObjectPool() {
		super();
		setSize(DEFAULT_SIZE);
	}

	public ObjectPool(int size) {
		super();
		setSize(size);
	}

	@Override
	public void reset() {
	}

	/** Allocates an object from the pool */
	protected Object allocate() {
		Object result = mAvailable.removeLast();
		assert result != null : "Object pool of type "
				+ this.getClass().getSimpleName() + " exhausted!!";
		return result;
	}

	/** Returns an object to the pool. */
	public void release(Object entry) {
		mAvailable.add(entry);
	}

	/**
	 * Returns the number of pooled elements that have been allocated but not
	 * released.
	 */
	public int getAllocatedCount() {
		return mAvailable.getCapacity() - mAvailable.getCount();
	}

	private void setSize(int size) {
		mSize = size;
		mAvailable = new FixedSizeArray<Object>(mSize);

		fill();
	}

	protected abstract void fill();

	protected FixedSizeArray<Object> getAvailable() {
		return mAvailable;
	}

	protected int getSize() {
		return mSize;
	}

}
