Feature: a student, I mark a project as done on my course to do list, so I can track my schoolwork.

  Background:
    Given the Todo API server is running

  Scenario Outline: mark a not active project as active (Normal Flow)
    Given "<title>" is the title of the project
    Given "<prevActiveStatus>" is the active state of the project
    When the user creates a project for a class
    When the user requests to mark the project "<title>" with an active status "<nextActiveStatus>"
    Then the project "<title>" will be marked with the active status "<nextActiveStatus>"

    Examples:
      | title       | prevActiveStatus | nextActiveStatus |
      | Assignment1 | false          | true           |
      | Assignment2 | false          | true           |
      | Quiz1       | false          | true           |

  Scenario Outline: mark a active task as active (Alternative Flow) 1
    Given "<title>" is the title of the project
    Given "<prevActiveStatus>" is the active state of the project
    When the user creates a project for a class
    When the user requests to mark the project "<title>" with an active status "<nextActiveStatus>"
    Then the project "<title>" will be marked with the active status "<nextActiveStatus>"

    Examples:
      | title       | prevActiveStatus | nextActiveStatus |
      | Assignment1 | true           | true           |
      | Assignment2 | true           | true           |
      | Quiz1       | true           | true           |

  Scenario Outline: mark a non-existing task as active (Error Flow) 1
    Given no project with id "<id>" is registered in the API server 1
    When the user requests to mark the project "<id>" with an active status "<nextActiveStatus>"
    Then system will output an error with error code "<errorCode>" 1

    Examples:
      | id   | nextActiveStatus | errorCode |
      | 100  | true       | 404       |
      | -1   | true       | 404       |
