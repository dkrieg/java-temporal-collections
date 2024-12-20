Feature: Bi-Temporal Collection

  Scenario: An empty collection
    Given an empty bi-temporal collection of Integer
    Then it should have a size of 0
    * is empty should be 'true'

  Scenario: Add one item effective as of now to an empty collection
    Given an empty bi-temporal collection of Integer
    When you add the number 10 to the collection effective as of now
    Then it should have a size of 1
    * is empty should be 'false'
    * get as of now should be present
    * get as of now should contain value 10


  Scenario: Add one item effective as of now to an empty collection and expire it as of now
    Given an empty bi-temporal collection of Integer
    When you add the number 10 to the collection effective as of now
    * you wait for 3 seconds
    * you expire as of now
    Then it should have a size of 2
    * is empty should be 'false'
    * get as of now should be empty
    * get prior to now should be present
    * get prior to now should contain value 10

  Scenario: Add more than one item effective as of now to an empty collection

  Scenario: Add more than one item effective as of the same valid time to an empty collection

  Scenario: Add more than one item effective as of the same valid time to an empty collection then expire it as of the same valid time

  Scenario: Populate an empty collection with bi-temporal data and verify its contents
