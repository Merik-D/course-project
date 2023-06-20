# Course-project

    The task of the course project was to create an API that allows you to receive and transmit information about
    songs, artists, albums, music labels, lyrics.

# To use the program you need:
    1) Clone the repository(https://github.com/Merik-D/course-project.git);
    2) Open a command line (terminal or console) and change to the directory with the Spring Boot project using the "cd" command;
    3) Run "mvn spring-boot:run" as this project uses Maven to build the project;
    4) The startup process remains active until the application is shut down, for example, by pressing Ctrl+C in terminal or console;

# How to use
    For this, we will need "Postman", through which we will send requests to our local server
    We need to use this address - http://localhost:8080:

    Songs:
    GET:/songs - request to pull all songs
    GET:/songs/{id} - request to pull a song by a specific ID
    POST:/songs - request to create song
    PUT:/songs/{id} - request to update song by a specific ID
    DELETE:/songs/{id} - request to delete songs by a specific ID

    Albums:
    GET:/albums - request to pull all albums
    GET:/albums/{id} - request to pull a album by a specific ID
    POST:/albums - request to create album
    PUT:/albums/{id} - request to update album by a specific ID
    DELETE:/albums/{id} - request to delete album by a specific ID
    GET:/albums/{id}/songs - request to pull all songs in album
    GET:/albums/{id}/songs/{id} - request to pull song by a specific ID in album

    Artists:
    GET:/artists - request to pull all artists
    GET:/artists/{id} - request to pull a artist by a specific ID
    POST:/artists - request to create artist
    PUT:/artists/{id} - request to update artist by a specific ID
    DELETE:/artists/{id} - request to delete artist by a specific ID
    GET:/artists/{id}/albums - request to pull all albums by artist
    GET:/artists/{id}/albums/{id} - request to pull a album by a specific ID by artist
    
    Music Labels:
    GET:/musicLabels - request to pull all music labels
    GET:/musicLabels/{id} - request to pull a music label by a specific ID
    POST:/musicLabels - request to create music label
    PUT:/musicLabels/{id} - request to update music label by a specific ID
    DELETE:/musicLabels/{id} - request to delete music label by a specific ID
    GET:/musicLabels/{id}/artists - request to pull all artists by music label
    GET:/musicLabels/{id}/artists/{id} - request to pull a artist by a specific ID by music label
