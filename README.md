Description     
The companies are all set and ready to rent out their cars. Until now, there was no opportunity to interact with potential customers in any way. Let's create a log-in option for the customers so that they can rent a car through the platform.

Objectives      
Create a table named CUSTOMER with the following columns:

ID column should be PRIMARY KEY and AUTO_INCREMENT with the type INT.
NAME column should be UNIQUE and NOT NULL with the type VARCHAR.
RENTED_CAR_ID should have the type INT. This column should be a FOREIGN KEY referring to the ID column of the CAR table, and this column can be NULL in case the customer didn't rent a car.
Update the main menu adding a couple of new options:

1. Log in as a manager
2. Log in as a customer
3. Create a customer
4. Exit

Log in as a manager should work the same as described in the previous stage.

If the option Create a customer was chosen, the program should prompt the user to enter a customer name and then save it in the database. By default, the column RENTED_CAR_ID of the customer should be NULL.

If the Log in as a customer option was chosen, the program should print the list of existing customers and prompt the user to choose one:

Choose a customer:
1. First customer
2. Second customer
3. Back
   
If the Back option was chosen, go back and print the main menu. If the customer list is empty, print the message The customer list is empty! and return to the main menu. If the user chooses one of the customers, print the customer menu:

7. Rent a car
8. Return a rented car
9. My rented car
10. Back
    
If the Back option was chosen, go back and print the main menu.

If the user chose My rented car, print the name of the car they rented and the company it belongs to:

Your rented car:        
Hyundai Venue       
Company:        
Car To Go       

In case a customer doesn't currently have any rented cars, print You didn't rent a car!.

If a customer wants to Return a rented car, the program should set the column RENTED_CAR_ID of the current customer to NULL. If the user tries to return a car that they didn't actually rent, print You didn't rent a car!.

If the customer wants to Rent a car, the program should print the list of available companies and prompt the user to choose one. If the company list is empty, the program should print the message The company list is empty! and return to the customer menu. If the customer has already rented a car, print You've already rented a car!.

If the customer chose a company that doesn't have any available cars at the moment, print No available cars in the 'Company name' company.

If the customer was lucky and the chosen company has cars to rent out, the program should print the cars ordered by their ID column and prompt the user to pick one. 

Remember that indexes in the list should start from 1:

Choose a car:
1. Hyundai Venue
2. Maruti Suzuki Dzire
3. Back

After the user has chosen a car, print the message You rented 'Car name' and then go to the customer menu. If the customer didn't like any of the options and wants to go Back, the program should take them back to the customer menu.

Note that a list numeration should always start with 1.

To pass the tests, you have to enable the auto-commit mode so that all changes are automatically saved in the database file. To do that, call the method connection.setAutoCommit(true) of the Connection object.
Examples
The greater-than symbol followed by a space > represents the user input. Note that it's not part of the input.

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 2

The customer list is empty!

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 3

Enter the customer name:
> First customer
The customer was added!

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 3

Enter the customer name:
> Second customer
The customer was added!

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 2

Customer list:
1. First customer
2. Second customer
0. Back
> 1

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 3

You didn't rent a car!

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 2

You didn't rent a car!

1. Rent a car
2. Return a rented car
3. My rented car
0. Back
> 0

1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 0