Feature: Porta Caneta

  Scenario: colocandoCaneta
    Given Abrir
    When Depositar
    And Fechar
    Then NaoVazio

  Scenario: colocaRetira
    Given Abrir
    When Depositar
    When Remover
    When Fechar
    Then Vazio

  Scenario: abreFecha
    Given Abrir
    Then Fechar

  Scenario: retiraCaneta
    Given Vazio
    When Abrir
    And Depositar
    And Remover
    Then Vazio