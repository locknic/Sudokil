package com.custardgames.sudokil.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.badlogic.gdx.utils.Array;
import com.custardgames.sudokil.events.AutocompleteRequestEvent;
import com.custardgames.sudokil.events.AutocompleteResponseEvent;
import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.events.CommandLineEvent;
import com.custardgames.sudokil.events.ConsoleLogEvent;
import com.custardgames.sudokil.events.commands.DisconnectEvent;
import com.custardgames.sudokil.events.commands.StopCommandsEvent;
import com.custardgames.sudokil.ui.cli.FolderCLI;
import com.custardgames.sudokil.ui.cli.ItemCLI;
import com.custardgames.sudokil.ui.cli.RootCLI;
import com.custardgames.sudokil.ui.cli.ScriptCLI;

public class CommandLineManager implements EventListener
{
	private String username;
	private String device;

	private RootCLI root;
	private FolderCLI currentItem;

	private Options options;

	private UUID ownerUUID;

	public CommandLineManager(RootCLI root, UUID ownerUI)
	{
		EventManager.get_instance().register(CommandLineEvent.class, this);
		EventManager.get_instance().register(AutocompleteRequestEvent.class, this);

		username = "anon";
		device = root.getDeviceName();

		this.root = root;
		this.ownerUUID = ownerUI;

		currentItem = this.root;

		options = new Options();

		Option echo = new Option("echo", "Output the ARGs.");
		echo.setArgs(Option.UNLIMITED_VALUES);
		echo.setOptionalArg(false);
		echo.setArgName("ARG");
		options.addOption(echo);

		options.addOption("pwd", false, "Prints the current working directory.");

		options.addOption("ls", false, "List directory contents.");

		Option cd = new Option("cd", "Change the current directory to DIR.");
		cd.setArgs(1);
		cd.setOptionalArg(true);
		options.addOption(cd);

		Option mv = new Option("mv", "Move");
		mv.setArgs(2);
		mv.setOptionalArg(false);
		options.addOption(mv);

		Option sh = new Option("sh", "Run the script using shell script.");
		sh.setArgs(Option.UNLIMITED_VALUES);
		sh.setOptionalArg(false);
		options.addOption(sh);

		options.addOption("stop", false, "Stop and delete all queued commands.");

		options.addOption("exit", false, "Exit from the currently connected interface.");

		options.addOption("help", false, "Show the help screen.");

	}

	public void setRoot(RootCLI root)
	{
		this.root = root;
		currentItem = root;
		device = root.getDeviceName();
	}

	public String getLocation()
	{
		return currentItem.getLocation() + "/" + currentItem.getName();
	}

	public String getInputPrefix()
	{
		String output = "";
		output += username + "@" + device + ":";

		return output + getLocation() + "$ ";
	}

	public ItemCLI findItem(String location)
	{
		ItemCLI newItem = null;
		boolean foundLocation = false;
		
		if (location != null && (location.equals("") || location.equals("~") || location.equals("/")))
		{
			newItem = root;
			return newItem;
		}
		
		if (location != null && (location.substring(0, 1).equals("/") || location.substring(0, 1).equals("~")))
		{
			newItem = root;
			foundLocation = true;
		}
		else if (currentItem != null)
		{
			newItem = currentItem;
		}		
		String[] locations;
		locations = location.split("/");
		for (int x = 0; x < locations.length; x++)
		{
			if (locations[x].length() > 0)
			{
				foundLocation = false;

				if (locations[x].equals(".."))
				{
					if (!newItem.getLocation().equals(""))
					{
						newItem = root.getItem(newItem.getLocation());
						foundLocation = true;
					}
					else
					{
						newItem = root;
						foundLocation = true;
					}
				}
				else if (newItem instanceof FolderCLI)
				{
					for (ItemCLI child : root.getChildren(((FolderCLI) newItem)))
					{
						if (child instanceof FolderCLI)
						{
							if (locations[x].equals(child.getName()))
							{
								newItem = (FolderCLI) child;
								foundLocation = true;
								break;
							}
						}
					}
				}

				if (foundLocation == false)
				{
					newItem = null;
					break;
				}
			}
		}
		return newItem;
	}

	public void pwd()
	{
		EventManager.get_instance().broadcast(new ConsoleLogEvent(getLocation(), ownerUUID));
	}

	public void list()
	{
		String output = "";
		for (ItemCLI child : root.getChildren(currentItem))
		{
			output += child.getName() + "\n";
		}
		EventManager.get_instance().broadcast(new ConsoleLogEvent(output, ownerUUID));
	}

