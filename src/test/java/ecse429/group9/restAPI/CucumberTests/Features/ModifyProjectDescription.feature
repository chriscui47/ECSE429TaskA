Feature: As a student, I want to adjust project descriptions so that I can adjust my schedule as needed.

Background:
Given the Todo API server is running 1

Scenario Outline: Change a project description (Normal Flow)
Given the title of the project "<title>"
When the user posts description change of project "<title>" to "<description>"
Then the project "<title>" description will be changed to "<description>"

Examples:
| title   | description                      |
| comp250 | Introduction-to-Computer-Science |
| ecse429 | Software-Validation              |
| math240 | Discrete-Structures              |

Scenario Outline: Change a project description to same description it currently has (Alternative Flow)
  Given the title of the project "<title>", the description "<description>"
  When the user posts description change of project "<title>" to "<newdescription>"
  Then the project "<title>" description will be changed to "<newdescription>"


Examples:
| title   | description                      | newdescription|
| comp250 | Introduction-to-Computer Science |    Introduction-to-Computer Science           |
| ecse429 | Software-Validation              |    Software-Validation            |
| math240 | Discrete-Structures              |        Discrete-Structures       |

Scenario Outline: Change a description for a non-existent project (Error Flow)
Given the id of a non-existent project is "<id>"
When the user posts description change of project "<title>" to "<description>"
Then an error message "<message>" with "<id>" will occur
Examples:
| id | description                      | message                                  | title |
| -1 | Introduction-to-Computer-Science | Could not find an instance with todos/-1 | comp  |
| 0  | Introduction-to-Computer-Science | Could not find an instance with todos/0  | ecse  |