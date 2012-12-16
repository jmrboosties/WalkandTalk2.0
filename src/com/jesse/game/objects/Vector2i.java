package com.jesse.game.objects;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.JsonObject;
import com.jesse.game.utils.Gsonable;
import com.jesse.game.utils.Constants.Direction;

public class Vector2i implements Comparable<Vector2i>, Serializable, Gsonable {

	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	
	public Vector2i() {
		this(0, 0);
	}
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(Vector2i source) {
		x = source.x;
		y = source.y;
	}

	@Override
	public int compareTo(Vector2i comparedVector) {
		if(x == comparedVector.x && y == comparedVector.y)
			return 0;
		else if(Math.abs(x * y) > Math.abs(comparedVector.x * comparedVector.y))
			return 1;
		else
			return -1;
	}
	
	public boolean equals(Object o) {
		try {
			Vector2i comparedVector = (Vector2i) o;
			return (x == comparedVector.x && y == comparedVector.y);
		} catch(ClassCastException e) {
			return false;
		}
	}
	
	public int hashCode() {
		return new HashCodeBuilder(19, 37).
				append(x).
				append(y).
				toHashCode();
	}
	
	public String toString() {
		return x + ", " + y;
	}
	
	/**
	 * This method is used to reassign the x & y values using a different vector, but keeps it as the same object.
	 * 
	 * @param vector
	 */
	public void reassign(Vector2i vector) {
		x = vector.x;
		y = vector.y;
	}
	
	public boolean touching(Vector2i vector) {
		if(this.equals(vector))
			return false; //TODO trial
		else if(Math.abs(this.x - vector.x) == 1)
			return true;
		else if(Math.abs(this.y - vector.y) == 1)
			return true;
		else
			return false;
	}
	
	public Direction getDirectionToCoordinate(Vector2i vector) {
		if(this.equals(vector))
			return null;
		
		int hor = vector.x - x;
		if(hor > 0)
			return Direction.RIGHT;
		else if(hor < 0)
			return Direction.LEFT;
		
		//the game doesnt operate on a standard x/y axis... 0, 0 is upper left, not center.
		//this means going down is actually an increase in y value
		int ver = vector.y - y;
		if(ver > 0)
			return Direction.DOWN;
		else if(ver < 0)
			return Direction.UP;
		
		return null;			
	}

	@Override
	public JsonObject getGson(boolean... bools) {
		JsonObject jo = new JsonObject();
		jo.addProperty("x", x);
		jo.addProperty("y", y);
		return jo;
	}
	

}
