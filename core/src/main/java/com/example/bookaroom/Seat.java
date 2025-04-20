package com.example.bookaroom;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Seat
{
    float x, y, size;
    SeatState state;

    /**
     * Constructor de asiento, que incluye sus coordenadas, el tamaño del cuadrado y el estado del
     * asiento (reservado o disponible.)
     * @param x
     * @param y
     * @param size
     * @param state
     */
    public Seat(float x, float y, float size, SeatState state) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.state = state;
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
}
