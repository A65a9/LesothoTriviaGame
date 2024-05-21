package com.example.demo122;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class LesothoTriviaGame extends Application {

    private String[] questions = {
            "What is the capital of Lesotho?",
            "Which river is the longest river entirely within Lesotho?",
            "Which famous rock formation is a national symbol of Lesotho?",
            "Which traditional Basotho hat is known as the 'Mokorotlo'?",
            "What is the traditional food of Lesotho?"
    };
    private String[] answers = {"Maseru", "Senqu River", "Thaba-Bosiu", "Mokorotlo", "Papa (maize porridge)"};
    private String[][] options = {
            {"Maseru", "Leribe", "Mafeteng", "Butha-Buthe"},
            {"Caledon River", "Senqu River", "Mohokare River", "Orange River"},
            {"Sani Pass", "Thaba-Bosiu", "Maloti Mountains", "Qiloane"},
            {"Mokorotlo", "Kobo", "Lipela", "Sotho Beret"},
            {"Papa (maize porridge)", "Biltong", "Bobotie", "Sadza"}
    };
    private String[] imagePaths = {
            "images/maseruu.jpg",
            "images/senquu.jpg",
            "images/thaba_bosiuu.jpg",
            "images/mokorotlo.jpg",
            "images/papaa.jpg"
    };
    private String[] videoPaths = {
            "videos/maseru.mp4",
            "videos/senqu.mp4",
            "videos/thaba_bosiu.mp4",
            "videos/mokorotlong.mp4",
            "videos/papa.mp4"
    };

    private ImageView imageView;
    private Button[] optionButtons;
    private VBox optionsBox;
    private Label questionLabel;
    private TextArea scoreArea;
    private Label timeLabel;
    private Label scoreLabel;
    private MediaView mediaView;
    private MediaPlayer mediaPlayer;

    private ToggleButton playPauseButton;
    private Button nextButton;
    private Button backButton;

    private Timer timer;
    private int timeRemaining = 15;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private int totalQuestions = questions.length;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showStartScreen();
    }

    private void showStartScreen() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root"); // Add root style class

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        Label titleLabel = new Label("Welcome to Lesotho Trivia Game");
        titleLabel.getStyleClass().add("title-label"); // Add custom title label style class
        centerBox.getChildren().add(titleLabel);

        Button startButton = new Button("Start Game");
        startButton.getStyleClass().add("start-button"); // Add custom start button style class
        startButton.setOnAction(e -> startGame());
        centerBox.getChildren().add(startButton);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // Link CSS file

        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGame() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root"); // Add root style class

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        HBox mediaBox = new HBox(20); // Create a new HBox to hold mediaView and imageView
        mediaBox.setAlignment(Pos.CENTER);

        mediaView = new MediaView();
        mediaView.setId("video-media-view"); // Set ID for video
        mediaView.setFitWidth(400); // Adjust width as per requirement
        mediaView.setFitHeight(400); // Adjust height as per requirement
        mediaBox.getChildren().add(mediaView);

        imageView = new ImageView();
        imageView.setId("maseru-image-view"); // Set ID for Maseru picture
        imageView.setFitWidth(300); // Adjust width as per requirement
        imageView.setFitHeight(230); // Adjust height as per requirement
        mediaBox.getChildren().add(imageView);

        centerBox.getChildren().add(mediaBox);

        questionLabel = new Label();
        questionLabel.getStyleClass().add("question-label"); // Add custom question label style class
        centerBox.getChildren().add(questionLabel);

        optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().add(optionsBox);

        optionButtons = new Button[4];
        for (int i = 0; i < 4; i++) {
            Button optionButton = new Button();
            optionButton.getStyleClass().add("button"); // Add custom button style class
            int finalI = i;
            optionButton.setOnAction(e -> handleAnswer(finalI));
            optionButtons[i] = optionButton;
            optionsBox.getChildren().add(optionButton);
        }

        scoreArea = new TextArea();
        scoreArea.getStyleClass().add("score-area"); // Add custom score area style class
        scoreArea.setEditable(false);
        scoreArea.setWrapText(true);
        centerBox.getChildren().add(scoreArea);

        timeLabel = new Label();
        timeLabel.getStyleClass().add("time-label"); // Add custom time label style class
        root.setBottom(timeLabel);

        // Create play/pause, next, and back buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().add(buttonBox);

        playPauseButton = new ToggleButton("Play");
        playPauseButton.getStyleClass().add("play-pause-button"); // Add custom play/pause button style class
        playPauseButton.setOnAction(e -> handlePlayPause());
        buttonBox.getChildren().add(playPauseButton);

        nextButton = new Button("Next");
        nextButton.getStyleClass().add("next-button"); // Add custom next button style class
        nextButton.setOnAction(e -> navigateNext());
        buttonBox.getChildren().add(nextButton);

        backButton = new Button("Back");
        backButton.getStyleClass().add("back-button"); // Add custom back button style class
        backButton.setOnAction(e -> navigateBack());
        buttonBox.getChildren().add(backButton);

        loadQuestion(currentQuestionIndex);

        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // Link CSS file

        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        startTimer();
    }

    private void handleAnswer(int selectedOptionIndex) {
        timer.cancel();
        boolean isCorrect = answers[currentQuestionIndex].equals(options[currentQuestionIndex][selectedOptionIndex]);
        if (isCorrect) {
            score++;
            updateScoreArea("Correct! Your score is now: " + score, true);
        } else {
            updateScoreArea("Incorrect. The correct answer is: " + answers[currentQuestionIndex], false);
        }
        navigateNext();
    }

    private void navigateNext() {
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            mediaPlayer.stop();
            loadQuestion(currentQuestionIndex);
            resetTimer();
            startTimer();
        } else {
            Platform.runLater(() -> {
                showEndScreen();
                timer.cancel();
            });
        }
    }

    private void navigateBack() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            mediaPlayer.stop();
            loadQuestion(currentQuestionIndex);
            resetTimer();
            startTimer();
        }
    }

    private void updateScoreArea(String message, boolean isCorrect) {
        Platform.runLater(() -> {
            scoreArea.setText(message);
            if (isCorrect) {
                scoreArea.getStyleClass().add("correct-answer");
                scoreArea.getStyleClass().remove("incorrect-answer");
            } else {
                scoreArea.getStyleClass().add("incorrect-answer");
                scoreArea.getStyleClass().remove("correct-answer");
            }
            // Animate the feedback message
            animateFeedback(scoreArea);
        });
    }

    private void loadQuestion(int questionIndex) {
        Platform.runLater(() -> {
            try {
                questionLabel.setText(questions[questionIndex]);
                String imagePath = imagePaths[questionIndex];
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                imageView.setImage(image);

                String videoPath = videoPaths[questionIndex];
                Media media = new Media(getClass().getResource(videoPath).toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                mediaView.setMediaPlayer(mediaPlayer); // Set the new media player

                for (int i = 0; i < optionButtons.length; i++) {
                    optionButtons[i].setText(options[questionIndex][i]);
                }
            } catch (Exception e) {
                System.err.println("Error loading question: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void startTimer() {
        timeRemaining = 15;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    Platform.runLater(() -> timeLabel.setText("Time remaining: " + timeRemaining + " seconds"));
                    timeRemaining--;
                } else {
                    timer.cancel();
                    updateScoreArea("Time's up! The correct answer is: " + answers[currentQuestionIndex], false);
                    navigateNext();
                }
            }
        }, 0, 1000);
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        startTimer();
    }

    private void showEndScreen() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root"); // Add root style class

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);

        Label titleLabel = new Label("Game Over!");
        titleLabel.getStyleClass().add("game-over-label"); // Add custom game over label style class
        centerBox.getChildren().add(titleLabel);

        scoreLabel = new Label("Your final score is: " + score + " out of " + totalQuestions);
        scoreLabel.getStyleClass().add("score-label"); // Add custom score label style class
        centerBox.getChildren().add(scoreLabel);

        Button restartButton = new Button("Restart Game");
        restartButton.getStyleClass().add("restart-button"); // Add custom restart button style class
        restartButton.setOnAction(e -> {
            resetGame();
            startGame();
        });
        centerBox.getChildren().add(restartButton);

        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("exit-button"); // Add custom exit button style class
        exitButton.setOnAction(e -> Platform.exit());
        centerBox.getChildren().add(exitButton);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // Link CSS file

        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Enable buttons after the game ends
        restartButton.setDisable(false);
        exitButton.setDisable(false);
    }

    private void resetGame() {
        score = 0;
        currentQuestionIndex = 0;

        // Disable buttons while restarting game
        for (Button optionButton : optionButtons) {
            optionButton.setDisable(false);
        }
    }

    // Define a method to animate feedback messages
    private void animateFeedback(TextArea feedbackLabel) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), feedbackLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        fadeTransition.play();
    }

    private void handlePlayPause() {
        if (playPauseButton.isSelected()) {
            mediaPlayer.pause();
            playPauseButton.setText("Play");
        } else {
            mediaPlayer.play();
            playPauseButton.setText("Pause");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
