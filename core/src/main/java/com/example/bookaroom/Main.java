    package com.example.bookaroom;

    import com.badlogic.gdx.ApplicationAdapter;
    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
    import com.badlogic.gdx.utils.Array;
    import com.badlogic.gdx.utils.ScreenUtils;

    import java.util.List;


    /** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
    public class Main extends ApplicationAdapter {
        private SpriteBatch batch;
        private ShapeRenderer shapeRenderer;
        private Array<Seat> seats;
        private int rows = 5, cols = 6;
        private float seatSize = 120;
        private int i = 1;
        private SeatSelectionNotifier notifier;
        private List<Integer> reservedSeats;

        public Main(SeatSelectionNotifier notifier, List<Integer> reservedSeats) {
            this.notifier = notifier;
            this.reservedSeats = reservedSeats;
        }

        /**
         * Permite generar las columnas de asientos y filas de la sala. Además, aplica el estado
         * que tengan asignado los asientos.
         */
        @Override
        public void create() {
            batch = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();
            seats = new Array<>();

            float spacing = 5f;
            float totalWidth = cols * seatSize + (cols - 1) * spacing;
            float totalHeight = rows * seatSize + (rows - 1) * spacing;

            float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
            float startY = (Gdx.graphics.getHeight() - totalHeight) / 2f;

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    float x = startX + col * (seatSize + spacing);
                    float y = startY + (rows - 1 - row) * (seatSize + spacing); // Top to bottom

                    SeatState state = SeatState.AVAILABLE;
                    for (int seatnumber : reservedSeats){
                        if (seatnumber == i){
                            state = SeatState.RESERVED;
                        }
                    }

                    seats.add(new Seat(x, y, seatSize, state, 1, 1, i, 1));
                    i++;
                }
            }
        }

        /**
         * Permite renderizar y dibujar los asientos en pantalla.
         */
        @Override
        public void render() {
            ScreenUtils.clear(254f / 255f, 250f / 255f, 224f / 255f, 1);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (Seat seat : seats) {
                seat.draw(batch, shapeRenderer);
            }
            shapeRenderer.end();

            if (Gdx.input.justTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

                for (Seat seat : seats) {
                    if (seat.state == SeatState.AVAILABLE && seat.isClicked(touchX, touchY)) {
                        for (Seat s : seats) {
                            if (s.state == SeatState.SELECTED) s.state = SeatState.AVAILABLE;
                        }
                        seat.state = SeatState.SELECTED;

                        if (notifier != null) {
                            notifier.notifySeatSelected(seat.getNum_seat());
                        }
                        break;
                    }
                }
            }
        }

        @Override
        public void dispose() {
            batch.dispose();
            shapeRenderer.dispose();
        }
    }
