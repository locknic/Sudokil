package com.custardgames.sudokil;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerBody
{
	public Body getPlayerBody(World world)
	{
		Body playerBody;
		BodyDef bDef = new BodyDef();
		bDef.position.set(5, 0);
		bDef.type = BodyType.DynamicBody;
		bDef.fixedRotation = false;
		playerBody = world.createBody(bDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(20);

		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.restitution = 0.5f;
		fDef.friction = 10;
		fDef.density = 1;
		playerBody.createFixture(fDef).setUserData("player");

		playerBody.setLinearDamping(5);
		playerBody.setAngularDamping(5);

		return playerBody;
	}
}
