import java.util.ArrayList;
import java.util.Objects;

import com.sun.org.apache.bcel.internal.classfile.Utility;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GuiClient extends Application {
	TextField messageInput, usernameInput;
	Button sendMessageButton, setUsernameButton;
	TextArea chatLog;
	Client clientConnection;
	private String username; // Stores client version of username to prevent over queries
	private Button joinGroupButton;
	private boolean groupButtonsDisabled;
	private Button createGroupButton;
	private Label errorLabel;

	public static void main(String[] args) {
		launch(args);
	}


	private Scene loadStyling(Scene scene) { // Method in order to load css styling for JavaFX scene
		scene.getRoot().setStyle("-fx-font-family: 'serif'; -fx-background-color: #ffffe4;");
		return scene;
	}
	private void disableGroupButtons() { // Method for disabling join group and create group buttons
		joinGroupButton.setDisable(true);
		createGroupButton.setDisable(true);
		groupButtonsDisabled = true;
	}
	private Scene joinGroupScene(Stage primaryStage, Button joinGroupButton, Button createGroupButton) { // Join group scene
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));


		Button goBackButton = new Button("Go Back");
		goBackButton.setOnAction(e -> primaryStage.setScene(mainScene(primaryStage))); // Returns to main scene
		layout.getChildren().add(new Label("Select a group to join:"));

		clientConnection.requestGroupSize(data -> {
			Platform.runLater(() -> {
				System.out.println(data);
				for (int i = 0; i <= data - 1; i++) {
					Button groupButton = new Button("Group #" + i);
					int finalI = i;
					groupButton.setOnAction(e -> {
						clientConnection.joinGroup(finalI);
						joinGroupButton.setDisable(true);
						createGroupButton.setDisable(true);
						primaryStage.setScene(mainScene(primaryStage));
					});
					layout.getChildren().add(groupButton);
				}
				layout.getChildren().add(goBackButton);
			});
		});

		layout.setAlignment(Pos.CENTER);

		return loadStyling(new Scene(layout, 400, 300));
	}

	// Create group scene
	private Scene createGroupScene(Stage primaryStage, Button joinGroupButton, Button createGroupButton) {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));

		ArrayList<String> selectedUsers = new ArrayList<>(); // List to store usernames of selected users for group creation
		Label label = new Label("Select users to add to the group: ");

		Button confirmButton = new Button("Create Group"); // Button to confirm creation of a group
		Button goBackButton = new Button("Go Back");
		goBackButton.setOnAction(e -> primaryStage.setScene(mainScene(primaryStage))); // Sets action to switch back to main scene when clicked

		confirmButton.setOnAction(e -> {
			clientConnection.createGroup(selectedUsers); // Calls method to create group with the selected users
			joinGroupButton.setDisable(true); // Disables buttons
			createGroupButton.setDisable(true); // Disables buttons
			primaryStage.setScene(mainScene(primaryStage));
		});

		layout.getChildren().add(label);

		clientConnection.requestUserList(true, data -> {
			for (String user : data) {
				Button userButton = new Button(user); // Creates button for each user
				userButton.setOnAction(e -> {
					if (!selectedUsers.contains(userButton.getText())) { // Adds user to group if not already added
						String labelText = label.getText();
						if (!selectedUsers.isEmpty())
							labelText += ", ";
						selectedUsers.add(userButton.getText()); // Adds user to selected list
						labelText += userButton.getText();
						label.setText(labelText);
					}
				});
				Platform.runLater(() -> {
					if (!user.equals(this.username)) // Makes sure current user not added to their own group
						layout.getChildren().add(userButton);
				});
			}
			Platform.runLater(() -> {
				layout.getChildren().addAll(confirmButton, goBackButton); // Adds confirm and go back buttons to the layout
			});
		});

		layout.setAlignment(Pos.CENTER); // Centers the VBox within the scene.

		return loadStyling(new Scene(layout, 400, 300)); // Returns the scene with styling applied.
	}


	// All users scene
	private Scene allUsersScene(Stage primaryStage, ArrayList<String> users) {
		VBox allUsersLayout = new VBox(10);
		allUsersLayout.setPadding(new Insets(10));

		ListView<String> usersListView = new ListView<>();
		usersListView.getItems().addAll(users);  // Populates ListView with user list passed to method

		Button goBackButton = new Button("Go Back");
		goBackButton.setOnAction(e -> primaryStage.setScene(mainScene(primaryStage))); // Returns to main scene

		allUsersLayout.getChildren().addAll(new Label("All Users:"), usersListView, goBackButton);
		allUsersLayout.setAlignment(Pos.CENTER);

		return loadStyling(new Scene(allUsersLayout, 400, 300));
	}

	// Updates the user list
	private void updateUserList(ComboBox<String> recipientSelector) {
		clientConnection.requestUserList(false, data -> { // Requests all users
			Platform.runLater(() -> {
				recipientSelector.getItems().clear();
				recipientSelector.getItems().add("All");
				for (String currUsername : data) {
					if (!currUsername.equals(this.username)) { // Checks if current username is not user's own username to prevent sending messages to oneself
						recipientSelector.getItems().add(currUsername);
					}
				}
			});
			clientConnection.getGroupIndex(groupIndex -> {
				if (groupIndex != -1) {
					Platform.runLater(() -> {
						recipientSelector.getItems().add("Group #" + groupIndex); // Add user's group as option for messaging
					});
				}
			});
		});
	}

	// Scene to view all the groups
	private Scene createAllGroupsScene(Stage primaryStage, int groupSize) {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));

		ListView<String> groupsListView = new ListView<>();
		for (int i = 0; i < groupSize; i++) { // Makes a list for the scene for user to see all the groups
			groupsListView.getItems().add("Group #" + i);
		}

		Button goBackButton = new Button("Go Back");
		goBackButton.setOnAction(e -> primaryStage.setScene(mainScene(primaryStage))); // Go back button changes scene back to main scene

		layout.getChildren().addAll(new Label("All Groups:"), groupsListView, goBackButton);
		layout.setAlignment(Pos.CENTER_RIGHT);

		return loadStyling(new Scene(layout, 400, 300));
	}

	// Error message method
	private void setUnexpectedErrorMessage() {
		errorLabel.setText("Unexpected error occurred, returned you to main scene");

		// set timer to reset error message soon
		Duration duration = Duration.seconds(3);
		KeyFrame keyFrame = new KeyFrame(duration, event -> {
			errorLabel.setText(""); // Clear error message are 3 seconds
		});

		Timeline timeline = new Timeline(keyFrame);
		timeline.setCycleCount(1); // Only runs timeline once
		timeline.play();
	}
	private Scene mainScene(Stage primaryStage) {
		BorderPane mainLayout = new BorderPane();

		// Chat log area
		if (chatLog == null) { // When we're switching between scenes, make sure we're not clearing the chat log element and erases old chats
			chatLog = new TextArea();
			chatLog.setPrefWidth(375);
			chatLog.setMaxWidth(375);
			chatLog.setEditable(false); // This makes TextArea a log area, not editable
			chatLog.setWrapText(true);
		}

		// Message input area
		TextField messageInput = new TextField();
		messageInput.setPromptText("Type a message...");
		messageInput.setPrefWidth(150);


		// Dropdown menu for selecting message recipients
		ComboBox<String> recipientSelector = new ComboBox<>();
		recipientSelector.setPrefWidth(100);
		recipientSelector.getItems().add("All");
		recipientSelector.setOnShowing(e -> {
			updateUserList(recipientSelector);
		});

		// Button to send message
		Button sendMessageButton = new Button("Send");
		sendMessageButton.setOnAction(e -> {
			String message = messageInput.getText();
			if (!message.isEmpty() && recipientSelector.getValue() != null) {
				String recipient = recipientSelector.getValue();
				clientConnection.sendMessage(message, recipient.startsWith("Group #"), recipient.equals("All"), recipient);
				messageInput.clear();
			}
		});


		// Layout for sending messages
		HBox sendMessageBox = new HBox(10, recipientSelector, messageInput, sendMessageButton);
		sendMessageBox.setAlignment(Pos.CENTER_LEFT);

		// Setting up main chat area layout
		VBox chatArea = new VBox(10, chatLog, sendMessageBox);
		chatArea.setAlignment(Pos.BOTTOM_LEFT);
		chatArea.setPadding(new Insets(10));

		// Add chat area to main layout
		mainLayout.setCenter(chatArea);

		// Buttons for group management
		joinGroupButton = new Button("Join Group");
		createGroupButton = new Button("Create Group");
		if (groupButtonsDisabled) {
			disableGroupButtons();
		}
		joinGroupButton.setOnAction(e -> primaryStage.setScene(joinGroupScene(primaryStage, joinGroupButton, createGroupButton)));
		createGroupButton.setOnAction(e -> primaryStage.setScene(createGroupScene(primaryStage, joinGroupButton, createGroupButton)));

		Button usersOnServerButton = new Button("Users On Server");
		usersOnServerButton.setOnAction(e -> {
			clientConnection.requestUserList(false, userList -> {
				Platform.runLater(() -> {
					primaryStage.setScene(allUsersScene(primaryStage, userList));
				});
			});
		});

		// Group buttons HBox
		HBox groupButtonBox = new HBox(10, joinGroupButton, createGroupButton);
		groupButtonBox.setAlignment(Pos.CENTER_LEFT);

		// User list button HBox
		Button viewAllGroupsButton = new Button("View All Groups");
		viewAllGroupsButton.setOnAction(e -> {
			clientConnection.requestGroupSize(groupSize -> {
				Platform.runLater(() -> {
					primaryStage.setScene(createAllGroupsScene(primaryStage, groupSize));
				});
			});
		});

		HBox listButtonBox = new HBox(10, viewAllGroupsButton, usersOnServerButton);
		listButtonBox.setAlignment(Pos.CENTER_RIGHT);

		// Top HBox that combines both groupButtonBox and userListButtonBox
		HBox topBox = new HBox(groupButtonBox, listButtonBox);
		HBox.setHgrow(groupButtonBox, javafx.scene.layout.Priority.ALWAYS);
		HBox.setHgrow(listButtonBox, javafx.scene.layout.Priority.ALWAYS);
		groupButtonBox.setMaxWidth(Double.MAX_VALUE);
		listButtonBox.setMaxWidth(Double.MAX_VALUE);

		errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red;"); // Sets to red

		// Main layout
		mainLayout = new BorderPane();
		mainLayout.setTop(topBox);  // Sets combined HBox to top
		mainLayout.setCenter(errorLabel);
		mainLayout.setBottom(chatArea);

		return loadStyling(new Scene(mainLayout, 400, 400));
	}




	private Scene initialScene(Stage primaryStage) { // INITIAL SCENE
		Label errorMsg = new Label(); // Starts as empty
		errorMsg.setStyle("-fx-text-fill: red;"); // Sets to red
		usernameInput = new TextField();
		usernameInput.setMaxWidth(150);
		setUsernameButton = new Button("Set Username");
		setUsernameButton.setOnAction(e -> {
			String username = usernameInput.getText();
			if (!username.isEmpty()) {
				clientConnection.verifyUsername(usernameInput.getText(), data -> {
					Platform.runLater(() -> {
						if (data) { // username is verified
							this.username = usernameInput.getText();
							primaryStage.setScene(mainScene(primaryStage)); // Switches to main scene
						} else { // Username is not verified
							errorMsg.setText("Username is already taken!");
							Duration duration = Duration.seconds(3);
							KeyFrame keyFrame = new KeyFrame(duration, event -> {
								errorMsg.setText(""); // Clear error messages are 3 seconds
							});

							Timeline timeline = new Timeline(keyFrame);
							timeline.setCycleCount(1); // Only runs timeline once
							timeline.play();
						}
					});
				});
			} else {
				errorMsg.setText("Username field must be populated!");
				Duration duration = Duration.seconds(3);
				KeyFrame keyFrame = new KeyFrame(duration, event -> {
					errorMsg.setText(""); // Clear error messages are 3 seconds
				});

				Timeline timeline = new Timeline(keyFrame);
				timeline.setCycleCount(1); // Only runs timeline once
				timeline.play();
			}
		});

		VBox initialLayout = new VBox(10, errorMsg, usernameInput, setUsernameButton);
		initialLayout.setAlignment(Pos.CENTER);
		initialLayout.setPadding(new Insets(10));
		return loadStyling(new Scene(initialLayout, 300, 200));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Initializes client connection
		clientConnection = new Client();
		clientConnection.onChatMessage(data -> {
			String origin = data.get("origin");
			String username = data.get("recipient");
			String message = data.get("message");
			if (chatLog != null) {
				Platform.runLater(() -> {
					chatLog.appendText(username + " (" + origin + "): " + message + "\n");
				});
			}
		});
		clientConnection.onDisableGroupMutation(data -> {
			Platform.runLater(() -> {
				disableGroupButtons();
			});
		});
		clientConnection.onUnexpectedError(data -> {
			Platform.runLater(() -> {
				primaryStage.setScene(mainScene(primaryStage));
				setUnexpectedErrorMessage();
			});
		});

		clientConnection.start();

		primaryStage.setScene(initialScene(primaryStage)); // Starts with initial scene
		primaryStage.setTitle("Client - Messaging App");
		primaryStage.show();
	}
}
