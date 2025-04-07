package com.example.bookaroom.android.GDX;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.example.bookaroom.Main;

public class GameFragment extends AndroidFragmentApplication
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return initializeForView(new Main());
    }
}
