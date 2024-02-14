package com.playlistgenerator.kris.song;

import com.playlistgenerator.kris.song.SongEntity;
import com.playlistgenerator.kris.song.SongRepository;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public SongEntity saveSong(SongEntity songEntity) {
        return songRepository.save(songEntity);
    }
}
