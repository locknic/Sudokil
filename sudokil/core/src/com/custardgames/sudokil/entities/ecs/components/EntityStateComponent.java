package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class EntityStateComponent extends Component
{
	public enum EntityStates
	{
		MOVING, 
		TURNING, 
		LIFTING, 
		HOLDING,
		LOWERING
	}
	
	private Array<EntityStates> currentStates;

	public EntityStateComponent()
	{
		currentStates = new Array<EntityStates>();
	}

	public void pushState(EntityStates state)
	{
		if (!currentStates.contains(state, false))
		{
			currentStates.add(state);
		}
	}

	public void popState(EntityStates state)
	{
		if (currentStates.contains(state, false))
		{
			currentStates.removeValue(state, false);
		}
	}

	public boolean hasState(EntityStates state)
	{
		return currentStates.contains(state, false);
	}
}
