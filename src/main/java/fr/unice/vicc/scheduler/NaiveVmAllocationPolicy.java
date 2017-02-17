package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fhermeni2 on 16/11/2015.
 */
public class NaiveVmAllocationPolicy extends VmAllocationPolicy {

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

    public NaiveVmAllocationPolicy(List<? extends Host> hosts) {
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
    	// no optimizations in the naive allocation
        return null;
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        // the first host in the host list having enough resources will be the one allocated
        for (Host host : getHostList()) {
            if(host.vmCreate(vm)) {
                hoster.put(vm, host);
//                System.out.println("VM " + host.getId() + " allocated");
                return true;
            }
        }
        
        // no appropriate host found!
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
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
            if (h.getVm(vmId, userId) != null){
                return h;
            }
        }
        
        // no such host
        return null;
    }
}