package com.mjj.aliplan;

public class OrbitalMagnetComponent extends GameComponent {
	private final static float DEFAULT_STRENGTH = 15.0f;

	private float mStrength;
	private Vector2 mCenter;
	private Vector2 mDelta;
	private Vector2 mRim;
	private Vector2 mVelocity;
	private float mMagnetRadius;
	private float mAreaRadius;

	public OrbitalMagnetComponent() {
		super();
		mCenter = new Vector2();
		mDelta = new Vector2();
		mRim = new Vector2();
		mVelocity = new Vector2();
		reset();
		setPhase(ComponentPhases.COLLISION_DETECTION.ordinal());
	}

	@Override
	public void reset() {
		mCenter.zero();
		mDelta.zero();
		mRim.zero();
		mVelocity.zero();
		mStrength = DEFAULT_STRENGTH;
		mAreaRadius = 0.0f;
		mMagnetRadius = 0.0f;
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		GameObjectManager manager = sSystemRegistry.gameObjectManager;
		if (manager != null) {
			GameObject player = manager.getPlayer();
			if (player != null) {
				GameObject parentObject = (GameObject) parent;
				applyMagnetism(player, parentObject.getCenteredPositionX(),
						parentObject.getCenteredPositionY(), timeDelta);
			}
		}
	}

	private void applyMagnetism(GameObject target, float centerX,
			float centerY, float timeDelta) {
		mCenter.set(centerX, centerY);
		final float targetX = target.getCenteredPositionX();
		final float targetY = target.getCenteredPositionY();
		mDelta.set(targetX, targetY);
		mDelta.subtract(mCenter);

		final float distanceFromCenter2 = mDelta.length2();
		final float area2 = mAreaRadius * mAreaRadius;
		if (distanceFromCenter2 < area2) {
			mRim.set(mDelta);
			mRim.normalize();
			mRim.multiply(mMagnetRadius);
			mRim.add(mCenter);
			// rim is now the closest point on the magnet circle

			// remove gravity
			final Vector2 targetVelocity = target.getVelocity();
			GravityComponent gravity = target
					.findByClass(GravityComponent.class);
			final Vector2 gravityVector = gravity.getGravity();
			mVelocity.set(gravityVector);
			mVelocity.multiply(timeDelta);
			targetVelocity.subtract(mVelocity);

			mDelta.add(targetVelocity);
			mDelta.normalize();
			mDelta.multiply(mMagnetRadius);
			mDelta.add(mCenter);

			// mDelta is now the next point on the magnet circle in the
			// direction of
			// movement.

			mDelta.subtract(mRim);
			mDelta.normalize();
			// Now mDelta is the tangent to the magnet circle, pointing in the
			// direction
			// of movement.

			mVelocity.set(mDelta);
			mVelocity.normalize();

			// mVelocity is now the direction to push the player

			mVelocity.multiply(mStrength);
			float weight = 1.0f;
			if (distanceFromCenter2 > mMagnetRadius * mMagnetRadius) {
				float distance = (float) Math.sqrt(distanceFromCenter2);
				weight = (distance - mMagnetRadius)
						/ (mAreaRadius - mMagnetRadius);
				weight = 1.0f - weight;
				mVelocity.multiply(weight);
			}
			final float speed = targetVelocity.length();
			targetVelocity.add(mVelocity);
			if (targetVelocity.length2() > (speed * speed)) {
				targetVelocity.normalize();
				targetVelocity.multiply(speed);
			}

		}

	}

	public void setup(float areaRadius, float orbitRadius) {
		mAreaRadius = areaRadius;
		mMagnetRadius = orbitRadius;
	}
}
