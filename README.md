# MAD
## Member: 
1. Ford Nguyen
2. Gyubeom Kim
3. Ivan Mendez
4. D. Jared Idler
## Description:
This is our group's project at UW Tacoma TCSS 450 in effort to learn Mobile Application Development. This app consists of a Login/Register System, Messaging, Contact and Weather Forecast. All these system above is supported by a Heroku back-end which will be link below. After the user register an account (there are verification steps), they will be to login and use other functionalities mentioned above and will be discuss in detail below.
## Links:
[Project](https://github.com/tainguyen2101/Team-1-TCSS-450)  
[Back-end](https://github.com/gyubeomK/mobileapp-group-backend)  
[Meeting Notes](https://drive.google.com/drive/folders/1vBoKj9sO7CnwNE1Vhs3ifGz9QgwD-oiq?usp=sharing)

## Test Account:
This one is the default username and password for those who want to try out our project quick  
Username: test12345@uw.edu  
Password: Test1234@  

Username: fordtest1234@test.com  
Password: Kello1234@

## Components:
### Contact (Ford Nguyen)
#### What has been implemented:
* Send friend request to another using their username
* Accept/Decline contact request
* Remove an existing contact
* New contact request notification (Foreground and Background)
* Remove contact from list remove it from chat room
* Search for contact
    - When first load Search, click to another tab then back, contact will show up
#### Extra Features:
* Floating Add Button
    - When trying to add with username, it will tell the user whether the contact with that username exists, already friend or request pending (works either side)
#### Bugs:
* When deleting a contact from the list, the contact (if they are in favorite) still persists until the user re-login.
* Contact in Search won't show up initially, change to another tab and back fixes it.

## Chat, User Setting(Look and Feel and Password Finding) (Gyubeom Kim)
#### What has been implemented:
*	Add a member into chat room from contact list 
*   Add new chat room with user-typed title
*	Remove an existing chat room
*	Able to view multiple chat rooms
*	New message received notification if a user is in different section (Foreground)
*	Send and receive messages
*	A user is able to change his/her password
*	Color scheme is able to be dynamically switched 
#### What has not been implemented:
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
*	Current weather conditions for an area
*   12 hour forecast for an area
*   5 day forecast for an area
*   Able to search weather by current location, by zip code, or by selecting an area on a map
#### What have not been implemented:
*	Ability to save locations
*   No images for weather conditions
#### Extra Features:
*	Preview of 4 upcoming hour conditions on weather home page (click for 12 hours)
*   Preview of 4 upcoming day conditions on weather home page   (click for 5 days)
#### Bugs:
*   Day and Hour still in ISO8601 format
*   Weather home fragment duplicates data causing the recycler view to add double the cards
*   Weather home page reverts to first area search location

## Email services (D. Jared Idler)
#### What was implemented:
* Functional validation / registration with real emails
* Sends a single use validation url one time
* Waits on screen to register
* Registers unique emails

#### What was not implemented
* Usage of username
* Security measures to prevent intentional denial of service (timeout between requests, etc.)
#### Extra features:
* Sends email to change password to a temporary password if password forgotten
* Password is changeable in the app upon login
#### Bugs:
* Back button not safe to use with connection to backend
* Registering multiple emails in one go may be inconsistent
* UI for some screens may be inconsistently spaced or sized depending on device
* Password post-change may be inconsistent to reach because of hashing process

## For Reviewer
- To register for an account, please use an actual email address for the verification steps
- If you guys have any questions, please email me at tng2101@uw.edu
- Do NOT hit the back arrows when registering an email
- Don't lose the validation email either