	public void changeDirectory(String location)
	{
		if (location == null || location.equals(""))
		{
			currentItem = root;
		}
		else
		{
			ItemCLI newItem = findItem(location);

			if (newItem instanceof FolderCLI)
			{
				currentItem = (FolderCLI) newItem;
			}
			else if (newItem instanceof ItemCLI)
			{
				EventManager.get_instance().broadcast(new ConsoleLogEvent("ERROR! Not a directory.", ownerUUID));
			}
			else
			{
				EventManager.get_instance()
						.broadcast(new ConsoleLogEvent("ERROR! No such file or directory.", ownerUUID));
			}
		}
	}

	public void runScript(String[] args)
	{
		ItemCLI newItem = findItem(args[0]);

		if (newItem == null)
		{
			EventManager.get_instance().broadcast(new ConsoleLogEvent("ERROR! No such file or directory", ownerUUID));
		}
		else if (newItem instanceof ScriptCLI)
		{
			((ScriptCLI) newItem).run(ownerUUID, args);
		}
		else if (newItem instanceof FolderCLI)
		{
			EventManager.get_instance().broadcast(new ConsoleLogEvent("ERROR! Is a directory.", ownerUUID));
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleLogEvent("ERROR! No such file or directory.", ownerUUID));
		}
	}
