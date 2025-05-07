package com.example.bookaroom.android.GDX;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.example.bookaroom.Main;
import com.example.bookaroom.SeatSelectionNotifier;

import java.util.List;

public class GameFragment extends AndroidFragmentApplication
{
    private SeatSelectionListener seatSelectionListener;
    private List<Integer> reservedSeats;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SeatSelectionNotifier notifier = new SeatSelectionNotifier() {
            @Override
            public void notifySeatSelected(int seatNumber) {
                if (seatSelectionListener != null) {
                    seatSelectionListener.onSeatSelected(seatNumber);
                }
            }
        };

        return initializeForView(new Main(notifier, reservedSeats));
    }

    public void setSeatSelectionListener(SeatSelectionListener listener) {
        this.seatSelectionListener = listener;
    }

    public interface SeatSelectionListener {
        void onSeatSelected(int seatNumber);
    }

    public void setReservedSeats(List<Integer> reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

}
