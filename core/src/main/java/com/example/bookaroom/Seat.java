package com.example.bookaroom;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Seat
{
    float x, y, size;
    SeatState state;

    int id_seat;
    int num_row;
    int num_seat;
    int id_room;

    /**
     * Constructor de asiento, que incluye sus coordenadas, el tamaño del cuadrado y el estado del
     * asiento (reservado o disponible.)
     * @param x
     * @param y
     * @param size
     * @param state
     */
    public Seat(float x, float y, float size, SeatState state, int id_seat, int num_row, int num_seat, int id_room) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.state = state;
        this.id_seat = id_seat;
        this.num_row = num_row;
        this.num_seat = num_seat;
        this.id_room = id_room;
    }

    /**
     * Metodo para dibujar el asiento dependiendo del estado que tenga asignado.
     * @param batch
     * @param shapeRenderer
     */
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        switch (state) {
            case RESERVED:
                shapeRenderer.setColor(Color.RED);
                break;
            case AVAILABLE:
                shapeRenderer.setColor(Color.LIGHT_GRAY);
                break;
            case SELECTED:
                shapeRenderer.setColor(Color.GREEN);
                break;
        }
        shapeRenderer.rect(x, y, size, size);
    }

    /**
     * Según donde clickemos, devuelve las coordenadas.
     * @param touchX
     * @param touchY
     * @return
     */
    public boolean isClicked(float touchX, float touchY) {
        return touchX > x && touchX < x + size && touchY > y && touchY < y + size;
    }

    public int getNum_seat()
    {
        return num_seat;
    }
}
