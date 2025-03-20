import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class deleteVisitDialog extends JDialog {
    private JButton deleteButton;
    private JTextField enterVisitID;
    String username_info;
    public deleteVisitDialog(JFrame parent,String username){
        super(parent, "Delete Visit", true);
        setSize(300, 200);
        setLayout(new GridLayout(5,2));
        username_info = username;
        add(new JLabel());
        add(new JLabel());
        enterVisitID = new JTextField();
        add(new JLabel("    Enter visitID: "));
        add(enterVisitID);
        add(new JLabel());
        add(new JLabel());
        deleteButton = new JButton("Delete");
        add(deleteButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVisit();
            }
        });
    }

    private void deleteVisit(){
        String query = "DELETE FROM visits WHERE VisitID = ? AND username = '" + username_info + "'";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/FavoriteSites", "root", "t*g*yKET0671");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(enterVisitID.getText()));
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Visit deleted successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Visit ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting visit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
