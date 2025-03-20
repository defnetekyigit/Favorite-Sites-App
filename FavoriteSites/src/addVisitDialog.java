import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addVisitDialog extends JDialog {
    private JTextField countryField, cityField, yearField, seasonField, bestFeatureField, commentField, ratingField;
    private JButton addButton;
    public String username_info;
    public addVisitDialog(JFrame parent, String username){
        super(parent, "Add Visit", true);

        setSize(400, 300);
        setLayout(new GridLayout(8,2));
        username_info = username;
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

        addButton = new JButton("Add");
        add(addButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVisit();
            }
        });

    }
    public String getUsername_info(){
        return username_info;
    }
    private void addVisit(){
        String query = "INSERT INTO visits (username, countryName, cityName, yearVisited, seasonVisited, bestFeature, comment, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/FavoriteSites", "root", "t*g*yKET0671");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, getUsername_info());
            preparedStatement.setString(2, countryField.getText().toLowerCase());
            preparedStatement.setString(3, cityField.getText().toLowerCase());
            preparedStatement.setInt(4, Integer.parseInt(yearField.getText()));
            preparedStatement.setString(5, seasonField.getText().toLowerCase());
            preparedStatement.setString(6, bestFeatureField.getText().toLowerCase());
            preparedStatement.setString(7, commentField.getText().toLowerCase());
            preparedStatement.setInt(8, Integer.parseInt(ratingField.getText()));
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(addVisitDialog.this,"Visit added successfully!");
            dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding visit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
