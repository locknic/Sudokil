package com.custardgames.sudokil.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
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
import com.custardgames.sudokil.events.commandLine.AutocompleteRequestEvent;
import com.custardgames.sudokil.events.commandLine.AutocompleteResponseEvent;
import com.custardgames.sudokil.events.commandLine.ChangedDirectoryEvent;
import com.custardgames.sudokil.events.commandLine.ClearTerminalEvent;
import com.custardgames.sudokil.events.commandLine.CommandEvent;
import com.custardgames.sudokil.events.commandLine.CommandLineEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleLogEvent;
import com.custardgames.sudokil.events.commandLine.ConsoleOutputEvent;
import com.custardgames.sudokil.events.commandLine.HighlightEvent;
import com.custardgames.sudokil.events.commandLine.ListDirectoryEvent;
import com.custardgames.sudokil.events.commandLine.ResetHighlightEvent;
import com.custardgames.sudokil.events.commandLine.device.IfconfigEvent;
import com.custardgames.sudokil.events.commandLine.device.SSHEvent;
import com.custardgames.sudokil.events.entities.commands.DisconnectEvent;
import com.custardgames.sudokil.events.entities.commands.EntityCommandEvent;
import com.custardgames.sudokil.events.entities.commands.StopCommandsEvent;
import com.custardgames.sudokil.ui.cli.FolderCLI;
import com.custardgames.sudokil.ui.cli.ItemCLI;
import com.custardgames.sudokil.ui.cli.RootCLI;
import com.custardgames.sudokil.ui.cli.ScriptCLI;
import com.custardgames.sudokil.utils.Streams;

public class CommandLineManager implements EventListener
{
	private String username;
	private String device;

	private RootCLI root;
	private FolderCLI currentItem;

	private Options options;

	private UUID parentUI;
	private Streams standardStream;

	public CommandLineManager(RootCLI root, UUID ownerUI, UUID parentUI)
	{
		EventManager.get_instance().register(CommandEvent.class, this);
		EventManager.get_instance().register(CommandLineEvent.class, this);
		EventManager.get_instance().register(AutocompleteRequestEvent.class, this);
		EventManager.get_instance().register(ConsoleOutputEvent.class, this);

		username = "anon";
		device = root.getDeviceName();

		this.root = root;
		this.standardStream = new Streams(ownerUI);
		this.parentUI = parentUI;

		currentItem = this.root;

		options = new Options();

		Option echo = new Option("echo", "Output the arg.");
		echo.setArgs(Option.UNLIMITED_VALUES);
		echo.setOptionalArg(false);
		echo.setArgName("arg ...");
		options.addOption(echo);

		options.addOption("pwd", false, "Prints the current working directory.");

		Option ls = new Option("ls", "List directory contents.");
		ls.setArgs(Option.UNLIMITED_VALUES);
		ls.setOptionalArg(true);
		options.addOption(ls);

		Option cd = new Option("cd", "Change the current directory to DIR.");
		cd.setArgs(1);
		cd.setOptionalArg(true);
		cd.setArgName("dir");
		options.addOption(cd);

		Option mv = new Option("mv", "Moves a file or directory into another directory.");
		mv.setArgs(2);
		mv.setOptionalArg(false);
		mv.setArgName("file dir");
		options.addOption(mv);

		Option cp = new Option("cp", "Copies a file or directory into another directory.");
		cp.setArgs(2);
		cp.setOptionalArg(false);
		cp.setArgName("file dir");
		options.addOption(cp);

		Option sh = new Option("sh", "Run the script using shell script.");
		sh.setArgs(Option.UNLIMITED_VALUES);
		sh.setOptionalArg(false);
		sh.setArgName("script");
		options.addOption(sh);

		options.addOption("stop", false, "Stop and delete all queued commands.");

		options.addOption("exit", false, "Exit from the currently connected interface.");

		Option help = new Option("help", "Show the help screen.");
		help.setArgs(1);
		help.setOptionalArg(true);
		help.setArgName("command");
		options.addOption(help);

		Option man = new Option("man", "Show the man screen of a specified script.");
		man.setArgs(1);
		man.setOptionalArg(false);
		man.setArgName("script");
		options.addOption(man);

		Option whatis = new Option("whatis", "Show a short one line description of a specified script.");
		whatis.setArgs(1);
		whatis.setOptionalArg(false);
		whatis.setArgName("script");
		options.addOption(whatis);

		options.addOption("clear", false, "Clear the terminal screen.");

		Option cat = new Option("cat", "Concatenate files and print on the standard output.");
		cat.setArgs(Option.UNLIMITED_VALUES);
		cat.setOptionalArg(true);
		cat.setArgName("file");
		options.addOption(cat);

		options.addOption("ifconfig", false, "Lists connected networks and devices.");

		Option ssh = new Option("ssh", "Connect console to a device on the network.");
		ssh.setArgs(1);
		ssh.setOptionalArg(true);
		ssh.setArgName("device");
		options.addOption(ssh);
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

	public Object parseCommands(String[] args, Streams stream)
	{
		Object returnObj = null;
		try
		{
			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("help"))
			{
				String[] arguments = commandLine.getOptionValues("help");
				if (arguments == null)
				{
					arguments = new String[0];
				}
				returnObj = help(arguments);
			}
			else if (commandLine.hasOption("man"))
			{
				String[] arguments = commandLine.getOptionValues("man");
				if (arguments == null)
				{
					arguments = new String[0];
				}
				returnObj = man(arguments);
			}
			else if (commandLine.hasOption("whatis"))
			{
				String[] arguments = commandLine.getOptionValues("whatis");
				if (arguments == null)
				{
					arguments = new String[0];
				}
				returnObj = whatis(arguments);
			}
			else if (commandLine.hasOption("pwd"))
			{
				returnObj = pwd();
			}
			else if (commandLine.hasOption("ls"))
			{
				String[] arguments = commandLine.getOptionValues("ls");
				if (arguments == null)
				{
					arguments = new String[0];
				}
				returnObj = list(arguments);
			}
			else if (commandLine.hasOption("cd"))
			{
				returnObj = changeDirectory(commandLine.getOptionValue("cd"));
			}
			else if (commandLine.hasOption("mv"))
			{
				returnObj = moveItem(commandLine.getOptionValues("mv"));
			}
			else if (commandLine.hasOption("cp"))
			{
				returnObj = copyItem(commandLine.getOptionValues("cp"));
			}
			else if (commandLine.hasOption("sh"))
			{
				returnObj = runScript(commandLine.getOptionValues("sh"));
			}
			else if (commandLine.hasOption("stop"))
			{
				for (String device : currentItem.getDevices())
				{
					EventManager.get_instance().broadcast(new StopCommandsEvent(standardStream, device));
				}
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "Stopping queued commands."));

				returnObj = null;
			}
			else if (commandLine.hasOption("exit"))
			{
				if (parentUI == null || parentUI == standardStream.getOwner())
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Cannot exit out of current session."));
				}
				else
				{
					DisconnectEvent disconnectEvent = new DisconnectEvent();
					disconnectEvent.setOwnerUI(new Streams(parentUI));
					EventManager.get_instance().broadcast(disconnectEvent);
				}

