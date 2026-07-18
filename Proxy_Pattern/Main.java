import java.util.HashMap;

interface VideoDownloader {
    void downloadVideo(String videoUrl);
}

class RealVideoDownloader implements VideoDownloader {
    @Override
    public void downloadVideo(String videoUrl) {
        System.out.println("Downloading video from: " + videoUrl);
    }
}

class CacheVideoDownloader implements VideoDownloader {
    private RealVideoDownloader realVideoDownloader;
    private HashMap<String, String> cache = new HashMap<>();

    CacheVideoDownloader() {
        this.realVideoDownloader = new RealVideoDownloader();
    }

    @Override
    public void downloadVideo(String videoUrl) {
        if (cache.containsKey(videoUrl)) {
            System.out.println("Video already downloaded. Fetching from cache: " + videoUrl);
        } else {
            realVideoDownloader.downloadVideo(videoUrl);
            cache.put(videoUrl, videoUrl);
        }
    }
}

class Main {
    public static void main(String[] args) {
        CacheVideoDownloader cacheVideoDownloader = new CacheVideoDownloader();
        cacheVideoDownloader.downloadVideo("http://example.com/video");
    }
}