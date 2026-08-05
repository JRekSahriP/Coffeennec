package org.coffeennec.game.abstractions;

import java.util.ArrayList;
import java.util.List;

import org.coffeennec.game.contexts.GameContext;
import org.coffeennec.game.contexts.RenderContext;

public abstract class GameObjectHandler<T extends GameObject> extends GameObject {
	private List<T> objectList;
	
	public GameObjectHandler() {
		this.objectList = new ArrayList<>();
	}
	
	@Override
	public void update(GameContext ctx) {
		this.getCopyList().forEach(e -> {
			if (e != null) e.update(ctx);
		});
	}
	
	@Override
	public void render(RenderContext ctx) {
		this.getCopyList().forEach(e -> {
			if (e != null) e.render(ctx);
		});
	}
	

	public void add(T object) {
		this.objectList.add(object);
	}
	public void remove(T object) {
		this.objectList.remove(object);
	}
	public void clear() {
		this.objectList.clear();
	}
	
	public List<T> getList() {
		return this.objectList;
	}
	public List<T> getCopyList() {
		return new ArrayList<>(this.objectList);
	}
}
