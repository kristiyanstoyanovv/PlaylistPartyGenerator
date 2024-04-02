In progress..

Java/Spring web application.
The application provides an opportunity to mix playlists with another users.
The concept is to make a individual playlist with songs,
make or join a groups with another users, mix your playlist with everybody's playlist in the group using a liking method.
Each user in the group has the right to like songs, the mixed group playlist is sorted by likes,
so for example the song with the most likes will be at the top of the list and the main idea is to 
create a good party mix.

Registering/Login method:
The application uses spring security and custom login page, every operation like registering
or resetting the password requires a confirmation using validation tokens.
The application generates a random validation token placed in a link,
which is send to the user's e-mail, the user needs to confirm the action with clicking on
the link received on his e-mail.

IMPORTANT:
Each user password is encrypted and stored in the database using BCrypt algorithm.
