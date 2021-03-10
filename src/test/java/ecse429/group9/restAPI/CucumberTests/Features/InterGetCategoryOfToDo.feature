Feature: As a user, I want to see which category does this todo belong to.

  Background:
    Given an instance of the Todo API server is running
    Then the user can access the Todo manager

  Scenario Outline: Getting the category of a todo (Normal Flow)
    Given The todo "<todoId>" has at least 1 category "<categoryId>"
    When I get the category "<categoryId>" of the todo "<todoId>"
    Then The todo "<todoId>" still has at least 1 category "<categoryId>"

    Examples:
      | categoryId | todoId    |
      | 2          | 1         |

  Scenario Outline: Getting multiple categories of a todo (Alternate Flow)
    Given The todo "<todoId>" has 2 categories
    When I get the category "<categoryId>" of the todo "<todoId>"
    Then The todo "<todoId>" still has 2 categories

    Examples:
      | categoryId | todoId    |
      | 1          | 1         |
      | 2          | 1         |

  Scenario Outline: Getting non existing category of a todo (Error Flow)
    Given The todo "<todoId>" has 2 categories
    When I get the non existing category "<categoryId>" of the todo "<todoId>"
    Then The todo "<todoId>" still has 2 categories

    Examples:
      | categoryId | todoId    |
      | 1000       | 1         |