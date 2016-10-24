package com.custardgames.sudokil.managers;

import com.custardgames.sudokil.entities.ecs.components.PositionComponent;
import com.custardgames.sudokil.entities.ecs.components.tween.PositionComponentAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class UniTweenManager
{
	private static TweenManager tweenManager;

	public UniTweenManager()
	{
		tweenManager = new TweenManager();
		Tween.registerAccessor(PositionComponent.class, new PositionComponentAccessor());
	}
	
	public static TweenManager getTweenManager()
	{
		return tweenManager;
	}

	public static void setTweenManager(TweenManager tweenManager)
	{
		UniTweenManager.tweenManager = tweenManager;
	}
	
	public static void update(float delta)
	{
		tweenManager.update(delta);
	}
}
