package com.custardgames.sudokil.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class PlayLoadAssets
{

	public AssetManager loadAssets(AssetManager assets)
	{
		assets.load("images/player/robot1.png", Texture.class);
		assets.load("images/entities/door-on-1.png", Texture.class);
		assets.load("images/entities/door-on-2.png", Texture.class);
		assets.load("images/entities/door-off.png", Texture.class);
		assets.load("images/entities/computer-off.png", Texture.class);
		assets.load("images/entities/computer-on.png", Texture.class);
		assets.load("images/entities/crate.png", Texture.class);
		assets.load("images/entities/generator.png", Texture.class);
		assets.load("images/entities/wire.png", Texture.class);
		assets.load("images/entities/wire2.png", Texture.class);
		assets.finishLoading();
		return assets;
	}
}
