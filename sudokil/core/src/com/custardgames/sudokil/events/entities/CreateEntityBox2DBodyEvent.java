package com.custardgames.sudokil.events.entities;

import com.custardgames.sudokil.entities.ecs.components.Box2DBodyComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.BaseEvent;

public class CreateEntityBox2DBodyEvent extends BaseEvent
{
	private PositionComponent positionComponent;
	private Box2DBodyComponent bodyComponent;
	
	public CreateEntityBox2DBodyEvent()
	{
		
	}
	
	public CreateEntityBox2DBodyEvent(PositionComponent positionComponent, Box2DBodyComponent bodyComponent)
	{
		this.positionComponent = positionComponent;
		this.bodyComponent = bodyComponent;
	}
	
	public PositionComponent getPositionComponent()
	{
		return positionComponent;
	}
	public void setPositionComponent(PositionComponent positionComponent)
	{
		this.positionComponent = positionComponent;
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
