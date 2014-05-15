
package com.mjj.aliplan;
/**
 * 
 * @author jiajia
 *
 */
public class GravityComponent extends GameComponent {
	private Vector2 mGravity;
	private Vector2 mScaledGravity;
	private static final Vector2 sDefaultGravity = new Vector2(0.0f, -400.0f);

	public GravityComponent() {
		super();
		mGravity = new Vector2(sDefaultGravity);
		mScaledGravity = new Vector2();
		setPhase(ComponentPhases.PHYSICS.ordinal());
	}

	@Override
	public void reset() {
		mGravity.set(sDefaultGravity);
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		mScaledGravity.set(mGravity);
		mScaledGravity.multiply(timeDelta);
		((GameObject) parent).getVelocity().add(mScaledGravity);
	}

	public Vector2 getGravity() {
		return mGravity;
	}

	public void setGravityMultiplier(float multiplier) {
		mGravity.set(sDefaultGravity);
		mGravity.multiply(multiplier);
	}
}
