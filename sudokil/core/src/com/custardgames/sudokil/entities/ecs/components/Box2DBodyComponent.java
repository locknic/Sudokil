package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class Box2DBodyComponent extends Component
{
	private Body body;
	
	public Box2DBodyComponent()
	{
		
	}

	public Body getBody()
	{
		return body;
	}

	public void setBody(Body body)
	{
		this.body = body;
	}
}
