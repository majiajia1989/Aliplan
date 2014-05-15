
package com.mjj.aliplan;

import java.util.Comparator;
/**
 * 
 * @author jiajia
 *
 */
public class GameObjectManager extends ObjectManager {
	private static final int MAX_GAME_OBJECTS = 384;
	private float mMaxActivationRadius;
	private final static HorizontalPositionComparator sGameObjectComparator = new HorizontalPositionComparator();
	private FixedSizeArray<BaseObject> mInactiveObjects;
	private FixedSizeArray<GameObject> mMarkedForDeathObjects;
	private GameObject mPlayer;
	private boolean mVisitingGraph;
	private Vector2 mCameraFocus;

	public GameObjectManager(float maxActivationRadius) {
		super(MAX_GAME_OBJECTS);
		mMaxActivationRadius = maxActivationRadius;

		mInactiveObjects = new FixedSizeArray<BaseObject>(MAX_GAME_OBJECTS);
		mInactiveObjects.setComparator(sGameObjectComparator);

		mMarkedForDeathObjects = new FixedSizeArray<GameObject>(
				MAX_GAME_OBJECTS);
		mVisitingGraph = false;

		mCameraFocus = new Vector2();

	}

	@Override
	public void commitUpdates() {
		super.commitUpdates();

		GameObjectFactory factory = sSystemRegistry.gameObjectFactory;
		final int objectsToKillCount = mMarkedForDeathObjects.getCount();
		if (factory != null && objectsToKillCount > 0) {
			final Object[] deathArray = mMarkedForDeathObjects.getArray();
			for (int x = 0; x < objectsToKillCount; x++) {
				factory.destroy((GameObject) deathArray[x]);
			}
			mMarkedForDeathObjects.clear();
		}
	}

	@Override
	public void update(float timeDelta, BaseObject parent) {
		commitUpdates();

		CameraSystem camera = sSystemRegistry.cameraSystem;

		mCameraFocus
				.set(camera.getFocusPositionX(), camera.getFocusPositionY());
		mVisitingGraph = true;
		FixedSizeArray<BaseObject> objects = getObjects();
		final int count = objects.getCount();

		if (count > 0) {
			final Object[] objectArray = objects.getArray();
			for (int i = count - 1; i >= 0; i--) {
				GameObject gameObject = (GameObject) objectArray[i];
				final float distance2 = mCameraFocus.distance2(gameObject
						.getPosition());
				if (distance2 < (gameObject.activationRadius * gameObject.activationRadius)
						|| gameObject.activationRadius == -1) {
					gameObject.update(timeDelta, this);
				} else {
					// Remove the object from the list.
					// It's safe to just swap the current object with the last
					// object because this list is being iterated backwards, so
					// the last object in the list has already been processed.
					objects.swapWithLast(i);
					objects.removeLast();
					if (gameObject.destroyOnDeactivation) {
						mMarkedForDeathObjects.add(gameObject);
					} else {
						mInactiveObjects.add((BaseObject) gameObject);
					}
				}
			}
		}

		mInactiveObjects.sort(false);
		final int inactiveCount = mInactiveObjects.getCount();
		if (inactiveCount > 0) {
			final Object[] inactiveArray = mInactiveObjects.getArray();
			for (int i = inactiveCount - 1; i >= 0; i--) {
				GameObject gameObject = (GameObject) inactiveArray[i];

				final Vector2 position = gameObject.getPosition();
				final float distance2 = mCameraFocus.distance2(position);
				final float xDistance = position.x - mCameraFocus.x;
				if (distance2 < (gameObject.activationRadius * gameObject.activationRadius)
						|| gameObject.activationRadius == -1) {
					gameObject.update(timeDelta, this);
					mInactiveObjects.swapWithLast(i);
					mInactiveObjects.removeLast();
					objects.add(gameObject);
				} else if (xDistance < -mMaxActivationRadius) {
					// We've passed the focus, we can stop processing now
					break;
				}
			}
		}
		mVisitingGraph = false;
	}

	@Override
	public void add(BaseObject object) {
		if (object instanceof GameObject) {
			super.add(object);
		}
	}

	@Override
	public void remove(BaseObject object) {
		super.remove(object);
		if (object == mPlayer) {
			mPlayer = null;
		}
	}

	public void destroy(GameObject object) {
		mMarkedForDeathObjects.add(object);
		remove(object);
	}

	public void destroyAll() {
		assert mVisitingGraph == false;
		commitUpdates();

		FixedSizeArray<BaseObject> objects = getObjects();
		final int count = objects.getCount();
		for (int i = count - 1; i >= 0; i--) {
			mMarkedForDeathObjects.add((GameObject) objects.get(i));
			objects.remove(i);
		}

		final int inactiveObjectCount = mInactiveObjects.getCount();
		for (int j = inactiveObjectCount - 1; j >= 0; j--) {
			mMarkedForDeathObjects.add((GameObject) mInactiveObjects.get(j));
			mInactiveObjects.remove(j);
		}

		mPlayer = null;
	}

	public void setPlayer(GameObject player) {
		mPlayer = player;
	}

	public GameObject getPlayer() {
		return mPlayer;
	}

	/** Comparator for game objects objects. */
	private final static class HorizontalPositionComparator implements
			Comparator<BaseObject> {
		public int compare(BaseObject object1, BaseObject object2) {
			int result = 0;
			if (object1 == null && object2 != null) {
				result = 1;
			} else if (object1 != null && object2 == null) {
				result = -1;
			} else if (object1 != null && object2 != null) {
				float delta = ((GameObject) object1).getPosition().x
						- ((GameObject) object2).getPosition().x;
				if (delta < 0) {
					result = -1;
				} else if (delta > 0) {
					result = 1;
				}
			}
			return result;
		}
	}
}
