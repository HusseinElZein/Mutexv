# Mutex

HOW TO COMPILE THE CODE:

The code representates a scenario, in which users are buying tickets through a booking system. A number of users would be able to buy ONE ticket, each time a user buys a ticket - the amount of tickets will decrease.

When running the code, you will need to enter two input fields. The first input field requires the amount of tickets available in stock, as seen below: "Amount of tickets in stock: "

The second input field required the amount of users that wants to buy the ticket, as seen below: "Amount of users: "

Be aware: If you enter an amount of tickets that's greater than the amount of users - there will still be a quantity of tickets left at the end of the program.

If you enter an amount of tickets that's equal to the amount of users - there will be no tickets left at the end of the program.

If you enter an amount of tickets that's less than the amount of users - some users wont be able to buy the tickets because it has been sold out (a way of displaying deadlock) - a user can stil escape the deadlock/waiting state if another user cancels his reservation for the ticket and therefore get to buy a ticket.
