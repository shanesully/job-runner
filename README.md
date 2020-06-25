# Description
A coding test for a prominent software vendor

### Fairness:
* 1000ms of processing time is allocated to each new uniqueId submitted
* In the interest of efficiency, if a job runs for less time than allowed for it, it will run for that time only
* Jobs sharing the same id share time also(large jobs can be split across multiple smaller ones if needed)
* A total job time allocation table is kept to prevent the same job from using too many resources over an infinite
timescale

### Notes:
* I used a TDD-style approach to tease out requirements early on
* Tests favour testing behaviour permutation over input permutation
* Tests try to follow a natural and expressive naming convention and minimize global state
* I completed this exercise in one evening, although I would have liked to spend more time on it
* I was careful to run my code through the formatter as I progressed
* Tested on a 6-core laptop using OpenJDK14
* Naming conventions and code readability are valued over comments where possible