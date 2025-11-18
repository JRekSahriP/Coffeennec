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
		this.initialPosition = position;
		this.position = new Point2D(this.initialPosition);
		this.size = size;
		this.speed = speed;
		this.angle = angle;
		this.rotationSpeed = rotationSpeed;
		this.colors = colors;
		this.effect = effect;
		this.movement = movement;
		this.disposeCondition = condition;

		
		this.rotation = 0;
		this.currentColor = colors[0];
		this.colorIdx = new AtomicInteger(0);
		this.age = 0;
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
		this.move();
		this.randomMove();
		this.setColor();
		this.age++;
	}

	
	private void move() {
		this.movement.move(this);
	}
	
	private void randomMove() {
		Point2D amount = new Point2D();
		amount.x = FennecMath.random(-1.0f, 1.0f) * this.speed;
		amount.y = FennecMath.random(-1.0f, 1.0f) * this.speed;
		this.position.add(amount);
	}
	
	private void setColor() {
		if (this.colors.length == 1) {
			this.currentColor = this.colors[0];
		}
		this.currentColor = this.effect.nextColor(this.colors, this.colorIdx);
	}
	
	@Override
	public void render(CoffeeBuffer b) {
		int x = (int) this.position.x;
		int y = (int) this.position.y;
		int width = this.size.width;
		int height = this.size.height;
		b.getRenderer().fillRect(x, y, width, height, this.currentColor);
	}
	
	public boolean isEnded() {
		return this.disposeCondition.test(this);
	}

	public Point2D getPosition() {
		return this.position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public Dimension getSize() {
		return this.size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getAngle() {
		return this.angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getRotationSpeed() {
		return this.rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public float getRotation() {
		return this.rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Hex[] getColors() {
		return this.colors;
	}

	public void setColors(Hex[] colors) {
		this.colors = colors;
	}

	public AtomicInteger getColorIdx() {
		return this.colorIdx;
	}

	public int getAge() {
		return this.age;
	}

	public Point2D getInitialPosition() {
		return this.initialPosition;
	}

	public Predicate<Particle> getDisposeCondition() {
		return this.disposeCondition;
	}

	public ParticleEffect getEffect() {
		return this.effect;
	}

	public ParticleMovement getMovement() {
		return this.movement;
	}

	
}
