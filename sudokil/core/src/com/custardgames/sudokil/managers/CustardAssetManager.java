package com.custardgames.sudokil.managers;

import com.badlogic.gdx.assets.AssetManager;

public class CustardAssetManager extends AssetManager
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object get(String fileName, Class type)
	{
		if (!super.isLoaded(fileName))
		{
			super.load(fileName, type);
			super.finishLoading();
		}
		return super.get(fileName);
	}
}
