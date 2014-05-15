package com.mjj.aliplan;

import java.io.IOException;
import java.io.InputStream;
import android.content.res.AssetManager;
/**
 * 
 * @author jiajia
 *
 */
public class TiledWorld extends AllocationGuard {
	private int[][] mTilesArray;
	private int mRowCount;
	private int mColCount;
	private byte[] mWorkspaceBytes;

	public TiledWorld(int cols, int rows) {
		super();
		mTilesArray = new int[cols][rows];
		mRowCount = rows;
		mColCount = cols;

		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				mTilesArray[x][y] = -1;
			}
		}

		mWorkspaceBytes = new byte[4];

		calculateSkips();
	}

	public TiledWorld(InputStream stream) {
		super();
		mWorkspaceBytes = new byte[4];
		parseInput(stream);
		calculateSkips();
	}

	public int getTile(int x, int y) {
		int result = -1;
		if (x >= 0 && x < mColCount && y >= 0 && y < mRowCount) {
			result = mTilesArray[x][y];
		}
		return result;
	}

	// Builds a tiled world from a simple map file input source. The map file
	// format is as follows:
	// First byte: signature. Must always be decimal 42.
	// Second byte: width of the world in tiles.
	// Third byte: height of the world in tiles.
	// Subsequent bytes: actual tile data in column-major order.
	// TODO: add a checksum in here somewhere.
	protected boolean parseInput(InputStream stream) {
		boolean success = false;
		AssetManager.AssetInputStream byteStream = (AssetManager.AssetInputStream) stream;
		int signature;
		try {
			signature = (byte) byteStream.read();
			if (signature == 42) {
				byteStream.read(mWorkspaceBytes, 0, 4);
				final int width = Utils.byteArrayToInt(mWorkspaceBytes);
				byteStream.read(mWorkspaceBytes, 0, 4);
				final int height = Utils.byteArrayToInt(mWorkspaceBytes);

				final int totalTiles = width * height;
				final int bytesRemaining = byteStream.available();
				assert bytesRemaining >= totalTiles;
				if (bytesRemaining >= totalTiles) {
					mTilesArray = new int[width][height];
					mRowCount = height;
					mColCount = width;
					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							mTilesArray[x][y] = (byte) byteStream.read();
						}
					}
					success = true;
				}
			}

		} catch (IOException e) {
			// TODO: figure out the best way to deal with this. Assert?
		}

		return success;
	}

	protected void calculateSkips() {
		int emptyTileCount = 0;
		for (int y = mRowCount - 1; y >= 0; y--) {
			for (int x = mColCount - 1; x >= 0; x--) {
				if (mTilesArray[x][y] < 0) {
					emptyTileCount++;
					mTilesArray[x][y] = -emptyTileCount;
				} else {
					emptyTileCount = 0;
				}
			}
		}
	}

	public final int getWidth() {
		return mColCount;
	}

	public final int getHeight() {
		return mRowCount;
	}

	public final int[][] getTiles() {
		return mTilesArray;
	}

}
