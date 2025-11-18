package com.coffeennec.game.abstractions;

import java.util.ArrayList;
import java.util.List;

import com.coffeennec.graphics.buffers.CoffeeBuffer;

public abstract class GameObjectHandler<T extends GameObject> extends GameObject {
	private List<T> objectList;
	
	public GameObjectHandler() {
		this.objectList = new ArrayList<>();
	}
	
	@Override
	public void update() {
		this.getCopyList().forEach(e -> {
			if (e != null) e.update();
		});
	}
	
	@Override
	public void render(CoffeeBuffer b) {
		this.getCopyList().forEach(e -> {
			if (e != null) e.render(b);
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
