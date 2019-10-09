Feature: Homophonic cypher encoder

  Scenario: Encode text
    Given I have loaded text from file "src/test/resources/lorem-ipsum.txt"
    When I create encryption key from loaded text
    And I encrypt loaded text
    Then I get encrypted text

  Scenario: Decode text
    Given I have loaded text from file "src/test/resources/lorem-ipsum.txt"
    When I create encryption key from loaded text
    And I encrypt loaded text
    And I decrypt encrypted text
    Then I get decrypted text
