package com.benjaminlimb.tutorial.hazelcastdemo;

import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

public class Menu {
	class Item 
	{
		public String commandName;
		public String commandDescription;		
		public Runnable action;		
	}
	public void printOptions()
	{
		System.out.println("Options:");
		
		Iterator<Item> itr = items.values().iterator();		
		while(itr.hasNext())
		{
			Item item= itr.next();
			System.out.println(item.commandName + " - " + item.commandDescription);			
		}		
	}
	
	private TreeMap<String,Item> items = new TreeMap<>();

	public Menu addAction(String name, String description, Runnable action)
	{		
		Item i = new Item();
		i.commandName = name;
		i.commandDescription = description;
		i.action = action;
		
		items.put(name, i);
		
		return this;
	}
	
	public void run()
	{
	     Scanner input = new Scanner(System.in);
	        boolean mainLoop = true;

	        String choice;
	        do{
	        		printOptions();
	            choice = input.nextLine();
	            if(choice.equalsIgnoreCase("q") || choice.equalsIgnoreCase("quit"))
	            {
	            		return;
	            }
	            if (items.containsKey(choice))
	            {
	            		Item item = items.get(choice);
	            		item.action.run();        		
	            }
	            else
	            {
	            		System.out.println(choice + " is not a valid Menu Option! Please Select Another.");
	            }
	            
	    }while(choice != "quit");
    }
	
	public static void main(String[] args)
	{
		Menu m = new Menu();
		m.addAction("d","display all values", new Runnable(){ public void run() {System.out.println("Displaying all values...");	}});
		m.run();
		
	}
}
