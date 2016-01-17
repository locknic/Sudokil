package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class ActivityBlockingComponent extends Component
{
	private Array<Class<?>> activityBlockers;
	
	public ActivityBlockingComponent()
	{
		activityBlockers = new Array<Class<?>>();
	}

	public boolean isActive()
	{
		if (activityBlockers.size > 0)
		{
			return false;
		}
		return true;
	}

	public Array<Class<?>> getComponents()
	{
		return activityBlockers;
	}

	public void addActivityBlocker(Class<?> componentClass)
	{
		if (!activityBlockers.contains(componentClass, true))
		{
			activityBlockers.add(componentClass);
		}
	}

	public void removeActivityBlocker(Class<?> componentClass)
	{
		if (activityBlockers.contains(componentClass, true))
		{
			activityBlockers.removeValue(componentClass, true);
		}
	}

}
