package com.custardgames.sudokil.ui.cli;

public class TextFileCLI extends FileCLI
{
	private String content;
	
	public TextFileCLI()
	{
		
	}
	
	public TextFileCLI(String content)
	{
		this.setContent(content);
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
	
	public void appendContent(String additions)
	{
		this.content += additions;
	}
}
