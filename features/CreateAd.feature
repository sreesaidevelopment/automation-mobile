Feature: Generating ads in olx app
Description Test all the user login functionalities

Scenario: Create ad in olx app
	Given Launch olx app
	And Login to olx account using credentials
	And Tap on menu button and tap on ‘Post an Ad’ link
	And Attach three images and fill all text fields
	And Tap on maps field and enter city (pick from file)
	And Select the location from drop down options
	And Tap on Submit button
	Then Verify that ad is created successfully
	