package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;

public class CreateActivityBlockerProcess extends EntityProcess
{
	private Class<?> componentClass;

	public CreateActivityBlockerProcess(Entity entity, Class<?> componentClass)
	{
		super(entity);
		this.componentClass = componentClass;
	}

	@Override
	public boolean process()
	{
		ActivityBlockingComponent activityBlockingComponent = entity.getComponent(ActivityBlockingComponent.class);
		if (activityBlockingComponent != null)
		{
			activityBlockingComponent.addActivityBlocker(componentClass);
		}
		return true;
	}

}
