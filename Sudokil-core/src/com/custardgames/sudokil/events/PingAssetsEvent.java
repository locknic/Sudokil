package com.custardgames.sudokil.events;

public class PingAssetsEvent
{
	private String assetLocation;
	private Object asset;

	public PingAssetsEvent(String assetLocation)
	{
		setAssetLocation(assetLocation);
	}

	public String getAssetLocation()
	{
		return assetLocation;
	}

	public void setAssetLocation(String assetLocation)
	{
		this.assetLocation = assetLocation;
	}

	public Object getAsset()
	{
		return asset;
	}

	public void setAsset(Object asset)
	{
		this.asset = asset;
	}

}
