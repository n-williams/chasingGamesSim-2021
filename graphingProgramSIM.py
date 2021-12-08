import matplotlib.pyplot as plt
import numpy as np

dataSingleC = np.loadtxt("chasers.txt")
dataC = np.loadtxt("resultsC.txt")
dataSingleR = np.loadtxt("runners.txt")
dataR = np.loadtxt("resultsR.txt")
dataSingleS = np.loadtxt("stuck.txt")
dataS = np.loadtxt("resultsS.txt")

####################

plt.figure()

with open("chasers.txt") as f:
    n_cols = len(f.readline().split("\t"))
    print (n_cols)

plt.plot(dataC[:,0], dataC[:,1], linestyle = 'dashed', color='r', label="chaser mean")

for x in range(1, round(n_cols)):
    plt.plot(dataSingleR[:,0], dataSingleR[:,x], color='b', linewidth=1, alpha=0.1)

plt.plot(dataR[:,0], dataR[:,1], linestyle = 'dashed', color='b', label="runner mean")
plt.fill_between(dataR[:,0], dataR[:,1] - dataR[:,2], dataR[:,1] + dataR[:,2], facecolor='b', alpha=0.2, label="runner standard deviation")

for x in range(1, round(n_cols)):
    plt.plot(dataSingleS[:,0], dataSingleS[:,x], color='g', linewidth=1, alpha=0.1)

plt.plot(dataS[:,0], dataS[:,1], linestyle = 'dashed', color='g', label="stuck mean")
plt.fill_between(dataS[:,0], dataS[:,1] - dataS[:,2], dataS[:,1] + dataS[:,2], facecolor='g', alpha=0.2, label="stuck standard deviation")

plt.legend(loc="upper left") 

plt.xlabel("Time (s)")
plt.ylabel("Proportion of Players")

####################

plt.figure()

plt.axhline(y=0, color='black', linewidth=1)

plt.plot(dataC[:,0], dataC[:,3], linestyle = 'solid', color='r', label="chaser gradient")
plt.plot(dataR[:,0], dataR[:,3], linestyle = 'solid', color='b', label="runner gradient")
plt.plot(dataR[:,0], dataS[:,3], linestyle = 'solid', color='g', label="stuck gradient")

plt.legend(loc="upper left") 

plt.xlabel("Time (s)")
plt.ylabel("Rate of Change")

####################

plt.show()
