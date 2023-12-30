package com.example.user.service;

import com.example.user.entities.User;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }


    public User updateUser(Long userId, User user) {
        if (userRepository.existsById(userId)) {
            user.setId(userId);
            return userRepository.save(user);
        } else {
            // Gérer l'absence de l'utilisateur avec l'ID spécifié
            return null;
        }
    }


    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User authenticateSuperuser(String email, String password) {
        // Retrieve the Superuser based on the provided username
        User superuser = userRepository.findByEmail(email);

        // Check if the Superuser exists and if the password matches
        if (superuser != null && superuser.getPassword().equals(password)) {
            return superuser; // Return the authenticated Superuser
        }

        return null; // Return null if authentication fails
    }

    public void sendemail(String email,int id) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Sender and recipient email addresses
        String senderEmail = "animeop96@gmail.com";
        String password = "jhgvcrhxsdckmwhf";

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            // Create a message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Récuperation du mot de passe");

            // HTML content with Formation name
            String htmlContent ="<html><body>"
                    + "<div style='text-align: center;'>"
                    + "<img src='https://www.dreamjob.ma/wp-content/uploads/2022/05/ONDA-Concours-Emploi-Recrutement.png' width='100px' height='100px' style='display: inline-block;'>"
                    + "</div>"
                    + "<div style='display: grid;grid-template-columns: auto auto;'>"
                    + "		<div style='float: left;font-size:15px; font-weight:200;'>Office National des Aéroport<br>Direction Capital Humain</div>"
                    + "</div>"
                    + "<p>Cher(e) utilisateur(trice),</p>"
                    + "<p>Vous avez récemment demandé la réinitialisation de votre mot de passe. Pour continuer le processus, "
                    + "veuillez suivre les étapes ci-dessous :"
                    + "Cliquez sur le lien ci-dessous pour accéder à la page de réinitialisation de mot de passe: http://localhost:3000/Changepassword/"+id+""
                    + " Une fois sur la page de réinitialisation, suivez les instructions pour céer un nouveau mot de passe "
                    + "sécurisé."
                    + "Aprés avoir défini votre nouveau mot de passe, cliquez sur 'Confirmer' pour finaliser la réinitialisation</p>"
                    + "<p>Si vous n'avez pas effectué cette demande ou si vous rencontrez des problémes, veuillez nous contacter"
                    + "immédiatement à MastourIbrahim@gmail.com ou au 0524994693 pour obtenir de l'aide </p>"
                    + "<p>Merci de faire confiance pour la gestion de votre compte. Nous sommes la pour vous aider à sécuriser votre"
                    + " accés en ligne. </p>"
                    + "</body></html>";

            // Create a body part to hold the HTML content
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html");

            // Create a multipart object to hold the HTML content
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);

            // Set the multipart as the content of the message
            message.setContent(multipart);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully Responsable.");
        } catch (MessagingException e) {
            System.err.println("Failed to send the email. Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User updatePassword(Long userId, String Password) {
        User superuser = userRepository.findById(userId).orElse(null);

        if (superuser != null) {
            superuser.setPassword(Password);
            return userRepository.save(superuser);
        }

        return null; // Handle the case where the user is not found.
    }

    public User updatePassword(Long userId, User updatedUser) {
        Optional<User> optionalExistingUser = userRepository.findById(userId);
        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
            existingUser.setNom(updatedUser.getNom());
            existingUser.setPrenom(updatedUser.getPrenom());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setPhoto(updatedUser.getPhoto());
            existingUser.setDescription(updatedUser.getDescription());
            return userRepository.save(existingUser);
        } else {
            // Handle user not found scenario or return null/throw an exception
            return null; // You can modify this to handle the scenario accordingly
        }
    }
}
