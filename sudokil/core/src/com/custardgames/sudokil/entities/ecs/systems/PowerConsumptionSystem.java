package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.PowerConsumerComponent;
import com.custardgames.sudokil.entities.ecs.components.PowerGeneratorComponent;
import com.custardgames.sudokil.entities.ecs.components.PowerInputComponent;
import com.custardgames.sudokil.entities.ecs.components.PowerOutputComponent;
import com.custardgames.sudokil.events.entities.PowerStorageEvent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.events.entities.map.PingCellEvent;
import com.custardgames.sudokil.events.entities.map.RemoveFromMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class PowerConsumptionSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<PowerConsumerComponent> powerConsumerComponents;
	private ComponentMapper<PositionComponent> positionComponents;
	private ComponentMapper<ActivityBlockingComponent> activityBlockingComponents;

	private ComponentMapper<PowerInputComponent> powerInputComponents;
	private ComponentMapper<PowerOutputComponent> powerOutputComponents;
	private ComponentMapper<PowerGeneratorComponent> powerGeneratorComponents;

	@SuppressWarnings("unchecked")
	public PowerConsumptionSystem()
	{
		super(Aspect.all(PowerConsumerComponent.class, PositionComponent.class, ActivityBlockingComponent.class));

		EventManager.get_instance().register(AddToMapEvent.class, this);
		EventManager.get_instance().register(RemoveFromMapEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(AddToMapEvent.class, this);
		EventManager.get_instance().deregister(RemoveFromMapEvent.class, this);
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

	public boolean isConnectedToGenerator(Entity sourceEntity)
	{
		Array<Entity> searchEntities = new Array<Entity>();
		searchEntities.add(sourceEntity);

		for (Entity entity : searchEntities)
		{
			PowerGeneratorComponent powerGeneratorComponent = powerGeneratorComponents.get(entity);
			if (powerGeneratorComponent != null)
			{
				if (powerGeneratorComponent.isGeneratingPower())
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
		PowerInputComponent powerInputComponent = powerInputComponents.get(entity);
		if (powerInputComponent != null)
		{
			for (int x = 0; x < 5; x++)
			{
				boolean willInput = false;
				int xDir = 0;
				int yDir = 0;

				switch (x)
				{
					case 0:
						if (powerInputComponent.isCentre())
						{
							willInput = true;
						}
						break;
					case 1:
						if (powerInputComponent.isLeft())
						{
							willInput = true;
							xDir = -1;
						}
						break;
					case 2:
						if (powerInputComponent.isRight())
						{
							willInput = true;
							xDir = 1;
						}
						break;
					case 3:
						if (powerInputComponent.isDown())
						{
							willInput = true;
							yDir = -1;
						}
						break;
					case 4:
						if (powerInputComponent.isUp())
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
							PowerOutputComponent inputEntityPowerOutputComponent = inputEntity.getComponent(PowerOutputComponent.class);
							PositionComponent inputEntityPositionComponent = inputEntity.getComponent(PositionComponent.class);

							if (inputEntityPowerOutputComponent != null)
							{
								if (inputEntityPositionComponent != null)
								{
									int inputEntityXDir = (int) inputEntityPositionComponent.unOrientateDirectionX(-xDir, -yDir);
									int inputEntityYDir = (int) inputEntityPositionComponent.unOrientateDirectionY(-xDir, -yDir);

									if (inputEntityPowerOutputComponent.isOutputting(inputEntityXDir, inputEntityYDir))
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

	public void checkPowerActivityBlocker()
	{
		ImmutableBag<Entity> entities = getEntities();
		for (Entity entity : entities)
		{
			PowerConsumerComponent powerConsumerComponent = powerConsumerComponents.get(entity);
			ActivityBlockingComponent activityBlockingComponent = activityBlockingComponents.get(entity);

			if (isConnectedToGenerator(entity))
			{
				powerConsumerComponent.setPowered(true);
				if (activityBlockingComponent != null)
				{
					activityBlockingComponent.removeActivityBlocker(powerConsumerComponent.getClass());
				}
				EventManager.get_instance().broadcast(new PowerStorageEvent(entity));
			}
			else
			{
				powerConsumerComponent.setPowered(false);
				if (activityBlockingComponent != null)
				{
					activityBlockingComponent.addActivityBlocker(powerConsumerComponent.getClass());
				}
				EventManager.get_instance().broadcast(new PowerStorageEvent(entity));
			}
		}
	}

	public void handleAddToMap(AddToMapEvent event)
	{
		Entity entity = event.getEntity();
		if (entity != null)
		{
			PowerInputComponent powerInputComponent = powerInputComponents.get(entity);
			PowerOutputComponent powerOutputComponent = powerOutputComponents.get(entity);
			if (powerInputComponent != null || powerOutputComponent != null)
			{
				checkPowerActivityBlocker();
			}
		}
	}

	public void handleRemoveFromMap(RemoveFromMapEvent event)
	{
		Entity entity = event.getEntity();
		if (entity != null)
		{
			PowerInputComponent powerInputComponent = powerInputComponents.get(entity);
			PowerOutputComponent powerOutputComponent = powerOutputComponents.get(entity);
			if (powerInputComponent != null || powerOutputComponent != null)
			{
				checkPowerActivityBlocker();
			}
		}
	}

}
