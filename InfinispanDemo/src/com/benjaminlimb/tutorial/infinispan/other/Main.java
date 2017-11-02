package com.benjaminlimb.tutorial.infinispan.other;

import static java.util.Calendar.MAY;

import java.util.GregorianCalendar;
import java.util.UUID;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.context.Flag;
import org.infinispan.manager.DefaultCacheManager;

public class Main {
    
    static class  Worker implements Runnable
     {
      private Thread t;
      private String threadName;
      
      Worker( String name) {
         threadName = name;
         System.out.println("Creating " +  threadName );
      }
      
      @Override
      public void run() {
        
        System.out.println("Running " +  threadName );
        
        // Setup up a clustered cache manager
        GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
        // Make the default cache a distributed synchronous one
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.clustering().cacheMode(CacheMode.DIST_SYNC);
        // Initialize the cache manager
        DefaultCacheManager cacheManager = new DefaultCacheManager(global.build(), builder.build());
        // Obtain the default cache
        Cache<String, String> cache = cacheManager.getCache();
        // Store the current node address in some random keys
        for(int i=0; i < 10; i++) 
        {
           cache.put(UUID.randomUUID().toString(), cacheManager.getNodeAddress());
        }
             
              
        // Display the current cache contents for the whole cluster      
        cache.entrySet().forEach(entry -> System.out.printf("%s = %s\n", entry.getKey(), entry.getValue()));
        // Display the current cache contents for this node
        cache.getAdvancedCache().withFlags(Flag.SKIP_REMOTE_LOOKUP)
           .entrySet().forEach(entry -> System.out.printf("%s = %s\n", entry.getKey(), entry.getValue()));
        // Stop the cache manager and release all resources
        cacheManager.stop();  
      }
            
      public void start() {
        System.out.println("Starting " +  threadName );
        if (t == null) {
           t = new Thread (this, threadName);
           t.start ();
        }
     }
    }
  
    public static void main(String[] args) throws Exception {
     
      Worker w1 = new Worker("Thread-1");
      w1.start();
            
    Person p = new Person(new GregorianCalendar(2000,MAY, 23),"Benjamin");
    System.out.println(p);
  }

}
