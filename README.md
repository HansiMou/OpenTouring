# OpenTouring

**NOT FINISHED**

Algorithm: A*


Supposing N site, K days

Data:
distance[i][j]: number of minutes between two site i, j in [0, N)
desiredTime[i]: time to spend in site i
value[i]: value of site i

start[i]: start time (in minute) of site i, here i in [0, KN)
end[i]: end time (in minute) of site i, here i in [0, KN)

Node:
1. site(node), index, [0, N). site number.
2. day(node), jth day.
3. explored(node), explored sites, a list, each element in [0, N). represents, in current root, which site has been explored.
4. time(node), current time: what's the time when finish this site

Cost function
g(n1, n2) = - value(site(n2)) if site(n2) is not in explored(node) and n2 is possible to reach from n1

Estimate function
h(n) =


