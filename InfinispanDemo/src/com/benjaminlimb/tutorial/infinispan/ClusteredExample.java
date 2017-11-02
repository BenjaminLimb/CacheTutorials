package com.benjaminlimb.tutorial.infinispan;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.context.Flag;
import org.infinispan.manager.DefaultCacheManager;

public class ClusteredExample{

	DefaultCacheManager instance;
	
	public ClusteredExample setup()
	{
	      GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();		// Setup up a clustered cache manager
	      global.transport().clusterName("MyCluster");
	      GlobalConfiguration globalConfiguration = global.build();
	      
	      ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();			      // Make the default cache a replicated synchronous one	    	    
	      configurationBuilder.clustering().cacheMode(CacheMode.REPL_SYNC);
	      
	      Configuration configuration = configurationBuilder.build();
	      
	      //instance.defineConfiguration("local", new ConfigurationBuilder().build());	      
	      instance = new DefaultCacheManager(globalConfiguration, configuration);			      // Initialize the cache manager	      
		
		return this;
	}
	
	public ClusteredExample tearDown()
	{
		instance.stop();  
		return this;
	}
	
	public void displayClusterInformation()
	{
        
        System.out.println("Cluster Name:"+instance.getClusterName());
        System.out.println("Cluster Size:"+instance.getClusterSize());
        System.out.println("Cluster Members:"+instance.getClusterMembers());        
        System.out.println("");
            
	}
	
	public void saveSomeRandomEntries()
	{
		Map<Integer, String> employees = instance.getCache();
		
	 	int previousSize = employees.size();
	    for (int i = 0; i < 10; i++)
	    {
	    	
	    	final String address = instance.getNodeAddress().toString();
	    		employees.put(i+previousSize, "EMP_"+UUID.randomUUID().toString()+"@"+address);
	    }	
	}
	
	public void printEntries()
	{
		Cache<Integer,String> employees = instance.getCache();
		
		System.out.println("Employee List");
		TreeMap<Integer, String> map = new TreeMap<>(employees);
	    for (Map.Entry<Integer, String> employee : map.entrySet()) {			
	    		System.out.println(employee);
		}	    

	    System.out.println("Cache Contents:");
	    employees.entrySet().forEach(entry -> System.out.printf("%s = %s\n", entry.getKey(), entry.getValue()));
	    System.out.println("TotalCount:"+employees.size());
	    
	    System.out.println("Local Cache Contents:");
        // Display the current cache contents for this node
	    employees.getAdvancedCache().withFlags(Flag.SKIP_REMOTE_LOOKUP)
           .entrySet().forEach(entry -> System.out.printf("%s = %s\n", entry.getKey(), entry.getValue()));
	    System.out.println("TotalCount:"+employees.getAdvancedCache().withFlags(Flag.SKIP_REMOTE_LOOKUP).size());
	}
	public void run()
	{
		setup();
		saveSomeRandomEntries();
		printEntries();
		
		new Menu().addAction("d","Display Entries", new Runnable(){public void run(){printEntries();}})
		.addAction("a","Add Some more Entries", new Runnable(){public void run(){saveSomeRandomEntries();}})
		.addAction("t","Stop this instance of Infinispan", new Runnable(){public void run(){tearDown();}})		
		.addAction("c","Display cluster information", new Runnable(){public void run(){displayClusterInformation();}})
		.addAction("x","Shut Down the instance", new Runnable(){public void run(){tearDown();}})
		.run();
		
	}
	
	public static void main(String[] args)
	{
		new ClusteredExample().run();
	}
	
	
}
