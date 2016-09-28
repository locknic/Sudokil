package com.custardgames.sudokil.entities.ecs.processes;

import java.util.LinkedList;
import java.util.Queue;

import com.artemis.Entity;

public class EntityProcessQueue extends EntityProcess
{
	private Queue<EntityProcess> queue;

	public EntityProcessQueue(Entity entity)
	{
		super(entity);
		queue = new LinkedList<EntityProcess>();
	}

	@Override
	public boolean process()
	{
		if (!queue.isEmpty())
		{
			if (queue.peek().process())
			{
				queue.poll().dispose();
			}
			return false;
		}
		return true;
	}

	public void addToQueue(EntityProcess process)
	{
		queue.add(process);
	}
	
	public void addAllToQueue(Queue<EntityProcess> queue2)
	{
		queue.addAll(queue2);
	}

	public Queue<EntityProcess> getQueue()
	{
		return queue;
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
	}

}
