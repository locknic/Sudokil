package com.custardgames.sudokil.entities.ecs.factories;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.entities.map.AddToMapEvent;
import com.custardgames.sudokil.managers.EventManager;

public class EntityFactoryJSON
{
	private World artemisWorld;

	public EntityFactoryJSON(World artemisWorld)
	{
		this.artemisWorld = artemisWorld;
	}

	public void createEntities(String fileLocation)
	{
		Json json = new Json();
		EntityHolder componentFactory = json.fromJson(EntityHolder.class, Gdx.files.internal(fileLocation));
		Array<Array<Component>> entities = componentFactory.getComponents();
		for (Array<Component> e : entities)
		{
			Entity entity = artemisWorld.createEntity();
			for (Component c : e)
			{
				entity.edit().add(c);
				if (c instanceof PositionComponent)
				{
					EventManager.get_instance().broadcast(new AddToMapEvent(entity));
				}
			}
		}
	}

}
