package com.custardgames.sudokil.entities.ecs.systems.current;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentConsumerComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentGeneratorComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentInputComponent;
import com.custardgames.sudokil.entities.ecs.components.current.CurrentOutputComponent;
import com.custardgames.sudokil.events.MapEntitiesLoadedEvent;
import com.custardgames.sudokil.events.entities.CurrentStorageEvent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.events.entities.map.PingCellEvent;
import com.custardgames.sudokil.events.entities.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class CurrentConsumptionSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<CurrentConsumerComponent> currentConsumerComponents;
	private ComponentMapper<PositionComponent> positionComponents;

	private ComponentMapper<CurrentInputComponent> currentInputComponents;
	private ComponentMapper<CurrentGeneratorComponent> currentGeneratorComponents;

	@SuppressWarnings("unchecked")
	public CurrentConsumptionSystem()
	{
		super(Aspect.all(CurrentConsumerComponent.class, PositionComponent.class));
		EventManager.get_instance().register(AddToMapEvent.class, this);
		EventManager.get_instance().register(RemoveFromMapEvent.class, this);
		EventManager.get_instance().register(MapEntitiesLoadedEvent.class, this);
	}

	@Override
	public void dispose()
	{
		EventManager.get_instance().deregister(AddToMapEvent.class, this);
		EventManager.get_instance().deregister(RemoveFromMapEvent.class, this);
		EventManager.get_instance().deregister(MapEntitiesLoadedEvent.class, this);
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

	public boolean isConnectedToGenerator(Entity sourceEntity)
	{
		Array<Entity> searchEntities = new Array<Entity>();
		searchEntities.add(sourceEntity);

		for (Entity entity : searchEntities)
		{
			CurrentGeneratorComponent currentGeneratorComponent = currentGeneratorComponents.get(entity);
			if (currentGeneratorComponent != null)
			{
				if (currentGeneratorComponent.isGeneratingCurrent())
				{
					return true;
				}
			}
			findGeneratorConnections(entity, searchEntities);
		}
		return false;
	}

	public Array<Entity> findGeneratorConnections(Entity entity, Array<Entity> entities)
	{
		PositionComponent positionComponent = positionComponents.get(entity);
		CurrentInputComponent currentInputComponent = currentInputComponents.get(entity);
		if (currentInputComponent != null)
		{
			for (int x = 0; x < 5; x++)
			{
				boolean willInput = false;
				int xDir = 0;
				int yDir = 0;

				switch (x)
				{
					case 0:
						if (currentInputComponent.isCentre())
						{
							willInput = true;
						}
						break;
					case 1:
						if (currentInputComponent.isLeft())
						{
							willInput = true;
							xDir = -1;
						}
						break;
					case 2:
						if (currentInputComponent.isRight())
						{
							willInput = true;
							xDir = 1;
						}
						break;
					case 3:
						if (currentInputComponent.isDown())
						{
							willInput = true;
							yDir = -1;
						}
						break;
					case 4:
						if (currentInputComponent.isUp())
						{
							willInput = true;
							yDir = 1;
						}
						break;
					default:
						willInput = false;
						break;
				}

				if (willInput)
				{
					float tmpX = xDir;
					float tmpY = yDir;
					xDir = (int) positionComponent.orientateDirectionX(tmpX, tmpY);
					yDir = (int) positionComponent.orientateDirectionY(tmpX, tmpY);
					Entity inputEntity = ((PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity, xDir, yDir))).getCellEntity();
					if (inputEntity != null)
					{
						if (!entities.contains(inputEntity, false))
						{
							CurrentOutputComponent inputEntityCurrentOutputComponent = inputEntity.getComponent(CurrentOutputComponent.class);
							PositionComponent inputEntityPositionComponent = inputEntity.getComponent(PositionComponent.class);

							if (inputEntityCurrentOutputComponent != null)
							{
								if (inputEntityPositionComponent != null)
								{
									int inputEntityXDir = (int) inputEntityPositionComponent.unOrientateDirectionX(-xDir, -yDir);
									int inputEntityYDir = (int) inputEntityPositionComponent.unOrientateDirectionY(-xDir, -yDir);

									if (inputEntityCurrentOutputComponent.isOutputting(inputEntityXDir, inputEntityYDir))
									{
										entities.add(inputEntity);
									}
								}
							}
						}
					}
				}
			}
		}
		return entities;
	}

	public void checkCurrentStorage()
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			CurrentConsumerComponent currentConsumerComponent = currentConsumerComponents.get(entity);

			if (isConnectedToGenerator(entity))
			{
				currentConsumerComponent.setHasCurrent(true);
				EventManager.get_instance().broadcast(new CurrentStorageEvent(entity));
			}
			else
			{
				currentConsumerComponent.setHasCurrent(false);
				EventManager.get_instance().broadcast(new CurrentStorageEvent(entity));
			}
		}
	}

	public void handleAddToMap(AddToMapEvent event)
	{
		Entity entity = event.getEntity();
		if (entity != null)
		{
			checkCurrentStorage();
		}
	}

	public void handleRemoveFromMap(RemoveFromMapEvent event)
	{
		Entity entity = event.getEntity();
		if (entity != null)
		{
			checkCurrentStorage();
		}
	}

	public void handleMapLoaded(MapEntitiesLoadedEvent event)
	{
		checkCurrentStorage();
	}
}
