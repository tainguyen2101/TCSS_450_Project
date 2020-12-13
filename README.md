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

## Chat, User Setting(Look and Feel and Password Finding) (Gyubeom Kim)
#### What have been implemented:
*	Add a member into chat room from contact list 
*   Add new chat room with user-typed title
*	Remove an existing chat room
*	Able to view multiple chat rooms
*	New message received notification if a user is in different section (Foreground)
*	Send and receive messages
*	A user is able to change his/her password
*	Color scheme is able to be dynamically switched 
#### What have not been implemented:
*	Notification for new chat room created
*	Recent Chat Room connects to home/landing page
#### Extra Features:
*	Toast Message
    - When a user tries to add existing member into chat, display corresponding message (a user is already joined!)
    - When a user tries to add non-existing member into chat, display corresponding message (added!)
*	Dialog
    - If a user typed different old password and all correct information, the message will be popped up corresponding to the situation.
#### Bugs:
*    When a user is added to chat, there is no live update. (should click refresh button)
*    The Toast message and dialog that I created for password finding and chat keep the previous status

## Weather (Ivan Mendez)
#### What have been implemented:
*	
#### What have not been implemented:
*	
#### Extra Features:
*	
#### Bugs:
*    

