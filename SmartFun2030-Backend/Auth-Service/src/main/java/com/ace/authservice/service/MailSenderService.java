package com.ace.authservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final JavaMailSender mailSender;


    public void sendVerificationEmail(String to, String firstName, String verificationLink) {

        String subject = "Vérification de votre compte SmartFun2030";

        String htmlContent = getVerificationEmailTemplate()
                .replace("{{FIRST_NAME}}", firstName)
                .replace("{{VERIFICATION_LINK}}", verificationLink);

        sendHtmlEmail(to, subject, htmlContent);
    }


    public void sendHtmlEmail(String to, String subject, String htmlContent) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

            System.out.println(" EMAIL ENVOYÉ → " + to);

        } catch (Exception e) {
            System.out.println(" ERREUR EMAIL : " + e.getMessage());
            throw new RuntimeException("Erreur d'envoi email", e);
        }
    }


    private String getVerificationEmailTemplate() {
        return """
                <!DOCTYPE html>
                <html lang="fr">
                <head>
                    <meta charset="UTF-8">
                    <title>Vérification de compte</title>
                </head>

                <body style="margin:0; padding:0; font-family:Arial, sans-serif; background-color:#f4f4f7;">

                <div style="max-width:600px; margin:auto; background:white; border-radius:10px; overflow:hidden; 
                            box-shadow:0 4px 12px rgba(0,0,0,0.1);">

                    <div style="background:#4CAF50; padding:25px 20px; text-align:center;">
                        <h1 style="color:white; margin:0; font-size:26px;">SmartFun2030</h1>
                        <p style="color:white; margin:5px 0 0; font-size:14px;">Activation de votre compte</p>
                    </div>

                    <div style="padding:30px 25px; color:#333;">
                        <h2 style="margin-top:0;">Bonjour {{FIRST_NAME}},</h2>

                        <p style="font-size:15px; line-height:1.6;">
                            Merci de vous être inscrit sur <strong>SmartFun2030</strong> 🎉<br>
                            Pour finaliser la création de votre compte, veuillez cliquer sur le bouton ci-dessous.
                        </p>

                        <div style="text-align:center; margin:35px 0;">
                            <a href="{{VERIFICATION_LINK}}"
                               style="background-color:#4CAF50; color:white; padding:15px 25px; 
                                      font-size:16px; text-decoration:none; border-radius:6px; 
                                      display:inline-block;">
                                Vérifier mon compte
                            </a>
                        </div>

                        <p style="font-size:14px; line-height:1.6;">
                            Si vous n'avez pas créé de compte, vous pouvez ignorer cet email.
                        </p>

                        <div style="margin-top:30px; background:#f4f4f7; padding:15px; border-radius:6px; font-size:13px;">
                            🔐 <strong>Note :</strong><br>
                            Ce lien est sécurisé et valable pour une durée limitée.
                        </div>
                    </div>

                    <div style="background:#f4f4f7; text-align:center; padding:18px; font-size:13px; color:#666;">
                        SmartFun2030 © 2025 — Tous droits réservés.<br>
                    </div>

                </div>

                </body>
                </html>
                """;
    }
}




