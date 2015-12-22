package com.custardgames.sudokil.entities.ecs.components;

import java.util.LinkedList;
import java.util.Queue;

import com.artemis.Component;
import com.custardgames.sudokil.entities.ecs.processes.EntityProcess;

public class ProcessQueueComponent extends Component
{
	private Queue<EntityProcess> queue;
	final private int limit = 100;

	public ProcessQueueComponent()
	{
		queue = new LinkedList<EntityProcess>();
	}

	public Queue<EntityProcess> getQueue()
	{
		return queue;
	}

	public void setQueue(Queue<EntityProcess> queue)
	{
		this.queue = queue;
	}

	public void addToQueue(EntityProcess process)
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

	public void clearQueue()
	{
		if (!queue.isEmpty())
		{
			EntityProcess currentProcess = queue.peek();
			queue.clear();

			if (currentProcess != null)
			{
				queue.add(currentProcess);
			}
		}
	}

	public void process()
	{
		if (!queue.isEmpty())
		{
			if (queue.peek().process())
			{
				queue.remove();
			}
		}
	}

}
