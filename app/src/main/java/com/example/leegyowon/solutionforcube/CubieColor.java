package com.example.leegyowon.solutionforcube;

public class CubieColor {

    private char color;
    private char dir; //Direction of color

    public CubieColor(char ncolor, char ndir) {
        color = ncolor;
        dir = ndir;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char ncolor) {
        color = ncolor;
    }

    public char getDir() {
        return dir;
    }

    public void setDir(char ndir) {
        dir = ndir;
    }

}

