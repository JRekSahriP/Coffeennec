package com.coffeennec.game.abstractions;

import java.util.ArrayList;
import java.util.List;

import com.coffeennec.graphics.buffers.CoffeeBuffer;

public abstract class GameObjectHandler<T extends GameObject> extends GameObject {
	private List<T> objectList;
	
	public GameObjectHandler() {
		objectList = new ArrayList<>();
	}
	
	@Override
	public void update() {
		getCopyList().forEach(e -> {
			if (e != null) e.update();
		});
	}
	
	@Override
	public void render(CoffeeBuffer b) {
		getCopyList().forEach(e -> {
			if (e != null) e.render(b);
		});
	}
	

	public void add(T object) {
		objectList.add(object);
	}
	public void remove(T object) {
		objectList.remove(object);
	}
	public void clear() {
		objectList.clear();
	}
	
	public List<T> getList() {
		return objectList;
	}
	public List<T> getCopyList() {
		return new ArrayList<>(objectList);
	}
}
