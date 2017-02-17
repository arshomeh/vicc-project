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
 * Scheduler purpose: in order to minimize losses in case of a single switch failure, all the
 * 		incoming VMs will be scheduled into the hosts in the most balanced way possible: the two
 * 		switches define two classes of hosts, the ones connected to the first switch and the ones
 * 		connected to the second switch. The new VMs arriving in the queue will be allocated
 * 		alternating the host families.
 * Design choice: we used a global variable in order to keep trace of where the last allocation
 * 		happened, so that the next one could be done in the first available host belonging to the
 * 		other class (i.e. connected to the other switch with respect to the last used host).
 * Worst-case temporal complexity: O(N) where N is the number of hosts since in the worst case
 * 		all the hosts in the list will be checked.
 * 
 * @author Manfredi Giordano
 * @version 06/02/2017
 */
public class DisasterRecoveryVmAllocationPolicy extends VmAllocationPolicy {

	/** Global variable referring to the switch the host where the last VM was allocated was connected to. */
	int prev;
	
	/** The map to track the server that host each running VM. */
	private Map<Vm, Host> hoster;

	public DisasterRecoveryVmAllocationPolicy(List<? extends Host> hosts) {
		super(hosts);
		hoster = new HashMap<>();
		prev = -1;
	}

	@Override
	protected void setHostList(List<? extends Host> hostList) {
		super.setHostList(hostList);
		hoster = new HashMap<>();
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> hosts) {
		// no optimizations here
		return null;
	}

	@Override
	public boolean allocateHostForVm(Vm vm) {
		// the first host in the host list having enough resources will be the one allocated
		for (Host current : getHostList()) {
			int hostFamily = current.getTotalMips();
			if (hostFamily != prev) {
				if (current.vmCreate(vm)) {
					hoster.put(vm, current);
					prev = hostFamily;
//                System.out.println("VM " + host.getId() + " allocated");
					return true;
				}
			}
		}

		// no appropriate host found!
		return false;
	}

	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		int hostFamily = host.getTotalMips();
		if (hostFamily == prev)
			return false;
		if (host.vmCreate(vm)) {
			hoster.put(vm, host);
			prev = hostFamily;
			return true;
		}
		
		// no such allocation possible
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
		for (Host h : getHostList()) {
			if (h.getVm(vmId, userId) != null) {
				return h;
			}
		}
		
		// no such host found
		return null;
	}
}
