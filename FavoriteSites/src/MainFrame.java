import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MainFrame extends JFrame {

    private  JTextField imageField, yearField, shareVisitIDField, friendUsernameField;
    private  JButton addVisitButton, deleteVisitButton, editButton, bestFoodButton,
            mostVisitedCountryButton, springVisitsButton, imageButton, yearButton, allVisitsButton, shareButton, seeSharedVisitsButton;
    private DefaultTableModel tableModel;
    private JTable favoriteSites;

    public String username_info;

    public MainFrame(String username){
        setTitle("Main Page");
        setSize(800,600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Welcome " + username));
        username_info = username;
        addVisitButton = new JButton("Add Visit");
        deleteVisitButton = new JButton("Delete Visit");
        editButton = new JButton("Edit Visit");

        topPanel.add(addVisitButton);
        topPanel.add(deleteVisitButton);
        topPanel.add(editButton);

        add(topPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Visit ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Country Name");
        tableModel.addColumn("City Name");
        tableModel.addColumn("Year");
        tableModel.addColumn("Season");
        tableModel.addColumn("Best Feature");
        tableModel.addColumn("Comment");
        tableModel.addColumn("Rating");
        favoriteSites = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(favoriteSites);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
        addDataFromDatabase();

        JPanel bottomPanel = new JPanel(new GridLayout(5,1));

        JPanel yearPanel = new JPanel(new FlowLayout());
        yearField = new JTextField(5);
        yearButton = new JButton("Display Visits by Year");
        yearPanel.add(new JLabel("Year:"));
        yearPanel.add(yearField);
        yearPanel.add(yearButton);
        bottomPanel.add(yearPanel);

        JPanel imagePanel = new JPanel(new FlowLayout());
        imageField = new JTextField(5);
        imageButton = new JButton("Display Image by Visit ID");
        imagePanel.add(new JLabel("Visit ID:"));
        imagePanel.add(imageField);
        imagePanel.add(imageButton);
        bottomPanel.add(imagePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        bestFoodButton = new JButton("Best Food");
        buttonPanel.add(bestFoodButton);

        mostVisitedCountryButton = new JButton("Most Visited Country");
        buttonPanel.add(mostVisitedCountryButton);

        springVisitsButton = new JButton("Spring Visits");
        buttonPanel.add(springVisitsButton);

        allVisitsButton = new JButton("All Visits");
        buttonPanel.add(allVisitsButton);

        JPanel sharePanel = new JPanel(new FlowLayout());
        friendUsernameField = new JTextField(20);
        shareVisitIDField = new JTextField(5);
        shareButton = new JButton("Share visit with friend");
        sharePanel.add(new JLabel("Visit ID:"));
        sharePanel.add(shareVisitIDField);
        sharePanel.add(new JLabel("Friend username:"));
        sharePanel.add(friendUsernameField);
        sharePanel.add(shareButton);
        seeSharedVisitsButton = new JButton("Shared with me");
        sharePanel.add(seeSharedVisitsButton);


        bottomPanel.add(buttonPanel);
        bottomPanel.add(sharePanel);

        add(bottomPanel, BorderLayout.SOUTH);

        yearButton.addActionListener(e -> displayVisitsByYear());
        imageButton.addActionListener(e -> displayImage());
        bestFoodButton.addActionListener(e -> displayBestFood());
        mostVisitedCountryButton.addActionListener(e -> displayMostVisited());
        springVisitsButton.addActionListener(e -> displaySpringVisits());
        allVisitsButton.addActionListener(e -> addDataFromDatabase());
        addVisitButton.addActionListener(e -> addVisit());
        deleteVisitButton.addActionListener(e -> deleteVisit());
        editButton.addActionListener(e -> editVisit());
        shareButton.addActionListener(e -> shareVisit());
        seeSharedVisitsButton.addActionListener(e -> displaySharedVisits());
    }

    private void addVisit(){
        addVisitDialog avd = new addVisitDialog(this, username_info);
        avd.setVisible(true);
        addDataFromDatabase();
    }
    private void deleteVisit(){
        deleteVisitDialog dvd = new deleteVisitDialog(this,username_info);
        dvd.setVisible(true);
        addDataFromDatabase();
    }
    private void editVisit(){
        editVisitDialog evd = new editVisitDialog(this, username_info);
        evd.setVisible(true);
        addDataFromDatabase();
    }
    private void addDataFromDatabase(){
        String query = "SELECT * FROM visits where username = '" + username_info + "'";
        displayOnTable(query);
    }
    private void displayVisitsByYear(){
        String yearText = yearField.getText();
        String query = "SELECT * FROM visits WHERE yearVisited = " + yearText + " AND  username = '" + username_info + "'";
        displayOnTable(query);
        yearField.setText("");
    }
    private void displayImage(){
        String visitID_text = imageField.getText();
        int visitID = Integer.parseInt(visitID_text);
        String imagePath = "images/location" + visitID + ".jpg";
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(imagePath);
        imageLabel.setIcon(icon);
        JOptionPane.showMessageDialog(this, imageLabel, "Location Image", JOptionPane.PLAIN_MESSAGE);
        imageField.setText("");
    }
    private void displayBestFood(){
        String query = "SELECT * FROM visits WHERE bestFeature = 'food' AND username = '" + username_info + "' ORDER BY rating DESC";
        displayOnTable(query);
    }
    private void displayMostVisited(){
        String query = "SELECT countryName, COUNT(*) as visitCount FROM visits WHERE username = '" + username_info + "' GROUP BY countryName ORDER BY visitCount DESC LIMIT 1";
        displayMessage(query);
    }
    private void displaySpringVisits(){
        String query = "SELECT * FROM visits WHERE seasonVisited = 'spring' AND username = '" + username_info +"'";
        displayOnTable(query);
    }
    private void shareVisit(){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/FavoriteSites", "root", "t*g*yKET0671")) {
            String friendUsername = friendUsernameField.getText();
            boolean friendExists = checkFriendExists(connection, friendUsername);
            if (!friendExists) {
                JOptionPane.showMessageDialog(this, "Friend username does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int visitID = Integer.parseInt(shareVisitIDField.getText());
            boolean visitBelongsToUser = checkVisitBelongsToUser(connection, visitID);
            if (!visitBelongsToUser) {
                JOptionPane.showMessageDialog(this, "Invalid visit ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "INSERT INTO sharedvisits (username, friendUsername, visitID) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username_info);
                preparedStatement.setString(2, friendUsernameField.getText());
                preparedStatement.setInt(3, Integer.parseInt(shareVisitIDField.getText()));
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this,"Visit shared successfully!");
                shareVisitIDField.setText("");
                friendUsernameField.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Visit ID not found.");
            e.printStackTrace();
        }
    }
    private void displaySharedVisits(){
        String query = "SELECT * FROM visits v WHERE v.visitID IN (" +
                " SELECT VisitID FROM sharedVisits WHERE friendUsername = '" + username_info+ "')";
        displayOnTable(query);
    }
    private void displayMessage(String query) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/FavoriteSites", "root", "t*g*yKET0671");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String countryName = resultSet.getString("countryName");
                    JOptionPane.showMessageDialog(this, "Most Visited Country: " + countryName, "Most Visited Country", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(this, "No visits found.", "Most Visited Country", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void displayOnTable(String query){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/FavoriteSites", "root", "t*g*yKET0671");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            tableModel.setRowCount(0);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getInt("VisitID"),
                            resultSet.getString("username"),
                            resultSet.getString("countryName"),
                            resultSet.getString("cityName"),
                            resultSet.getInt("yearVisited"),
                            resultSet.getString("seasonVisited"),
                            resultSet.getString("bestFeature"),
                            resultSet.getString("comment"),
                            resultSet.getInt("rating"),
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean checkFriendExists(Connection connection, String friendUsername) throws SQLException {
        String query = "SELECT COUNT(*) FROM userinfo WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, friendUsername);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
    private boolean checkVisitBelongsToUser(Connection connection, int visitID) throws SQLException {
        String query = "SELECT COUNT(*) FROM visits WHERE username = ? AND visitID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username_info);
            preparedStatement.setInt(2, visitID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
}
