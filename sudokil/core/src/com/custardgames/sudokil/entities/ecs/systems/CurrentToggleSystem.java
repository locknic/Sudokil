package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentConsumerComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentGeneratorComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentToggleComponent;
import com.custardgames.sudokil.events.entities.commands.ToggleEvent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.events.entities.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class CurrentToggleSystem extends EntityProcessingSystem implements EventListener
{
	ComponentMapper<CurrentGeneratorComponent> currentGeneratorComponents;
	ComponentMapper<CurrentConsumerComponent> currentConsumerComponents;
	
	ComponentMapper<EntityComponent> entityComponents;
	
	@SuppressWarnings("unchecked")
	public CurrentToggleSystem()
	{
		super(Aspect.all(EntityComponent.class, CurrentGeneratorComponent.class, CurrentToggleComponent.class));
		EventManager.get_instance().register(ToggleEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();
		EventManager.get_instance().deregister(ToggleEvent.class, this);
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity e)
	{
		
	}
	
	public void toggle(Entity entity)
	{
		CurrentGeneratorComponent currentGeneratorComponent = currentGeneratorComponents.get(entity);
		if(currentGeneratorComponent.isGeneratingCurrent())
		{
			currentGeneratorComponent.setGeneratingCurrent(false);
		}
		else
		{
			currentGeneratorComponent.setGeneratingCurrent(true);
		}
		EventManager.get_instance().broadcast(new RemoveFromMapEvent(entity));
		EventManager.get_instance().broadcast(new AddToMapEvent(entity));
	}
	
	public void handleToggle(ToggleEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EntityComponent entityComponent = entityComponents.get(entity);
			if (event.getEntityName().equals(entityComponent.getId()))
			{
				toggle(entity);
			}
		}
	}

}
