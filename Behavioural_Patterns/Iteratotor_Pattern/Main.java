package Behavioural_Patterns.Iteratotor_Pattern;

import java.util.*;

// Element
class Video {
    private String title;

    public Video(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

// Iterator Interface
interface PlaylistIterator {
    boolean hasNext();
    Video next();
}

// Concrete Iterator
class YouTubePlaylistIterator implements PlaylistIterator {
    private List<Video> videos;
    private int position;

    public YouTubePlaylistIterator(List<Video> videos) {
        this.videos = videos;
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < videos.size();
    }

    @Override
    public Video next() {
        if (hasNext()) {
            return videos.get(position++);
        }
        return null;
    }
}

// Playlist Interface
interface Playlist {
    PlaylistIterator createIterator();
}

// Concrete Playlist
class YouTubePlaylist implements Playlist {
    private List<Video> videos;

    public YouTubePlaylist() {
        videos = new ArrayList<>();
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    @Override
    public PlaylistIterator createIterator() {
        return new YouTubePlaylistIterator(videos);
    }
}

// Driver
public class Main {
    public static void main(String[] args) {

        YouTubePlaylist playlist = new YouTubePlaylist();

        playlist.addVideo(new Video("Flyweight Pattern"));
        playlist.addVideo(new Video("Iterator Pattern"));
        playlist.addVideo(new Video("Observer Pattern"));
        playlist.addVideo(new Video("Decorator Pattern"));

        PlaylistIterator iterator = playlist.createIterator();

        System.out.println("Playlist Videos:");

        while (iterator.hasNext()) {
            Video video = iterator.next();
            System.out.println(video.getTitle());
        }
    }
}