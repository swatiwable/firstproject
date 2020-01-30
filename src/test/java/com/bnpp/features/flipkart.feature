Feature: flipkart
  
  Scenario: login_to_flipkart_site
    Given user navigate to flipcart
    And login as "mobile_no" with "password"
    And click on button "login"
    Then user navigate to home page
    And user enter "phone" in "search_field" 
    And user click on "search_button" 
    And user click on checkbox "Assured_checkbox"
    Then show mobiles of flipkart assure 
    And click on "RAM_field"
    And user selects "min_value" from "min_field" 
    