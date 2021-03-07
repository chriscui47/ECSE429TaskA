Feature: As a user, I want to get a category
  Background:
    Given the get category API server is running
  Scenario Outline: Get a category with category id (Normal Flow)
    Given there exists 2 categories for get category
    When I get a category with id "<id>" and title "<categoryTitle>"
    Then there exists 2 categories for get category

    Examples:
      | id  | categoryTitle |
      | 1   | Office        |
      | 2   | Home          |

  Scenario Outline: Get a category with category name (Alternate Flow)
    Given there exists 2 categories for get category
    When I get a category with title "<categoryTitle>"
    Then there exists 2 categories for get category

    Examples:
      | categoryTitle |
      | Office        |
      | Home          |

  Scenario Outline: Get a category with invalid category id (ErrorFlow)
    Given there exists 2 categories for get category
    When I get a category with invalid id "<id>" with title "<categoryTitle>"
    Then there exists 2 categories for get category

    Examples:
      | id    | categoryTitle |
      | 1001  | Office        |
      | 1002  | Home          |