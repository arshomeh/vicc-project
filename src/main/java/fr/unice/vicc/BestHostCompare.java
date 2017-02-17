package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.Comparator;

/**
 * Created by arsha on 16-Feb-17.
 * 
 * Comparator for Best-Fit scheduling algorithm.
 */
public class BestHostCompare implements Comparator<Host> {
	
    @Override
    public int compare(Host h1, Host h2) {

        PowerHost host1 = (PowerHost) h1;
        PowerHost host2 = (PowerHost) h2;

        if (host1.getMaxPower() < host2.getMaxPower())
            return -1;
        else if (host1.getMaxPower() > host2.getMaxPower())
            return 1;
        else {
            double cpu1 = h1.getAvailableMips();
            double cpu2 = h2.getAvailableMips();

            double ram1 = h1.getRamProvisioner().getAvailableRam();
            double ram2 = h2.getRamProvisioner().getAvailableRam();

            // weights of the two dimensions
            double x = 0.7;
            double y = 0.3;

            // linear combination
            double total1 = cpu1 * y + ram1 * x;
            double total2 = cpu2 * y + ram2 * x;

//        System.out.println("host 1: memory " + ram1 + " CPU " + cpu1 + " overall score " + total1);
//        System.out.println("host 2: memory " + ram2 + " CPU " + cpu2 + " overall score " + total2);
//        System.out.println("--------------------------------------------");
            if (total1 < total2)
                return -1;
            else if (total1 > total2)
                return 1;
            return 0;
        }
    }
    
}