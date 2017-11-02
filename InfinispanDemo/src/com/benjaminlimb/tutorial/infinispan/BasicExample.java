package com.benjaminlimb.tutorial.infinispan;

import java.util.Map;
import java.util.UUID;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

public class BasicExample{

	DefaultCacheManager instance;
	
	public BasicExample setup()
	{
		instance = new DefaultCacheManager();
	    instance.defineConfiguration("local", new ConfigurationBuilder().build());
		
		return this;
	}
	
	public BasicExample tearDown()
	{
		instance.stop();  
		return this;
	}
	
	public void run()
	{
		setup();		 

        Cache<Integer, String> employees = instance.getCache("local");
        System.out.println("Cluster Name:"+instance.getClusterName());
        System.out.println("Cluster Size:"+instance.getClusterSize());
        System.out.println("Cluster Members:"+instance.getClusterMembers());        
        System.out.println("");
        
		    employees.put(1, "Joe");
		    employees.put(2, "Ali");
		    employees.put(3, "Avi");
		    
		    for (int i = 0; i < 10; i++)
		    {
		    		employees.put(i+4, UUID.randomUUID().toString() + "@" + instance.getNodeAddress());
		    }		    		   
		    
		    System.out.println("Employee List");
		    for (Map.Entry<Integer, String> employee : employees.entrySet()) {
				
		    		System.out.println(employee);
			}
		    
		    tearDown();
	}
	
	public static void main(String[] args)
	{
		new BasicExample().run();
	}
	
	
}