//
//	public void moveItem(String[] args)
//	{
//		ItemCLI sourceItem = findItem(args[0]);
//		
//		System.out.println("Destination: " +args[1]);
//		ItemCLI destinationItem = findItem(args[1]);
//
//		if (sourceItem != root)
//		{
//			if (sourceItem != null)
//			{
//				if (destinationItem != null)
//				{
//					if (destinationItem instanceof FolderCLI)
//					{
//						System.out.println("Working");
//						System.out.println("Destination: " + root.getItemID(destinationItem));
//						root.moveItem(root.getItemID(destinationItem), root.getItemID(sourceItem));
//					}
//					else
//					{
//						EventManager.get_instance()
//								.broadcast(new ConsoleLogEvent("ERROR! Not a directory.", ownerUUID));
//					}
//				}
//				else
//				{
//					String location = "";
//					String[] locations;
//					locations = args[1].split("/");
//					for (int x = 0; x < locations.length - 1; x++)
//					{
//						location += locations[x];
//					}
//					ItemCLI parent = findItem(location);
//					if (parent != null && parent instanceof FolderCLI)
//					{
//						root.deleteItem(root.getItemID(sourceItem));
//						root.addItemTo(root.getItemID(parent), locations[locations.length - 1], sourceItem);
//					}
//					else
//					{
//						EventManager.get_instance()
//								.broadcast(new ConsoleLogEvent("ERROR! No such file or directory.", ownerUUID));
//					}
//				}
//			}
//			else
//			{
//				EventManager.get_instance()
//						.broadcast(new ConsoleLogEvent("ERROR! No such file or directory.", ownerUUID));
//			}
//		}
//	}

	public void parseCommands(String[] args)
	{
		try
		{
			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("help"))
			{
				HelpFormatter formatter = new HelpFormatter();
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				formatter.printHelp(printWriter, 100, "Available Commands", "", options, 1, 3, "");
				EventManager.get_instance().broadcast(new ConsoleLogEvent(stringWriter.toString(), ownerUUID));
			}
			else if (commandLine.hasOption("pwd"))
			{
				pwd();
			}
			else if (commandLine.hasOption("ls"))
			{
				list();
			}
			else if (commandLine.hasOption("cd"))
			{
				changeDirectory(commandLine.getOptionValue("cd"));
			}
			else if (commandLine.hasOption("mv"))
			{
//				moveItem(commandLine.getOptionValues("mv"));
			}
			else if (commandLine.hasOption("sh"))
			{
				runScript(commandLine.getOptionValues("sh"));
			}
			else if (commandLine.hasOption("stop"))
			{
				EventManager.get_instance().broadcast(new StopCommandsEvent(currentItem.getName()));
				EventManager.get_instance().broadcast(new ConsoleLogEvent("Stopping queued commands.", ownerUUID));
			}
			else if (commandLine.hasOption("exit"))
			{
				DisconnectEvent disconnectEvent = new DisconnectEvent();
				disconnectEvent.setOwner(device);
				EventManager.get_instance().broadcast(disconnectEvent);
			}
			else if (commandLine.hasOption("echo"))
			{
				EventManager.get_instance()
						.broadcast(new ConsoleLogEvent(commandLine.getOptionValues("echo"), ownerUUID));
			}

		}
		catch (Exception e)
		{
			if (e instanceof UnrecognizedOptionException)
			{
				args[0] = args[0].substring(1, args[0].length());
				ItemCLI script = findItem(args[0]);
				if (script != null && script instanceof ScriptCLI)
				{
					runScript(args);
				}
				else
				{
					EventManager.get_instance()
							.broadcast(new ConsoleLogEvent("" + args[0] + ": command not found.", ownerUUID));
				}
			}
			else if (e instanceof MissingArgumentException)
			{
				EventManager.get_instance().broadcast(new ConsoleLogEvent(
						"" + args[0].substring(1, args[0].length()) + ": missing arguments.", ownerUUID));
			}

		}
	}

	public void repeatEvent(BaseEvent event, String number)
	{
		int num = 0;

		try
		{
			if (number != null)
			{
				num = Integer.parseInt(number);
			}

			if (num <= 0 || num > 99)
			{
				num = 1;
			}
		}
		catch (NumberFormatException e)
		{
			EventManager.get_instance()
					.broadcast(new ConsoleLogEvent("" + number + ": argument must be an integer.", ownerUUID));
		}

		for (int x = 0; x < num; x++)
		{
			EventManager.get_instance().broadcast(event);
		}
	}

	public Array<String> autoComplete(String text)
	{
		Collection<Option> optionList = options.getOptions();
		Array<String> matches = new Array<String>();
		for (Option option : optionList)
		{
			if (option.getOpt().startsWith(text))
			{
				matches.add(option.getOpt());
			}
			if (option.hasLongOpt() && option.getLongOpt().startsWith(text))
			{
				matches.add(option.getLongOpt());
			}
		}

		String[] locations = text.split("(?<=/)");
		String tempLocationString = "";
		for (int x = 0; x < locations.length - 1; x++)
		{
			tempLocationString += locations[x];
		}
		ItemCLI tempLocation = findItem(root.getItemID(currentItem) + tempLocationString);
		for (ItemCLI child : root.getChildren((FolderCLI) tempLocation))
		{
			if (child.getName().startsWith(locations[locations.length - 1]))
			{
				String result = child.getName();
				matches.add(tempLocationString + result);
			}
		}
		return matches;
	}

	public void handleAutocompleteRequest(AutocompleteRequestEvent event)
	{
		String[] commands = event.getCommand().split(";| ");
		Array<String> matches = autoComplete(commands[commands.length - 1]);
		if (matches != null)
		{
			if (matches.size == 1)
			{
				String result = event.getCommand().substring(0,
						event.getCommand().length() - commands[commands.length - 1].length()) + matches.first();
				EventManager.get_instance().broadcast(new AutocompleteResponseEvent(result, ownerUUID));
			}
			else if (matches.size > 1)
			{
				String output = "";
				for (String match : matches)
				{
					output += match + " ";
				}
				EventManager.get_instance().broadcast(
						new ConsoleLogEvent(getInputPrefix() + event.getCommand() + "\n" + output, ownerUUID));
			}
		}
	}

	public void handleConsoleCommand(CommandLineEvent event)
	{
		if (event.getOwnerUI().equals(ownerUUID))
		{
			EventManager.get_instance()
					.broadcast(new ConsoleLogEvent(getInputPrefix() + event.getCommand(), ownerUUID));
			try
			{
				String[] commands;
				commands = event.getCommand().split(";");
				for (int x = 0; x < commands.length; x++)
				{
					String[] args = commands[x].split(" ");
					Array<String> argsList = new Array<String>();
					for (int y = 0; y < args.length; y++)
					{
						argsList.add(args[y]);
					}
					Iterator<String> iter = argsList.iterator();
					while (iter.hasNext())
					{
						String next = iter.next();
						if (next == null || next.equals(""))
						{
							iter.remove();
						}
					}
					String[] argsFinal = new String[argsList.size];
					for (int y = 0; y < argsList.size; y++)
					{
						argsFinal[y] = argsList.get(y);
					}

					if (argsFinal.length > 0)
					{
						argsFinal[0] = "-" + argsFinal[0];
						parseCommands(argsFinal);
					}
				}
			}
			catch (Exception e)
			{

				e.printStackTrace();
			}
		}
	}
}
