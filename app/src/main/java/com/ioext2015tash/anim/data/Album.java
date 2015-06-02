package com.ioext2015tash.anim.data;

/**
 * Created by aziz on 5/31/15.
 */
public class Album {
    public int resourceIdSmall;
    public int resourceIdLarge;
    public String album;
    public String singer;
    public String songTitle;
    public String songDuration;
    public boolean favorite;
    public int paletteColor;

    public Album(int resourceIdSmall, int resourceIdLarge, String album, String singer, String songTitle, String songDuration) {
        this.resourceIdSmall = resourceIdSmall;
        this.resourceIdLarge = resourceIdLarge;
        this.album = album;
        this.singer = singer;
        this.songTitle = songTitle;
        this.songDuration = songDuration;
        this.favorite = false;
    }
}
