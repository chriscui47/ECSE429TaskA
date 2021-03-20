Feature:
  As a student, I create a project,
  so that I can organize my work
  Background:
    Given the Todo API server is running 1
  Scenario Outline: Creating a new project (Normal Flow)
    Given "<projectTitle>" is the title of the project
    Given "<projectDescription>" is the description of the project
    Given "<projectActiveStatus>" is the active state of the project
    When the user creates a project for a class
    Then a project with "<projectTitle>", "<projectActiveStatus>", "<projectDescription>" will be created

    Examples:
      | projectTitle |projectDescription| projectActiveStatus|
      | Today        |    hallo         | True               |
      | General      |    goodbye       |False               |

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

  Scenario Outline: Creating project with id
    Given "<id>" is the id of the project
    When the user creates a project for a class
    Then there exists 0 projects for id "<id>"
    Examples:
      |  id|
      |  999    |