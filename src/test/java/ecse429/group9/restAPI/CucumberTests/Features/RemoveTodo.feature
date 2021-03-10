Feature: As a user, I want to remove a Todo so that I do not track unnecessary tasks

  Background:
    Given an instance of the Todo API server is running
    Then the user can access the Todo manager

  Scenario Outline: Remove an existing Todo with a valid ID (Normal Flow)
    Given "<title>" is the title of a Todo that needs to be removed
    And the "<title>" Todo exists
    When the user wants to remove the "<title>" Todo
    Then the Todo will be removed

    Examples:
      | title          |
      | Homework       |
      | Study          |
      | Attend Lecture |


  Scenario Outline: Remove all existing Todos that have been completed (Alternate Flow)
    Given "<title1>" and "<title2>" are existing Todos with the completion status "<doneStatus1>" and "<doneStatus2>"
    When the user wants to remove the completed Todos
    Then the completed Todo instances will be removed

    Examples:
      | title1   | doneStatus1 | title2   | doneStatus2 |
      | Dummy1-1 | false       | Dummy2-1 | false       |
      | Dummy1-2 | false       | Dummy2-2 | true        |
      | Dummy1-3 | true        | Dummy2-3 | true        |

  Scenario Outline: Remove a Todo that does not exist (Error flow)
    Given "<title>" is the title of a Todo that does not exist
    When the user wants to remove the "<title>" Todo
    Then the Todo will not be removed

    Examples:
      | title           |
      | Procrastination |
      | Sleeping        |
      | Skip Lecture    |