import javax.swing.*;
import java.awt.*;

public class PreethiMart {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Preethi's Mart");
        frame.setSize(450, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("≡ƒ¢Æ Preethi Mart");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(new Color(102, 126, 234));
        title.setBounds(80, 40, 300, 40);
        panel.add(title);

        JLabel subtitle = new JLabel("Your Online Shopping Store");
        subtitle.setBounds(130, 85, 220, 25);
        panel.add(subtitle);

        JLabel loginTitle = new JLabel("LOGIN");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 22));
        loginTitle.setBounds(180, 130, 100, 30);
        panel.add(loginTitle);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(70, 180, 100, 25);
        panel.add(userLabel);

        JTextField username = new JTextField();
        username.setBounds(70, 205, 300, 35);
        panel.add(username);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(70, 255, 100, 25);
        panel.add(passLabel);

        JPasswordField password = new JPasswordField();
        password.setBounds(70, 280, 300, 35);
        panel.add(password);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(70, 330, 300, 40);
        loginButton.setBackground(new Color(102, 126, 234));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton);

        JLabel registerLabel =
                new JLabel("Don't have an account?");
        registerLabel.setBounds(135, 390, 200, 25);
        panel.add(registerLabel);

        JButton registerButton =
                new JButton("New Register");
        registerButton.setBounds(140, 420, 170, 30);
        panel.add(registerButton);

        // Login
        loginButton.addActionListener(e -> {

            String user = username.getText();
            String pass =
                    new String(password.getPassword());

            if (user.equals("admin") && pass.equals("1234")) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Login Successful!\n" +
                        "Welcome to Preethi Mart ≡ƒ¢Æ"
                );

            } else {

                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid Username or Password!",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Register
        registerButton.addActionListener(e -> {

            new RegisterPage();

        });

        frame.add(panel);
        frame.setVisible(true);
    }
}


// Registration Page
class RegisterPage {

    RegisterPage() {

        JFrame frame =
                new JFrame("Preethi Mart - Register");

        frame.setSize(450, 550);
        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel title =
                new JLabel("Create Account");

        title.setFont(
                new Font("Arial", Font.BOLD, 25)
        );

        title.setBounds(140, 25, 200, 35);
        panel.add(title);

        JLabel first =
                new JLabel("First Name");

        first.setBounds(50, 80, 100, 25);
        panel.add(first);

        JTextField firstName =
                new JTextField();

        firstName.setBounds(170, 80, 220, 30);
        panel.add(firstName);

        JLabel last =
                new JLabel("Last Name");

        last.setBounds(50, 125, 100, 25);
        panel.add(last);

        JTextField lastName =
                new JTextField();

        lastName.setBounds(170, 125, 220, 30);
        panel.add(lastName);

        JLabel email =
                new JLabel("Email");

        email.setBounds(50, 170, 100, 25);
        panel.add(email);

        JTextField emailField =
                new JTextField();

        emailField.setBounds(170, 170, 220, 30);
        panel.add(emailField);

        JLabel phone =
                new JLabel("Phone");

        phone.setBounds(50, 215, 100, 25);
        panel.add(phone);

        JTextField phoneField =
                new JTextField();

        phoneField.setBounds(170, 215, 220, 30);
        panel.add(phoneField);

        JLabel user =
                new JLabel("Username");

        user.setBounds(50, 260, 100, 25);
        panel.add(user);

        JTextField username =
                new JTextField();

        username.setBounds(170, 260, 220, 30);
        panel.add(username);

        JLabel pass =
                new JLabel("Password");

        pass.setBounds(50, 305, 100, 25);
        panel.add(pass);

        JPasswordField password =
                new JPasswordField();

        password.setBounds(170, 305, 220, 30);
        panel.add(password);

        JLabel confirm =
                new JLabel("Confirm Password");

        confirm.setBounds(50, 350, 120, 25);
        panel.add(confirm);

        JPasswordField confirmPassword =
                new JPasswordField();

        confirmPassword.setBounds(170, 350, 220, 30);
        panel.add(confirmPassword);

        JButton register =
                new JButton("Register");

        register.setBounds(140, 410, 170, 40);
        panel.add(register);

        register.addActionListener(e -> {

            if (firstName.getText().isEmpty() ||
                lastName.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() ||
                username.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please fill all fields!"
                );

                return;
            }

            String p1 =
                    new String(
                        password.getPassword()
                    );

            String p2 =
                    new String(
                        confirmPassword.getPassword()
                    );

            if (!p1.equals(p2)) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Passwords do not match!"
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    frame,
                    "Registration Successful! ≡ƒÄë"
            );
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
