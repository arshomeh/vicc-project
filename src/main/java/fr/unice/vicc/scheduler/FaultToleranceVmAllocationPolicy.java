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

    private final static int multiple = 10;
    
    /** The map to track the server that host each running VM. */
	private Map<Vm, Host> hoster;

	public FaultToleranceVmAllocationPolicy(List<? extends Host> hosts) {
		super(hosts);
		hoster = new HashMap<>();
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
		boolean bCheckIfSuitable = false; 
		
		if (vmId % multiple == 0)
			bCheckIfSuitable = true; 
        
        for (Host host : getHostList()) {
        	if (bCheckIfSuitable) {
        		if (!host.isSuitableForVm(vm))
        			continue; //allocate only if host is suitable 
        		//otherwise we allocate on any host (actually on the first where creation was successful) 
        	} 
 
        	if (createHost(host, vm)) {
                return true;
            }   		
        } 
        
        return false; 
	}
     
	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		// check if the host is appropriate for ensuring the fault tolerance for this VM 
		if ((vm.getId() % multiple == 0) && !host.isSuitableForVm(vm))
			return false;
		
		return createHost(host, vm);  
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