				returnObj = null;
			}
			else if (commandLine.hasOption("echo"))
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, commandLine.getOptionValues("echo")));

				returnObj = null;
			}
			else if (commandLine.hasOption("clear"))
			{
				EventManager.get_instance().broadcast(new ClearTerminalEvent(standardStream));

				returnObj = null;
			}
			else if (commandLine.hasOption("cat"))
			{
				returnObj = cat(commandLine.getOptionValues("cat"));

				returnObj = null;
			}
			else if (commandLine.hasOption("ifconfig"))
			{
				EventManager.get_instance().broadcast(new IfconfigEvent(standardStream, device));

				returnObj = null;
			}
			else if (commandLine.hasOption("ssh"))
			{
				String[] arguments = commandLine.getOptionValues("ssh");
				if (arguments != null && arguments.length > 0)
				{
					EventManager.get_instance().broadcast(new SSHEvent(standardStream, device, arguments[0]));
					returnObj = null;
				}
				else
				{
					String[] arg =
					{ "ssh" };
					returnObj = help(arg);
				}
			}
		}
		catch (Exception e)
		{
			if (e instanceof UnrecognizedOptionException)
			{
				args[0] = args[0].substring(1, args[0].length());
				ItemCLI script = findItem(args[0]);
				if (script != null)
				{
					returnObj = runScript(args);

				}
				else
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! " + args[0] + ": command not found."));
					returnObj = null;
				}
			}
			else if (e instanceof MissingArgumentException)
			{
				EventManager.get_instance()
						.broadcast(new ConsoleOutputEvent(standardStream, "ERROR! " + args[0].substring(1, args[0].length()) + ": missing arguments."));
				returnObj = null;
			}

		}

		return returnObj;
	}

	public Object help(String[] args)
	{
		HelpFormatter formatter = new HelpFormatter();
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		if (args.length > 0)
		{
			if (options.hasOption(args[0]))
			{
				String text = args[0] + "\n" + options.getOption(args[0]).getDescription();
				if (options.getOption(args[0]).getArgs() > 0)
				{
					text = args[0] + " [arg]\n" + options.getOption(args[0]).getDescription();
				}
				formatter.printUsage(printWriter, 100, text);
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, stringWriter.toString()));
			}
			else
			{
				ItemCLI item = findItem(args[0]);
				if (item != null && item instanceof ScriptCLI && ((ScriptCLI) item).getEvent() != null
						&& ((ScriptCLI) item).getEvent() instanceof EntityCommandEvent)
				{
					EventManager.get_instance()
							.broadcast(new ConsoleOutputEvent(standardStream, "Usage: " + ((EntityCommandEvent) ((ScriptCLI) item).getEvent()).getUsage() + "\n"
									+ ((EntityCommandEvent) ((ScriptCLI) item).getEvent()).getDescription()));
				}
				else
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No help topics match '" + args[0] + "'."));
				}
			}
		}
		else
		{
			formatter.printHelp(printWriter, 100, "Available Commands", "", options, 1, 3, "");
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, stringWriter.toString()));
		}

		return true;
	}

	public Object man(String[] args)
	{
		if (args.length > 0)
		{
			if (options.hasOption(args[0]))
			{
				String text = "NAME\n    " + args[0] + " - " + options.getOption(args[0]).getDescription() + "\n\nSYNOPSIS\n    " + args[0]
						+ "\n\nDESCRIPTION\n    " + options.getOption(args[0]).getDescription();
				if (options.getOption(args[0]).getArgs() > 0)
				{
					text = "NAME\n    " + args[0] + " - " + options.getOption(args[0]).getDescription() + "\n\nSYNOPSIS\n    " + args[0] + " ["
							+ options.getOption(args[0]).getArgName() + "]\n\nDESCRIPTION\n    " + options.getOption(args[0]).getDescription();
				}
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, text));
			}
			else
			{
				ItemCLI item = findItem(args[0]);
				if (item != null && item instanceof ScriptCLI && ((ScriptCLI) item).getEvent() != null
						&& ((ScriptCLI) item).getEvent() instanceof EntityCommandEvent)
				{
					EntityCommandEvent event = ((EntityCommandEvent) ((ScriptCLI) item).getEvent());
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream,
							"NAME\n    " + event.getName() + "\n\nSYNOPSIS\n    " + event.getUsage() + "\n\nDESCRIPTION\n    " + event.getDescription()));
				}
				else
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No manual entry for '" + args[0] + "'."));
				}
			}
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! What manual page do you want?"));
		}

		return null;
	}

	public Object whatis(String[] args)
	{
		if (args.length > 0)
		{
			if (options.hasOption(args[0]))
			{
				String text = args[0] + "    - " + options.getOption(args[0]).getDescription();
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, text));
			}
			else
			{
				ItemCLI item = findItem(args[0]);
				if (item != null && item instanceof ScriptCLI && ((ScriptCLI) item).getEvent() != null
						&& ((ScriptCLI) item).getEvent() instanceof EntityCommandEvent)
				{
					EntityCommandEvent event = ((EntityCommandEvent) ((ScriptCLI) item).getEvent());
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, event.getName()));
				}
				else
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! nothing appropriate for '" + args[0] + "'."));
				}
			}
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! whatis keword ..."));
		}

		return null;
	}

	public Object pwd()
	{
		EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, getLocation()));

		return null;
	}

	public Object list(String args[])
	{
		if (args == null || args.length < 1)
		{
			String output = "";
			for (ItemCLI child : currentItem.getChildren())
			{
				output += child.getName() + "\n";
			}
			EventManager.get_instance().broadcast(new ListDirectoryEvent(standardStream));
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, output));
		}
		for (int x = 0; x < args.length; x++)
		{
			if (args[x] != null && !args[x].equals(""))
			{
				ItemCLI newItem = findItem(args[x]);
				if (newItem instanceof FolderCLI)
				{
					String output = args[x] + "/:\n";
					for (ItemCLI child : ((FolderCLI) newItem).getChildren())
					{
						output += child.getName() + "\n";
					}
					EventManager.get_instance().broadcast(new ListDirectoryEvent(standardStream));
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, output));
				}
				else if (newItem instanceof ItemCLI)
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, newItem.getName()));
				}
				else
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such file or directory."));
				}
			}
		}

		return null;
	}

	public Object changeDirectory(String location)
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
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Not a directory."));
			}
			else
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such file or directory."));
			}
		}

		return null;
	}

	public Object runScript(String[] args)
	{
		ItemCLI newItem = findItem(args[0]);

		if (newItem == null)
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such file or directory"));
		}
		else if (newItem instanceof ItemCLI)
		{
			if (newItem.isExecutePerm())
			{
				return ((ItemCLI) newItem).run(standardStream, args);
			}
			else
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Permission denied."));
			}
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such file or directory."));
		}

		return null;
	}

	public Object moveItem(String[] args)
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
							EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Name already in use."));
						}
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Destination not a directory."));
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
							EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Name already in use."));
						}
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such destination file or directory."));
					}
				}
			}
			else
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such source file or directory."));
			}
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such file or directory."));
		}

		return null;
	}

	public Object copyItem(String[] args)
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
							EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Name already in use."));
						}
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Destination not a directory."));
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
							EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Name already in use."));
						}
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such destination file or directory."));
					}
				}
			}
			else
			{
				EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such source file or directory."));
			}
		}
		else
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such file or directory."));
		}

		return null;
	}

	public Object cat(String args[])
	{
		for (int x = 0; x < args.length; x++)
		{
			if (args[x] != null && !args[x].equals(""))
			{
				ItemCLI newItem = findItem(args[x]);
				if (newItem instanceof ItemCLI)
				{
					if (newItem.isReadPerm())
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, newItem.read()));
					}
					else
					{
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! Permission denied."));
					}
				}
				else
				{
					EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, "ERROR! No such file or directory."));
				}
			}
		}

		return null;
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

	public Array<String> autoComplete(String text, boolean includeCommands)
	{
		Array<String> matches = new Array<String>();

		if (includeCommands)
		{
			Collection<Option> optionList = options.getOptions();
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
		EventManager.get_instance().broadcast(new ResetHighlightEvent(standardStream));

		Array<String> entities = currentItem.getDevices();
		for (String entity : entities)
		{
			HighlightEvent event = new HighlightEvent(standardStream, entity, false);
			EventManager.get_instance().broadcast(event);
		}

		entities = currentItem.getSubDevices();
		for (String entity : entities)
		{
			HighlightEvent event = new HighlightEvent(standardStream, entity, true);
			EventManager.get_instance().broadcast(event);
		}
	}

	public void handleAutocompleteRequest(AutocompleteRequestEvent event)
	{
		if (event.getOwnerUI().getOwner().equals(standardStream.getOwner()))
		{
			if (!event.getText().substring(event.getText().length() - 1).equals(";") && !event.getText().substring(event.getText().length() - 1).equals(" "))
			{
				String[] commands = event.getText().split(";");
				String[] command = commands[commands.length - 1].split(" ");
				Array<String> matches = autoComplete(command[command.length - 1], (command.length <= 1));
				if (matches != null)
				{
					if (matches.size == 1)
					{
						String result = event.getText().substring(0, event.getText().length() - command[command.length - 1].length()) + matches.first();
						EventManager.get_instance().broadcast(new AutocompleteResponseEvent(standardStream, result));
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
						EventManager.get_instance().broadcast(new AutocompleteResponseEvent(standardStream,
								event.getText().substring(0, event.getText().length() - command[command.length - 1].length()) + result));
						EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, getInputPrefix() + event.getText() + "\n" + output));
					}
				}
			}
		}
	}

	public Object handleInquiryCommand(CommandEvent event)
	{
		if (event.getOwnerUI().getOwner().equals(standardStream.getOwner()))
		{
			ArrayList<Object> objects = new ArrayList<Object>();

			try
			{
				String[] commands;
				commands = event.getText().split(";|&|&&");
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
//						Streams newStream = new Streams(standardStream.getOwner(), standardStream.getOutput(), standardStream.getError());
						objects.add(parseCommands(argsFinal, standardStream));
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return objects;
		}

		return null;
	}

	public Object handleInquiryConsoleCommand(CommandLineEvent event)
	{
		System.out.println(event.getOwnerUI().getOwner() + " vs " + standardStream.getOwner());
		if (event.getOwnerUI().getOwner().equals(standardStream.getOwner()))
		{
			EventManager.get_instance().broadcast(new ConsoleOutputEvent(standardStream, getInputPrefix() + event.getText()));
			return EventManager.get_instance().broadcastInquiry(new CommandEvent(event.getOwnerUI(), event.getText()));
		}
		return null;
	}

	public void handleConsoleOutput(ConsoleOutputEvent event)
	{
		if (event.getOwnerUI().getOutput() == this.standardStream.getOutput())
		{
			EventManager.get_instance().broadcast(new ConsoleLogEvent(event.getOwnerUI(), event.getText()));
		}
	}
}
