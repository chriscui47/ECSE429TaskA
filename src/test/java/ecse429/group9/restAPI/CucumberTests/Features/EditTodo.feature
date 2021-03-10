Feature: As a user, I want to modify a Todo so that I can update my tasks

  Background:
    Given the Todo API server is running
    Then the user can access the Task Manager

  Scenario Outline: Overwrite the title and description of an existing Todo (Normal Flow)
    Given the "<title>" Todo with description "<description>" exists
    And the user knows the "<title>" Todo ID
    When the user wants to modify a Todos title and description to "<newTitle>" and "<newDescription>"
    Then the Todo will have a new title "<newTitle>" and a new description "<newDescription>"

    Examples:
      | title          | description                    | newTitle | newDescription        |
      | Homework       | Complete homework for tomorrow | Project  | Work on Team Project  |
      | Study          |                                | Meeting  | Talk to Members       |
      | Attend Lecture | Join Zoom lecture              | Sleep    | Rest for coming exams |


  Scenario Outline: Change the completion status of an existing Todo (Alternate Flow)
    Given the "<title>" Todo with completion status "<doneStatus>" exists
    And the user knows the "<title>" Todo ID
    When the user wants to modify a Todos completion status to "<newDoneStatus>"
    Then the Todo will have a new completion status "<newDoneStatus>"

    Examples:
      | title          | doneStatus | newDoneStatus |
      | Homework       | false      | true          |
      | Study          | true       | false         |
      | Attend Lecture | false      | true          |

  Scenario Outline: Modify the title, completion status or the description of a Todo that does not exist (Error flow)
    Given "<id>" is the ID of a Todo that does not exist
    When the user wants to modify the "<field>" of a Todo with ID "<id>"
    Then a Todo instance will not be modified

    Examples:
      | id  | field       |
      | 143 | title       |
      | 793 | doneStatus  |
      | 99  | description |