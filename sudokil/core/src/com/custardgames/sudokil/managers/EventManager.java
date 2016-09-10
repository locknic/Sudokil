package com.custardgames.sudokil.managers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;

public class EventManager
{
	private static EventManager _instance = new EventManager();
	private Map<Class<?>, Array<EventListener>> listeners;

	public EventManager()
	{
		listeners = new HashMap<Class<?>, Array<EventListener>>();
	}

	public static EventManager get_instance()
	{
		return _instance;
	}

	public void register(Class<?> eventType, EventListener listener)
	{
		if (!listeners.containsKey(eventType))
		{
			listeners.put(eventType, new Array<EventListener>());
		}

		listeners.get(eventType).add(listener);

		System.out.println("REGISTERING : " + listener + ", " + eventType);
	}

	public void deregister(Class<?> eventType, EventListener listener)
	{
		if (listeners.containsKey(eventType))
		{
			if (listeners.get(eventType).contains(listener, true))
			{
				listeners.get(eventType).removeValue(listener, true);
			}
		}
		System.out.println("DEREGISTERING : " + listener + ", " + eventType);
	}

	public void broadcast(Object event)
	{
		Array<EventListener> currentListeners = listeners.get(event.getClass());
		if (currentListeners != null)
		{
			// This is janky because some event broadcasts cause classes to
			// deregister themselves
			// which causes issues when trying to iterate in a for each
			EventListener[] currentListenersArray = currentListeners.toArray(EventListener.class);
			for (int x = 0; x < currentListenersArray.length; x++)
			{
				Method m = findMethod(currentListenersArray[x], event.getClass());
				try
				{
					// if (m != null)
					m.invoke(currentListenersArray[x], event);
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				catch (NullPointerException e)
//				{
//					e.printStackTrace();
//				}
			}
		}
		System.out.println("BROADCASTING EVENT : " + event.getClass().getSimpleName());
	}

	public Object broadcastInquiry(Object event)
	{
		Array<EventListener> currentListeners = listeners.get(event.getClass());
		if (currentListeners != null)
		{
			for (EventListener l : currentListeners)
			{
				Method m = findInquiry(l, event.getClass());
				try
				{
					return m.invoke(l, event);
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private Method findMethod(EventListener listener, Class<?> eventType)
	{
		Method[] methods = listener.getClass().getMethods();
		for (Method m : methods)
		{
			if (m.getName().startsWith("handle"))
			{
				Class<?>[] params = m.getParameterTypes();
				if (params.length == 1 && params[0] == eventType)
				{
					return m;
				}
			}
		}
		for (Method m : methods)
		{
			if (m.getName().startsWith("handle"))
			{
				Class<?>[] params = m.getParameterTypes();
				if (params.length == 1 && params[0].isAssignableFrom(eventType))
				{
					return m;
				}
			}
		}
		return null;
	}

	private Method findInquiry(EventListener listener, Class<?> eventType)
	{
		Method[] methods = listener.getClass().getMethods();
		for (Method m : methods)
		{
			if (m.getName().startsWith("handleInquiry"))
			{
				if (m.getReturnType() instanceof Object)
				{
					Class<?>[] params = m.getParameterTypes();
					if (params.length == 1 && params[0] == eventType)
					{
						return m;
					}
				}
			}
		}
		return null;
	}
}
