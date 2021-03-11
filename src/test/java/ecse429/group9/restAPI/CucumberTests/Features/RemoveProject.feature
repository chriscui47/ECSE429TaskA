Feature: a student, I remove a project that I no longer need to do, to declutter my schedule.

  Background:
    Given the Todo API server is running 1

  Scenario Outline: Remove a project (Normal Flow)
    Given "<title>" is the title of the project to be removed
    When the user posts a request to the server to remove a project "<title>"
    Then the project with "<title>" will no longer exist

    Examples:
      | title   | titlecat |
      | yolooooooo | camp  |
      | comp250 | house    |
      | ecse429 | camp  |
      | comp250 | house    |
      | ecse429 | camp  |
      | ecse429 | camp  |

  Scenario Outline: Remove a project related to a category (Alternative Flow)
    Given "<title>" is the title of the project to be removed
    And "<titlecat>" is the title of the project related to the to do list "<title>"
    When the user posts a request to the server to remove a project "<title>"
    Then the project with "<title>" will no longer exist

    Examples:
      | title   | titlecat |
      | comp250 | house    |
      | ecse429 | camp  |

  Scenario Outline: Change a description for a non-existent task (Error Flow)
    Given the id of a non-existent to do list is "<id>"
    When the user posts a request to the server to remove a project "<title>"
    Then an error message "<message>" with "<id>" will occur
    Examples:
      | id | message                                  | title |
      | -1 | Could not find an instance with todos/-1 | comp  |
      | 0  | Could not find an instance with todos/0  | ecse  |