Feature: a student, I mark a project as done on my course to do list, so I can track my schoolwork.

  Background:
    Given the Todo API server running 1

  Scenario Outline: mark a not active project as active (Normal Flow) 1
    Given a project with the title "<title>" and active status "<prevActiveStatus>" 1
    When the user requests to mark the project "<title>" with an active status "<nextActiveStatus>" 1
    Then the project "<title>" will be marked with the active status "<prevActiveStatus>" 1

    Examples:
      | title       | prevActiveStatus | nextActiveStatus |
      | Assignment1 | false          | true           |
      | Assignment2 | false          | true           |
      | Quiz1       | false          | true           |

  Scenario Outline: mark a active task as active (Alternative Flow) 1
    Given a project with the title "<title>" and active status "<prevActiveStatus>" 1
    When the user requests to mark the project "<title>" with an active status "<nextActiveStatus>" 1
    Then the project "<title>" will be marked with the active status "<nextActiveStatus>" 1

    Examples:
      | title       | prevActiveStatus | nextActiveStatus |
      | Assignment1 | false           | true           |
      | Assignment2 | false           | true           |
      | Quiz1       | true           | true           |

  Scenario Outline: mark a non-existing task as active (Error Flow) 1
    Given no project with id "<id>" is registered in the API server 1
    When the user requests to mark the project "<id>" with an active status "<nextActiveStatus>" 1
    Then system will output an error with error code "<errorCode>" 1

    Examples:
      | id   | nextActiveStatus | errorCode |
      | 100  | true       | 404       |
      | -1   | true       | 404       |
