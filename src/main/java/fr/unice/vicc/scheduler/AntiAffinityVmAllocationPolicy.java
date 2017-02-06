package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
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

    public AntiAffinityVmAllocationPolicy(List<PowerHost> hosts) {

        super(hosts);
        hoster = new HashMap<>();
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
    	// no two hosts from the same interval have to be allocated to the same VM 
        for (Host host : getHostList()){
            if(host.vmCreate(vm)) {
                hoster.put(vm, host);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {

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
