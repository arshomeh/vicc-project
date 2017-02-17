# Notes about the project

## The team

- Leila Kuntar: leila.kuntar@gmail.com
- Manfredi Giordano: manfredi.giordano@me.com
- Arsak Megkrampian: sdi1100049@di.uoa.gr

## Comments

###noViolations

We used checkAvailableMIPS() to check that the host has enough MIPS 
for that VM, for VMs lifetime

- Incomes:    12398.59€
- Penalties:  0.00€
- Energy:     2868.74€
- Revenue:    9529.85€

###Fault tolerant 

- Incomes:    12398.59€
- Penalties:  5303.61€
- Energy:     2645.17€
- Revenue:    4449.81€

###Worst Fit

We ordered the hosts with deceasing order to make sure that the 
host has enough resources to allocate the VM. Therefor the VM will always get the needed resources  

- Incomes:    12398.59€
- Penalties:  6.06€
- Energy:     3298.22€
- Revenue:    9094.31€

###Energy

For the energy saving we need to use the less number of hosts as much as possible and turn off the other hosts
To achieve that we used Best Fit algorithm

- Incomes:    12398.59€
- Penalties:  1413.50€
- Energy:     2613.28€
- Revenue:    8371.81€


###Greedy

We combine the Best fit with the no Violation to obtain higher revenue, because
The revenue is computed as clientIncomes() - energyCost() - penalties()

- Incomes:    12398.59€
- Penalties:  0.00€
- Energy:     2870.81€
- Revenue:    9527.78€
