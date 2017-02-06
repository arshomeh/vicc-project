package fr.unice.vicc.scheduler;

import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

/**
 * Created by Leila K. on 01/02/2017.
 * 
 * Scheduler purpose: ...
 * Design choice: ... 
 * Worst-case temporal complexity: ...
 */
public class FaultToleranceVmAllocationPolicy extends VmAllocationPolicy {

	public FaultToleranceVmAllocationPolicy(List<? extends Host> hosts) {
		super(hosts);
	}

	@Override
	public boolean allocateHostForVm(Vm arg0) {
		return false;
	}

	@Override
	public boolean allocateHostForVm(Vm arg0, Host arg1) {
		return false;
	}

	@Override
	public void deallocateHostForVm(Vm arg0) {
		
	}

	@Override
	public Host getHost(Vm arg0) {
		return null;
	}

	@Override
	public Host getHost(int arg0, int arg1) {
		return null;
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> arg0) {
		return null;
	}

}
