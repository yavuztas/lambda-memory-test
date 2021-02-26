# lambda-memory-test
After Java 8 introduced the Lambda expressions, the way how JVM treats them is still a curious subject.

Especially in terms of memory consumption, it is interesting for most developers to know if Lambdas are more efficient or not rather than the traditional 
concrete or abstract inner classes.

## Test Cases
This project contains three test cases to shed some light on the memory consumption of Lambda Expressions in Java 8:
* testObjectMemoryConsumption
* testAICMemoryConsumption
* testLambdaMemoryConsumption

All of them execute the same operation but use different structures as concrete classes, abstract inner classes, and Lambda expression respectively.
The operation is a one-time pass iteration over a pre-populated ArrayList by using Java Streams' `filter()` function.

## How to Measure Memory Consumption
We should pay attention to the Garbage Collection which can be deceptive in our case. Any unexpected GC executions (either minor or full) 
in the middle of our test case will possibly end up with false metrics.

Since there is no way to disable GC in Java 8, we use some optimizations to prevent GC from taking action. Setting a higher amount for the `-Xms` and `-Xmx` 
and enabling GC logging (to be sure if the GC doesn't work) is a workaround for this. See some more discussion in this 
[stackoverflow thread](https://stackoverflow.com/questions/2980019/jvm-with-no-garbage-collection).

Therefore, each test case is executed by enabling the following VM options:
```
-Xms2g -Xmx2g -verbose:gc
```

## The Result
We obtained the results by running each test case 10 times and calculating the average consumption in KB:
| Concrete Class | Abstract Inline Class | Lambda Expression |
| --- | --- | --- |
| 243826.2 KB | 233759.7 KB | 215864.4 KB |

Looking at the metrics, Lambda Expressions takes the lead with almost **~28 MB** less memory consumption than Concrete Classes and **~18 MB** less than Abstract Inner Classes. 

As a result, we can say that Lambda Expressions is treated somehow in a more memory-optimized way by the JVM. Hopefully, these results can give us a more clear perception to prefer which structure to use :)     

## More Resources about the topic
* [How Lambdas And Anonymous Inner Classes Work](https://dzone.com/articles/how-lambdas-and-anonymous-inner-classesaic-work)
* [Verbose Garbage Collection in Java](https://www.baeldung.com/java-verbose-gc)
* [The Last Frontier in Java Performance: Remove the Garbage Collector](https://www.infoq.com/news/2017/03/java-epsilon-gc/)
* [JEP-318](https://openjdk.java.net/jeps/318)

