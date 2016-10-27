package com.custardgames.sudokil.entities.ecs.components.lights;

public class ConeLightComponent extends LightComponent
{
	private float directionDegree, coneDegree;

	public ConeLightComponent()
	{
		super();
	}
	
	public float getDirectionDegree()
	{
		return directionDegree;
	}

	public void setDirectionDegree(float directionDegree)
	{
		this.directionDegree = directionDegree;
	}

	public float getConeDegree()
	{
		return coneDegree;
	}

	public void setConeDegree(float coneDegree)
	{
		this.coneDegree = coneDegree;
	}
	
}
