package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arsha on 30-Jan-17.
 * 
 * Scheduler purpose: for fault tolerance reasons VMs are placed into different hosts with regards
 * 		to their affinity. In particular, two VMs whose ID belongs to the same hundred will be placed
 * 		on different physical hosts. The implication of this desired property on the cluster hosting
 * 		capacity is that we need at least a number of hosts equal to the desired anti-affinity interval
 * 		(say, 100) so that all the VMs can be allocated somewhere.
 * Design choice: the anti-affinity property is guaranteed by the modulo operation that let
 * 		us (try to) allocate a VM in the host with ID equal to the VM ID modulo the interval value
 * 		(e.g. modulo 100; for instance the VM with ID 315 will be allocated into the host with ID 15).
 * Worst-case temporal complexity: O(N) where N is the number of hosts since our implementation
 * 		checks all the list of hosts in the worst case only to find the host with the right ID.
 */
public class AntiAffinityVmAllocationPolicy extends VmAllocationPolicy {
	
	/** Anti-affinity interval. */
    private final static int interval = 100;

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

    public AntiAffinityVmAllocationPolicy(List<? extends Host> hosts) {
        super(hosts);
        hoster = new HashMap<>();
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
    	// by hashing the VM ID with the number of host we guarantee the anti-affinity
        int value = vm.getId() % interval;
        
        for (Host host : getHostList()) {
        	// choose the host with the same ID value
            if (value == host.getId() % interval) {
                if (host.vmCreate(vm)) {
                    hoster.put(vm, host);
                    return true;
                }
            }
        }
        
        // no such allocation possible
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
    	// the allocation can only occur if the host is the appropriate for the VM
        if (vm.getId() % interval != host.getId() % interval && host.vmCreate(vm)) {
            hoster.put(vm, host);
            return true;
        }
        
        // no such allocation possible
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
    	// no optimizations here
        return null;
    }

    @Override
    public void deallocateHostForVm(Vm vm) {
    	Host hostToRemove = vm.getHost();
    	hostToRemove.vmDestroy(vm);
    	hoster.remove(vm, hostToRemove);
    }

    @Override
    public Host getHost(Vm vm) {
        return vm.getHost();
    }

    @Override
    public Host getHost(int vmId, int userId) {
        for (Host h: getHostList()) {
            if (h.getVm(vmId, userId) != null){
                return h;
            }
        }
        
        // no such host
        return null;
    }
}
