package com.benjaminlimb.tutorial.hazelcastdemo;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class BasicExample {
	
	HazelcastInstance instance;
	public void setup()
	{
		Config config = new Config();
		config.getNetworkConfig().setPort(5900); // NetworkConfig.DEFAULT_PORT = 5701
	    instance = Hazelcast.newHazelcastInstance(config);	    
	}
	
	public BasicExample tearDown()
	{
		instance.shutdown();  		
		return this;
	}
	
	public BasicExample shutDownALl()
	{
		instance.getLifecycleService().shutdown();
		Hazelcast.shutdownAll();
		return this;
	}
	
	public void saveSomeRandomEntries()
	{
		Map<Integer, String> employees = instance.getMap("employees");
	 	int previousSize = employees.size();
	    for (int i = 0; i < 10; i++)
	    {
	    	final String address = instance.getLocalEndpoint().getSocketAddress().toString();
	    		employees.put(i+previousSize, "EMP_"+UUID.randomUUID().toString()+"@"+address);
	    }	
	}
	
	public void printEntries()
	{
		Map<Integer, String> employees = instance.getMap("employees");
		System.out.println("Employee List");
		TreeMap<Integer, String> map = new TreeMap<>(employees);
	    for (Map.Entry<Integer, String> employee : map.entrySet()) {			
	    		System.out.println(employee);
		}
	}
	public void run()
	{
		setup();
		saveSomeRandomEntries();
		printEntries();
		
		new Menu().addAction("d","Display Entries", new Runnable(){public void run(){printEntries();}})
		.addAction("a","Add Some more Entries", new Runnable(){public void run(){saveSomeRandomEntries();}})
		.addAction("t","Stop this instance of Hazelcast", new Runnable(){public void run(){tearDown();}})
		.addAction("s","Stop ALL instances of Hazelcast", new Runnable(){public void run(){ShutdownCluster.shutdown(instance);}})
		.run();
		
		    //tearDown();
	}
	
	public static void main(String[] args) {
		new BasicExample().run();
	}
	
	
}
