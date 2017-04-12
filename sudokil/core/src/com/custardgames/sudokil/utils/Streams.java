package com.custardgames.sudokil.utils;

import java.util.UUID;

public class Streams
{
	private UUID owner;
	private UUID input;
	private UUID output;
	private UUID error;

	public Streams(UUID owner)
	{
		this(owner, owner, owner, owner);
	}

	public Streams(UUID owner, UUID input, UUID output, UUID error)
	{
		this.owner = owner;
		this.input = input;
		this.output = output;
		this.error = error;
	}

	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	public UUID getOutput()
	{
		return output;
	}

	public void setOutput(UUID output)
	{
		this.output = output;
	}

	public UUID getError()
	{
		return error;
	}

	public void setError(UUID error)
	{
		this.error = error;
	}

	public UUID getInput()
	{
		return input;
	}

	public void setInput(UUID input)
	{
		this.input = input;
	}

}
