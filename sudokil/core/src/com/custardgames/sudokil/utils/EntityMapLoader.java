package com.custardgames.sudokil.utils;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class EntityMapLoader
{
	public static EntityHolder createEntityHolderJsonFromMap(Map map)
	{
		Json json = new Json();
		JsonTags jsonTags = json.fromJson(JsonTags.class, Gdx.files.internal("data/tags.json"));
		jsonTags.addTags(json);

		String writing = "{\nentities :\n[\n";

		MapLayers mapLayers = map.getLayers();
		for (MapLayer layer : mapLayers)
		{
			MapObjects mapObjects = layer.getObjects();
			for (MapObject object : mapObjects)
			{
				MapProperties properties = object.getProperties();
				Array<String> foundComponents = new Array<String>();
				HashMap<String, Array<String>> foundVariables = new HashMap<String, Array<String>>();
				Iterator<String> it = properties.getKeys();

				while (it.hasNext())
				{
					String name = it.next();
					if (!name.contains("."))
					{
						boolean value = Boolean.valueOf(properties.get(name).toString());
						if (value)
						{
							foundComponents.add(name);
						}
					}
					else
					{
						String[] splitName = name.split("\\.");
						if (splitName.length == 2)
						{
							if (!foundVariables.containsKey(splitName[0]))
							{
								foundVariables.put(splitName[0], new Array<String>());
							}
							foundVariables.get(splitName[0]).add(splitName[1] + " : " + properties.get(name) + ",\n");
						}
					}
				}

				if (foundComponents.size > 0)
				{
					writing += "[\n";
					for (String foundComponent : foundComponents)
					{
						writing += "{\n";
						writing += "class : " + foundComponent + ",\n";

						if (foundComponent.equals("EntityComponent"))
						{
							writing += "id : " + object.getName() + ", \n";
						}
						else if (foundComponent.equals("PositionComponent"))
						{
							writing += "x : " + getNumberString(object.getProperties().get("x")) + ", \n";
							writing += "y : " + getNumberString(object.getProperties().get("y")) + ", \n";
							writing += "width : " + getNumberString(object.getProperties().get("width")) + ", \n";
							writing += "height : " + getNumberString(object.getProperties().get("height")) + ", \n";
						}

						if (foundVariables.containsKey(foundComponent))
						{
							for (String foundVariable : foundVariables.get(foundComponent))
							{
								writing += foundVariable;
							}
						}
						writing += "},\n";
					}
					writing += "]\n";
				}
			}
		}
		writing += "]\n}";
		return json.fromJson(EntityHolder.class, writing);
	}

	private static String getNumberString(Object input)
	{
		if (input == null || input.equals(""))
		{
			return "0";
		}
		else
		{
			return input.toString();
		}
	}
}
