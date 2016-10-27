package com.custardgames.sudokil.entities.ecs.components.entities.robot;

import com.artemis.Component;

public class RobotSpritesComponent extends Component
{
	private String robotUp;
	private String robotDown;
	private String robotLeft;
	private String robotRight;
	
	public RobotSpritesComponent()
	{
		robotUp = "images/player/robot_back.png";
		robotDown = "images/player/robot_front.png";
		robotLeft = "images/player/robot_left.png";
		robotRight = "images/player/robot_right.png";
	}

	public String getRobotUp()
	{
		return robotUp;
	}

	public String getRobotDown()
	{
		return robotDown;
	}

	public String getRobotLeft()
	{
		return robotLeft;
	}

	public String getRobotRight()
	{
		return robotRight;
	}
	
	
}
