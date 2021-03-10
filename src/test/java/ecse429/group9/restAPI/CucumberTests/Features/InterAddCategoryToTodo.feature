Feature: As a user, I want to add an existing category to an existing todo.

  Background:
    Given the add category API server is running

  Scenario Outline: Adding an existing category to an existing todo (Normal Flow)
    Given there exists 2 categories and 2 todo
    When I add an category with the id "<categoryId>" to an todo with the id "<todoId>"
    Then there will be a new category with id "<categoryId>" to "<todoId>"

    Examples:
      | categoryId | todoId    |
      | 2          | 1         |
      | 1          | 2         |

  Scenario Outline: Adding an existing category that has already been assigned to an existing todo to this todo again (Alternate Flow)
    Given there exists at least 1 category and 1 todo, this todo "<todoId>" already has this category "<categoryId>"
    When I add an category with the id "<categoryId>" to an todo with the id "<todoId>"
    Then the category "<categoryId>" is still assigned to this todo "<todoId>"

    Examples:
      | categoryId | todoId      |
      | 1          | 2           |

  Scenario Outline: Adding a non existing category to an existing todo (ErrorFlow)
    Given there exists at least 1 todo "<todoId>"
    When I add an category with the id "<categoryId>" to an todo with the id "<todoId>"
    Then the system should be aware that this category "<categoryId>" does not exist

    Examples:
      | categoryId | todoId      |
      | O          | 1           |