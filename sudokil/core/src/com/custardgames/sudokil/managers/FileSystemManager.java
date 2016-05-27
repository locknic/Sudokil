package com.custardgames.sudokil.managers;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.custardgames.sudokil.events.CopyItemBetweenFileSystemsEvent;
import com.custardgames.sudokil.events.PingFileSystemEvent;
import com.custardgames.sudokil.ui.cli.FolderCLI;
import com.custardgames.sudokil.ui.cli.ItemCLI;
import com.custardgames.sudokil.ui.cli.RootCLI;

public class FileSystemManager implements EventListener
{
	private Map<String, RootCLI> fileSystems;
	private UUID uuid;
	private CommandLineManager commandLineManager;

	public FileSystemManager()
	{
		fileSystems = new HashMap<String, RootCLI>();
		uuid = UUID.randomUUID();
		commandLineManager = new CommandLineManager(new RootCLI(), uuid);
		
		EventManager.get_instance().register(PingFileSystemEvent.class, this);
		EventManager.get_instance().register(CopyItemBetweenFileSystemsEvent.class, this);
	}

	public void addFileSystem(String location)
	{
		Json json = new Json();
		RootCLI root = json.fromJson(RootCLI.class, Gdx.files.internal(location));
		linkChildren(root);
		fileSystems.put(location, root);
	}

	public void linkChildren(FolderCLI parent)
	{
		ItemCLI[] children = parent.getChildren().toArray(ItemCLI.class);
		for (int x = 0; x < children.length; x++)
		{
			parent.removeChild(children[x]);
			parent.addChild(children[x]);

			if (children[x] instanceof FolderCLI)
			{
				linkChildren((FolderCLI) children[x]);
			}
		}
	}

	public RootCLI getFileSystem(String location)
	{
		if (fileSystems.containsKey(location))
		{
			return fileSystems.get(location);
		}
		else
		{
			addFileSystem(location);
			return fileSystems.get(location);
		}
	}
	
	public void copyItemBetweenFileSystems(String sourceFileSystemLocation, String targetFileSystemLocation, String sourceItemLocation, String targetItemLocation)
	{
		RootCLI sourceFileSystem = getFileSystem(sourceFileSystemLocation);
		commandLineManager.setRoot(sourceFileSystem);
		ItemCLI sourceItem = commandLineManager.findItem(sourceItemLocation);
		RootCLI targetFileSystem = getFileSystem(targetFileSystemLocation);
		commandLineManager.setRoot(targetFileSystem);
		ItemCLI targetItem = commandLineManager.findItem(targetItemLocation);
		if (targetItem instanceof FolderCLI)
		{
			((FolderCLI) targetItem).addChild(sourceItem.copy());
		}
	}

	public PingFileSystemEvent handleInquiryPingFileSystemEvent(PingFileSystemEvent event)
	{
		event.setFileSystem(getFileSystem(event.getAssetLocation()));
		return event;
	}
	
	public void handleCopyItemBetweenFileSystem(CopyItemBetweenFileSystemsEvent event)
	{
		copyItemBetweenFileSystems(event.getSourceFileSystem(), event.getTargetFileSystem(), event.getSourceItemLocation(), event.getTargetItemLocation());
	}

}
