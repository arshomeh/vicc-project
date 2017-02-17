package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arsha on 30-Jan-17.
 * 
 * Scheduler purpose: ...
 * Design choice: ... 
 * Worst-case temporal complexity: ...
 */
public class AntiAffinityVmAllocationPolicy extends VmAllocationPolicy {

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;
    private final static int interval = 100;

    public AntiAffinityVmAllocationPolicy(List<? extends Host> hosts) {

        super(hosts);
        hoster = new HashMap<>();
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {

        int value = vm.getId() % interval; // by hashing the vm id with the number of host we guarantee the anti affinity
        for (Host host : getHostList()) {
            if (value == host.getId() % interval) { //choose the host with id value
                if (host.vmCreate(vm)) {
                    hoster.put(vm, host);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {

        int value = vm.getId() % interval;
        if (value != host.getId() % interval)
            return false;

        if(host.vmCreate(vm)) {
            hoster.put(vm, host);
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {

        return null;
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

        for (Host h: getHostList()){
            if (h.getVm(vmId,userId) != null){
                return h;
            }
        }
        return null;
    }
}
