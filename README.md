# Evento 
![alt tag](https://github.com/sweng-epfl-2015/team-localeventapp/blob/master/Client/Evento/app/src/main/res/drawable/logo_evento.png)

- "Hey dude, what about a foot game tomorrow?"
- "Sounds good! How many are we?"
- "You plus me... mmmh... Two at this time"
- "What a pity. I guess we only can make a Scrabble game then..."
- "Don't worry, don't you know Evento app ? Let's invite people to join us ;)"

Evento is an event manager application allowing people to create, share and join event around them. Crossing the border 
of "friend based" system, the events are open to all people around you sharing the same hobbies, disregarding their age, jobs 
or anything else.

Search events by location, kind of activities and date and join people for a good time sharing your hobbies.

You don't find what you like? You are not enough to organize a football, a paintball party or role playing game? You want
to hike some mountains or to hit the road in motorcycle in group? Let's create your own !

## Content
The repository contains both android and Django server files, stored respectively in Client and Server folder.

## GUI testing
The GUI tests can NOT be launch in jenkins, because of an unkown issue. To the corrector: you can run these tests locally.
1) Comment the ```@ignore``` instruction above all GUI tests
(an exhaustive lists of all test classes containing GUI tests will be provided right here)

2) Set the xamarin emulator and the Google Play as described in forum post
https://sweng-forum.epfl.ch/t/android-emulator-with-google-play-services/191

3) In Client folder, run the test with commande ```./gradlew jacocoReport```

4) The jacocoReport is visible at path ```app/build/reports/jacoco/jacocoReport/html/index.html```




## Credits 
This application was realized as part of Software Engineering project at EPFL, in 2015 by
Ben Gaffinet, Valentin Nigolian, Christophe Schäfer, Gautier Manquat, Thomas Eslier & Joachim Muth
