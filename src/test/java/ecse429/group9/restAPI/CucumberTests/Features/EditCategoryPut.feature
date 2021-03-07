Feature: As a user, I want to use the PUT method to edit a category
  Background:
    Given the edit category PUT API server is running
  Scenario Outline: Editing a category's title (Normal Flow)
    Given there exists a new category with title "<categoryTitle>" with POST for PUT
    When I change a category with title "<categoryTitle>" to "<newCategoryTitle>" with PUT
    Then there will be a new category with the title "<newCategoryTitle>" with PUT

    Examples:
      | categoryTitle | newCategoryTitle  |
      | School        | School2           |
      | Library       | Library2          |

  Scenario Outline: Editing a category's description (Alternate Flow)
    Given there exists a new category with title "<categoryTitle>" and description "<categoryDescription>" with POST for PUT
    When I change a category's description with title "<categoryTitle>" to "<newCategoryDescription>" with PUT
    Then there will be a new category with the title "<categoryTitle>" and description "<newCategoryDescription>" with PUT

    Examples:
      | categoryTitle | categoryDescription | newCategoryDescription  |
      | School        | School Description  | School2 Description     |
      | Library       | Library Description | Library2 Description    |

  Scenario Outline: Editing a category's title with invalid id (ErrorFlow)
    Given there exists 2 categories for edit category PUT
    When I change a category's title with id "<categoryID>" to "<newCategoryTitle>" with PUT
    Then the category with id "<categoryID>" will not have "<newCategoryTitle>" as title with PUT

    Examples:
      | categoryID | newCategoryTitle  |
      | 10001      | School            |
      | 10002      | Library           |