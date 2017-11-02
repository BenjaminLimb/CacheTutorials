package com.benjaminlimb.tutorial.infinispan;


import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;

import com.benjaminlimb.tutorial.infinispan.other.Person;

@Listener(clustered = true)
public class CacheListener {

   @CacheEntryCreated
   public void entryCreated(CacheEntryCreatedEvent<String, Person> event) {
      if (!event.isOriginLocal()) {
         System.out.printf("-- Entry for %s modified by another node in the cluster\n", event.getKey());
      }
   }
}
