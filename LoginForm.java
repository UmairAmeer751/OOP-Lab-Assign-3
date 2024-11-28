package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;

public class LoginForm extends Application {

    private static final String USERS_FILE = "users.txt"; // File for storing user credentials
    private final HashMap<String, String> userDatabase = new HashMap<>(); // In-memory user data

    @Override
    public void start(Stage primaryStage) {

        initializeUsers(); // Load existing users from file

        Label usernameLabel = new Label("User Name");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Signup");
        Button exitButton = new Button("Exit");
        Button uploadImageButton = new Button("Upload Image");
        Label notificationLabel = new Label();
        notificationLabel.setStyle("-fx-text-fill: red;");

        // ImageView for uploaded image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        // Layout for Upload Button and Image
        VBox uploadBox = new VBox(10, uploadImageButton, imageView);
        uploadBox.setAlignment(Pos.CENTER);

        // Layout for Form
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(10));
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.add(usernameLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(passwordLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);

        // Buttons Layout
        HBox buttonBox = new HBox(10, loginButton, signupButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Main Layout
        VBox mainLayout = new VBox(20, uploadBox, formGrid, buttonBox, notificationLabel);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        // FileChooser for Image Upload
        FileChooser fileChooser = new FileChooser();
        uploadImageButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                imageView.setImage(new Image(file.toURI().toString()));
            }
        });

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (validateCredentials(username, password)) {
                notificationLabel.setText("");
                openWelcomeWindow(username);
            } else {
                notificationLabel.setText("Incorrect username/password");
            }
        });

        signupButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                notificationLabel.setText("Username or password cannot be empty");
            } else if (userDatabase.containsKey(username)) {
                notificationLabel.setText("Username already exists");
            } else {
                addUserToFile(username, password); // Add new user to file
                userDatabase.put(username, password); // Add user to in-memory database
                notificationLabel.setStyle("-fx-text-fill: green;");
                notificationLabel.setText("User registered successfully!");
            }
        });

        exitButton.setOnAction(e -> primaryStage.close());

        // Set Scene and Show Stage
        Scene scene = new Scene(mainLayout, 500, 400);
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to initialize users from the file
    private void initializeUsers() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        userDatabase.put(parts[0], parts[1]); // Load the user into the in-memory database
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to validate credentials
    private boolean validateCredentials(String username, String password) {
        return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
    }

    // Method to add user to file
    private void addUserToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(username + ":" + password); // Store user data as username:password
            writer.newLine(); // New line for the next user
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to open a welcome window
    private void openWelcomeWindow(String username) {
        Stage welcomeStage = new Stage();
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        Scene scene = new Scene(new VBox(welcomeLabel), 200, 100);
        welcomeStage.setTitle("Welcome");
        welcomeStage.setScene(scene);
        welcomeStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
