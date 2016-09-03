package com.custardgames.sudokil.managers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class Box2dWorldManager
{
	private World world; 
	
	public Box2dWorldManager()
	{
		world = new World(new Vector2(0, 0), true);
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public void loadMap(TiledMap map)
	{
		new Box2DMapObjectParser().load(world, map);
	}
}
