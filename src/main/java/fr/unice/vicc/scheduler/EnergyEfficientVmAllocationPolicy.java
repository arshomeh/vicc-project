package fr.unice.vicc.scheduler;

import fr.unice.vicc.BestHostCompare;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arsha on 16-Feb-17.
 * 
 * Scheduler purpose: the purpose of this scheduler is to consume as little energy as possible.
 * 		The strategy we used is to allocate all the VMs in the smallest possible number of different
 * 		hosts as in a Best-Fit scheduling algorithm.
 * Design choice: the Best-Fit algorithm is implemented by directly implementing the {@link
 * 		BestHostCompare#compare} method.
 * Worst-case temporal complexity: the allocation here takes O(1) time complexity but it follows
 * 		a sorting phase whose worst-case temporal complexity is N*log(N) where N is the number of
 * 		hosts as from the official Java documentation.
 */
public class EnergyEfficientVmAllocationPolicy extends VmAllocationPolicy {

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

    public EnergyEfficientVmAllocationPolicy(List<? extends Host> hosts) {
        super(hosts);
        hoster = new HashMap<>();
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
    	// get the list of available hosts, ordered with the criterion of "more available resources"
        List<Host> host = getHostList();
        host.sort(new BestHostCompare());
        
        for (Host currentBest : host) {
            if (currentBest.vmCreate(vm)) {
                hoster.put(vm, currentBest);
//                System.out.println("VM " + best.getId() + " allocated");
                return true;
            }
        }

        // no appropriate host found!
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
    	// nothing special here
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
        for (Host h: getHostList()){
            if (h.getVm(vmId, userId) != null) {
                return h;
            }
        }
        
        // no such host found
        return null;
    }
}
