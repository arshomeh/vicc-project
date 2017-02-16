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
 */
public class NextFitVmAllocationPolicy extends VmAllocationPolicy {

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;
    int i;

    public NextFitVmAllocationPolicy(List<? extends Host> list) {

        super(list);
        hoster = new HashMap<>();
        i = 0;
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
        int last = i;

        while( i < getHostList().size() || i == last-1){
            Host host = getHostList().get(i);
            if(host.vmCreate(vm)) {
                hoster.put(vm, host);
//                System.out.println("VM " + host.getId() + " allocated");
                return true;
            }
            i++;
            if(i >= getHostList().size())
                i = 0;
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
        return false;
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
