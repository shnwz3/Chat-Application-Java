import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class ChatApplication {
    private JFrame mainFrame;
    private JPanel cardPanel;
    private JPanel loginPanel;
    private JPanel SignInPanel;
    private List<JFrame> chatWindows;
    private List<JTextArea> chatTextAreas;
    private List<JTextField> messageFields;
    private String yourName; // Store the authenticated user's name
    private UserAccountManager userAccountManager;
    private boolean isLoginPanelVisible; // Track the current state

    public ChatApplication() {
        mainFrame = new JFrame("Chat Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(380, 640); // Mobile-sized dimensions

        // Create a panel to hold login and SignIn panels using CardLayout
        cardPanel = new JPanel(new CardLayout());

        // Initialize UserAccountManager
        userAccountManager = new UserAccountManager();

        // Initialize login and SignIn panels
        createLoginPanel();
        createSignInPanel();

        // Add login and SignIn panels to the cardPanel
        cardPanel.add(loginPanel, "login");
        cardPanel.add(SignInPanel, "SignUP");

        // Add the cardPanel to the mainFrame
        mainFrame.add(cardPanel, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    private void createLoginPanel() {
        // Create labels and text fields for username and password
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        JTextField usernameField = new JTextField(20); // Increased the width
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        JPasswordField passwordField = new JPasswordField(20); // Increased the width
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JButton loginButton = new JButton("Login");

        // Create a panel for login components using GridBagLayout
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        loginPanel.add(titleLabel, constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 1;
        loginPanel.add(usernameLabel, constraints);

        constraints.gridx = 1;
        loginPanel.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        loginPanel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        loginPanel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        loginPanel.add(loginButton, constraints);

        // Set the login button action
        loginButton.addActionListener(e -> {
            // Authenticate the user using the UserAccountManager
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();

            if (isValidInput(username, password)) {
                if (userAccountManager.authenticateUser(username, String.valueOf(password))) {
                    yourName = username; // Set the authenticated user's name
                    displayChatOptions();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Authentication failed. Please try again.");
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a valid username and password.");
            }
        });

        // Add a toggle button to switch between login and SignIn
        JButton toggleButton = new JButton("Sign In");
        toggleButton.addActionListener(e -> toggleLoginSignIn());
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        loginPanel.add(toggleButton, constraints);
    }

    private void createSignInPanel() {
        // Initialize SignInPanel and its components
        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        SignInPanel = new JPanel(new GridBagLayout());
        GridBagConstraints SignInConstraints = new GridBagConstraints();
        SignInConstraints.insets = new Insets(5, 5, 5, 5);
        SignInConstraints.anchor = GridBagConstraints.CENTER;

        JTextField SignInUsernameField = new JTextField(20);
        JPasswordField SignInPasswordField = new JPasswordField(20);
        JButton SignInButton = new JButton("Sign In");

        SignInConstraints.gridx = 0;
        SignInConstraints.gridy = 0;
        SignInConstraints.gridwidth = 2;
        SignInPanel.add(titleLabel, SignInConstraints);

        SignInConstraints.gridy = 1;
        SignInConstraints.gridwidth = 1;
        SignInPanel.add(new JLabel("Username:"), SignInConstraints);

        SignInConstraints.gridx = 1;
        SignInPanel.add(SignInUsernameField, SignInConstraints);

        SignInConstraints.gridx = 0;
        SignInConstraints.gridy = 2;
        SignInPanel.add(new JLabel("Password:"), SignInConstraints);

        SignInConstraints.gridx = 1;
        SignInPanel.add(SignInPasswordField, SignInConstraints);

        SignInConstraints.gridx = 0;
        SignInConstraints.gridy = 3;
        SignInConstraints.gridwidth = 2;
        SignInPanel.add(SignInButton, SignInConstraints);

        SignInButton.addActionListener(e -> {
            // Implement Sign In logic here
            String username = SignInUsernameField.getText();
            char[] password = SignInPasswordField.getPassword();

            if (isValidInput(username, password)) {
                // Example: Save the new user in the userAccountManager
                userAccountManager.addUser(username, String.valueOf(password));

                // Switch to login after Sign In
                toggleLoginSignIn();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a valid username and password.");
            }
        });

        // Add a toggle button to switch between login and SignIn
        JButton toggleButton = new JButton("Login");
        toggleButton.addActionListener(e -> toggleLoginSignIn());
        SignInConstraints.gridx = 0;
        SignInConstraints.gridy = 4;
        SignInConstraints.gridwidth = 2;
        SignInPanel.add(toggleButton, SignInConstraints);

        // Hide the SignInPanel initially
        SignInPanel.setVisible(false);
    }

    // Method to toggle between login and SignIn panels using CardLayout
    private void toggleLoginSignIn() {
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        if (isLoginPanelVisible) {
            cardLayout.show(cardPanel, "SignIn");
        } else {
            cardLayout.show(cardPanel, "login");
        }
        isLoginPanelVisible = !isLoginPanelVisible; // Toggle the state
    }

    private void displayChatOptions() {
        // Create a new JFrame for chat options
        JFrame optionsFrame = new JFrame("Chat Options");
        optionsFrame.setSize(500, 300);
        optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load your logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("resources/oklogo.png"));
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(logoIcon);

        JButton personalChatButton = new JButton("Personal Chat");
        personalChatButton.addActionListener(e -> createPersonalChatWindow());
        personalChatButton.setBackground(new Color(0, 150, 136)); // WhatsApp green color
        personalChatButton.setForeground(Color.WHITE); // White text color

        JButton groupChatButton = new JButton("Group Chat");
        groupChatButton.addActionListener(e -> createGroupChatWindow());
        groupChatButton.setBackground(new Color(0, 150, 136)); // WhatsApp green color
        groupChatButton.setForeground(Color.WHITE); // White text color

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(personalChatButton);
        buttonPanel.add(groupChatButton);

        // Set the background color to gray
        buttonPanel.setBackground(Color.white);

        // Create a panel for logo and buttons
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(logoLabel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        optionsFrame.add(contentPanel);
        optionsFrame.setVisible(true);
    }

    private void createPersonalChatWindow() {
        mainFrame.setVisible(false);
        chatWindows = new ArrayList<>();
        chatTextAreas = new ArrayList<>();
        messageFields = new ArrayList<>();

        JFrame youChatWindow = createChatWindow(yourName, "Friend", "profile_you.png");
        chatWindows.add(youChatWindow);

        JFrame friendChatWindow = createChatWindow("Friend", yourName, "profile_friend.png");
        chatWindows.add(friendChatWindow);
    }

    private List<String> getFriendList() {
        // Replace this with your actual logic for retrieving the friend list
        List<String> friends = new ArrayList<>();
        friends.add("Friend1");
        friends.add("Friend2");
        friends.add("Friend3");
        // Add more friends as needed
        return friends;
    }

    private void createGroupChatWindow() {
        mainFrame.setVisible(false);

        // Initialize the list of friends (you can replace it with your actual friend list logic)
        List<String> friendList = getFriendList();

        // Ask the user to choose friends from the list
        List<String> selectedFriends = getSelectedFriends(friendList);

        // Create chat windows for the selected friends
        chatWindows = new ArrayList<>();
        chatTextAreas = new ArrayList<>();
        messageFields = new ArrayList<>();

        JFrame youChatWindow = createChatWindow(yourName, "Friend", "profile_you.png");
        chatWindows.add(youChatWindow);

        for (String selectedFriend : selectedFriends) {
            // Create a chat window for each selected friend
            JFrame friendChatWindow = createChatWindow(selectedFriend, yourName, "group_profile.png");
            chatWindows.add(friendChatWindow);
        }
    }

    private List<String> getSelectedFriends(List<String> friendList) {
        List<String> selectedFriends = new ArrayList<>();

        while (true) {
            Object[] options = {"OK", "Add More"};

            String selectedFriend = (String) JOptionPane.showInputDialog(
                    mainFrame,
                    "Select friends (OK to add more,Cancel to finish):",
                    "Choose Friend you want to chat",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    friendList.toArray(),
                    friendList.get(0) // Default selection
            );

            // If the user cancels or closes the dialog, break the loop
            if (selectedFriend == null || selectedFriend.trim().isEmpty() || selectedFriend.equals("OK")) {
                break;
            } else if (selectedFriend.equals("Add More")) {
                // Add More: User wants to add more friends
                String newFriend = (String) JOptionPane.showInputDialog(
                        mainFrame,
                        "Enter the name of the new friend:",
                        "Add Friend",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        ""
                );

                // If the user cancels or closes the dialog, break the loop
                if (newFriend == null || newFriend.trim().isEmpty()) {
                    break;
                }

                friendList.add(newFriend);
            } else {
                selectedFriends.add(selectedFriend);
            }
        }

        return selectedFriends;
    }
    
    
    

    private JFrame createChatWindow(String title, String friendTitle, String profileFileName) {
        JFrame chatWindow = new JFrame(title);
        chatWindow.setSize(360, 640); // Mobile-sized dimensions
        chatWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        chatWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeAllWindows();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(360, 50)); // Adjusted the width
        topPanel.setBackground(new Color(0, 150, 136)); // WhatsApp green color

        JPanel avatarPanel = new JPanel();
        avatarPanel.setPreferredSize(new Dimension(50, 50));

        // Set the profile based on the profileFileName
        URL avatarURL = getClass().getResource("resources/" + profileFileName);
        if (avatarURL != null) {
            ImageIcon avatarIcon = new ImageIcon(avatarURL);
            Image avatarImage = avatarIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            avatarIcon = new ImageIcon(avatarImage);
            JLabel avatarLabel = new JLabel(avatarIcon);
            avatarPanel.add(avatarLabel);
        }

        JLabel nameLabel = new JLabel(title);
        nameLabel.setForeground(Color.WHITE); // White text color

        JButton moreButton = new JButton(":");
        moreButton.addActionListener(e -> {
            JPopupMenu optionsMenu = createOptionsMenu(chatWindow);
            optionsMenu.show(moreButton, moreButton.getWidth(), moreButton.getHeight());
        });
        moreButton.setForeground(Color.black);

        topPanel.add(avatarPanel, BorderLayout.WEST);
        topPanel.add(nameLabel, BorderLayout.CENTER);
        topPanel.add(moreButton, BorderLayout.EAST);

        chatWindow.add(topPanel, BorderLayout.NORTH);

        JTextArea chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setFont(new Font("Tahoma", Font.PLAIN, 16));
        chatTextArea.setBackground(Color.WHITE); // White background
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);

        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
        chatWindow.add(chatScrollPane, BorderLayout.CENTER);

        JTextField messageField = new JTextField("Message..");
        messageField.setForeground(Color.GRAY);
        messageField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        messageField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (messageField.getText().equals("Message..")) {
                    messageField.setText("");
                    messageField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (messageField.getText().isEmpty()) {
                    messageField.setText("Message..");
                    messageField.setForeground(Color.GRAY);
                }
            }
        });

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(0, 150, 136)); // WhatsApp green color
        sendButton.setForeground(Color.WHITE); // White text color

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        chatWindow.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            if (!message.isEmpty() && !message.equals("Message..")) {
                String sender = title;
                int index =chatWindows.indexOf(chatWindow);

                chatTextAreas.get(index).append(sender + ": " + message + "\n");

                for (int i = 0; i < chatTextAreas.size(); i++) {
                    if (i != index) {
                        chatTextAreas.get(i).append(sender + ": " + message + "\n");
                    }
                }

                messageField.setText("Message..");
                messageField.setForeground(Color.GRAY);
            }
        });

        chatTextAreas.add(chatTextArea);
        messageFields.add(messageField);

        chatWindow.setVisible(true);
        return chatWindow;
    }

    private JPopupMenu createOptionsMenu(JFrame chatWindow) {
        JPopupMenu optionsMenu = new JPopupMenu();

        JMenuItem signOutItem = new JMenuItem("Log Out");
        signOutItem.addActionListener(e -> signOut());
        optionsMenu.add(signOutItem);

        JMenuItem clearChatItem = new JMenuItem("Clear Chat");
        clearChatItem.addActionListener(e -> clearChatText(chatWindow));
        optionsMenu.add(clearChatItem);

        return optionsMenu;
    }

    private void clearChatText(JFrame chatWindow) {
        int index = chatWindows.indexOf(chatWindow);
        JTextArea chatTextArea = chatTextAreas.get(index);
        chatTextArea.setText("");
    }
    private void signOut() {
        if (chatWindows != null) {
            for (JFrame window : chatWindows) {
                window.dispose();
            }
        }
    
        chatWindows.clear();
        chatTextAreas.clear();
        messageFields.clear();
    
        yourName = null;
    
        // Recreate the login panel
        createLoginPanel();
    
        // Add the login panel to the main frame
        mainFrame.getContentPane().removeAll();
        mainFrame.add(cardPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }
    

    private void closeAllWindows() {
        if (chatWindows != null) {
            for (JFrame window : chatWindows) {
                window.dispose();
            }
        }

        System.exit(0);
    }
    private boolean isValidInput(String username, char[] password) {
        return username != null && !username.trim().isEmpty() && password != null && password.length > 0;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatApplication());
        
    }
}
