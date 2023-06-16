package ua.lviv.iot.spring.first.rest.service;

import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Song;


import java.io.IOException;
import java.util.List;


public interface AlbumService extends TemplateService<Album, Integer> {

    List<Song> getAllSongsInAlbum(Integer albumId) throws IOException;

    Song getSongInAlbumById(Integer albumId, Integer songId);

//    Song deleteSongFromAlbum(Integer albumId, Integer songId) throws IOException;
//
//    Song updateSongInAlbum(Integer albumId, Integer songId, Song updatedSong) throws IOException;

    //void addSongToAlbum(Integer albumId, Integer songId, Song newSong) throws IOException;
}
