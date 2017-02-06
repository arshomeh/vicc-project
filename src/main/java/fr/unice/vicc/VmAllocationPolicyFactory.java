package fr.unice.vicc;

import java.util.List;

import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import fr.unice.vicc.scheduler.AntiAffinityeVmAllocationPolicy;
import fr.unice.vicc.scheduler.FaultToleranceVmAllocationPolicy;
import fr.unice.vicc.scheduler.NaiveVmAllocationPolicy;


/**
 * @author Fabien Hermenier
 */
public class VmAllocationPolicyFactory {

    /**
     * Return the VMAllocationPolicy associated to id
     * @param id the algorithm identifier
     * @param hosts the host list
     * @return the selected algorithm
     */
    VmAllocationPolicy make(String id, List<PowerHost> hosts) {
        switch (id) {
            case "naive":  return new NaiveVmAllocationPolicy(hosts);
            case "antiAffinity":  return new AntiAffinityeVmAllocationPolicy(hosts);
            case "ft": return new FaultToleranceVmAllocationPolicy(hosts);
        }
        throw new IllegalArgumentException("No such policy '" + id + "'");
    }
}
