package PokerAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Human extends Player {

	Human(String name, int deposit) {
		super(name, deposit);
	}
	
	public String getInput() {
		BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
		try {
			return buffer.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	

}
