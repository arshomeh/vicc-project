package fr.unice.vicc.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

/**
 * Created by Leila K. on 01/02/2017
 */
public class FaultToleranceVmAllocationPolicy extends VmAllocationPolicy {
	private Map<Vm, Host> hoster;
    private final static int multiple = 10;

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
		//if we should ensure the fault tolerance for this VM but host is not appropriate
		if ((vm.getId() % multiple == 0) && !host.isSuitableForVm(vm))
			return false;
		
		return createHost(host, vm);  
	}

	@Override
	public void deallocateHostForVm(Vm vm) {
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
        return null;
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
		return null;
	}

}
