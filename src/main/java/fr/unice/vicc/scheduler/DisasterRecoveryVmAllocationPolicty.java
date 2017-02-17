package fr.unice.vicc.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

/**
 * Disaster recovery allocation policy.
 * 
 * @author Manfredi Giordano
 * @version 06/02/2017
 */
public class DisasterRecoveryVmAllocationPolicty extends VmAllocationPolicy {

    /** The map to track the server that host each running VM. */
    private Map<Vm, Host> hoster;

	public DisasterRecoveryVmAllocationPolicty(List<? extends Host> hosts) {
		super(hosts);
        hoster = new HashMap<>();
	}

	@Override
	public boolean allocateHostForVm(Vm arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allocateHostForVm(Vm arg0, Host arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deallocateHostForVm(Vm arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Host getHost(Vm arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Host getHost(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
