package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;

public class RemoveActivityBlockerProcess extends EntityProcess
{
	private Class<?> componentClass;

	public RemoveActivityBlockerProcess(Entity entity, Class<?> componentClass)
	{
		super(entity);
		this.componentClass = componentClass;
	}

	@Override
	public boolean preProcess()
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean process()
	{
		ActivityBlockingComponent activityBlockingComponent = entity.getComponent(ActivityBlockingComponent.class);
		if (activityBlockingComponent != null && activityBlockingComponent.getComponents().contains(componentClass, false))
		{
			activityBlockingComponent.removeActivityBlocker(componentClass);
		}
		return true;
	}

	@Override
	public void postProcess()
	{
		// TODO Auto-generated method stub
		
	}

}
