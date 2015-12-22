package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;

public class VelocityComponent extends Component
{
	private float maxTravelVelocity;
	private double maxTurnVelocity;

	public VelocityComponent()
	{

	}

	public VelocityComponent(float maxVelocity, double maxTurnVelocity)
	{
		setMaxVelocity(maxVelocity);
		setMaxTurnVelocity(maxTurnVelocity);
	}

	public float getMaxVelocity()
	{
		return maxTravelVelocity;
	}

	public void setMaxVelocity(float maxVelocity)
	{
		this.maxTravelVelocity = maxVelocity;
	}

	public double getMaxTurnVelocity()
	{
		return maxTurnVelocity;
	}

	public void setMaxTurnVelocity(double maxTurnVelocity2)
	{
		this.maxTurnVelocity = maxTurnVelocity2;
	}
}
