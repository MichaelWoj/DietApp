# Diet Tracking App

This is an open source Android app which allows the user to calculate their nutritional intake, set the target for each nutrient respectively and save foods on their local database for easy addition in the future.   

# App Walkthrough

## Main Screen

This is the main screen of the app and these are the features being as followed. 
![A Main Menu](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/59daf680-7058-4782-adc2-3d3a5d36493c)


A1. This is where the users daily total nutrient will be calculated and displayed.

A2. A target for each nutrient set by the user.

A3. By default the lock locked, clicking on it unlock it and allow the user to set the values they desire for each field. Clicking the lock again locks it and sets the targets until they're manually changed again. 

![A Unlocked Target](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/612ab0ca-cc01-4006-8f25-1f5306ba5ff1)

A4. Brings the user to their local database where all of the foods they saved for ease of access are stored. Users can also add new foods there.

A5. Brings the user to screen that they can manually enter nutrients to be added to the counter as once off or added to the counter and saved in the database at the same time.

A6. Brings the user to the calendar where all of the food they have added in a day is displayed on the according day. This can be navigated with the calendar widget

A7. Undo's the previously added food. This be used until all of the fields are 0's.

A8. Resets all of the nutrient fields (except the set targets) to 0.

## Database Screen

![B Databse Screen](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/80a44ba2-ddae-4716-881b-5ee249e0879a)

B1. Search bar to allow easily find specific foods.

B2. The button brings up different types of sorts to allow the user to for example sort the database by calories in a descending order.

![B Sort Screen](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/08279c08-1923-4b3f-b34d-46dfc92af1a8)

B3. This is where all of the entries in the database are displayed. Click on one brings the user to that item's page.

B4. Allows the user to directly add a food into the database
    
![B Food Type Popup](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/aa7322db-6e8d-4447-af3e-bd39106463ac)

B5. Allows the user to add foods with set weight/that will always have the same values. Eg an egg.

B6. Allows the user to add foods that weight which might change from meal to meal. Eg rice.

## Item Page

![C Item Page](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/1deb70fe-f9ff-4bcf-90e4-0b7a7b639603)

C1. Brings up the same looking card as 9. but this one allows the user to either edit the entry or delete it

C2. Adds the entry to the daily total shown in 1.

## Variable Weight Item Page

![C Variable Weight Item Page](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/39c13dff-c574-467e-b2cf-a71884e3bc30)

C3. The user inputs the weight of their current meal so the nutritional value gets calculated and added accordingly.

## Add Variable Weight Item

![D Variable Weight Add Item](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/8057cca6-e9ff-410f-8678-2df34c52f056)

D1. The user inputs the weight corresponding to the nutrient ammount of the food being added. Eg put 100g when adding nutrients corresponding to 100g of rice.

D2. Brings the user to a popup which allows the user to input the display weight. This weight is used to display the corresponding nutrient values in the Databse and Item Page screens

![D Display Weight](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/2078d134-9313-4d75-abbf-8c714ac963b1)

## Manual Item Add

![D Manually Add Item](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/617505b8-47ee-4031-b1b5-5e5724e9a48a)

D3. Switch which determines if the added food will just be added to the total count shown in 1. or added to the count and saved in the database for future use. If the switch is flipped then the user must provide a name for the food, otherwise the name option will be unavailable.

## Calendar

![E Calendar](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/0cdee6bd-8616-4a74-adda-be69cda1644d)

E1. Calendar widget that works like any other.

E2. User entered weight for that day. 

E3. Turns on Calendar Settings

E4. Changes the card from displaying the user set weight to the sum of each nutrient added on that day

E5. Clicking an entry will show up popup asking if you want to delete the entry. If you chose yes, the entry will be deleted and if it is currently the same day as when the entry was added, the entry's nutrition will be subtracted from the daily total in the main screen. 

![E Calendar Nutrition](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/87efc522-c270-4e46-bf37-0e8905706c69)

![E Calendar Settings](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/b1cccefa-6612-4096-8c19-1248c88d180f)

E6. Brings up a popup for the user to enter the weight for the day the calendar is currently on. Only one weight will be saved for day so if weight is entered more than once, the one entered later will override the previous one.

E7. Brings up a popup which allows the user to enter a number and select if its that may days or hours, and delete all entires older than that date.

![E Calendar Mass Delete](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/61aba659-6b8e-4f2c-b7ff-6d6e24c2f55d)

E8. Changes the text beside user set weight to KG or LBS.

E9. Drops down a window allowing users to select "Month/s" or "Day/s"

# Known Issues

Sometimes when the database is sorted using the sorts shown in 9. the name of the food doesn't move with the nutrient values. I am unsure what causes it and the bug is not easily replicable. A simple fix is just to go back to the main menu and re enter the Local Food Database.

