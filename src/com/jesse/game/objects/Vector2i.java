package com.jesse.game.objects;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.JsonObject;
import com.jesse.game.utils.Gsonable;

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
	
	public boolean touching(Vector2i vector) {
		if(this.equals(vector))
			return true;
		else if(Math.abs(this.x - vector.x) == 1)
			return true;
		else if(Math.abs(this.y - vector.y) == 1)
			return true;
		else
			return false;
	}

	@Override
	public JsonObject getGson(boolean... bools) {
//		return "{\"x\":"+x+",\"y\":"+y+"}";
//		return new Gson().toJson(this);
//		return (JsonObject) new Gson().toJsonTree(this);
		JsonObject jo = new JsonObject();
		jo.addProperty("x", x);
		jo.addProperty("y", y);
		return jo;
	}
	
	public JsonObject test() {
		JsonObject jo = new JsonObject();
		jo.addProperty("x", x);
		jo.addProperty("y", y);
		return jo;
	}
	
}
