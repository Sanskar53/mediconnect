package com.mediconnect.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

/**
 * SPRING BOOT CONCEPT: JavaMailSender
 * Spring Boot auto-configures JavaMailSender when you add the mail dependency
 * and provide SMTP settings in application.yml.
 *
 * We use MimeMessage (instead of SimpleMailMessage) because it supports
 * HTML content — so emails look professional with formatting.
 *
 * For free sending, we use Gmail SMTP or SendGrid.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Sends an HTML email.
     * @param to      recipient email address
     * @param subject email subject line
     * @param body    HTML content of the email
     */
    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);  // true = HTML content

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }

    /**
     * Builds a professional HTML email for appointment confirmation.
     */
    public String buildAppointmentConfirmationEmail(String patientName, String doctorName,
            String specialization, String date, String time, String fee) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto;">
                    <div style="background-color: #0077b6; padding: 20px; text-align: center;">
                        <h1 style="color: white; margin: 0;">MediConnect</h1>
                        <p style="color: #caf0f8; margin: 5px 0;">Your Healthcare Platform</p>
                    </div>
                    <div style="padding: 30px; background: #f8f9fa;">
                        <h2 style="color: #0077b6;">Appointment Confirmed ✅</h2>
                        <p>Dear <strong>%s</strong>,</p>
                        <p>Your appointment has been successfully booked. Here are the details:</p>
                        <table style="width:100%%; border-collapse: collapse; margin: 20px 0;">
                            <tr style="background: #e9ecef;">
                                <td style="padding: 10px; border: 1px solid #dee2e6;"><strong>Doctor</strong></td>
                                <td style="padding: 10px; border: 1px solid #dee2e6;">Dr. %s</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; border: 1px solid #dee2e6;"><strong>Specialization</strong></td>
                                <td style="padding: 10px; border: 1px solid #dee2e6;">%s</td>
                            </tr>
                            <tr style="background: #e9ecef;">
                                <td style="padding: 10px; border: 1px solid #dee2e6;"><strong>Date</strong></td>
                                <td style="padding: 10px; border: 1px solid #dee2e6;">%s</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; border: 1px solid #dee2e6;"><strong>Time</strong></td>
                                <td style="padding: 10px; border: 1px solid #dee2e6;">%s</td>
                            </tr>
                            <tr style="background: #e9ecef;">
                                <td style="padding: 10px; border: 1px solid #dee2e6;"><strong>Consultation Fee</strong></td>
                                <td style="padding: 10px; border: 1px solid #dee2e6;">₹%s</td>
                            </tr>
                        </table>
                        <p style="color: #6c757d; font-size: 13px;">
                            Please arrive 10 minutes before your appointment time.<br>
                            Bring any previous medical reports if available.
                        </p>
                    </div>
                    <div style="background: #0077b6; padding: 15px; text-align: center;">
                        <p style="color: white; margin: 0; font-size: 12px;">
                            © 2024 MediConnect | Your Health, Our Priority
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(patientName, doctorName, specialization, date, time, fee);
    }

    /**
     * Builds an appointment reminder email (sent 24 hours before).
     */
    public String buildReminderEmail(String patientName, String doctorName, String date, String time) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto;">
                    <div style="background-color: #f4a261; padding: 20px; text-align: center;">
                        <h1 style="color: white; margin: 0;">⏰ Appointment Reminder</h1>
                    </div>
                    <div style="padding: 30px; background: #f8f9fa;">
                        <p>Dear <strong>%s</strong>,</p>
                        <p>This is a friendly reminder that you have an appointment <strong>tomorrow</strong>:</p>
                        <p style="font-size: 18px; color: #0077b6;">
                            📅 %s &nbsp;&nbsp; 🕐 %s &nbsp;&nbsp; 👨‍⚕️ Dr. %s
                        </p>
                        <p>Please make sure to arrive on time. Stay healthy!</p>
                    </div>
                    <div style="background: #f4a261; padding: 15px; text-align: center;">
                        <p style="color: white; margin: 0; font-size: 12px;">MediConnect — Your Healthcare Platform</p>
                    </div>
                </body>
                </html>
                """.formatted(patientName, date, time, doctorName);
    }

    /**
     * Builds a welcome email for new registrations.
     */
    public String buildWelcomeEmail(String name, String role) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto;">
                    <div style="background-color: #0077b6; padding: 20px; text-align: center;">
                        <h1 style="color: white; margin: 0;">Welcome to MediConnect 🏥</h1>
                    </div>
                    <div style="padding: 30px; background: #f8f9fa;">
                        <h2>Hello, %s! 👋</h2>
                        <p>Welcome to MediConnect. Your account has been created successfully as a <strong>%s</strong>.</p>
                        <p>You can now:</p>
                        <ul>
                            <li>Book appointments with qualified doctors</li>
                            <li>View your prescriptions online</li>
                            <li>Track your medical history</li>
                            <li>Receive appointment reminders</li>
                        </ul>
                        <p>We're glad to have you with us!</p>
                    </div>
                    <div style="background: #0077b6; padding: 15px; text-align: center;">
                        <p style="color: white; margin: 0; font-size: 12px;">© 2024 MediConnect | Your Health, Our Priority</p>
                    </div>
                </body>
                </html>
                """.formatted(name, role);
    }
}
