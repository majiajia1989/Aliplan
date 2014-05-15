
package com.mjj.aliplan;

import java.util.ArrayList;
/**
 * 
 * @author jiajia
 *
 */
public class ObjectRegistry extends BaseObject {

	public BufferLibrary bufferLibrary;
	public CameraSystem cameraSystem;
	public ChannelSystem channelSystem;
	public CollisionSystem collisionSystem;
	public ContextParameters contextParameters;
	public CustomToastSystem customToastSystem;
	public DebugSystem debugSystem;
	public DrawableFactory drawableFactory;
	public EventRecorder eventRecorder;
	public GameObjectCollisionSystem gameObjectCollisionSystem;
	public GameObjectFactory gameObjectFactory;
	public GameObjectManager gameObjectManager;
	public HitPointPool hitPointPool;
	public HotSpotSystem hotSpotSystem;
	public HudSystem hudSystem;
	public InputGameInterface inputGameInterface;
	public InputSystem inputSystem;
	public LevelBuilder levelBuilder;
	public LevelSystem levelSystem;
	public OpenGLSystem openGLSystem;
	public SoundSystem soundSystem;
	public TextureLibrary shortTermTextureLibrary;
	public TextureLibrary longTermTextureLibrary;
	public TimeSystem timeSystem;
	public RenderSystem renderSystem;
	public VectorPool vectorPool;
	public VibrationSystem vibrationSystem;

	private ArrayList<BaseObject> mItemsNeedingReset = new ArrayList<BaseObject>();

	public ObjectRegistry() {
		super();
	}

	public void registerForReset(BaseObject object) {
		final boolean contained = mItemsNeedingReset.contains(object);
		assert !contained;
		if (!contained) {
			mItemsNeedingReset.add(object);
		}
	}

	@Override
	public void reset() {
		final int count = mItemsNeedingReset.size();
		for (int x = 0; x < count; x++) {
			mItemsNeedingReset.get(x).reset();
		}
	}

}
