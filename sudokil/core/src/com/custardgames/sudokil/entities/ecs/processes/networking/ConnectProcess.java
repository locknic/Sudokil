package com.custardgames.sudokil.entities.ecs.processes.networking;

import java.util.EventListener;
import java.util.UUID;

import com.artemis.Entity;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
import com.custardgames.sudokil.entities.ecs.processes.EntityProcess;
import com.custardgames.sudokil.events.DisposeWorldEvent;
import com.custardgames.sudokil.events.commandLine.CloseCommandLineWindowEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleConnectEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.events.entities.commands.DisconnectEvent;
import com.custardgames.sudokil.managers.EventManager;

public abstract class ConnectProcess extends EntityProcess implements EventListener
{
	protected UUID consoleUUID;
	protected Entity connectedTo;

	protected boolean disconnect;
	protected boolean triedConnecting;

	public ConnectProcess(UUID consoleUUID, Entity entity, Entity connectedTo)
	{
		super(entity);

		EventManager.get_instance().register(DisposeWorldEvent.class, this);
		EventManager.get_instance().register(CloseCommandLineWindowEvent.class, this);
		EventManager.get_instance().register(DisconnectEvent.class, this);

		this.consoleUUID = consoleUUID;
		this.connectedTo = connectedTo;
		this.disconnect = false;
		this.triedConnecting = false;
	}

	@Override
	public void dispose()
	{
		EventManager.get_instance().deregister(DisconnectEvent.class, this);
		EventManager.get_instance().deregister(CloseCommandLineWindowEvent.class, this);
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
	}

	@Override
	public boolean process()
	{
		if (!triedConnecting)
		{
			this.triedConnecting = true;

			if (!connect())
			{
				this.disconnect = true;
			}
		}
		else
		{
			ActivityBlockingComponent activityBlockingComponent = entity.getComponent(ActivityBlockingComponent.class);
			if (activityBlockingComponent != null && !activityBlockingComponent.isActive())
			{
				disconnect();
			}
			else if (connectedTo != null)
			{
				activityBlockingComponent = connectedTo.getComponent(ActivityBlockingComponent.class);
				if (activityBlockingComponent != null && !activityBlockingComponent.isActive())
				{
					disconnect();
				}
			}
		}
		return disconnect;
	}

	public void disconnect()
	{
		EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, null));
		EntityComponent entityComponent = connectedTo.getComponent(EntityComponent.class);
		if (entityComponent != null)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(consoleUUID, "Connection to " + entityComponent.getId() + " closed." ));
		}
		disconnect = true;
	}

	public abstract boolean connect();

	public void handleDisconnectEvent(DisconnectEvent event)
	{
		if (event.getOwnerUI() != null && event.getOwnerUI().equals(consoleUUID))
		{
			disconnect();
		}
		else if (event.getEntityName() != null)
		{
			EntityComponent entityComponent = entity.getComponent(EntityComponent.class);
			if (event.getEntityName().equals(entityComponent.getId()))
			{
				disconnect();
			}
		}
	}

	public void handleCloseCommandLineWindow(CloseCommandLineWindowEvent event)
	{
		for (UUID uuid : event.getUuids())
		{
			if (uuid.equals(consoleUUID))
			{
				disconnect();
			}
		}
	}

	public void handleDisposeWorld(DisposeWorldEvent event)
	{
		dispose();
	}

}
