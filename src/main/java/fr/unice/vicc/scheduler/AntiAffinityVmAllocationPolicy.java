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
 * Scheduler purpose: no two hosts from the same affinity interval have to be allocated to the same VM
 * Design choice: the assignment to the VM to an host is made with the modulo operation
 * Worst-case temporal complexity: ...
 */
public class AntiAffinityVmAllocationPolicy extends VmAllocationPolicy {

	/** Interval for affinity */
    final static private int interval = 100;
    
    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

    public AntiAffinityVmAllocationPolicy(List<? extends Host> hosts) {
        super(hosts);
        hoster = new HashMap<>();
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        int wantedHostId = vm.getId() % interval;
        Host host = getHostList().get(wantedHostId);
        Log.print(
        		String.format("Trying to allocate VM %d in host %d",
        				vm.getId(),
        				host.getId()));
        
        // check if the host can create the VM
        if(host.vmCreate(vm)) {
            hoster.put(vm, host);
            System.out.println(String.format("[+] Allocated VM %d in host %d", vm.getId(), host.getId()));
            return true;
        }
        
        // no appropriate host found
        System.err.println(
        		String.format("[-] Cannot allocate VM %d on wanted host %d",
        				vm.getId(),
        				wantedHostId));
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        int wantedHostId = vm.getId() % interval;
        Log.print(
        		String.format("Trying to allocate VM %d in host %d (requested host: %d)",
        				vm.getId(),
        				wantedHostId,
        				host.getId()));

        // check if the host can create the VM
        if(host.getId() == wantedHostId && host.vmCreate(vm)) {
            hoster.put(vm, host);
            System.out.println(String.format("[+] Allocated VM %d in host %d", vm.getId(), host.getId()));
            return true;
        }
        
        // no appropriate host found
        System.err.println(String.format("[-] Cannot allocate VM %d on host %d", vm.getId(), host.getId()));
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
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
        for(Host h: getHostList()) {
            if (h.getVm(vmId,userId) != null){
                return h;
            }
        }
        return null;
    }
}
