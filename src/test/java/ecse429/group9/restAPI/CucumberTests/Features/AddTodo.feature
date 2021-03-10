Feature: As a user, I want to create a Todo so that I can track my tasks

  Background:
    Given an instance of the Todo API server is running
    Then the user can access the Todo manager

  Scenario Outline: Create a new Todo with a title, a 'doneStatus' and a description (Normal Flow)
    Given "<title>" is the title of the new Todo
    And "<doneStatus>" is the completion status of the new Todo
    And "<description>" is the description of the new Todo
    When the user creates a new Todo
    Then a todo instance with "<title>", "<doneStatus>" and "<description>" will be created

    Examples:
      | title          | doneStatus | description                    |
      | Homework       | false      | Complete homework for tomorrow |
      | Study          | false      |                                |
      | Attend Lecture | true       | Join Zoom lecture              |


  Scenario Outline: Create a new Todo with only a title (Alternate Flow)
    Given "<title>" is the title of the new Todo
    When the user creates a new Todo
    Then a Todo instance with "<title>" will be created

    Examples:
      | title          |
      | Homework       |
      | Study          |
      | Attend Lecture |

  Scenario Outline: Create a new Todo with an invalid/missing title (Error flow)
    Given "<doneStatus>" is the completion status of the new Todo
    And "<description>" is the description of the new Todo
    When the user creates a new Todo
    Then a Todo instance will not be created

    Examples:
      | doneStatus | description              |
      | false      | A title is missing       |
      |            | Title and status missing |
      |            |                          |