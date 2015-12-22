package com.custardgames.sudokil.managers;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import com.artemis.Entity;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.events.map.AddToMapEvent;
import com.custardgames.sudokil.events.map.PingCellEvent;
import com.custardgames.sudokil.events.map.RemoveFromMapEvent;
import com.custardgames.sudokil.events.map.RequestMoveEvent;

public class MapManager implements EventListener
{
	private TiledMap map;
	private int tileWidth;
	private int tileHeight;
	private Map<Cell, Entity> tiledEntities;

	public MapManager(TiledMap map)
	{
		EventManager.get_instance().register(AddToMapEvent.class, this);
		EventManager.get_instance().register(RemoveFromMapEvent.class, this);
		EventManager.get_instance().register(RequestMoveEvent.class, this);
		EventManager.get_instance().register(PingCellEvent.class, this);

		this.map = map;

		tileWidth = map.getProperties().get("tilewidth", Integer.class);
		tileHeight = map.getProperties().get("tileheight", Integer.class);
		tiledEntities = new HashMap<Cell, Entity>();
	}

	public void addToMap(Entity entity, int x, int y)
	{
		Cell tiledEntity = new Cell();
		tiledEntities.put(tiledEntity, entity);
		((TiledMapTileLayer) map.getLayers().get("objects")).setCell(x, y, tiledEntity);
	}

	public void addToMap(Entity entity)
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);
		addToMap(entity, (int) (position.getX() / tileWidth), (int) (position.getY() / tileHeight));
	}

	public void removeFromMap(Entity entity, int x, int y)
	{
		Cell tileEntity = ((TiledMapTileLayer) map.getLayers().get("objects")).getCell(x, y);
		tiledEntities.remove(tileEntity);
		((TiledMapTileLayer) map.getLayers().get("objects")).setCell(x, y, null);
	}

	public void removeFromMap(Entity entity)
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);
		removeFromMap(entity, (int) (position.getX() / tileWidth), (int) (position.getY() / tileHeight));
	}

	public boolean requestMove(Entity entity, int xDir, int yDir)
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);

		int xPosition = (int) (position.getX() / tileWidth) + xDir;
		int yPosition = (int) (position.getY() / tileHeight) + yDir;

		if (((TiledMapTileLayer) map.getLayers().get("floor")).getCell(xPosition, yPosition) != null)
		{
			if (getCellEntity(xPosition, yPosition) == null)
			{
				removeFromMap(entity);
				addToMap(entity, xPosition, yPosition);
				return true;
			}
		}

		return false;
	}

	public Entity getCellEntity(int xCo, int yCo)
	{
		Cell tiledEntity = ((TiledMapTileLayer) map.getLayers().get("objects")).getCell(xCo, yCo);
		return tiledEntities.get(tiledEntity);
	}

	public Entity getCellEntity(Entity entity, int xDir, int yDir)
	{
		PositionComponent position = entity.getComponent(PositionComponent.class);

		int xPosition = (int) (position.getX() / tileWidth) + xDir;
		int yPosition = (int) (position.getY() / tileHeight) + yDir;

		return getCellEntity(xPosition, yPosition);
	}

	public void handleAddToMapEvent(AddToMapEvent event)
	{
		addToMap(event.getEntity());
	}

	public void handleRemoveFromMapEvent(RemoveFromMapEvent event)
	{
		removeFromMap(event.getEntity());
	}

	public RequestMoveEvent handleInquiryRequestMoveEvent(RequestMoveEvent event)
	{
		if (requestMove(event.getEntity(), event.getxDir(), event.getyDir()))
		{
			event.setAllowedMove(true);
			event.setxDir(event.getxDir() * tileWidth);
			event.setyDir(event.getyDir() * tileHeight);
			return event;
		}
		else
		{
			return event;
		}
	}

	public PingCellEvent handleInquiryPingEntityEvent(PingCellEvent event)
	{
		event.setCellEntity(getCellEntity(event.getOwnerEntity(), event.getxDir(), event.getyDir()));
		return event;
	}
}
