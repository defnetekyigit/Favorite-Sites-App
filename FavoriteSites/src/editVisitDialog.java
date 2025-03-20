import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class editVisitDialog extends JDialog {
    private JTextField countryField, cityField, yearField, seasonField, bestFeatureField, commentField, ratingField;
    private JButton editButton, displayButton;
    private JTextField enterVisitID;
    public String username_info;

    public editVisitDialog(JFrame parent,String username){
        super(parent, "Edit Visit", true);
        setSize(400, 300);
        setLayout(new GridLayout(10, 2));

        username_info = username;

        enterVisitID = new JTextField();
        add(new JLabel("Enter visit ID: "));
        add(enterVisitID);

        displayButton = new JButton("Display");
        add(displayButton);
        add(new JLabel());

        countryField = new JTextField();
        add(new JLabel("Country Name:"));
        add(countryField);

        cityField = new JTextField();
        add(new JLabel("City Name:"));
        add(cityField);

        yearField = new JTextField();
        add(new JLabel("Year visited:"));
        add(yearField);

        seasonField = new JTextField();
        add(new JLabel("Season visited:"));
        add(seasonField);

        bestFeatureField = new JTextField();
        add(new JLabel("Best feature of the visit:"));
        add(bestFeatureField);

        commentField = new JTextField();
        add(new JLabel("Comment:"));
        add(commentField);

        ratingField = new JTextField();
        add(new JLabel("Rating:"));
        add(ratingField);

        editButton = new JButton("Edit");
        add(editButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayVisit();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editVisit();
            }
        });
    }
    private void displayVisit(){
        String query = "SELECT * FROM visits WHERE VisitID = ? AND username = '" + username_info + "'";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/FavoriteSites", "root", "t*g*yKET0671");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(enterVisitID.getText()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    countryField.setText(resultSet.getString("countryName"));
                    cityField.setText(resultSet.getString("cityName"));
                    yearField.setText(String.valueOf(resultSet.getInt("yearVisited")));
                    seasonField.setText(resultSet.getString("seasonVisited"));
                    bestFeatureField.setText(resultSet.getString("bestFeature"));
                    commentField.setText(resultSet.getString("comment"));
                    ratingField.setText(String.valueOf(resultSet.getInt("rating")));
                } else {
                    JOptionPane.showMessageDialog(this, "Visit ID not found.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving visit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void editVisit(){
        String query = "UPDATE visits SET countryName = ?, cityName = ?, yearVisited = ?, seasonVisited = ?, bestFeature = ?, comment = ?, rating = ? WHERE VisitID = ? AND username = '" + username_info + "'";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/FavoriteSites", "root", "t*g*yKET0671");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, countryField.getText());
            preparedStatement.setString(2, cityField.getText());
            preparedStatement.setInt(3, Integer.parseInt(yearField.getText()));
            preparedStatement.setString(4, seasonField.getText());
            preparedStatement.setString(5, bestFeatureField.getText());
            preparedStatement.setString(6, commentField.getText());
            preparedStatement.setInt(7, Integer.parseInt(ratingField.getText()));
            preparedStatement.setInt(8, Integer.parseInt(enterVisitID.getText()));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Visit updated successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Visit ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating visit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
