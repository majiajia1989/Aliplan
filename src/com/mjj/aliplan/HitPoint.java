
package com.mjj.aliplan;

public class HitPoint extends AllocationGuard {
	public Vector2 hitPoint;
	public Vector2 hitNormal;

	public HitPoint() {
		super();
	}

	public final void reset() {
		hitPoint = null;
		hitNormal = null;
	}
}
