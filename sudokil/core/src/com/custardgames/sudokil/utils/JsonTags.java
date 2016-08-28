package com.custardgames.sudokil.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class JsonTags
{
	private Array<JsonTagNode> tags;

	public void addTags(Json json)
	{
		for (JsonTagNode tag : tags)
		{
			try
			{
				json.addClassTag(tag.getName(), Class.forName(tag.getClassName()));
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
