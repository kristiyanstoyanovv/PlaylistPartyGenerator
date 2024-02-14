package com.playlistgenerator.kris.song;

import com.playlistgenerator.kris.user.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name="songs")
public class SongEntity {
    @Id
    @SequenceGenerator(name = "song_sequence", sequenceName =  "song_sequence")
    @GeneratedValue(generator = "song_sequence", strategy = GenerationType.SEQUENCE)
    private Long songId;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
    @Column(name="url", nullable = false, unique = true)
    private String url;
    @Column(name="name")
    private String name;

    public SongEntity() {
    }

    public SongEntity(UserEntity userEntity, String url) {
        this.userEntity = userEntity;
        this.url = url;
    }

    public SongEntity(UserEntity userEntity, String url, String name) {
        this.userEntity = userEntity;
        this.url = url;
        this.name = name;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + songId +
                ", user=" + userEntity +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
