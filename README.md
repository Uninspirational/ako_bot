# ako_bot
Bandori Discord bot in Java
Currently:
can search http://bandori.wikia.com/wiki/BanG_Dream!_Wikia to play songs
can search and play youtube videos
can search https://bandori.party/ api for cards and members
can play audio files hosted on webservices (i.e. https://mixtape.moe/)

Future:
upload audio files to mixtape.moe and return the url
create and store playlists using such urls
allow shuffling of multiple bands' songs into queue
implement more commands
fix known bugs

uses Lavaplayer
https://github.com/sedmelluq/lavaplayer

JDA
https://github.com/DV8FromTheWorld/JDA

GSON
https://github.com/google/gson

Known bugs
sometimes fails to load a song from a youtube search, but works after the 2nd or 3rd try
sometimes fails to play next song in queue(should be fixed)

**new**
playing from youtube link broke, oops

0.8.5
updated responses to be more characteristic of Ako
added logger
more ways to search bang dream cards

**fixed**
$queue now works for bandori songs, but not for length
