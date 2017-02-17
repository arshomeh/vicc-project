package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arsha on 16-Feb-17.
 * 
 * Scheduler purpose: similarly to a first fit algorithm, it sequentially searches for hosts
 * 		with enough free resources to allocate the next VM, but differently from a first fit approach
 * 		it starts from the last checked hosts to achieve better performances (since the first hosts
 * 		will likely have not so much free space). 
 * Design choice: we used a global variable for keeping track of which was the last host checked for
 * 		enough free resources. As long as a new VM arrives in the queue, the scheduling algorithm
 * 		will check the hosts starting from that last host, and then it will continue until a full
 * 		"round" is done: after the last host in the order it will start again from 0 and it will
 * 		only stop at the host that comes before the stored global value.
 * Worst-case temporal complexity: ...
 */
public class NextFitVmAllocationPolicy extends VmAllocationPolicy {

	/** Global variable referring to the last checked host ID. */
    int startFrom;
    
    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

    public NextFitVmAllocationPolicy(List<? extends Host> hosts) {
        super(hosts);
        hoster = new HashMap<>();
        startFrom = 0;
    }

    @Override
    protected void setHostList(List<? extends Host> hostList) {
        super.setHostList(hostList);
        hoster = new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> hosts) {
        // no optimizations here
        return null;
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        int last = startFrom;
        while (startFrom < getHostList().size() || startFrom == last - 1) {
            Host host = getHostList().get(startFrom);
            if (host.vmCreate(vm)) {
                hoster.put(vm, host);
//                System.out.println("VM " + host.getId() + " allocated");
                return true;
            }
            startFrom++;
            
            // check if we have to start again from host ID = 0
            if (startFrom >= getHostList().size())
                startFrom = 0;
        }
        
        // no appropriate host found!
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
    	// simply (try to) allocate the VM in the given host
        if (host.vmCreate(vm)) {
            hoster.put(vm, host);
            return true;
        }
        
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
        for (Host h : getHostList()) {
            if (h.getVm(vmId, userId) != null) {
                return h;
            }
        }
        
        // no such host
        return null;
    }
}
