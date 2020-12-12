# MAD
## Member: 
1. Ford Nguyen
2. Gyubeom Kim
3. Ivan Mendez
4. D. Jared Idler
## Description:
This is our group's project at UW Tacoma TCSS 450 in effort to learn Mobile Application Development. This app consists of a Login/Register System, Messaging, Contact and Weather Forecast. All these system above is supported by a Heroku back-end which will be link below. After the user register an account (there are verification steps), they will be to login and use other functionalities mentioned above and will be discuss in detail below.
## Links:
[Back-end](https://github.com/gyubeomK/mobileapp-group-backend)

This auto deploy over to Heroku.
## Components:
### Contact (Ford Nguyen)
#### What have been implemented:
* Send friend request to another using their username
* Accept/Decline contact request
* Remove an existing contact
* New contact request notification (Foreground and Background)
* Remove contact from list remove it from chat room
#### What have not been implemented:
* Search for contact
#### Extra Features:
* Floating Add Button
    - When trying to add with username, it will tell the user whether the contact with that username exists, already friend or request pending (works either side)
#### Bugs:
* When deleting a contact from the list, the contact (if they are in favorite) still persists until the user re-login.
