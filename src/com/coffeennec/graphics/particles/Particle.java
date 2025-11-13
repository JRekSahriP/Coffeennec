package com.coffeennec.graphics.particles;

import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import com.coffeennec.game.abstractions.GameObject;
import com.coffeennec.graphics.FennecColor.Hex;
import com.coffeennec.graphics.buffers.CoffeeBuffer;
import com.coffeennec.graphics.particles.effects.ParticleEffect;
import com.coffeennec.graphics.particles.movements.ParticleMovement;
import com.coffeennec.math.FennecMath;
import com.coffeennec.math.Point2D;

public class Particle extends GameObject {

	private Point2D position;
	private final Point2D initialPosition;
	private Dimension size;
	
	private float speed;
	private float angle;
	
	private float rotationSpeed;
	private float rotation;
	
	private final Predicate<Particle> disposeCondition;
	
	private final ParticleEffect effect;
	private final ParticleMovement movement;
	
	private Hex currentColor;
	private Hex[] colors;
	private AtomicInteger colorIdx;
	
	private int age;
	
	
	public Particle(Point2D position, Dimension size, float speed, float angle, float rotationSpeed, Hex[] colors, ParticleEffect effect, ParticleMovement movement, Predicate<Particle> condition) {
		initialPosition = position;
		this.position = new Point2D(initialPosition);
		this.size = size;
		this.speed = speed;
		this.angle = angle;
		this.rotationSpeed = rotationSpeed;
		this.colors = colors;
		this.effect = effect;
		this.movement = movement;
		disposeCondition = condition;

		
		rotation = 0;
		currentColor = colors[0];
		colorIdx = new AtomicInteger(0);
		age = 0;
	}	
	
	public Particle(Particle other) {
        this.position = new Point2D(other.position);
        this.initialPosition = new Point2D(other.initialPosition);
        this.size = new Dimension(other.size);
        this.speed = other.speed;
        this.angle = other.angle;
        this.disposeCondition = other.disposeCondition;
        this.effect = other.effect;
        this.movement = other.movement;
        this.colorIdx = new AtomicInteger(other.colorIdx.get());
        this.currentColor = new Hex(other.currentColor.getHex());
        this.colors = other.colors != null ? other.colors.clone() : new Hex[1];
        this.age = other.age;
    }
	
	@Override
	public void update() {
		move();
		randomMove();
		setColor();
		age++;
	}

	
	private void move() {
		movement.move(this);
	}
	
	private void randomMove() {
		Point2D amount = new Point2D();
		amount.x = FennecMath.random(-1.0f, 1.0f) * speed;
		amount.y = FennecMath.random(-1.0f, 1.0f) * speed;
		position.add(amount);
	}
	
	private void setColor() {
		if (colors.length == 1) {
			currentColor = colors[0];
		}
		currentColor = effect.nextColor(colors, colorIdx);
	}
	
	@Override
	public void render(CoffeeBuffer b) {
		int x = (int) position.x;
		int y = (int) position.y;
		int width = size.width;
		int height = size.height;
		b.getRenderer().fillRect(x, y, width, height, currentColor);
	}
	
	public boolean isEnded() {
		return disposeCondition.test(this);
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Hex[] getColors() {
		return colors;
	}

	public void setColors(Hex[] colors) {
		this.colors = colors;
	}

	public AtomicInteger getColorIdx() {
		return colorIdx;
	}

	public int getAge() {
		return age;
	}

	public Point2D getInitialPosition() {
		return initialPosition;
	}

	public Predicate<Particle> getDisposeCondition() {
		return disposeCondition;
	}

	public ParticleEffect getEffect() {
		return effect;
	}

	public ParticleMovement getMovement() {
		return movement;
	}

	
}
