package fr.unice.vicc.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

/**
 * Created by Leila K. on 01/02/2017
 * 
 * Scheduler purpose: the purpose of this scheduler is to ensure fault tolerance on the VMs
 * 		meaning that ...
 * Design choice: ...
 * Worst-case temporal complexity: O(N^2) where N = number of hosts since for the VM with ID
 * 		multiple of 10 there's need to reserve room in another host, so the host list has
 * 		to be examined twice in the worst case.
 */
public class FaultToleranceVmAllocationPolicy extends VmAllocationPolicy {
	private Map<Vm, Host> hoster;
	private Map<Vm, Host> reserve; 

    private final static int multiple = 10;

	public FaultToleranceVmAllocationPolicy(List<? extends Host> hosts) {
		super(hosts);
		hoster = new HashMap<>();
		reserve = new HashMap<>();
	}

	private boolean createHost(Host host, Vm vm) {
		if (host.vmCreate(vm)) {
            hoster.put(vm, host);
            //System.out.println("VM " + vm.getId() + " allocated on " + host.getId());
            return true;
        }
		return false; 
	}
	
	@Override
	public boolean allocateHostForVm(Vm vm) {
		int vmId = vm.getId();
        
        for (Host host : getHostList()) {
        	if (createHost(host, vm)) {
        		hoster.put(vm, host); 
        		if (vmId % multiple == 0) {
                	//ensure the fault tolerance to 1 node failure for all the VM having an id that is a multiple of 10
        			return allocateReserve(host.getId(), vm); 
        		} 
        		return true; 
        	}
        } 
        
        return false; 
	}
	
	private boolean allocateReserve(int originalHostId, Vm vm) {
		for (Host host : getHostList()) {
    		if (originalHostId != host.getId()) {
    			if (host.allocatePesForVm(vm, vm.getCurrentRequestedMips())) { 
					reserve.put(vm, host); 
					//System.out.println("VM " + vm.getId() + " is allocated on reserve " + host.getId());
					return true;
    			}
    		} 
    	} 
		return false; 
	}

	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		if (createHost(host, vm)) {
			if (vm.getId() % multiple == 0) {
				return allocateReserve(host.getId(), vm);	
			}
			return true; 
		}
		return false;  
	}

	@Override
	public void deallocateHostForVm(Vm vm) {
		if (vm.getId() % multiple == 0) {
			reserve.get(vm).deallocatePesForVm(vm);
			reserve.remove(vm);
		}
		Host toRemove = vm.getHost();
		toRemove.vmDestroy(vm);
		hoster.remove(vm, toRemove);
	}

	@Override
	public Host getHost(Vm vm) {
		return vm.getHost();
	}

	@Override
	public Host getHost(int vmId, int userId) {
        for (Host h: getHostList()) {
            if (h.getVm(vmId, userId) != null) {
                return h;
            }
        }
        
        // no such host
        return null;
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
		// nothing to do here
		return null;
	}

}
