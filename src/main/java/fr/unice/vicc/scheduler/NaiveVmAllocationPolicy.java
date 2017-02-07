package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;

import sun.misc.VM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fhermeni2 on 16/11/2015.
 */
public class NaiveVmAllocationPolicy extends VmAllocationPolicy {

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

    public NaiveVmAllocationPolicy(List<? extends Host> list) {

        super(list);
        hoster = new HashMap<>();
    }

    @Override
    protected void setHostList(List<? extends Host> hostList) {

        super.setHostList(hostList);
        hoster = new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
    	// no optimizations in the naive allocation
        return null;
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        Log.print(String.format("Trying to allocate VM %d", vm.getId()));
    	
        // check if the host can create the VM
        for (Host host : getHostList()) {
            if(host.vmCreate(vm)) {
                hoster.put(vm, host);
//                System.out.println(String.format("[+] Put VM %d in host %d", vm.getId(), host.getId()));
                return true;
            }
        }
        
        // no appropriate host found
        System.err.println(String.format("[-] Cannot allocate VM %d", vm.getId()));
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        Log.print(
        		String.format("Trying to allocate VM %d in host %d",
        				vm.getId(),
        				host.getId()));

        if(host.vmCreate(vm)) {
            hoster.put(vm, host);
            System.out.println(String.format("[+] Allocated VM %d in host %d", vm.getId(), host.getId()));
            return true;
        }
        
        // no appropriate host found
        System.err.println(String.format("[-] Cannot allocate VM %d on host %d", vm.getId(), host.getId()));
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
            if (h.getVm(vmId,userId) != null){
                return h;
            }
        }
        return null;
    }
}