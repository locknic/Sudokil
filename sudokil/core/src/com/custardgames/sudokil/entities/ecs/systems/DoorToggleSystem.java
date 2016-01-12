package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.DoorGroupComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.SpriteComponent;
import com.custardgames.sudokil.entities.ecs.processes.DoorToggleProcess;
import com.custardgames.sudokil.events.PingEntityEvent;
import com.custardgames.sudokil.events.entities.ProcessEvent;
import com.custardgames.sudokil.events.entities.commands.ToggleEvent;
import com.custardgames.sudokil.managers.EventManager;

public class DoorToggleSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<EntityComponent> entityComponents;
	private ComponentMapper<DoorGroupComponent> doorGroupComponents;

	@SuppressWarnings("unchecked")
	public DoorToggleSystem()
	{
		super(Aspect.all(EntityComponent.class, DoorGroupComponent.class, SpriteComponent.class,
				PositionComponent.class));

		EventManager.get_instance().register(ToggleEvent.class, this);
	}

	@Override
	protected void process(Entity e)
	{

	}

	private void toggleDoor(Entity entity)
	{
		DoorGroupComponent doorGroupComponent = doorGroupComponents.get(entity);

		if (doorGroupComponent.getDoorTilesEntities().size <= 0)
		{
			for (String s : doorGroupComponent.getDoorTiles())
			{
				PingEntityEvent event = (PingEntityEvent) EventManager.get_instance()
						.broadcastInquiry(new PingEntityEvent(s));

				if (event != null && event instanceof PingEntityEvent)
				{
					Entity doorTile = event.getEntity();
					DoorToggleProcess doorToggleProcess = new DoorToggleProcess(doorTile);
					EventManager.get_instance()
							.broadcast(new ProcessEvent(entity, doorToggleProcess));
				}
			}
		}

		DoorToggleProcess doorToggleProcess = new DoorToggleProcess(entity);
		EventManager.get_instance().broadcast(new ProcessEvent(entity, doorToggleProcess));
	}

	public void handleToggleEvent(ToggleEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			EntityComponent entityComponent = entityComponents.get(entities.get(x));
			String entityID = entityComponent.getId();
			if (event.getEntityName().equals(entityID))
			{
				toggleDoor(entities.get(x));
			}
		}
	}
}
