package com.custardgames.sudokil.entities.ecs.components.lights;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public abstract class LightComponent extends Component
{
	private float red, green, blue, alpha;
	private float xCo, yCo;
	private float distance;

	public Color getColor()
	{
		return new Color(red, green, blue, alpha);
	}
	
	public float getRed()
	{
		return red;
	}

	public void setRed(float red)
	{
		this.red = red;
	}

	public float getGreen()
	{
		return green;
	}

	public void setGreen(float green)
	{
		this.green = green;
	}

	public float getBlue()
	{
		return blue;
	}

	public void setBlue(float blue)
	{
		this.blue = blue;
	}

	public float getAlpha()
	{
		return alpha;
	}

	public void setAlpha(float alpha)
	{
		this.alpha = alpha;
	}

	public float getxCo()
	{
		return xCo;
	}

	public void setxCo(float xCo)
	{
		this.xCo = xCo;
	}

	public float getyCo()
	{
		return yCo;
	}

	public void setyCo(float yCo)
	{
		this.yCo = yCo;
	}

	public float getDistance()
	{
		return distance;
	}

	public void setDistance(float distance)
	{
		this.distance = distance;
	}

}
