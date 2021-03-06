Feature: a student, I mark a project as completed on my course to do list, so I can track my schoolwork.

  Background:
    Given the Todo API server running

  Scenario Outline: mark a not done project as done (Normal Flow)
    Given a project with the title "<title>" and completed status "<prevCompletedStatus>"
    When the user requests to mark the project "<title>" with a completed status "<nextCompletedStatus>"
    Then the project "<title>" will be marked with the completed status "<nextCompletedStatus>"

    Examples:
      | title       | prevCompletedStatus | nextCompletedStatus |
      | Assignment1 | false          | true           |
      | Assignment2 | false          | true           |
      | Quiz1       | false          | true           |

  Scenario Outline: mark a completed project as completed (Alternative Flow)
    Given a project with the title "<title>" and completed status "<prevCompletedStatus>"
    When the user requests to mark the project "<title>" with a completed status "<nextCompletedStatus>"
    Then the project "<title>" will be marked with the completed status "<nextCompletedStatus>"

    Examples:
      | title       | prevCompletedStatus | nextCompletedStatus |
      | Assignment1 | false           | true           |
      | Assignment2 | false           | true           |
      | Quiz1       | true           | true           |

  Scenario Outline: mark a non-existing task as done (Error Flow)
    Given no project with id "<id>" is registered in the API server
    When the user requests to mark the project "<id>" with a completed status "<completedStatus>"
    Then system will output an error with error code "<errorCode>"

    Examples:
      | id   | completedStatus | errorCode |
      | 100  | true       | 404       |
      | -1   | true       | 404       |
