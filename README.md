# MetricExtractor
Java Code base.Metric Extractor 

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

- Set `projectPath` in `src/main/java/base.Main.java` to the Java project you wish to analyze.
- Run `base.Main.java`

### Work in Progress

- [ ] Test your code
- [x] Refactor code
- [x] Add base.Search by list of methods
- [x] Add Depth of Inheritance metric

### Miscellaneous

- Command to run the program: `./metricExtractor.sh -projectPath ... -listPath ...`.
- Get the classpath is correct by running `mvn dependency:build-classpath -Dmdep.outputFile=tmpCp.txt`

### Authors

Guillaume Haben - guillaume.haben@uni.lu