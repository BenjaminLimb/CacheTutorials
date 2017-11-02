package com.benjaminlimb.tutorial.hazelcastdemo;

import java.io.Serializable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IExecutorService;

public class ShutdownCluster {

public static void shutdown(HazelcastInstance instance) {
    
    if(instance.getPartitionService().isClusterSafe()) {
        IExecutorService executorService = instance.getExecutorService(ShutdownCluster.class.getName());
        executorService.executeOnAllMembers(new ShutdownMember());
    }
}

private static class ShutdownMember implements Runnable, HazelcastInstanceAware, Serializable {
    
	private static final long serialVersionUID = 123241234L;
	private HazelcastInstance node;

    @Override
    public void run() {
        node.getLifecycleService().shutdown();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance node) {
        this.node = node;
    }
}
}
