package com.custardgames.sudokil.events.entities;

import com.custardgames.sudokil.entities.ecs.components.Box2DBodyComponent;
import com.custardgames.sudokil.entities.ecs.components.lights.LightComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class CreateEntityLightEvent extends BaseEvent
{
	private LightComponent lightComponent;
	private Box2DBodyComponent bodyComponent;

	public CreateEntityLightEvent()
	{
		
	}
	
	public CreateEntityLightEvent(LightComponent lightComponent, Box2DBodyComponent bodyComponent)
	{
		this.lightComponent = lightComponent;
		this.bodyComponent = bodyComponent;
	}
	
	public LightComponent getLightComponent()
	{
		return lightComponent;
	}

	public void setLightComponent(LightComponent lightComponent)
	{
		this.lightComponent = lightComponent;
	}

	public Box2DBodyComponent getBodyComponent()
	{
		return bodyComponent;
	}

	public void setBodyComponent(Box2DBodyComponent bodyComponent)
	{
		this.bodyComponent = bodyComponent;
	}

}
