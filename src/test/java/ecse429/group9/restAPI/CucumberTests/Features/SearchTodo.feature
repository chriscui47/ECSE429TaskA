Feature: As a user, I want to filter a Todo list so that I can track specific tasks

  Background:
    Given an instance of the Todo API server is running
    Then the user can access the Todo manager

  Scenario Outline: Filter a Todo list using the completion status (Normal Flow)
    Given Todos "<title1>" and "<title2>" with the completion statuses "<doneStatus1>" and "<doneStatus2>" exist
    When the user wants to filter for completion status "<filter>"
    Then Todos will be filtered by completion status "<filter>"

    Examples:
      | title1         | doneStatus1 | title2  | doneStatus2 | filter |
      | Homework       | false       | Project | true        | false  |
      | Study          | true        | Meeting | false       | false  |
      | Attend Lecture | false       | Sleep   | true        | true   |


  Scenario Outline: Filter a Todo list using a specific title (Alternate Flow)
    Given a Todo with title "<title>" exists
    When the user wants to filter for "<title>" todos
    Then Todos will be filtered by title "<title>"

    Examples:
      | title          |
      | Homework       |
      | Study          |
      | Attend Lecture |

  Scenario Outline: Filter a Todo list using invalid URL Query Parameters (Error flow)
    Given a Todo with title "<title>" exists
    When the user wants to filter for "<title>" todos using an invalid URL Query
    Then the Todo instances will not be filtered

    Examples:
      | title          |
      | Homework       |
      | Study          |
      | Attend Lecture |