# Diet Tracking App

This is an open source Android app which allows the user to calculate their nutritional intake, set the target for each nutrient respectively and save foods on their local database for easy addition in the future.   

# App Walkthrough

## Main Screen

This is the main screen of the app and these are the features being as followed. 
![Main Screen](https://github.com/MichaelWoj/DietApp/assets/43684434/01e6c5ef-be2e-4549-bc86-32d63661fe4f)

1. This is where the users daily total nutrient will be calculated and displayed.

2. A target for each nutrient set by the user.

3. By default the lock locked, clicking on it unlock it and allow the user to set the values they desire for each field. Clicking the lock again locks it and sets the targets until they're manually changed again. 

![Unlocked Target](https://github.com/MichaelWoj/DietApp/assets/43684434/39572e38-286d-4439-ae98-6082c09b5fac)

4. Brings the user to their local database where all of the foods they saved for ease of access are stored. Users can also add new foods there.

5. Brings the user to screen that they can manually enter nutrients to be added to the counter as once off or added to the counter and saved in the database at the same time.

6. Undo's the previously added food. This be used until all of the fields are 0's.

7. Resets all of the nutrient fields (except the set targets) to 0.

## Database Screen

![Database Screen](https://github.com/MichaelWoj/DietApp/assets/43684434/b16376e2-dda7-4a79-80e8-e81d73b58346)

8. Search bar to allow easily find specific foods.

9. The button brings up different types of sorts to allow the user to for example sort the database by calories in a descending order.
    
![Sort Screen](https://github.com/MichaelWoj/DietApp/assets/43684434/40e2557f-3395-4640-ab43-6227b84b2477)

10. This is where all of the entries in the database are displayed. Click on one brings the user to that item's page.

11. Allows the user to directly add a food into the database
    
![Food Type Popup](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/23db8ea2-35f1-4fec-b094-e1e0968506ee)

12. Allows the user to add foods with set weight/that will always have the same values. Eg an egg.

13. Allows the user to add foods that weight which might change from meal to meal. Eg rice.

## Item Page

![Item Page](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/82afefd8-97e8-4754-bbca-4352ed306c13)

14. Brings up the same looking card as 9. but this one allows the user to either edit the entry or delete it

15. Adds the entry to the daily total shown in 1.

## Variable Weight Item Page

![Variable Weight Item Page](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/069e0ee6-5ad1-49db-96a8-9b7289d9246c)

16. The user inputs the weight of their current meal so the nutritional value gets calculated and added accordingly.

## Add Variable Weight Item

![Variable Weight Add Item](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/e38f7ff6-dd21-46f4-a63e-61b9ef0e95b5)

17. The user inputs the weight corresponding to the nutrient ammount of the food being added. Eg put 100g when adding nutrients corresponding to 100g of rice.

18. Brings the user to a popup which allows the user to input the display weight. This weight is used to display the corresponding nutrient values in the Databse and Item Page screens

![Variable Weight Add Item](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/2e88b38b-6e0f-4ee2-8334-6ed1b804c33a)

## Manual Item Add

![Manually Add Item](https://github.com/MichaelWoj/DietTrackingApp/assets/43684434/04b625d0-341a-4ea3-9fa8-68129f0c2711)

19. Switch which determines if the added food will just be added to the total count shown in 1. or added to the count and saved in the database for future use. If the switch is flipped then the user must provide a name for the food, otherwise the name option will be unavailable.

# Known Issues

Sometimes when the database is sorted using the sorts shown in 9. the name of the food doesn't move with the nutrient values. I am unsure what causes it and the bug is not easily replicable. A simple fix is just to go back to the main menu and re enter the Local Food Database.

