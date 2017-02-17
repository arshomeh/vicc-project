package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.Pe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leila K. on 17/02/2017
 * 
 * Scheduler purpose: the aim is to always give clients the desired MIPS so as not to incur
 * 		in SLAs violations and subsequent penalties.
 * Design choice: Before allocation we check that host has enough MIPS 
 * Worst-case temporal complexity: the allocation takes O(n) time complexity where n - number of hosts 
 */
public class NoViolationsVmAllocationPolicy extends VmAllocationPolicy {
    
    /** The map to track the server that host each running VM. */
	private Map<Vm, Host> hoster;
	
	public NoViolationsVmAllocationPolicy(List<? extends Host> hosts) {
		super(hosts);
		hoster = new HashMap<>();
	}
	
	private boolean checkAvailableMIPS(Host host, double MIPScount) {
		for (Pe pe : host.getPeList()) {
			if (pe.getPeProvisioner().getAvailableMips() >= MIPScount)
				return true; 
        }
		
		// no available MIPS
		return false;	
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
		// choose the first host in the host list having enough MIPS 
		for (Host host : getHostList()) {
			if (checkAvailableMIPS(host, vm.getMips())) {
				if (createHost(host, vm)) 
					return true; 	
			}
		}
			
		// no appropriate host found
		return false;
	}
	
	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		if (checkAvailableMIPS(host, vm.getMips())) {
			if (createHost(host, vm)) 
				return true; 	
		}
		
		// no such allocation possible
		return false;
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
