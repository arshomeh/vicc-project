package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import fr.unice.vicc.BestHostCompare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arsha on 17-Feb-17.
 * 
 * Scheduler purpose: the purpose of this scheduler is to get as much revenues as possible.
 * Design choice: ... best fit + is it soutable
 * Worst-case temporal complexity: ...
 */
public class GreedyVmAllocationPolicy extends VmAllocationPolicy {

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

    public GreedyVmAllocationPolicy(List<? extends Host> hosts) {
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
        // the first host in the list having enough available resources will be the one allocated
        List<Host> host = getHostList();
        host.sort(new BestHostCompare());
        for (Host host1 : host) {
            if(host1.isSuitableForVm(vm)) {
                if (host1.vmCreate(vm)) {
                    hoster.put(vm, host1);
                    return true;
                }
            }
        }

        // no appropriate host found!
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        if(host.vmCreate(vm)) {
            hoster.put(vm, host);
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
        for (Host h: getHostList()){
            if (h.getVm(vmId, userId) != null){
                return h;
            }
        }
        
        // no such host found
        return null;
    }
}
