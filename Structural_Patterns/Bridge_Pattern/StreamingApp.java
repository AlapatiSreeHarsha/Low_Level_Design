package Structural_Patterns.Bridge_Pattern;

interface VideoQuality {
    void load();
    void play();
}

class UltraHDQuality implements VideoQuality {
    public void load() {
        System.out.println("Loading Ultra HD quality stream...");
    }
    public void play() {
        System.out.println("Playing in Ultra HD quality.");
    }
}

class HDQuality implements VideoQuality {
    public void load() {
        System.out.println("Loading HD quality stream...");
    }
    public void play() {
        System.out.println("Playing in HD quality.");
    }
}

class SDQuality implements VideoQuality {
    public void load() {
        System.out.println("Loading SD quality stream...");
    }
    public void play() {
        System.out.println("Playing in SD quality.");
    }
}

abstract class VideoPlayer {
    protected VideoQuality quality;

    public VideoPlayer(VideoQuality quality) {
        this.quality = quality;
    }

    public abstract void load();
    public abstract void play();
}

class SmartTvPlayer extends VideoPlayer {
    public SmartTvPlayer(VideoQuality quality) {
        super(quality);
    }

    public void load() {
        System.out.print("[Smart TV] ");
        quality.load();
    }

    public void play() {
        System.out.print("[Smart TV] ");
        quality.play();
    }
}

class MobilePlayer extends VideoPlayer {
    public MobilePlayer(VideoQuality quality) {
        super(quality);
    }

    public void load() {
        System.out.print("[Mobile] ");
        quality.load();
    }

    public void play() {
        System.out.print("[Mobile] ");
        quality.play();
    }
}

class WebPlayer extends VideoPlayer {
    public WebPlayer(VideoQuality quality) {
        super(quality);
    }

    public void load() {
        System.out.print("[Web] ");
        quality.load();
    }

    public void play() {
        System.out.print("[Web] ");
        quality.play();
    }
}

public class StreamingApp {
    public static void main(String[] args) {
        VideoPlayer tvPlayer = new SmartTvPlayer(new UltraHDQuality());
        tvPlayer.load();
        tvPlayer.play();

        VideoPlayer mobilePlayer = new MobilePlayer(new SDQuality());
        mobilePlayer.load();
        mobilePlayer.play();

        VideoPlayer webPlayer = new WebPlayer(new HDQuality());
        webPlayer.load();
        webPlayer.play();
    }
}