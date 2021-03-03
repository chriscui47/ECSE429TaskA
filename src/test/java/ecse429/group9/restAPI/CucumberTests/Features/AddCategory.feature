Feature: As a user, I want to add a new category

  Scenario Outline: Adding a new category title to categories list (Normal Flow)
    Given there exists 2 categories
    When I add a new category with the title "<categoryTitle>"
    Then there will be a new category with title "<categoryTitle>"

    Examples:
      | categoryTitle |
      | School        |
      | Library       |

  Scenario Outline: Adding a new category title and description to categories list (Alternate Flow)
    Given there exists 2 categories
    When I add a new category with the title "<categoryTitle>" and description "<categoryDescription>"
    Then there will be a new category with title "<categoryTitle>" and description "<categoryDescription>"

    Examples:
      | categoryTitle | categoryDescription |
      | School        | School Description  |
      | Library       | Library Description |

  Scenario Outline: Adding a new category title to categories list and specifying id (ErrorFlow)
    Given there exists 2 categories
    When I add a new category with the title "<categoryTitle>" and id "<id>"
    Then there exists 2 categories

    Examples:
      | categoryTitle | id  |
      | School        | 3   |
      | Library       | 4   |