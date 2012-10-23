package com.jesse.game.objects;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Vector2i implements Comparable<Vector2i>, Serializable {

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
	
}