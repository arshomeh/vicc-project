package fr.unice.vicc.scheduler;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoViolationsVmAllocationPolicy extends VmAllocationPolicy {

	public NoViolationsVmAllocationPolicy(List<? extends Host> list) {
		super(list);
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
