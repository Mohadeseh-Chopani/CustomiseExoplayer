package com.example.exo.Model;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class DataPlayer {
  public   List<Format> languageList = new ArrayList<>();

  public List<MediaItem.SubtitleConfiguration> subtitleList = new ArrayList<>();
  public String videoUrl;
  public int statusLanguage ;
  public int statusSubtitle;

    public List<Format> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(Format languagelist) {
        this.languageList.add(languagelist);
    }

    public List<MediaItem.SubtitleConfiguration> getSubtitleList() {
        return subtitleList;
    }

    public void setSubtitleList(MediaItem.SubtitleConfiguration subtitleList) {
        this.subtitleList.add(subtitleList);
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
