package com.jesse.game.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandPattern {
	
	public static CommandPattern commandPattern;
	
	public static synchronized CommandPattern getInstance() {
		if(commandPattern == null)
			commandPattern = new CommandPattern();
			
		return commandPattern;
	}
	
	public static void test() throws IOException {
		Light light = getInstance().new Light();
		Command switchUp = getInstance().new TurnOnCmd(light);
		Command switchDown = getInstance().new TurnOffCmd(light);
		
		Switch s = getInstance().new Switch();
		
		Print.log("Use E and Q to turn switch on and off respectively.");
		String input;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while((input = reader.readLine()) != null) {
			
			if(input.substring(0, 1).equalsIgnoreCase("x"))
				break;
			
			if(input.substring(0, 1).equalsIgnoreCase("e"))
				s.store(switchUp);
			else if(input.substring(0, 1).equals("q"))
				s.store(switchDown);
			else if(input.substring(0, 1).equals("g"))
				s.executeAll();
			
			for (Command command : s.getHistory()) {
				Print.log(command.getCommand());
			}
				
		}
		
		System.exit(0);
	}
	
	public interface Command {
		
		public String getCommand();
		
		public void execute();
	}
	
	public class Switch {
		
		private List<Command> mHistory = new ArrayList<Command>();
		private List<Command> mTodoList = new ArrayList<Command>();
		
		public Switch() { }
		
		public void storeAndExecute(Command cmd) {
			mHistory.add(cmd);
			cmd.execute();
		}
		
		public void store(Command cmd) {
			mTodoList.add(cmd);
		}
		
		public void executeAll() {
			for (Command cmd : mTodoList) {
				cmd.execute();
				mHistory.add(cmd);
			}
			mTodoList.clear();
		}
		
		public List<Command> getHistory() {
			return mHistory;
		}
	}
	
	public class Light {
		
		public Light() { }
		
		public void turnOn() {
			Print.log("Light on");
		}
		
		public void turnOff() {
			Print.log("Light off");
		}
	}
	
	public class TurnOnCmd implements Command {

		private Light mLight;
		
		public TurnOnCmd(Light light) {
			mLight = light;
		}
		
		@Override
		public void execute() {
			mLight.turnOn();
		}

		@Override
		public String getCommand() {
			return TurnOnCmd.class.toString();
		}
		
	}
	
	public class TurnOffCmd implements Command {

		private Light mLight;
		
		public TurnOffCmd(Light light) {
			mLight = light;
		}
		
		@Override
		public void execute() {
			mLight.turnOff();
		}

		@Override
		public String getCommand() {
			return TurnOffCmd.class.toString();
		}
		
	}
	
}
