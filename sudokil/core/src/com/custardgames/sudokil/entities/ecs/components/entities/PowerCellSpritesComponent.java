package com.custardgames.sudokil.entities.ecs.components.entities;

import com.artemis.Component;

public class PowerCellSpritesComponent extends Component
{
	private String normal;
	private String lifted;

	public PowerCellSpritesComponent()
	{
		normal = "images/entities/power_cell.png";
		lifted = "images/entities/power_cell_levitation.png";
	}

	public String getNormal()
	{
		return normal;
	}

	public void setNormal(String normal)
	{
		this.normal = normal;
	}

	public String getLifted()
	{
		return lifted;
	}

	public void setLifted(String lifted)
	{
		this.lifted = lifted;
	}
}
