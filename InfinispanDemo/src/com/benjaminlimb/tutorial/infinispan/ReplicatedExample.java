package com.benjaminlimb.tutorial.infinispan;

import java.util.Map;
import java.util.UUID;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.context.Flag;
import org.infinispan.manager.DefaultCacheManager;


public class ReplicatedExample{

	DefaultCacheManager instance;
	
	public ReplicatedExample setup()
	{
		
		// Setup up a clustered cache manager
	      GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
	      // Make the default cache a replicated synchronous one
	      ConfigurationBuilder builder = new ConfigurationBuilder();
	      builder.clustering().cacheMode(CacheMode.REPL_SYNC);
	      // Initialize the cache manager
	      instance = new DefaultCacheManager(global.build(), builder.build());
	      // Obtain the default cache
		
		return this;
	}
	
	public ReplicatedExample tearDown()
	{
		instance.stop();  
		return this;
	}
	
	public void displayValues()
	{
		  Cache<String, String> cache = instance.getCache();
		// Display the current cache contents for the whole cluster
	      cache.entrySet().forEach(entry -> System.out.printf("%s = %s\n", entry.getKey(), entry.getValue()));
	      // Display the current cache contents for this node
	      cache.getAdvancedCache().withFlags(Flag.SKIP_REMOTE_LOOKUP)
	         .entrySet().forEach(entry -> System.out.printf("%s = %s\n", entry.getKey(), entry.getValue()));
	      // Stop the cache manager and release all resources
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
		    
		    displayValues();
		    tearDown();
	}
	
	public static void main(String[] args)
	{
		new ReplicatedExample().run();
	}
	
}
