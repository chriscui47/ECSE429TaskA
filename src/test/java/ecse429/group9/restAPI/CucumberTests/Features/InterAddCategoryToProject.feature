Feature: As a user, I want to add an existing category to an existing project.

  Background:
    Given the Todo API server is running
    Then the user can access the Task Manager

  Scenario Outline: Adding an existing category to an existing project (Normal Flow)
    Given there exists 2 categories and 1 project
    When I add an category with the id "<categoryId>" to an project with the id "<projectId>"
    Then there will be a new category with id "<categoryId>" to project "<projectId>"

    Examples:
      | categoryId | projectId |
      | 1          | 1         |
      | 2          | 1         |

  Scenario Outline: Adding an existing category that has already been assigned to an existing todo to this project again (Alternate Flow)
    Given there exists at least 1 category and 1 project, this project "<projectId>" already has this category "<categoryId>"
    When I add an category with the id "<categoryId>" to an project with the id "<projectId>"
    Then the category "<categoryId>" is still assigned to this project "<projectId>"

    Examples:
      | categoryId | projectId |
      | 1          | 1         |

  Scenario Outline: Adding a non existing category to an existing project (ErrorFlow)
    Given there exists at least 1 project "<projectId>"
    When I add an category with the id "<categoryId>" to an project with the id "<projectId>"
    Then the system should be aware that this category "<categoryId>" does not exist

    Examples:
      | categoryId | projectId |
      | O          | 1         |