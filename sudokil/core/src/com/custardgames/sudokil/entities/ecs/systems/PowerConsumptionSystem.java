package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.components.ActivityBlockingComponent;
import com.custardgames.sudokil.entities.ecs.components.EntityComponent;
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
	private ComponentMapper<PowerInputComponent> powerInputComponents;
	private ComponentMapper<PositionComponent> positionComponents;
	private ComponentMapper<ActivityBlockingComponent> activityBlockingComponents;

	@SuppressWarnings("unchecked")
	public PowerConsumptionSystem()
	{
		super(Aspect.all(EntityComponent.class, PowerConsumerComponent.class, PowerInputComponent.class, PositionComponent.class));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(Entity e)
	{
		// TODO Auto-generated method stub

	}

	public boolean isConnectedToGenerator(Entity entity)
	{
		Array<Entity> searchEntities = new Array<Entity>();
		searchEntities.add(entity);

		for (int x = 0; x < searchEntities.size; x++)
		{
			PowerGeneratorComponent powerGeneratorComponent = searchEntities.get(x).getComponent(PowerGeneratorComponent.class);
			if (powerGeneratorComponent != null)
			{
				if (powerGeneratorComponent.isGeneratingPower())
				{
					return true;
				}
			}
			findGeneratorConnections(searchEntities.get(x), searchEntities);
		}
		return false;
	}

	public Array<Entity> findGeneratorConnections(Entity entity, Array<Entity> entities)
	{
		PositionComponent positionComponent = positionComponents.get(entity);
		PowerInputComponent powerInputComponent = powerInputComponents.get(entity);

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
				xDir = (int) positionComponent.orientateDirectionX(xDir, yDir);
				yDir = (int) positionComponent.orientateDirectionY(xDir, yDir);
				Entity inputEntity = ((PingCellEvent) EventManager.get_instance().broadcastInquiry(new PingCellEvent(entity, xDir, yDir))).getEntity();
				if (inputEntity != null)
				{
					if (!entities.contains(inputEntity, true))
					{
						PowerOutputComponent inputEntityPowerOutputComponent = inputEntity.getComponent(PowerOutputComponent.class);
						PositionComponent inputEntityPositionComponent = inputEntity.getComponent(PositionComponent.class);

						if (inputEntityPowerOutputComponent != null)
						{
							if (inputEntityPositionComponent != null)
							{
								int inputEntityXDir = (int) inputEntityPositionComponent.orientateDirectionX(xDir, yDir);
								int inputEntityYDir = (int) inputEntityPositionComponent.orientateDirectionY(xDir, yDir);

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
			}
			else
			{
				powerConsumerComponent.setPowered(false);
				if (activityBlockingComponent != null)
				{
					activityBlockingComponent.addActivityBlocker(powerConsumerComponent.getClass());
				}
			}
		}
	}

	public void handlePowerStorage(PowerStorageEvent event)
	{
		checkPowerActivityBlocker();
	}

	public void handleAddToMap(AddToMapEvent event)
	{
		Entity newEntity = event.getEntity();
		if (newEntity != null)
		{
			PowerOutputComponent newEntityPowerOutputComponent = newEntity.getComponent(PowerOutputComponent.class);
			if (newEntityPowerOutputComponent != null)
			{
				checkPowerActivityBlocker();
			}
		}
	}
	
	public void handleRemoveFromMap(RemoveFromMapEvent event)
	{
		Entity oldEntity = event.getEntity();
		if (oldEntity != null)
		{
			PowerOutputComponent oldEntityPowerOutputComponent = oldEntity.getComponent(PowerOutputComponent.class);
			if (oldEntityPowerOutputComponent != null)
			{
				checkPowerActivityBlocker();
			}
		}
	}

}
