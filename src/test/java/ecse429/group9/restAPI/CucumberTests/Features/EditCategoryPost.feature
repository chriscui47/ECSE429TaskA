Feature: As a user, I want to use the POST method to edit a category
  Background:
    Given the Todo API server is running
    Then the user can access the Task Manager
  Scenario Outline: Editing a category's title (Normal Flow)
    Given there exists a new category with title "<categoryTitle>" with POST
    When I change a category with title "<categoryTitle>" to "<newCategoryTitle>" with POST
    Then there will be a new category with the title "<newCategoryTitle>" with POST

    Examples:
      | categoryTitle | newCategoryTitle  |
      | School        | School2           |
      | Library       | Library2          |

  Scenario Outline: Editing a category's description (Alternate Flow)
    Given there exists a new category with title "<categoryTitle>" and description "<categoryDescription>" with POST
    When I change a category's description with title "<categoryTitle>" to "<newCategoryDescription>" with POST
    Then there will be a new category with the title "<categoryTitle>" and description "<newCategoryDescription>" with POST

    Examples:
      | categoryTitle | categoryDescription | newCategoryDescription  |
      | School        | School Description  | School2 Description     |
      | Library       | Library Description | Library2 Description    |

  Scenario Outline: Editing a category's title with invalid id (ErrorFlow)
    Given there exists 2 categories for edit category POST
    When I change a category's title with id "<categoryID>" to "<newCategoryTitle>" with POST
    Then the category with id "<categoryID>" will not have "<newCategoryTitle>" as title with POST

    Examples:
      | categoryID | newCategoryTitle  |
      | 10001      | School            |
      | 10002      | Library           |