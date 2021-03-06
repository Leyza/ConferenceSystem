# Conference System

An application to manage the logistics of conference events. Designed and created by 8 University of Toronto students utilizing Clean Architecture and Design patterns for course CSC207 in Fall 2020.

## Users

### Guest

Guests can explore the application without logging in, but are limited to only viewing the event list.

### Attendee

Attendees are the basic user type in our program. Attendees are able to access messenger and friends functions, view events, and register/cancel registration to event.

### Speaker

Speakers are users who can also give speeches/talks at events. They inherit all functionality attendees have, and can also view and message attendees registered for an event they will be presenting at.

### Organizer

Organizers are users who manage the events, user requests, and attendee/speaker accounts. They inherit all functionality attendees have as well.

### Vip

Vips inherit all attendee functionality, but also have access to special functions such as favoriting events, favoriting speakers, and reserving rooms to host parties.

## Messenger

All users have access to the messaging functionality. All users have the ability to start messages with one or multiple users via friend list or email. Speakers and Organizers can also message all attendees of a specific event.

## Events

Events can be created, modified, and canceled by organizers. Events contain the following information:

- Room: The room the event will be held in.
- Capacity: Maximum number of users that can attend.
- Speaker: The speaker(s) who will be speaking in the event.
- Features: The room features that this event will need to utilize.
- Event date and time: When the event starts.
- Event duration: How long the event will last.

## Rooms

Rooms are locations that can be used to host events. Rooms contain the following information:

- Opening hours: When the room is available to host an event.
- Schedule: The booking schedule for the room.
- List of features: Features that the room has (e.g. microphone, projector, whiteboard, etc.)

## Data

TestData.java in phase2/src/main contains sample data to run this program. Sample credentials listed below:

Organizer - Email: organizer1@conference.com / Password: organizer1

Speaker   - Email: speaker1@conference.com / Password: speaker1

Attendee  - Email: attendee1@conference.com / Password: attendee1
          - Email: attendee2@conference.com / Password: attendee2

VIP - Email: vip1@conference.com / Password: vip1

All data will be saved upon exiting and saving from within the program.

## Usage

Run phase2/src/main/App.java. Select the options that appear by typing its index or typing the option in the shell.
