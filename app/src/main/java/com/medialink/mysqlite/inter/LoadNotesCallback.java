package com.medialink.mysqlite.inter;

import com.medialink.mysqlite.Note;

import java.util.ArrayList;

public interface LoadNotesCallback {
    void preExecute();
    void postExecute(ArrayList<Note> notes);
}
