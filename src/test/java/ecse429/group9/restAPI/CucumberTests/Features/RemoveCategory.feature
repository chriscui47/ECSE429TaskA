Feature: As a user, I want to remove a category
  Background:
    Given the Todo API server is running
    Then the user can access the Task Manager
  Scenario Outline: Removing a category from the categories list with title (Normal Flow)
    Given there exists a new category "<categoryTitle>"
    When I want to remove a category with the title "<categoryTitle>"
    Then there exists 2 categories for remove category

    Examples:
      | categoryTitle |
      | School        |
      | Library       |

  Scenario Outline: Removing a category from the categories list with title and description (Alternate Flow)
    Given there exists a new category "<categoryTitle>" with description "<categoryDescription>"
    When I want to remove a category with the title "<categoryTitle>" and description "<categoryDescription>"
    Then there exists 2 categories for remove category

    Examples:
      | categoryTitle | categoryDescription |
      | School        | School Description  |
      | Library       | Library Description |

  Scenario Outline: Removing a category from categories list with invalid id (ErrorFlow)
    Given there exists a new category "<categoryTitle>"
    When I want to remove a category with id 1000
    Then there will still be 3 categories, remove "<categoryTitle>" to reset

    Examples:
      | categoryTitle |
      | School        |
      | Library       |