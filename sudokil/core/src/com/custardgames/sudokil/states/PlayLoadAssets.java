package com.custardgames.sudokil.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class PlayLoadAssets
{

	public AssetManager loadAssets(AssetManager assets, LevelData levelData)
	{
		for (String image : levelData.getImages())
		{
			assets.load(image, Texture.class);
		}
		assets.finishLoading();
		return assets;
	}
}
