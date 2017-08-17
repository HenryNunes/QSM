Feature: Porta Caneta

  Scenario: colocandoCaneta
    Given Open
    When Deposita
    And Fecha
    Then NaoVazio

  Scenario: retiraCaneta
    Given Vazio
    When Open
    And Deposita
    And Remove
    Then Vazio

  Scenario: colocaRetira
    Given Open
    When Deposita
    When Remove
    When Fecha
    Then Vazio

  Scenario: abreFecha
    Given Open
    Then Fecha