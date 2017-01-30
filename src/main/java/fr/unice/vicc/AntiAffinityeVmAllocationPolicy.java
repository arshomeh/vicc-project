package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.List;
import java.util.Map;

/**
 * Created by arsha on 30-Jan-17.
 */
public class AntiAffinityeVmAllocationPolicy extends VmAllocationPolicy {
    public AntiAffinityeVmAllocationPolicy(List<PowerHost> hosts) {
        super(hosts);
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
        return null;
    }

    @Override
    public void deallocateHostForVm(Vm vm) {

    }

    @Override
    public Host getHost(Vm vm) {
        return null;
    }

    @Override
    public Host getHost(int i, int i1) {
        return null;
    }
}
