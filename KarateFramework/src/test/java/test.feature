Feature: Testing Restful Booker API

  Background: 
    * url 'https://restful-booker.herokuapp.com'

  Scenario: GET Booking IDs Request
    Given path '/booking/{bookingId}'
    And def bookingId = 1
    When method GET
    Then status 200
    And print response

 Scenario: POST Request
    Given path '/booking'
    And header Content-type = "application/json"
    And request { "firstname": "Shaik", "lastname": "Hasansaheb", "totalprice": 100, "depositpaid": true, "bookingdates": { "checkin": "2024-02-01", "checkout": "2024-02-02" }, "additionalneeds": "Breakout" }
    When method POST
    Then status 200
    And print response
    # And def expectedOutPut = projectPath+ 'src/test/java/responseBody/postRequestResponse.json'
    # And match response == expectedOutPut
    And match response == { "firstname": "Shaik", "lastname": "Hasansaheb", "totalprice": 100, "depositpaid": true, "bookingdates": { "checkin": "2024-02-06", "checkout": "2024-02-06" }, "additionalneeds": "Breakout" }

  Scenario: PUT Request
    Given path '/booking/{bookingId}'
    And def bookingId = 1
    And header Content-type = "application/json"
    And request { "firstname": "Shaik", "lastname": "Hasansaheb", "totalprice": 200, "depositpaid": true, "bookingdates": { "checkin": "2024-02-06", "checkout": "2024-02-06" }, "additionalneeds": "mobile" }
    When method PUT
    Then status 200
    And print response
    And match response contains { "firstname": "Shaik", "lastname": "Hasansaheb", "totalprice": 200 }

  Scenario: PATCH Request
    Given path '/booking/{bookingId}'
    And def bookingId = 1
    And header Content-type = "application/json"
    And request { "firstname": "LTIMindtree" }
    When method PATCH
    Then status 200
    And print response
    And match response contains { "firstname": "LTIMindtree" }

  Scenario: DELETE Request
    Given path '/booking/{bookingId}'
    And def bookingId = 1
    And header Content-type = "application/json"
    When method DELETE
    Then status 201
  