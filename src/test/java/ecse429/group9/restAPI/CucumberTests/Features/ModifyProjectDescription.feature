Feature: As a student, I want to adjust project descriptions so that I can adjust my schedule as needed.

Background:
Given the Todo API server is running

Scenario Outline: Change a project description (Normal Flow)
Given the title of the project "<title>"
When the user posts description change of project "<title>" to "<description>"
Then the project "<title>" description will be changed to "<description>"

Examples:
| title   | description                      |
| comp250 | Introduction-to-Computer-Science |
| ecse429 | Software-Validation              |
| math240 | Discrete-Structures              |

Scenario Outline: Change a project description that is related to a project (Alternative Flow)
Given the title of the project "<title>"
And "<title>" is related to projects with title "<title>"
When the user posts description change of project "<title>" to "<description>"
Then the project "<title>" description will be changed to "<description>"

Examples:
| title   | description                      | projecttitle |
| comp250 | Introduction-to-Computer Science | 1            |
| ecse429 | Software-Validation              | 2            |
| math240 | Discrete-Structures              | 3            |

Scenario Outline: Change a description for a non-existent project (Error Flow)
Given the id of a non-existent project is "<id>"
When the user posts description change of project "<title>" to "<description>"
Then an error message "<message>" with "<id>" will occur
Examples:
| id | description                      | message                                  | title |
| -1 | Introduction-to-Computer-Science | Could not find an instance with todos/-1 | comp  |
| 0  | Introduction-to-Computer-Science | Could not find an instance with todos/0  | ecse  |