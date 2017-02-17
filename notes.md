# Notes about the project

## The team

- Leila Kuntar: leila.kuntar@gmail.com
- Manfredi Giordano: manfredi.giordano@me.com
- Arsak Megkrampian: sdi1100049@di.uoa.gr

We liked this course. We have learned a lot of useful things.  

## Results

### AntiAffinity:
for fault tolerance reasons VMs are placed into different hosts with regards to their affinity.

	Incomes:    12479,04€
	Penalties:  200,38€
	Energy:     2700,91€
	Revenue:    9577,75€
	
### Disaster Recovery:
in order to minimize losses in case of a single switch failure, all the incoming VMs will be scheduled into the hosts in the most balanced way possible: the two switches define two classes of hosts, the ones connected to the first switch and the ones connected to the second switch.
The new VMs arriving in the queue will be allocated alternating the host families.

	Incomes:    12479,04€
	Penalties:  2232,88€
	Energy:     2658,64€
	Revenue:    7587,52€
	
### Energy efficient:
For the energy saving we need to use the less number of hosts as much as possible and turn off the other hosts.
To achieve that we used Best Fit algorithm.

	Incomes: 12398.59€
	Penalties: 1413.50€
	Energy: 2613.28€
	Revenue: 8371.81€
	
### Fault Tolerance:
The purpose of this scheduler is to ensure fault tolerance on the VMs.

	Incomes:    12479,04€
	Penalties:  205,90€
	Energy:     2654,51€
	Revenue:    9618,62€

### Greedy:
We combine the Best fit with the no Violation to obtain higher revenue, because the revenue is computed as clientIncomes() - energyCost() - penalties()

	Incomes: 12398.59€
	Penalties: 0.00€
	Energy: 2870.81€
	Revenue: 9527.78€
	
### Next-Fit:
similarly to a first fit algorithm, it sequentially searches for hosts with enough free resources to allocate the next VM, but differently from a first fit approach it starts from the last checked hosts to achieve better performances.

	Incomes:    12479,04€
	Penalties:  337,14€
	Energy:     2724,45€
	Revenue:    9417,45€

### No violations:
We used checkAvailableMIPS() to check that the host has enough MIPS for that VM, for VMs lifetime.

	Incomes: 12398.59€
	Penalties: 0.00€
	Energy: 2868.74€
	Revenue: 9529.85

### Worst-Fit:
We ordered the hosts with deceasing order to make sure that the host has enough resources to allocate the VM. Therefore the VM will always get the needed resources.

	Incomes: 12398.59€
	Penalties: 6.06€
	Energy: 3298.22€
	Revenue: 9094.31€
	
