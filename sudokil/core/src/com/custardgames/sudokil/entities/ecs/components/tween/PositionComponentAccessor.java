package com.custardgames.sudokil.entities.ecs.components.tween;

import com.custardgames.sudokil.entities.ecs.components.PositionComponent;

import aurelienribon.tweenengine.TweenAccessor;

public class PositionComponentAccessor implements TweenAccessor<PositionComponent>
{
	public static final int X = 0;
	public static final int Y = 1;
	public static final int WIDTH = 2;
	public static final int HEIGHT = 3;
	public static final int ANGLE = 4;
	
	@Override
	public int getValues(PositionComponent target, int tweenType, float[] returnValues)
	{
		switch(tweenType)
		{
			case X:
				returnValues[0] = target.getX();
				return 1;
			case Y:
				returnValues[0] = target.getY();
				return 1;
			case ANGLE:
				returnValues[0] = target.getAngle();
				return 1;
			default:
				assert false;
				return -1;
		}
	}

	@Override
	public void setValues(PositionComponent target, int tweenType, float[] returnValues)
	{
		switch(tweenType)
		{
			case X:
				target.setX(returnValues[0]);
				break;
			case Y:
				target.setY(returnValues[0]);
				break;
			case ANGLE:
				target.setAngle(returnValues[0]);
				break;
			default:
				assert false;
		}
	}

}
