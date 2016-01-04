package com.custardgames.sudokil.entities.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class ActivityBlockingComponent extends Component
{
	private Array<Component> activityBlockers;

	public ActivityBlockingComponent()
	{
		activityBlockers = new Array<Component>();
	}

	public boolean isActive()
	{
		if (activityBlockers.size > 0)
		{
			return false;
		}
		return true;
	}

	public Array<Component> getComponents()
	{
		return activityBlockers;
	}

	public void addActivityBlocker(Component component)
	{
		if (!activityBlockers.contains(component, true))
		{
			activityBlockers.add(component);
		}
	}

	public void removeActivityBlocker(Component component)
	{
		if (activityBlockers.contains(component, true))
		{
			activityBlockers.removeValue(component, true);
		}
	}

}
