Feature: As a user, I want to modify the information of a category of an existing todo.

  Background:
    Given the Todo API server is running
    Then the user can access the Task Manager

  Scenario Outline: Modifying an existing category to an existing todo (Normal Flow)
    Given The todo "<todoId>" has at least one category "<categoryId>"
    When I modify the description "<description>" with the id "<categoryId>" to an todo with the id "<todoId>"
    Then the description of the category "<categoryId>" of "<todoId>" will change to "<description>"

    Examples:
      | categoryId | todoId    | description |
      | 2          | 1         | Moscow      |
      | 1          | 2         | Helsinki    |

  Scenario Outline: Modifying an existing category that already has a description to an existing todo (Alternate Flow)
    Given The todo "<todoId>" has at least one category "<categoryId>", and it has a description "<description>"
    When I modify the description "<newDescription>" with the id "<categoryId>" to an todo with the id "<todoId>"
    Then the description of the category "<categoryId>" of "<todoId>" will change to "<newDescription>"

    Examples:
      | categoryId | todoId    | description | newDescription |
      | 2          | 1         | Shanghai    | Budapest       |
      | 1          | 2         | Bucharest   | Oslo           |

  Scenario Outline: Modifying an non existing category to an existing todo (Error Flow)
    Given The todo "<todoId>" has at least one category "<validCatId>"
    When I modify the description "<description>" with the id "<categoryId>" that does not exist to an todo with the id "<todoId>"
    Then the system should be aware that this category "<categoryId>" does not exist

    Examples:
      | categoryId | todoId    | description | validCatId |
      | 4000       | 1         | Moscow      | 2          |
      | 5000       | 2         | Helsinki    | 1          |