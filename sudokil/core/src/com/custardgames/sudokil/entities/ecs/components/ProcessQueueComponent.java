package com.custardgames.sudokil.entities.ecs.components;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.entities.ecs.processes.EntityProcess;

public class ProcessQueueComponent extends Component
{
	private Queue<EntityProcess> queue;
	private Array<EntityProcess> background;

	private int limit = 100;

	public ProcessQueueComponent()
	{
		queue = new LinkedList<EntityProcess>();
		background = new Array<EntityProcess>();
	}

	public void addToQueue(EntityProcess process)
	{
		if (process.isBackgroundProcess())
		{
			if (background.size <= limit)
			{
				background.add(process);
			}
			else
			{
				System.out.println("ENTITY QUEUE TOO LONG");
			}
		}
		else
		{
			if (queue.size() <= limit)
			{
				queue.add(process);
			}
			else
			{
				System.out.println("ENTITY QUEUE TOO LONG");
			}
		}
	}
	
	public void addToFrontQueue(EntityProcess process)
	{
		if (process.isBackgroundProcess())
		{
			if (background.size <= limit)
			{
				background.add(process);
			}
			else
			{
				System.out.println("ENTITY QUEUE TOO LONG");
			}
		}
		else
		{
			Queue<EntityProcess> newQueue = new LinkedList<EntityProcess>();
			if (queue.size() <= limit)
			{
				newQueue.add(process);
				newQueue.addAll(queue);
				queue = newQueue;
			}
			else
			{
				System.out.println("ENTITY QUEUE TOO LONG");
			}
		}
	}

	public void clearQueue()
	{
		if (!queue.isEmpty())
		{
			EntityProcess currentProcess = queue.poll();

			while (!queue.isEmpty())
			{
				queue.poll().dispose();
			}

			if (currentProcess != null)
			{
				queue.add(currentProcess);
			}
		}
		if (background.size > 0)
		{
			for(EntityProcess process : background)
			{
				process.dispose();
			}
			background.clear();
		}
	}

	public void process()
	{
		if (!queue.isEmpty())
		{
			if (queue.peek().process())
			{
				queue.poll().dispose();
			}
		}
		Iterator<EntityProcess> iterator = background.iterator();
		while (iterator.hasNext())
		{
			EntityProcess next = iterator.next();
			if (next.process())
			{
				next.dispose();
				iterator.remove();
			}
		}
	}

}
