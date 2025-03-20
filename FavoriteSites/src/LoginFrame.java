import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField , passwordField;
    public String username;
    public LoginFrame(){
        setTitle("\uD83D\uDD11 Login \uD83D\uDD11");
        setSize(375,400);
        setLayout(new GridLayout(10,2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        add(new JLabel());
        add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        add(passwordField);

        JButton loginButton = new JButton("\uD83D\uDD13 Login \uD83D\uDD13");
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }
    private void login(){
        username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password. ↻");
        }
        String url = "jdbc:mysql://localhost:3306/FavoriteSites";
        String user = "root";
        String user_password = "t*g*yKET0671";
        try (Connection connection = DriverManager.getConnection(url, user, user_password)) {
            String query = "SELECT * FROM userinfo WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "Login successful!");
                        openMainFrame();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
        }
    }
    private void openMainFrame(){
        new MainFrame(username).setVisible(true);
        this.dispose();
    }


}
