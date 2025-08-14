#@FXAPI
Feature: FX Rate API Basic Verification

  Scenario: Verify AUD to USD exchange rate conversion
    Given I prepare to convert 100 AUD to USD
    When I send the exchange rate request
    Then the response status code should be 200
#    Then the response should contain valid AUD and USD rate details
  #扩展case
#Scenario Outline: Verify valid currency pair conversion
#    Given I prepare to convert <amount> <buyCurrency> to <sellCurrency>
#    When I send the exchange rate request
#    Then the response status code should be 200
#    And the response should contain valid <buyCurrency> and <sellCurrency> rate details
#    Examples:
#      | amount | buyCurrency | sellCurrency |
#      | 100    | AUD         | USD         |
#      | 500    | HKD         | EUR         |
#      | 1000   | CNY         | SGD         |


 @Negative
  Scenario: Verify error when buy_currency is not specified
    Given I prepare to convert 100 AUD without specifying buy currency
    When I send the exchange rate request without "buy_currency"
    Then the response status code should be 400
    And the response should contain error message "'buy_currency' is not specified"
  @Negative
  Scenario: Verify error when sell_currency is not specified
    Given I prepare to convert 100 AUD without specifying sell currency
    When I send the exchange rate request without "sell_currency"
    Then the response status code should be 400
    And the response should contain error message "'sell_currency' is not specified"

#  @Negative
#  Scenario: Verify unauthorized request
#    Given I have an invalid API authorization token
#    And I prepare to query exchange rate from "AUD" to "USD" with amount 100
#    When I send the current exchange rate request
#    Then the response status code should be 401