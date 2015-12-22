package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;
import java.util.UUID;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.ProcessQueueComponent;
import com.custardgames.sudokil.entities.ecs.processes.ConnectProcess;
import com.custardgames.sudokil.events.ProcessEvent;
import com.custardgames.sudokil.events.commands.WiredConnectEvent;
import com.custardgames.sudokil.events.map.PingCellEvent;
import com.custardgames.sudokil.managers.EventManager;

public class WiredConnectionSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<EntityComponent> entityComponents;
	private ComponentMapper<PositionComponent> positionComponents;

	@SuppressWarnings("unchecked")
	public WiredConnectionSystem()
	{
		super(Aspect.all(EntityComponent.class, PositionComponent.class, ProcessQueueComponent.class));

		EventManager.get_instance().register(WiredConnectEvent.class, this);
	}

	@Override
	protected void process(Entity entity)
	{

	}

	public void connectCommand(UUID consoleUUID, String connectingWith)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			EntityComponent entityComponent = entityComponents.get(entities.get(x));
			PositionComponent positionComponent = positionComponents.get(entities.get(x));

			if (entityComponent.getId().equals(connectingWith))
			{
				PingCellEvent pingCell = ((PingCellEvent) EventManager.get_instance()
						.broadcastInquiry(new PingCellEvent(entities.get(x),
								(int) Math.cos(Math.toRadians(positionComponent.getAngle())),
								(int) Math.sin(Math.toRadians(positionComponent.getAngle())))));
				if (pingCell != null)
				{
					Entity targetEntity = pingCell.getCellEntity();
					if (targetEntity != null)
					{
						ConnectProcess connectProcess = new ConnectProcess(consoleUUID, entities.get(x), targetEntity);
						EventManager.get_instance().broadcast(new ProcessEvent(connectingWith, connectProcess));
					}
				}
			}
		}
	}

	public void handleWiredConnectEvent(WiredConnectEvent event)
	{
		connectCommand(event.getConsoleUUID(), event.getOwner());
	}

}
