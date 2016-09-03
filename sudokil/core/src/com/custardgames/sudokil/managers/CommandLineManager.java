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
import com.custardgames.sudokil.events.BaseEvent;
import com.custardgames.sudokil.events.commandLine.AutocompleteRequestEvent;
import com.custardgames.sudokil.events.commandLine.AutocompleteResponseEvent;
import com.custardgames.sudokil.events.commandLine.ChangedDirectoryEvent;
import com.custardgames.sudokil.events.commandLine.ClearTerminalEvent;
import com.custardgames.sudokil.events.commandLine.CommandLineEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleLogEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.events.commandLine.ListDirectoryEvent;
import com.custardgames.sudokil.events.entities.commands.DisconnectEvent;
import com.custardgames.sudokil.events.entities.commands.HighlightEvent;
import com.custardgames.sudokil.events.entities.commands.ResetHighlightEvent;
import com.custardgames.sudokil.events.entities.commands.StopCommandsEvent;
import com.custardgames.sudokil.ui.cli.FolderCLI;
import com.custardgames.sudokil.ui.cli.ItemCLI;
import com.custardgames.sudokil.ui.cli.RootCLI;
import com.custardgames.sudokil.ui.cli.ScriptCLI;
import com.custardgames.sudokil.ui.cli.TextFileCLI;

public class CommandLineManager implements EventListener
{
	private String username;
	private String device;

	private RootCLI root;
	private FolderCLI currentItem;

	private Options options;

	private UUID ownerUI;

	public CommandLineManager(RootCLI root, UUID ownerUI)
	{
		EventManager.get_instance().register(CommandLineEvent.class, this);
		EventManager.get_instance().register(AutocompleteRequestEvent.class, this);
		EventManager.get_instance().register(ConsoleOutputEvent.class, this);

		username = "anon";
		device = root.getDeviceName();

		this.root = root;
		this.ownerUI = ownerUI;

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

		Option mv = new Option("mv", "Moves a file or directory into another directory.");
		mv.setArgs(2);
		mv.setOptionalArg(false);
		options.addOption(mv);

		Option cp = new Option("cp", "Copies a file or directory into another directory.");
		cp.setArgs(2);
		cp.setOptionalArg(false);
		options.addOption(cp);

		Option sh = new Option("sh", "Run the script using shell script.");
		sh.setArgs(Option.UNLIMITED_VALUES);
		sh.setOptionalArg(false);
		options.addOption(sh);

		options.addOption("stop", false, "Stop and delete all queued commands.");

		options.addOption("exit", false, "Exit from the currently connected interface.");

		options.addOption("help", false, "Show the help screen.");

		options.addOption("clear", false, "Clear the terminal screen.");

		Option cat = new Option("cat", "Concatenate files and print on the standard output.");
		cat.setArgs(Option.UNLIMITED_VALUES);
		cat.setOptionalArg(true);
		options.addOption(cat);
	}

	public void dispose()
	{
		EventManager.get_instance().deregister(CommandLineEvent.class, this);
		EventManager.get_instance().deregister(AutocompleteRequestEvent.class, this);
		EventManager.get_instance().deregister(ConsoleOutputEvent.class, this);
	}

	public void setRoot(RootCLI root)
	{
		this.root = root;
		currentItem = root;
		device = root.getDeviceName();
	}

	public ItemCLI findItemRelative(FolderCLI startingLoc, String path)
	{
		ItemCLI selectedItem = startingLoc;
		String[] locations;
		locations = path.split("/");
		for (String currentLocation : locations)
		{
			boolean foundItem = false;
			if (currentLocation.equals(".."))
			{
				foundItem = true;
				if (selectedItem.getParent() != null)
				{
					selectedItem = selectedItem.getParent();
				}
			}
			else if (currentLocation.equals("."))
			{
				foundItem = true;
				selectedItem = currentItem;
			}
			else
			{
				for (ItemCLI child : ((FolderCLI) selectedItem).getChildren())
				{
					if (currentLocation.equals(child.getName()))
					{
						foundItem = true;
						selectedItem = child;
					}
				}
			}
			if (!foundItem)
			{
				return null;
			}
		}
		return selectedItem;
	}

	public ItemCLI findItem(String path)
	{
		if (path.equals("/") || path.equals("~"))
		{
			return root;
		}
		else if (path.length() > 0 && (path.substring(0, 1).equals("/") || path.substring(0, 1).equals("~")))
		{
			return findItemRelative(root, path.substring(1));
		}
		else if (path.equals(""))
		{
			return currentItem;
		}
		else
		{
			return findItemRelative(currentItem, path);
		}
	}

	public String getLocation()
	{
		return currentItem.getPath();
	}

