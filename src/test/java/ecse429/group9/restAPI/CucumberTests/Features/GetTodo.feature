Feature: As a user, I want to retrieve a Todo so that I can view a task

  Background:
    Given the Todo API server is running
    Then the user can access the Task Manager

  Scenario Outline: Retrieve a Todo using it's ID (Normal Flow)
    Given "<title>" is the title of an existing Todo
    And the user knows the ID of the "<title>" Todo
    When the user retrieves a Todo using it's ID
    Then the "<title>" todo instance will be returned

    Examples:
      | title          |
      | Homework       |
      | Study          |
      | Attend Lecture |


  Scenario Outline: Retrieve a Todo using it's Title (Alternate Flow)
    Given "<title>" is the title of an existing Todo
    When the user uses a URL filter to search for the "<title>" Todo
    Then one or more "<title>" todo instances will be returned

    Examples:
      | title          |
      | Homework       |
      | Study          |
      | Attend Lecture |

  Scenario Outline: Retrieve a Todo using an invalid/missing ID (Error flow)
    Given "<id>" is not the id of an existing Todo
    And "<id>" is the id of the Todo the user will retrieve
    When the user retrieves a Todo using it's ID
    Then no Todo instances will be returned

    Examples:
      | id  |
      | 89  |
      | 999 |
      | 0   |