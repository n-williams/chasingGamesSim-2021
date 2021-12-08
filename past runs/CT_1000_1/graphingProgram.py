import matplotlib.pyplot as plt
import numpy as np

dataSingleC = np.loadtxt("chasers.txt")
dataC = np.loadtxt("resultsC.txt")
dataSingleR = np.loadtxt("runners.txt")
dataR = np.loadtxt("resultsR.txt")
dataNu = np.loadtxt("nu.txt")

####################

plt.figure()

with open("chasers.txt") as f:
    n_cols = len(f.readline().split("\t"))
    print (n_cols)

for x in range(1, 6):
    plt.plot(dataSingleC[:,0], dataSingleC[:,x], color='r', linewidth=1, alpha=0.5)
#plt.plot(dataSingleC[:,0], dataSingleC[:,1], color='r', linewidth=1, alpha=0.2)


plt.plot(dataC[:,0], dataC[:,1], linestyle = 'dashed', color='r', label="chaser mean")
plt.fill_between(dataC[:,0], dataC[:,1] - dataC[:,2], dataC[:,1] + dataC[:,2], facecolor='r', alpha=0.2, label="chaser standard deviation")

for x in range(1, 6):
    plt.plot(dataSingleR[:,0], dataSingleR[:,x], color='b', linewidth=1, alpha=0.5)
#plt.plot(dataSingleR[:,0], dataSingleR[:,1], color='b', linewidth=1, alpha=0.2)

plt.plot(dataR[:,0], dataR[:,1], linestyle = 'dashed', color='b', label="runner mean")
plt.fill_between(dataR[:,0], dataR[:,1] - dataR[:,2], dataR[:,1] + dataR[:,2], facecolor='b', alpha=0.2, label="runner standard deviation")

plt.legend(loc="upper left") 

plt.xlabel("Time (s)")
plt.ylabel("Proportion of Players")

####################

plt.figure()

plt.axhline(y=0, color='black', linewidth=1)

plt.plot(dataC[:,0], dataC[:,3], linestyle = 'solid', color='r', label="chaser gradient")
plt.plot(dataR[:,0], dataR[:,3], linestyle = 'solid', color='b', label="runner gradient")

plt.legend(loc="upper right")

plt.xlabel("Time (s)")
plt.ylabel("Rate of Change")

####################

plt.figure()

plt.axhline(y=0, color='black', linewidth=1)

plt.plot(dataNu[:,0], dataNu[:,1], linestyle = 'solid', color='r', alpha=0.5, label="chaser transition rate ($\\nu$)")
plt.plot(dataNu[:,0], dataNu[:,2], linestyle = 'solid', color='b', alpha=0.5, label="runner transition rate ($\\nu$)")

mean_lineC = np.array([np.mean(dataNu[:,1]) for i in range(len(dataNu[:,0]))])
mean_lineR = np.array([np.mean(dataNu[:,2]) for i in range(len(dataNu[:,0]))])

std_lineC = np.array([np.std(dataNu[:,1]) for i in range(len(dataNu[:,0]))])
std_lineR = np.array([np.std(dataNu[:,2]) for i in range(len(dataNu[:,0]))])

plt.plot(dataNu[:,0], mean_lineC, linestyle = 'dashed', color='r', label="chaser measured $\\nu$ mean")
plt.plot(dataNu[:,0], mean_lineR, linestyle = 'dashed', color='b', label="runner measured $\\nu$ mean")

plt.plot(dataNu[:,0], dataNu[:,3], linestyle = 'solid', color='r', label="chaser kinetic model $\\nu$")
plt.hlines(y=-0.03336, xmin=40.8, xmax=128.2, linestyle = 'solid', color='b', label="runner kinetic model $\\nu$")

plt.fill_between(dataNu[:,0], mean_lineC - std_lineC, mean_lineC + std_lineC, facecolor='r', alpha=0.2, label="chaser $\\nu$ standard deviation")
plt.fill_between(dataNu[:,0], mean_lineR - std_lineR, mean_lineR + std_lineR, facecolor='b', alpha=0.2, label="runner $\\nu$ standard deviation")

print(np.mean(dataNu[:,1]))
print(np.mean(dataNu[:,2]))

plt.legend(loc="upper right")

plt.xlabel("Time (s)")
plt.ylabel("Transition Rate ($\\nu$)")

####################

plt.show()
