package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.ChangedMapEvent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class MapRefresherSystem extends EntityProcessingSystem implements EventListener
{

	@SuppressWarnings("unchecked")
	public MapRefresherSystem()
	{
		super(Aspect.all(PositionComponent.class));
		EventManager.get_instance().register(ChangedMapEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(ChangedMapEvent.class, this);
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity e)
	{
		// TODO Auto-generated method stub

	}

	public void handleChangedMap(ChangedMapEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			EventManager.get_instance().broadcast(new AddToMapEvent(entity));
		}
	}
}
