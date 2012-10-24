package com.jesse.server;

import java.io.IOException;

import com.jesse.game.utils.Print;

public class ServerLauncher {

	public static void main(String[] args) throws IOException {
		Print.log("begin");
		new Server().start();
	}
	
}
