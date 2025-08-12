# Parabank Web Automation

This project provides an automated test framework for the Parabank web application using Selenium WebDriver, Cucumber, JUnit Platform, Extent Reports, Log4J and FillO (for test data management). It supports parallel test execution and integrates with MySQL for analytics and reporting.

## Features
- Cucumber BDD framework for test scenarios
- JUnit5 Platform for test execution
- Parallel execution using Cucumber and JUnit Platform
- Selenium Grid support for distributed execution
- Cross-browser testing (Chrome, Firefox, Edge, Safari)
- Thread-safe test context management
- FillO for test data management
- Configurable test data via Excel files
- Extent Reports integration for rich test reporting
- Log4J for logging
- Configurable via properties files
- Database connectivity for analytics

## Prerequisites

- Java 17 or higher
- Maven 3.x
- MySQL database (for analytics)
- Docker (for Selenium Grid, optional)

## Setup

1. **Clone the repository**  
   ```
   git clone <repo-url>
   cd parabank-web-automation
   ```

2. **Configure properties**  
   - Edit `Config.properties` for environment, browser, and grid settings.
   - Edit `AnalyticsDB.properties` for database connection details.

3. **(Optional) Start Selenium Grid**  
   - To start the grid with default node counts:
     ```
     docker compose -f selenium-grid.yml up -d
     ```
   - To start the grid with multiple browser nodes (e.g., 2 Firefox, 2 Chrome, 2 Edge):
     ```
     docker compose -f selenium-grid.yml up -d --scale node-firefox=2 --scale node-chrome=2 --scale node-edge=2
     ```
   Update `Config.properties` to enable grid and set the grid URL.

4. **Install dependencies**  
   ```
   mvn clean install
   ```

## Execution

- **Run tests locally**  
  ```
  mvn test -Dcucumber.filter.tags=“@SanityTests“ -Dapp.env=sit -Drun.browser=FIREFOX
  ```

- **Run tests on Selenium Grid**  
  Set `enable.grid=true` in `Config.properties` and run:
  ```
  mvn test
  ```
  or simply run:
  ```
  mvn clean test -Dcucumber.filter.tags=“@SanityTests“ -Dapp.env=sit -Drun.browser=FIREFOX -Denable.grid=true
  ```

- **Parallel Execution**  
  Parallelism is configured in `src/test/resources/junit-platform.properties`.

- **Cross Browser Execution**  
  Supported browsers: Chrome, Firefox, Edge, and Safari.  
  You can select the browser by setting `run.browser` in `Config.properties`.  
  Example:
  ```
  run.browser=CHROME
  ```
  or simply run:
  ```
  mvn clean test -Dcucumber.filter.tags=“@SanityTests“ -Dapp.env=sit -Drun.browser=CHROME
  ```
  Change this value to `FIREFOX`, `EDGE`, or `SAFARI` as needed.

## Test Data

Test data is maintained in Excel files located in the `src/test/resources/testData` folder.  
- Each Excel file contains sheets named after modules, corresponding to feature files.  
- For example, all scenarios related to `TransferFundsTests` are stored in the `Transfer_Funds` sheet.
- The specific test data file to be used is specified in `Config.properties` using the `data.filePath` property.

Example:
```
data.filePath=src/test/resources/testData/RegressionTests.xlsx
```

## Reporting

- Test execution reports are generated using Extent Reports.
- Analytics data is stored in the configured MySQL database. 
  To enable analytics, set `enable.analytics=true` in `Config.properties`
  or simply run:
  ```
  mvn clean test -Dcucumber.filter.tags=“@SanityTests“ -Dapp.env=sit -Drun.browser=FIREFOX -Denable.analytics=true
  ```

## Customization

- Test data is managed via the Excel file specified in `Config.properties`.
- Add or modify test scenarios in the `src/test` directory.

## Project Structure Diagram

```
parabank-web-automation/
├── pom.xml
├── README.md
├── Config.properties (configuration for environment, browser, grid)
├── AnalyticsDB.properties (db connection properties for analytics)
├── selenium-grid.yml (for Docker Selenium Grid setup)
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── parasoft/
│   │               └── parabank/
│   │                   ├── commands/
│   │                   │   └── ... (selenium commands using command design pattern)
│   │                   ├── controller/
│   │                   │   └── ... (driver, data, report classes)
│   │                   ├── data/ 
│   │                   │   └── ... (data object classes)
│   │                   └── utility/
│   │                   │   └── ... (helper classes)
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── parasoft/
│       │           └── parabank/
│       │               ├── testRunner/
│       │               │   └── RunTest.java
│       │               ├── glue/
│       │               │   └── ... (step definition files)
│       │               ├── pages/
│       │               │   └── ... (pom classes)
│       │               └── base/
│       │                   └── ... (base test and base page classes)
│       └── resources/
│           ├── features/
│           │   └── ... (feature files)
│           ├── testData/
│           │   └── ... (excel files for test data)
│           └── junit-platform.properties (for parallel execution)
│           └── log4j2.xml (for logging configuration)
└── target/
    └── ... (build output)
```

## License

This project is for demonstration and educational purposes.
