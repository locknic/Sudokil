package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class CameraComponent extends Component
{
	private String targetID;
	private Component target;
	private float minX, minY, maxX, maxY;
	private float minZoom, maxZoom;
	private float targetX, targetY;
	private float targetOffsetX, targetOffsetY;
	private float panSpeed;
	private float zoomAmount;

	public CameraComponent()
	{
		minX = -1000;
		minY = -1000;
		maxX = 2000;
		maxY = 2000;
		minZoom = 0.1f;
		maxZoom = 3;
	}

	public String getTargetID()
	{
		return targetID;
	}

	public void setTargetID(String targetID)
	{
		this.targetID = targetID;
	}

	public void setTarget(Component target)
	{
		this.target = target;
	}

	public Component getTarget()
	{
		return target;
	}

	public float getTargetX()
	{
		return targetX;
	}

	public void setTargetX(float targetX)
	{
		this.targetX = targetX;
	}

	public float getTargetY()
	{
		return targetY;
	}

	public void setTargetY(float targetY)
	{
		this.targetY = targetY;
	}

	public float getTargetOffsetX()
	{
		return targetOffsetX;
	}

	public void setTargetOffsetX(float targetOffsetX)
	{
		this.targetOffsetX = targetOffsetX;
	}

	public float getTargetOffsetY()
	{
		return targetOffsetY;
	}

	public void setTargetOffsetY(float targetOffsetY)
	{
		this.targetOffsetY = targetOffsetY;
	}

	public double getPanSpeed()
	{
		return panSpeed;
	}

	public void setPanSpeed(float panSpeed)
	{
		this.panSpeed = panSpeed;
	}

	public float getZoomAmount()
	{
		return zoomAmount;
	}

	public void setZoomAmount(float zoomAmount)
	{
		this.zoomAmount = zoomAmount;
	}

	public float getMinX()
	{
		return minX;
	}

	public void setMinX(float minX)
	{
		this.minX = minX;
	}

	public float getMinY()
	{
		return minY;
	}

	public void setMinY(float minY)
	{
		this.minY = minY;
	}

	public float getMaxX()
	{
		return maxX;
	}

	public void setMaxX(float maxX)
	{
		this.maxX = maxX;
	}

	public float getMaxY()
	{
		return maxY;
	}

	public void setMaxY(float maxY)
	{
		this.maxY = maxY;
	}

	public float getMinZoom()
	{
		return minZoom;
	}

	public void setMinZoom(float minZoom)
	{
		this.minZoom = minZoom;
	}

	public float getMaxZoom()
	{
		return maxZoom;
	}

	public void setMaxZoom(float maxZoom)
	{
		this.maxZoom = maxZoom;
	}

}
