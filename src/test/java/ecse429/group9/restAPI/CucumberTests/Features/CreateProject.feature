Feature:
  As a student,
  I add a project to a course todo list,
  so I can remember it.
  Background:
    Given the Todo API server is running
  Scenario Outline: Adding a new unique task to a todo list (Normal Flow)
    Given "<projectTitle>" is the title of the project
    Given "<projectDescription>" is the description of the project
    Given "<projectActiveStatus>" is the active state of the project
    When the user creates a project for a class
    Then a project with "<projectTitle>" will be created

    Examples:
      | projectTitle |projectDescription| projectActiveStatus|
      | Today        |    hallo         | True               |
      | General      |    goodbye       |False               |
      | Today        |    yessir         |True               |

  Scenario Outline: Creating a pre-existing project
    Given "<projectTitle>" is the title of the project
    Given "<projectDescription>" is the description of the project
    Given "<projectActiveStatus>" is the active state of the project
    When the user creates a project for a class
    Then a project with "<projectTitle>" will be created
    Examples:
      | projectTitle | projectDescription | projectActiveStatus|
      | Today        |    hallo         |True                    |
      | General      |    goodbye       |True                    |
      | Today        |    yessir         |True                   |

  Scenario Outline: Creating project without title
    Given "<projectTitle>" is the title of the project
    When the user creates a project for a class
    Then error 404 will occur

    Examples:
      | projectTitle |
      |      |