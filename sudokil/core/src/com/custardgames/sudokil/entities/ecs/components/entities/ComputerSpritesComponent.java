package com.custardgames.sudokil.entities.ecs.components.entities;

import com.artemis.Component;

public class ComputerSpritesComponent extends Component
{
	private String frontOff;
	private String frontOn;
	private String leftOff;
	private String leftOn;
	private String rightOff;
	private String rightOn;
	private String backOff;
	private String backOn;

	public ComputerSpritesComponent()
	{
		frontOff = "images/entities/pc_front_off.png";
		frontOn = "images/entities/pc_front_on.png";
		leftOff = "images/entities/pc_left_off.png";
		leftOn = "images/entities/pc_left_on.png";
		rightOff = "images/entities/pc_right_off.png";
		rightOn = "images/entities/pc_right_on.png";
		backOff = "images/entities/pc_back_off.png";
		backOn = "images/entities/pc_back_on.png";
	}

	public String getFrontOff()
	{
		return frontOff;
	}

	public String getFrontOn()
	{
		return frontOn;
	}

	public String getLeftOff()
	{
		return leftOff;
	}

	public String getLeftOn()
	{
		return leftOn;
	}

	public String getRightOff()
	{
		return rightOff;
	}

	public String getRightOn()
	{
		return rightOn;
	}

	public String getBackOff()
	{
		return backOff;
	}

	public String getBackOn()
	{
		return backOn;
	}

}
