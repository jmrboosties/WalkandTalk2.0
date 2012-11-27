package com.jesse.game.data;

import java.io.Serializable;

public class Packet implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Command mCommand;
	private GameSnapshot mState;
	private PlayerHolder mPlayer;

}
