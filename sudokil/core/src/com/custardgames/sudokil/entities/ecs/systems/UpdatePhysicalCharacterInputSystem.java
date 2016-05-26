package com.custardgames.sudokil.entities.ecs.systems;

import java.util.EventListener;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.custardgames.sudokil.entities.ecs.components.CharacterInputComponent;
import com.custardgames.sudokil.events.physicalinput.KeyPressedEvent;
import com.custardgames.sudokil.events.physicalinput.KeyReleasedEvent;
import com.custardgames.sudokil.events.physicalinput.MousePressedEvent;
import com.custardgames.sudokil.events.physicalinput.MouseReleasedEvent;
import com.custardgames.sudokil.managers.EventManager;

public class UpdatePhysicalCharacterInputSystem extends EntityProcessingSystem implements EventListener
{
	private ComponentMapper<CharacterInputComponent> characterInputComponent;

	@SuppressWarnings("unchecked")
	public UpdatePhysicalCharacterInputSystem()
	{
		super(Aspect.all(CharacterInputComponent.class));

		EventManager.get_instance().register(KeyPressedEvent.class, this);
		EventManager.get_instance().register(KeyReleasedEvent.class, this);
		EventManager.get_instance().register(MousePressedEvent.class, this);
		EventManager.get_instance().register(MouseReleasedEvent.class, this);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		EventManager.get_instance().deregister(KeyPressedEvent.class, this);
		EventManager.get_instance().deregister(KeyReleasedEvent.class, this);
		EventManager.get_instance().deregister(MousePressedEvent.class, this);
		EventManager.get_instance().deregister(MouseReleasedEvent.class, this);
	}

	@Override
	public boolean checkProcessing()
	{
		return false;
	}

	@Override
	protected void process(Entity e)
	{

	}

	public void updateKBInput(int keyCode, boolean pressed, CharacterInputComponent component)
	{
		if (keyCode == 51)
		{
			component.setUp(pressed);
		}
		else if (keyCode == 47)
		{
			component.setDown(pressed);
		}
		else if (keyCode == 29)
		{
			component.setLeft(pressed);
		}
		else if (keyCode == 32)
		{
			component.setRight(pressed);
		}
	}

	public void updateMouseInput(int buttonNumber, boolean pressed, CharacterInputComponent component)
	{
		if (buttonNumber == 0)
		{
			component.setAction1(pressed);
		}
		else if (buttonNumber == 1)
		{
			component.setAction2(pressed);
		}
	}

	public void handleKeyPressed(KeyPressedEvent keyEvent)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			updateKBInput(keyEvent.getKeyCode(), true, characterInputComponent.get(entities.get(x)));
		}
	}

	public void handleKeyReleased(KeyReleasedEvent keyEvent)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			updateKBInput(keyEvent.getKeyCode(), false, characterInputComponent.get(entities.get(x)));
		}
	}

	public void handleMousePressed(MousePressedEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			updateMouseInput(event.getButtonNumber(), true, characterInputComponent.get(entities.get(x)));
		}
	}

	public void handleMouseReleased(MouseReleasedEvent event)
	{
		ImmutableBag<Entity> entities = getEntities();
		for (int x = 0; x < entities.size(); x++)
		{
			updateMouseInput(event.getButtonNumber(), false, characterInputComponent.get(entities.get(x)));
		}
	}

}
