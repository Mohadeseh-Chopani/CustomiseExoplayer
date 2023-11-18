package com.example.exo.Subtitle;

import java.util.ArrayList;
import java.util.List;

public class SubtitleList {
    public static List<Subtitle> subtitles ;

    public SubtitleList() {
        this.subtitles = new ArrayList<>();
    }

    public void addSubtitle(String url, String id, String language) {
        Subtitle subtitle = new Subtitle(url, id, language);
        subtitles.add(subtitle);
    }
}
