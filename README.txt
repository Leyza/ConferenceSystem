Hi there!

Upon starting the program, you would be provided with 3 options:
(1) Login (2) Explore as a Guest (3) Exit and Save the Program

You can choose to explore as a Guest without logging in. The only function you can enjoy is to browse all scheduled
events.

Alternatively, you can use any of the following credentials to log in and interact more with our program (e.g. sign up
for an event, add friend, send message, etc.):

Organizer Role - Email: organizer1@conference.com / Password: organizer1

Speaker Role   - Email: speaker1@conference.com / Password: speaker1

Attendee Role  - Email: attendee1@conference.com / Password: attendee1
               - Email: attendee2@conference.com / Password: attendee2

[Added]VIP Role - Email: vip1@conference.com / Password: vip1


After you successfully log in, you will be provided a menu of options and you can select the option using the index
and/or typing the name of the option. We try to make the option self-explanatory and everything from this point should
be straightforward.


Please make sure to use the "Exit and Save the Program" option when closing the program as that will preserve and save
the data.

Note:
We pre-generate sample users, events, rooms, friends, and conversations data on first launch. We save ALL program
information if you Exit and Save the Program successfully.

========================
Different Users Instructions
========================

About Attendee

    Attendee is the basic user type in our program. Attendees are able to access messenger, view events and register/cancel
    an event when they are logged in. Detailed functions will be shown in the menu.


About Organizer

    Organizer inherits all the functions from Attendee. Additionally, Organizer can manage events, manage users, manage
    user requests and view summary data of conferences. Detailed functions will be shown in the menu.


About Speaker

    Speaker inherits all the functions from Attendee. Additionally, Speaker can view the events he gives as well as view
    the attendees and send message to them.


About Guest

    Guest does not inherits functions from Attendee. Guests can be either interpreted as users, or non-users. It really
    depend on how we view them. The idea is that we won't need to login if we want to use a program as a guest. However,
    unlike other users that require you to login, guests have heavier restrictions on what actions they can take.

    Basically, guests will only be able to explore the program. In other words, the guests can view events,
    including viewing events by speakers and days. No other actions are provided.

    How? Simply select Explore as a Guest. For further actions, we encourage to log-in!


About Vip User

    Vips have access to all the actions that any other Attendees can take, but also with extra features.
    Let us introduce the main features of Vip!

    1. Vip users can manage their own interested event list. They can show their interest in some events and
     keep them as a wishlist to sign up in the future or just to remember which they liked. Similarly, they
     can remove an event from their interested events, whenever they want to. They can view their list
     to see what they liked, maybe to recommend to their friends about some of them, or to look up for
     speakers if they want to!

     How? Select "Manage Interested Events" and choose the relevant options.


    2. Vips also can manage their favourite speakers list. They can add/remove a speaker as their favourites
     and view whenever they want. You might notice that this list works almost the same as the interested events,
     except that it acts like a "following" list in social media. That is, vips will get notified when there
     are new events schedules with their favourite speakers. This is done by a messenger.

     How? Select "Manage Favourite Speakers" and choose the relevant options.


    3. Vips can hold a party. Note that it is restricted as a party particularly, so they cannot invite
     speakers, even if they are their favourites. They get to choose which rooms they want
     to have a party at, time, duration, and etc.

     How? This is done by the menu option: Host a Party


========================
Messenger Instructions
========================

The first option presented to all users is 'Enter Messenger'. Upon selection, the user enters the messenger subprogram.
Here you can view all of your (i.e. the logged in user's) conversations and their messages, along with your friends list.
The messenger subprogram has it's own list of options and all future input prompts (preceded by '[Messenger]') will be
for selecting actions from this list. To exit the subprogram and return to the main menu, enter the exit option ('8').

A few users have demo conversations included with this build. Note that all demo messages will have the same time (the
time of program execution).

To create a new conversation you can send a message to a user (or users) that you do not currently have a conversation
with. Attendees must both have each other on their respective friends lists to start a conversation, organizers and
speakers have no such requirement.

When prompts ask for an index (such as a conversation or recipient index), look at the number to the left of the desired
item when viewing your conversations/friends list. This is the index. If you want to message a user that's not on your
friends list (and if you have the permissions to do so), you can alternatively enter the recipient's email.

Special messenger functions only accessible by Organizers and Speakers (such as the ability to message all attendees of
an event) are accessed outside of the messenger subprogram, listed as additional options in the main menu.


========================
Events Instructions
========================

Each event has its unique ID, attendees list, room, capacity, speaker(s), event date and time, and duration of event.

Event can be created or cancelled by an organizer. Room, capacity, speaker(s), event date and time, and duration of
event are set when organizer creates an event.

- Room: Organizers need to specify a room and may request special features in the room when creating an event.
- Capacity: Each event has a maximum number of people who can attend it. This amount can be set when the event is
created and also changed later, by an organizer. our program should check the maximum capacity for the room where the
event will be held, and prevent the number of attendees from going above the room's capacity.
- Speaker: This is optional as there are many types of events: one-speaker event, multi-speaker events, and no-speaker
events.
- Event date and time: The date should be entered in the format of dd/mm/yyyy HH:MM
- Event duration: Events can last from 1 to 23 hours and it must be increment in hour, e.g. an event cannot be 1 hour
and 40 minutes


========================
Room Instructions
========================

Room is the place where every event is held. Each room has its unique ID, name, capacity, opening hours, the schedule,
list of features.

Room can be created by an organizer. Name, capacity and opening hours are set when organizer creates a room.

- Opening hours: Rooms also have its own opening and closing time and all events must start and end during this period.
Longest room can open for each day is from 00:00 to 23:00 (can be shorter of course, depending on how the room is
created by organizer). There will be at least 1 hour down time everyday from 23:00 to 00:00 where no events can be
scheduled for.






