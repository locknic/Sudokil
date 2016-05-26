package com.custardgames.sudokil.utils;

import com.badlogic.gdx.utils.Array;

public class CircularArray<T> extends Array<T>
{
	private int maxSize;
	private int currentSize;

	public CircularArray(int maxSize)
	{
		super(true, maxSize);
		this.maxSize = maxSize;
		this.currentSize = 0;
	}

	public int getCurrentSize()
	{
		return currentSize;
	}

	@Override
	public void add(T value)
	{
		if (size >= maxSize)
		{
			pop();
		}
		else
		{
			currentSize++;
		}
		insert(0, value);
	}
}