	public String getInputPrefix()
	{
		String output = "";
		output += username + "@" + device + ":";

		return output + getLocation() + "$ ";
	}

	public void pwd()
	{
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, getLocation()));
	}

	public void list()
	{
		String output = "";
		for (ItemCLI child : currentItem.getChildren())
		{
			output += child.getName() + "\n";
		}
		EventManager.get_instance().broadcast(new ListDirectoryEvent(ownerUI));
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, output));
	}

	public void changeDirectory(String location)
	{
		if (location == null || location.equals(""))
		{
			currentItem = root;
			EventManager.get_instance().broadcast(new ChangedDirectoryEvent(currentItem.getName()));
		}
		else
		{
			ItemCLI newItem = findItem(location);
			if (newItem instanceof FolderCLI)
			{
				currentItem = (FolderCLI) newItem;
				EventManager.get_instance().broadcast(new ChangedDirectoryEvent(newItem.getName()));
			}
			else if (newItem instanceof ItemCLI)
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Not a directory."));
			}
			else
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such file or directory."));
			}
		}
	}

	public void runScript(String[] args)
	{
		ItemCLI newItem = findItem(args[0]);

		if (newItem == null)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such file or directory"));
		}
		else if (newItem instanceof ScriptCLI)
		{
			((ScriptCLI) newItem).run(ownerUI, args);
		}
		else if (newItem instanceof FolderCLI)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Is a directory."));
		}
		else if (newItem instanceof FolderCLI)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Is not a runnable file."));
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such file or directory."));
		}
	}

	public void moveItem(String[] args)
	{
		if (args[0] != null && args[0].length() > 0 && args[1] != null && args[1].length() > 0)
		{
			ItemCLI sourceItem = findItem(args[0]);

			ItemCLI destinationItem = findItem(args[1]);

			if (sourceItem != null && sourceItem != root)
			{
				if (destinationItem != null)
				{
					if (destinationItem instanceof FolderCLI)
					{
						if (!((FolderCLI) destinationItem).nameTaken(sourceItem.getName()))
						{
							sourceItem.changeParent((FolderCLI) destinationItem);
						}
						else
						{
							EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Name already in use."));
						}
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Destination not a directory."));
					}
				}
				else
				{
					String location = "";
					String[] locations;
					locations = args[1].split("(?<=/)");
					for (int x = 0; x < locations.length - 1; x++)
					{
						location += locations[x];
					}
					ItemCLI parent = findItem(location);
					if (parent != null && parent instanceof FolderCLI)
					{
						if (!((FolderCLI) parent).nameTaken(locations[locations.length - 1]))
						{
							sourceItem.setName(locations[locations.length - 1]);
							sourceItem.changeParent((FolderCLI) parent);
						}
						else
						{
							EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Name already in use."));
						}
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such destination file or directory."));
					}
				}
			}
			else
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such source file or directory."));
			}
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such file or directory."));
		}
	}

	public void copyItem(String[] args)
	{
		if (args[0] != null && args[0].length() > 0 && args[1] != null && args[1].length() > 0)
		{
			ItemCLI sourceItem = findItem(args[0]);

			ItemCLI destinationItem = findItem(args[1]);

			if (sourceItem != null && sourceItem != root)
			{
				if (destinationItem != null)
				{
					if (destinationItem instanceof FolderCLI)
					{
						if (!((FolderCLI) destinationItem).nameTaken(sourceItem.getName()))
						{
							((FolderCLI) destinationItem).addChild(sourceItem.copy());
						}
						else
						{
							EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Name already in use."));
						}
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Destination not a directory."));
					}
				}
				else
				{
					String location = "";
					String[] locations;
					locations = args[1].split("(?<=/)");
					for (int x = 0; x < locations.length - 1; x++)
					{
						location += locations[x];
					}
					ItemCLI parent = findItem(location);
					if (parent != null && parent instanceof FolderCLI)
					{
						if (!((FolderCLI) parent).nameTaken(locations[locations.length - 1]))
						{
							ItemCLI newItem = sourceItem.copy();
							newItem.setName(locations[locations.length - 1]);
							((FolderCLI) parent).addChild(newItem);
						}
						else
						{
							EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Name already in use."));
						}
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such destination file or directory."));
					}
				}
			}
			else
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such source file or directory."));
			}
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such file or directory."));
		}
	}

	public void cat(String options[])
	{
		for (int x = 0; x < options.length; x++)
		{
			if (options[x] != null && !options[x].equals(""))
			{
				ItemCLI newItem = findItem(options[x]);
				if (newItem instanceof FolderCLI)
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Is a directory."));
				}
				else if (newItem instanceof TextFileCLI)
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, ((TextFileCLI) newItem).getContent()));
				}
				else if (newItem instanceof ItemCLI)
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! Could not read file."));
				}
				else
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "ERROR! No such file or directory."));
				}
			}
		}
	}

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
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, stringWriter.toString()));
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
				moveItem(commandLine.getOptionValues("mv"));
			}
			else if (commandLine.hasOption("cp"))
			{
				copyItem(commandLine.getOptionValues("cp"));
			}
			else if (commandLine.hasOption("sh"))
			{
				runScript(commandLine.getOptionValues("sh"));
			}
			else if (commandLine.hasOption("stop"))
			{
				EventManager.get_instance().broadcast(new StopCommandsEvent(currentItem.getName()));
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "Stopping queued commands."));
			}
			else if (commandLine.hasOption("exit"))
			{
				DisconnectEvent disconnectEvent = new DisconnectEvent();
				disconnectEvent.setEntityName(device);
				EventManager.get_instance().broadcast(disconnectEvent);
			}
			else if (commandLine.hasOption("echo"))
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, commandLine.getOptionValues("echo")));
			}
			else if (commandLine.hasOption("clear"))
			{
				EventManager.get_instance().broadcast(new ClearTerminalEvent(ownerUI));
			}
			else if (commandLine.hasOption("cat"))
			{
				cat(commandLine.getOptionValues("cat"));
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
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "" + args[0] + ": command not found."));
				}
			}
			else if (e instanceof MissingArgumentException)
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "" + args[0].substring(1, args[0].length()) + ": missing arguments."));
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
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, "" + number + ": argument must be an integer."));
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
		ItemCLI tempLocation = findItem(tempLocationString);
		if (tempLocation != null && ((FolderCLI) tempLocation).getChildren().size >= 0)
		{
			for (ItemCLI child : ((FolderCLI) tempLocation).getChildren())
			{
				if (child.getName().startsWith(locations[locations.length - 1]))
				{
					String result = child.getName();
					matches.add(tempLocationString + result);
				}
			}
		}
		return matches;
	}

	public void highlightEntities(String text)
	{
		EventManager.get_instance().broadcast(new ResetHighlightEvent());

		Array<String> entities = currentItem.getDevices();
		for (String entity : entities)
		{
			HighlightEvent event = new HighlightEvent();
			event.setPossibleSelection(false);
			event.setOwnerUI(ownerUI);
			event.setEntityName(entity);
			EventManager.get_instance().broadcast(event);
		}

		entities = currentItem.getSubDevices();
		for (String entity : entities)
		{
			HighlightEvent event = new HighlightEvent();
			event.setPossibleSelection(true);
			event.setOwnerUI(ownerUI);
			event.setEntityName(entity);
			EventManager.get_instance().broadcast(event);
		}
	}

	public void handleAutocompleteRequest(AutocompleteRequestEvent event)
	{
		if (event.getOwnerUI().equals(ownerUI))
		{
			if (!event.getText().substring(event.getText().length() - 1).equals(";") && !event.getText().substring(event.getText().length() - 1).equals(" "))
			{
				String[] commands = event.getText().split(";| ");
				Array<String> matches = autoComplete(commands[commands.length - 1]);
				if (matches != null)
				{
					if (matches.size == 1)
					{
						String result = event.getText().substring(0, event.getText().length() - commands[commands.length - 1].length()) + matches.first();
						EventManager.get_instance().broadcast(new AutocompleteResponseEvent(ownerUI, result));
					}
					else if (matches.size > 1)
					{
						String output = "";
						String result = "";
						for (String match : matches)
						{
							if (result.equals(""))
							{
								result = match;
							}
							else
							{
								for (int x = 0; x < result.length(); x++)
								{
									if (result.charAt(x) != match.charAt(x))
									{
										result = result.substring(0, x);
										break;
									}
								}
							}
							output += match + " ";
						}
						EventManager.get_instance().broadcast(new AutocompleteResponseEvent(ownerUI,
								event.getText().substring(0, event.getText().length() - commands[commands.length - 1].length()) + result));
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, getInputPrefix() + event.getText() + "\n" + output));
					}
				}
			}
		}
	}

	public void handleConsoleCommand(CommandLineEvent event)
	{
		if (event.getOwnerUI().equals(ownerUI))
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(ownerUI, getInputPrefix() + event.getText()));
			try
			{
				String[] commands;
				commands = event.getText().split(";");
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

	public void handleConsoleOutput(ConsoleOutputEvent event)
	{
		if (event.getOwnerUI() == this.ownerUI)
		{
			EventManager.get_instance().broadcast(new ConsoleLogEvent(event.getOwnerUI(), event.getText()));
		}
	}
}
