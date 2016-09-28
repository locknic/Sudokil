package com.custardgames.sudokil.ui.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.custardgames.sudokil.events.ChangeLevelEvent;
import com.custardgames.sudokil.managers.EventManager;
import com.custardgames.sudokil.ui.tools.MapDescriptionHolder;

public class LevelLoaderInterface
{
	private Stage stage;

	private Window dialog;
	private ScrollPane consoleScroll;
	private Table levelTable;
	private List<String> levelList;
	private Label descriptionDialog;

	Array<MapDescriptionHolder> descHolder;

	@SuppressWarnings("unchecked")
	public LevelLoaderInterface(Stage stage)
	{
		this.stage = stage;

		Json json = new Json();
		descHolder = json.fromJson(Array.class, Gdx.files.internal("maps/map-list.json"));

		createWindow();
		hideWindow();
	}

	public void createWindow()
	{
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		TextButton closeButton = new TextButton("", skin, "close-toggle");

		dialog = new Window("Level Select", skin);
		dialog.setBounds(stage.getWidth() - stage.getWidth() / 2 - 300, stage.getHeight() - stage.getHeight() / 2 - 200, 600, 400);
		dialog.setResizable(false);
		dialog.setKeepWithinStage(true);
		dialog.getTitleTable().add(closeButton).size(dialog.getPadTop() * 4 / 5, dialog.getPadTop() * 4 / 5).padRight(dialog.getPadRight());
		dialog.left().top();
		dialog.setResizeBorder(5);
		dialog.padRight(0);
		dialog.padBottom(0);
		
		closeButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				hideWindow();
			}
		});
		
		levelList = new List<String>(skin);
		Array<String> levelListArray = new Array<String>();
		for(MapDescriptionHolder desc : descHolder)
		{
			levelListArray.add(desc.getName());
		}
		levelList.setItems(levelListArray);
		
		levelList.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				setDescription();
			}
		});

		levelTable = new Table();
		levelTable.add(levelList).width(200).fillY().expandY().top().padLeft(20).padTop(20).padBottom(20);

		consoleScroll = new ScrollPane(levelTable, skin);
		consoleScroll.setFadeScrollBars(false);
		consoleScroll.setVariableSizeKnobs(true);
		consoleScroll.setFlickScroll(false);
		dialog.add(consoleScroll).fillY().expandY().top();
		
		Table rightTable = new Table();

		descriptionDialog = new Label("", skin, "orange-outlined");
		descriptionDialog.setWrap(true);
		descriptionDialog.setAlignment(Align.topLeft);
		rightTable.add(descriptionDialog).fill().expand().pad(20).colspan(2);
		rightTable.row();

		Button cancelButton = new TextButton("CANCEL", skin, "orange");
		cancelButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				hideWindow();
			}
		});
		rightTable.add(cancelButton).width(150).height(50).bottom().expandX().padBottom(20).padLeft(20).left();

		Button playButton = new TextButton("LOAD", skin, "orange");
		playButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				pressedLoad();
				hideWindow();
			}
		});
		rightTable.add(playButton).width(150).height(50).bottom().expandX().padBottom(20).padRight(20).right();
		dialog.add(rightTable).expand().fill();
		
		
		
		stage.addActor(dialog);
		
		setDescription();
	}

	public void hideWindow()
	{
		if (stage.getActors().contains(dialog, true))
		{
			dialog.remove();
		}
	}

	public void showWindow()
	{
		if (!stage.getActors().contains(dialog, true))
		{
			stage.addActor(dialog);
		}
	}

	public void pressedLoad()
	{
		for (MapDescriptionHolder desc : descHolder)
		{
			if (levelList.getSelected().equals(desc.getName()))
			{
				EventManager.get_instance().broadcast(new ChangeLevelEvent(desc.getFileLocation()));
			}
		}
	}
	
	public void setDescription()
	{
		String selected = "";
		
		for (MapDescriptionHolder desc : descHolder)
		{
			if (levelList.getSelected().equals(desc.getName()))
			{
				selected += "name: " + desc.getName() + "\n";
				selected += "description: " + desc.getDescription() + "\n";
			}
		}
		
		descriptionDialog.setText(selected);
	}
}
