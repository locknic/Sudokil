package com.custardgames.sudokil.entities.ecs.processes;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;

public class CreateActivityBlockerProcess extends EntityProcess
{
	private Class<?> componentClass;
	private ActivityBlockingComponent activityBlockingComponent;
	
	public CreateActivityBlockerProcess(Entity entity, Class<?> componentClass)
	{
		super(entity);
		this.componentClass = componentClass;
		
		activityBlockingComponent = entity.getComponent(ActivityBlockingComponent.class);
	}

	@Override
	public boolean preProcess()
	{
		return activityBlockingComponent != null;
	}
	
	@Override
	public boolean process()
	{
		activityBlockingComponent.addActivityBlocker(componentClass);

		return true;
	}

	@Override
	public void postProcess()
	{
		// TODO Auto-generated method stub
		
	}

}
