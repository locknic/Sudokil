package com.custardgames.sudokil.entities.ecs.components.current;

import com.artemis.Component;

public class CurrentGeneratorComponent extends Component
{
	private boolean generatingCurrent;
	
	public CurrentGeneratorComponent()
	{
		generatingCurrent = false;
	}

	public boolean isGeneratingCurrent()
	{
		return generatingCurrent;
	}

	public void setGeneratingCurrent(boolean generatingCurrent)
	{
		this.generatingCurrent = generatingCurrent;
	}
}
