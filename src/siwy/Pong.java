package siwy;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Pong extends Application {
    //Variables
    public static final int width = 800, height = 600;
    public static final int PLAYER_HEIGHT = 100, PLAYER_WIDTH = 15;
    public static final double BALL_RADIUS = 15;
    private int ballYSpeed = 1, ballXSpeed = 1;
    private double player1YPos = height / 2, player2YPos = height / 2;
    private double ballXPos = width / 2, ballYPos = width / 2;
    private int scoreP1 = 0, scoreP2 = 0;
    private boolean gameStarted;
    private int player1XPos = 0;
    private double player2XPos = width - PLAYER_WIDTH;
    private int[] pseudoRandomArray = {5, 2, 8, 4, 1, 10, 9, 3, 6, 7};
    private int pseudoRandom = pseudoRandomArray[(int)(Math.random()*10)];

    public void start(Stage stage) throws Exception {
        stage.setTitle("Tennis for 2 by Siwy");
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);

        //Mouse control
        canvas.setOnMouseMoved(e -> player1YPos = e.getY());
        canvas.setOnMouseClicked(e -> gameStarted = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();
    }

    private void run(GraphicsContext gc) {
        //Set background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        //Set text
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));

        if (gameStarted){
            //Ball movement
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;

            //"AI"
            if (ballXPos < width - width / 12) {
                player2YPos = ballYPos - PLAYER_HEIGHT / 2;
            } else {
                player2YPos = ballYPos > player2YPos + PLAYER_HEIGHT / 2 ? player2YPos += 1: player2YPos;
            }

            //Draw ball
            gc.fillOval(ballXPos, ballYPos, BALL_RADIUS, BALL_RADIUS);
        } else {
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Press a mouse button to start", width / 2, height / 2);

            //Reset the ball
            ballXPos = width / 2;
            ballYPos = height * 2 / pseudoRandom;

            //Reset speed and direction
            ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
        }

        //Playing field boundaries
        if (ballYPos > height || ballYPos < 0) ballYSpeed *=-1;

        //Enemy gets a point
        if (ballXPos < player1XPos - PLAYER_WIDTH) {
            scoreP2++;
            gameStarted = false;
        }

        //Player gets a point
        if (ballXPos > player2XPos + PLAYER_WIDTH) {
            scoreP1++;
            gameStarted = false;
        }

        //Increase the ball speed
        if (((ballXPos + BALL_RADIUS > player2XPos) && ballYPos >= player2YPos && ballYPos <= player2YPos + PLAYER_HEIGHT) ||
                ((ballXPos < player1XPos + PLAYER_WIDTH) && ballYPos >= ballYPos && ballYPos <= player1YPos + PLAYER_HEIGHT)){
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        //Draw score
        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t\t\t\t\t" + scoreP2, width / 2, 100);

        //Draw players
        gc.fillRect(player1XPos, player1YPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        gc.fillRect(player2XPos, player2YPos, PLAYER_WIDTH, PLAYER_HEIGHT);
    }
}
