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
- Body

### Dependencies

- Spoon v7.5.0
- JDK 12
- Simple-json 1.1.1

### Install

- Clone the project. `git clone git@github.com:GuillaumeHaben/MetricExtractor.git && cd ./MetricExtractor`
- Install dependencies `Reimport All Maven Projects`

##### Run with Intellij

- In `./src/main/java/base/Main.java` right-click on the `main` function, `Create 'Main.main()'...` 
- Add `Program arguments`: `-interactive`
- Build

##### Run from the Command Line

- Get the classpath by running `mvn dependency:build-classpath -Dmdep.outputFile=tmpCp.txt`
- In `./metricExtractor.sh` replace classpath with the one in `tmpCp.txt` (Keep `./target/classes/:`)

### Usage

- Set `projectPath` in `src/main/java/base.Main.java` to the Java project you wish to analyze.
- Run `base.Main.java` (IntelliJ)
- Run `./metricExtractor.sh` (Command Line)

##### Options

`-interactive` to run an interactive command. (only option in this case)
`-projectPath` followed by the absolute path to the Maven project you want to analyze.
`-getAllTestMethods` will get metrics for all tests in the project
`-getAllMethods` will get metrics for all methods (CUT) in the project
`-listPath` followed by the aboluste path to a .txt file containing list of CLASS_NAME.METHOD_NAME. Will get metrics for them.

Results are saved in `./results/PROJECT/`

### Work in Progress

- [ ] Test your code
- [x] Refactor code
- [x] Add base.Search by list of methods
- [x] Add Depth of Inheritance metric

### Authors

Guillaume Haben - guillaume.haben@uni.lu
