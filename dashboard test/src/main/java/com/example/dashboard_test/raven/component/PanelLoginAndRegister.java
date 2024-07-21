package com.example.dashboard_test.raven.component;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;

import com.example.dashboard_test.HelloApplication;

import com.example.dashboard_test.SessionManager;
import net.miginfocom.swing.MigLayout;
import com.example.dashboard_test.raven.swing.Button;
import com.example.dashboard_test.raven.swing.MyPasswordField;
import com.example.dashboard_test.raven.swing.MyTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    private MyTextField txtUser;
    private MyTextField txtUser2;
    private MyTextField txtEmailRegister; // Separate instance for register panel
    private MyTextField txtEmailForgotPass; // Separate instance for forgot pass panel
    private MyPasswordField txtPass;
    private MyPasswordField txtConfirmPass;
    private MyPasswordField txtNewPass;
    private MyPasswordField txtConfirmNewPass;

    public PanelLoginAndRegister() {
        initComponents();
        initRegister();
        initLogin();
        initForgotPass();
        initCreateNewPass();
        initEmailSent();

        // Show only login panel initially
        login.setVisible(true);
        register.setVisible(false);
        forgotPass.setVisible(false);
        createNewPass.setVisible(false);
        emailSent.setVisible(false);
    }

    private void initComponents() {
        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();
        forgotPass = new javax.swing.JPanel();
        createNewPass = new javax.swing.JPanel();
        emailSent = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());
        add(login, "card3");
        add(register, "card4");
        add(forgotPass, "card5");
        add(createNewPass, "card6");
        add(emailSent, "card7");

        // Initialize txtEmail for register panel
        txtEmailRegister = new MyTextField();
        txtEmailRegister.setHint("Email");

        // Initialize txtEmail for forgot pass panel
        txtEmailForgotPass = new MyTextField();
        txtEmailForgotPass.setHint("Email");
    }

    private void initRegister() {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));

        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(0, 0, 0));
        register.add(label, "wrap 20");

        txtUser = new MyTextField();
        txtUser.setHint("Last Name");
        register.add(txtUser, "w 60%, wrap 15");

        txtUser2 = new MyTextField();
        txtUser2.setHint("First Name");
        register.add(txtUser2, "w 60%, wrap 15");

        // Use txtEmailRegister for register panel
        register.add(txtEmailRegister, "w 60%, wrap 15");

        txtPass = new MyPasswordField();
        txtPass.setHint("Password");
        register.add(txtPass, "w 60%, wrap 15");

        txtConfirmPass = new MyPasswordField();
        txtConfirmPass.setHint("Confirm Password");
        register.add(txtConfirmPass, "w 60%, wrap 15");

        // Adjust the registration action to use txtEmailRegister
        Button cmd = new Button();
        cmd.setBackground(new Color(165, 42, 42));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN UP");
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String lastName = txtUser.getText().trim();
                String firstName = txtUser2.getText().trim();
                String email = txtEmailRegister.getText().trim(); // Use txtEmailRegister
                String password = new String(txtPass.getPassword());
                String confirmPassword = new String(txtConfirmPass.getPassword());

                // Validate password
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Database insert logic
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop_main", "root", "")) {
                    String query = "INSERT INTO users (userName, userEmail, userPassword) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, lastName + ", " + firstName);
                        pstmt.setString(2, email);
                        pstmt.setString(3, password);
                        int rowsInserted = pstmt.executeUpdate();
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(null, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                            // Clear input fields only on successful registration
                            txtUser.setText("");
                            txtUser2.setText("");
                            txtEmailRegister.setText("");
                            txtPass.setText("");
                            txtConfirmPass.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        register.add(cmd, "w 40%, h 40, wrap 20");
    }


    private void initLogin() {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));

        JLabel label = new JLabel("<html><div style='text-align: center;'>Welcome to PUP-SRC<br>Room Scheduling System</div></html>");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(0, 0, 0));
        login.add(label);

        MyTextField txtEmail = new MyTextField();  // Local variable shadowing issue
        txtEmail.setHint("Email");
        login.add(txtEmail, "w 60%");

        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setHint("Password");
        login.add(txtPass, "w 60%");

        JButton cmdForget = new JButton("Forgot your password ?");
        cmdForget.setForeground(new Color(165, 42, 42));
        cmdForget.setFont(new Font("sansserif", 1, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdForget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showForgotPass(true);
            }
        });
        login.add(cmdForget);

        Button cmd = new Button();
        cmd.setBackground(new Color(165, 42, 42));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN IN");
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();  // Use the correct txtEmail field
                String password = new String(txtPass.getPassword());

                // Database query logic
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop_main", "root", "")) {
                    String query = "SELECT * FROM users WHERE userEmail = ? AND userPassword = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, email);
                        pstmt.setString(2, password);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            int userId = rs.getInt("userId");
                            String userRole = rs.getString("userRole");
                            JOptionPane.showMessageDialog(null, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                            // Clear input fields or perform necessary actions upon successful login
                            txtEmail.setText("");
                            txtPass.setText("");
                            // Proceed to your application's main functionality
                            SessionManager.setAttribute("userId", userId);
                            SessionManager.setAttribute("userRole", userRole);
                            launchHelloApplication(); // Launch HelloApplication after successful login
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        login.add(cmd, "w 40%, h 40");
    }

    // Eto yung pang kuha ng naka set na user id sa session: Integer userId = (Integer) SessionManager.getAttribute("userId");

    private void launchHelloApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                HelloApplication.main(new String[0]); // Launch HelloApplication

                // Close current window (PanelLoginAndRegister)
                Window currentWindow = SwingUtilities.getWindowAncestor(PanelLoginAndRegister.this);
                currentWindow.dispose();
            }
        });
    }

    private void initForgotPass() {
        forgotPass.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Forgot Password");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(0, 0, 0));
        forgotPass.add(label);

        // Use txtEmailForgotPass for forgot pass panel
        forgotPass.add(txtEmailForgotPass, "w 60%, wrap 15");

        JButton sendEmailButton = new JButton("Send Email");
        sendEmailButton.setBackground(new Color(165, 42, 42));
        sendEmailButton.setForeground(new Color(250, 250, 250));
        sendEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmailForgotPass.getText().trim();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop_main", "root", "")) {
                    String query = "SELECT COUNT(*) AS count FROM users WHERE userEmail = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, email);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next() && rs.getInt("count") > 0) {
                            // Email exists, proceed to change password
                            showCreateNewPass(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Email does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        forgotPass.add(sendEmailButton, "w 40%, h 40");
    }

    private void initCreateNewPass() {
        createNewPass.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Create New Password");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(0, 0, 0));
        createNewPass.add(label);

        txtNewPass = new MyPasswordField();
        txtNewPass.setHint("New Password");
        createNewPass.add(txtNewPass, "w 60%, wrap 15");

        txtConfirmNewPass = new MyPasswordField();
        txtConfirmNewPass.setHint("Confirm New Password");
        createNewPass.add(txtConfirmNewPass, "w 60%, wrap 15");

        Button cmd = new Button();
        cmd.setBackground(new Color(165, 42, 42));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("RESET PASSWORD");
        cmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = new String(txtNewPass.getPassword());
                String confirmNewPassword = new String(txtConfirmNewPass.getPassword());

                // Validate password
                if (!newPassword.equals(confirmNewPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Database update logic
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/oop_main", "root", "")) {
                    String query = "UPDATE users SET userPassword = ? WHERE userEmail = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, newPassword);
                        pstmt.setString(2, txtEmailForgotPass.getText().trim()); // Update password based on userEmail
                        int rowsUpdated = pstmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "Password reset successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                            // Clear input fields or perform necessary actions
                            txtNewPass.setText("");
                            txtConfirmNewPass.setText("");
                            // Proceed to login or show confirmation panel
                            showLogin(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Password reset failed", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        createNewPass.add(cmd, "w 40%, h 40, wrap 20");
    }

    private void initEmailSent() {
        emailSent.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Email Sent");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(0, 0, 0));
        emailSent.add(label);

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>Please check your email for further instructions.</div></html>");
        infoLabel.setFont(new Font("sansserif", 0, 14));
        infoLabel.setForeground(new Color(0, 0, 0));
        emailSent.add(infoLabel, "wrap");

        JButton backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setBackground(new Color(165, 42, 42));
        backToLoginButton.setForeground(new Color(250, 250, 250));
        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLogin(true);
            }
        });
        emailSent.add(backToLoginButton, "w 40%, h 40");
    }

    public void showRegister(boolean show) {
        register.setVisible(show);
        login.setVisible(!show);
        forgotPass.setVisible(false);
        createNewPass.setVisible(false);
        emailSent.setVisible(false);
    }

    private void showLogin(boolean show) {
        login.setVisible(show);
        register.setVisible(!show);
        forgotPass.setVisible(false);
        createNewPass.setVisible(false);
        emailSent.setVisible(false);
    }

    private void showForgotPass(boolean show) {
        forgotPass.setVisible(show);
        login.setVisible(!show);
        register.setVisible(false);
        createNewPass.setVisible(false);
        emailSent.setVisible(false);
    }

    private void showCreateNewPass(boolean show) {
        createNewPass.setVisible(show);
        forgotPass.setVisible(!show);
        login.setVisible(!show);
        register.setVisible(false);
        emailSent.setVisible(false);
    }

    private void showEmailSent(boolean show) {
        emailSent.setVisible(show);
        createNewPass.setVisible(!show);
        forgotPass.setVisible(!show);
        login.setVisible(!show);
        register.setVisible(false);
    }

    private javax.swing.JPanel createNewPass;
    private javax.swing.JPanel emailSent;
    private javax.swing.JPanel forgotPass;
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
}