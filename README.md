# MetricExtractor
Java Code Metric Extractor 

MetricExtractor aims to extract features from source code. 
More precisely, it gives information about a given method in a project.
The ultimate goal is to use those features as metrics to associate a method
(unit test) with a level of flakiness likelihood.

### Supported features

- Assertion count
- Method size
- Depth of Inheritance
- Cyclomatic Complexity
- Explicit wait count
- Presence of hard coded timeout

### Dependencies

- Spoon v7.5.0
- JDK 12
- Simple-json 1.1.1

### Usage

- Set `projectPath` in `src/main/java/Main.java` to the Java project you wish to analyze.
- Run `Main.java`

### Work in Progress

- [ ] Explore control flow further until invoked method is no longer in the (a?) test class.
- [ ] Test your code
- [ ] Refactor code
- [x] Add Search by list of methods
- [x] Add Depth of Inheritance metric


### Authors

Guillaume Haben - guillaume.haben@uni.lu