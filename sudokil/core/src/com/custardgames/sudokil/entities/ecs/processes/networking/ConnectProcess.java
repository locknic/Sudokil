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
import com.custardgames.sudokil.utils.Streams;

public abstract class ConnectProcess extends EntityProcess implements EventListener
{
	protected Streams consoleUUID;
	protected Entity connectedTo;

	protected boolean disconnect;

	private ActivityBlockingComponent activityBlockingComponent;
	private ActivityBlockingComponent connectedActivityBlockingComponent;

	public ConnectProcess(Streams consoleUUID, Entity entity, Entity connectedTo)
	{
		super(entity);

		EventManager.get_instance().register(DisposeWorldEvent.class, this);
		EventManager.get_instance().register(CloseCommandLineWindowEvent.class, this);
		EventManager.get_instance().register(DisconnectEvent.class, this);

		this.consoleUUID = consoleUUID;
		this.connectedTo = connectedTo;

		activityBlockingComponent = entity.getComponent(ActivityBlockingComponent.class);

		this.disconnect = false;
	}

	@Override
	public void dispose()
	{
		EventManager.get_instance().deregister(DisconnectEvent.class, this);
		EventManager.get_instance().deregister(CloseCommandLineWindowEvent.class, this);
		EventManager.get_instance().deregister(DisposeWorldEvent.class, this);
	}

	@Override
	public boolean preProcess()
	{
		boolean canContinue = connect();
		
		if (connectedTo != null)
		{
			connectedActivityBlockingComponent = connectedTo.getComponent(ActivityBlockingComponent.class);
		}
		
		return canContinue && activityBlockingComponent != null && connectedActivityBlockingComponent != null;
	}

	@Override
	public boolean process()
	{
		if (!activityBlockingComponent.isActive() || !connectedActivityBlockingComponent.isActive())
		{
			hardDisconnect();
		}

		return disconnect;
	}

	public void disconnect()
	{
		EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, null));
		EntityComponent entityComponent = connectedTo.getComponent(EntityComponent.class);
		if (entityComponent != null)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(consoleUUID, "Connection to " + entityComponent.getId() + " closed."));
		}
		disconnect = true;
	}

	public void hardDisconnect()
	{
		EventManager.get_instance().broadcast(new ConsoleConnectEvent(consoleUUID, null));
		EntityComponent entityComponent = connectedTo.getComponent(EntityComponent.class);
		if (entityComponent != null)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(consoleUUID, "ERROR! Connection to " + entityComponent.getId() + " was lost."));
		}
		disconnect = true;
	}

	public abstract boolean connect();

	public void handleDisconnectEvent(DisconnectEvent event)
	{
		if (event.getOwnerUI() != null && event.getOwnerUI().getOwner().equals(consoleUUID.getOwner()))
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
			if (uuid.equals(consoleUUID.getOwner()))
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
