# AdminUnit management and CSV Reader
Completing the project was a lot of fun and provided valuable skills in problem solving, testing, debugging and optimizing the produced software. Thanks to the knowledge of more advanced areas of the language (extensive switches, streams, lambdas), the written code runs faster, is more elegant and concise.

## Code Notes
- in AdminUnit  Writing "children" is commented here to avoid spam by voivodeship
- The program lists neighbors for an entity only with a common parent. It ignores real neighbors from other voivodeships, for example, for a commune that is on the edge of the voivodeship
- Surfaces are converted into rectangles, which is a significant simplification and is completely sufficient for learning.
- 

## Introduction and brief description
The 'CSV Reader' project serves as a tool designed to enable the extraction, manipulation, and analysis of data stored within CSV files, providing extensive functionalities and configurations during the data retrieval process.

At its core, this project aims to offer a versatile solution for reading CSV file data while accommodating diverse settings and formats. It excels in its capacity to handle various data settings, such as delimiter specifications, header row presence, and managing missing values.

Notably, it grants users the capability to seamlessly replace missing values based on their preferences, whether they are numerical or textual, thus enhancing data consistency and integrity. Additionally, the project allows for fine-tuning time and date formatting settings to suit specific user requirements.

Furthermore, the 'CSV Reader' project prioritizes adaptability by permitting the implementation of custom exceptions, empowering users to handle specific error scenarios effectively.

# Features

## Reading Data from CSV Files
The CSVReader class enables reading data from CSV files, handling different settings such as field separator, header row existence, and missing values handling.
## Replacing Missing Values
Users can define behavior for missing numerical and textual values, choosing whether to replace these values with specified default values.
## Data Manipulation
The project allows processing the read data, enabling extraction of values from specific columns in the form of numbers, strings, dates, and times.
## Time and Date Formatting
The CSVReader class allows users to define their preferred time and date format for the data read from CSV files.
## Extensibility
The project is designed with easy extensibility in mind. There is potential to add new functionalities such as generating statistics, data filtering, CSV file writing, etc.

# Application of the Project
The "CSV Data Analyzer" project can find applications in various fields such as financial data analysis, business data processing, scientific research, or in scientific research applications where there's a need for processing and analyzing CSV file data.

# Summary
The project offers a versatile tool for handling CSV files, allowing the reading of data with various configurations and providing flexible processing options. It can be used across multiple domains where there's a need to analyze and manipulate CSV file data. Furthermore, it can be expanded to add new functionalities and increase data processing flexibility.

# Potential for Further Development / Ideas
- Create a mechanism to generate statistics based on data from the CSV file. Enable users to calculate basic statistical measures (mean, median, standard deviation, etc.) for selected columns.
- Add functionality to save processed data to a new CSV file. Enable users to save analysis results or processed data in a different CSV file format.
- Optimize the project for handling large CSV files by implementing partitioning or buffering mechanisms, potentially speeding up and optimizing the data loading process.
- Develop a graphical interface that allows users to interact with the application intuitively and user-friendly, instead of using command-line interfaces only.
- Incorporate data validation mechanisms to check the correctness of data in the CSV file according to specified criteria or rules.

# Administrative Units Management

This project enables the management of administrative units, allowing checking for neighboring units, and much more.

## Classes

### AdminUnit

The `AdminUnit` class represents an individual administrative unit. It contains information such as an identifier, name, administrative level, population, area, population density, and more. It also includes functions to fix missing data and manipulate it.

### AdminUnitList

`AdminUnitList` is a class that manages a list of administrative units. It allows loading data from a file, listing units, selecting units based on name patterns, finding neighbors of a unit, and other operations.

### BoundingBox

The `BoundingBox` class represents a rectangular geographic area defined by coordinates. It includes methods to check for intersection with another area, calculate the center, measure distance between areas, and more.

### Haversine

`Haversine` is a class containing an implementation of the haversine formula to calculate distances between two points on a sphere.

## Instructions

- `AdminUnitList.read(filename)`: Reads records from a file.
- `AdminUnitList.list(out)`: Displays a list of administrative units.
- `AdminUnitList.selectByName(pattern)`: Selects units based on name patterns.
- `AdminUnitList.getNeighbors(unit, maxdistance)`: Finds neighbors of a unit within a specified range.

## Usage Example

```java
// Example usage
AdminUnitList adminUnitList = new AdminUnitList();
adminUnitList.read("filename.csv");

AdminUnitList neighbors = adminUnitList.getNeighbors(selectedUnit, 15.0);
neighbors.list(System.out);
```