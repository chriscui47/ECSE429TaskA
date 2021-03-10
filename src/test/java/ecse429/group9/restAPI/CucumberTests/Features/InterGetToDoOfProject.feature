Feature: As a user, I want to see which category does this todo belong to.

  Background:
    Given an instance of the Todo API server is running
    Then the user can access the Todo manager

  Scenario Outline: Getting the todo of a project (Normal Flow)
    Given The project "<projectId>" has at least 1 todo "<todoId>"
    When I get the todo "<todoId>" of the project "<projectId>"
    Then The project "<projectId>" still has at least 1 todo "<todoId>"

    Examples:
      | projectId | todoId    |
      | 1         | 1         |
      | 1         | 2         |

  Scenario Outline: Getting the done status of todo of a project (Alternate Flow)
    Given The project "<projectId>" has at least 1 todo "<todoId>"
    When I get the done status of the todo "<todoId>" of the project "<projectId>"
    Then The project "<projectId>" still has at least 1 todo "<todoId>" with the done status "<doneStatus>"

    Examples:
      | projectId | todoId    | doneStatus |
      | 1         | 1         | false      |
      | 1         | 2         | false      |

  Scenario Outline: Getting the non existing todo of a project (Error Flow)
    Given The project "<projectId>" has at least 1 todo "<todoItHas>"
    When I get the non existing todo "<todoId>" of the project "<projectId>"
    Then The project "<projectId>" still has at least 1 todo "<todoId>"

    Examples:
      | projectId | todoId    | todoItHas |
      | 1         | 2000      | 1         |
      | 1         | 3000      | 2         |