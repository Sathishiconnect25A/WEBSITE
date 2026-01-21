package com.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendUserConfirmation(String type, String to, String name) {
        String recipient = (to != null) ? to.trim() : "";
        String sender = (fromEmail != null) ? fromEmail.trim() : "";

        if (recipient.isEmpty() || !recipient.contains("@")) {
            return;
        }

        try {
            String subject;
            String headerTitle;
            String bodyContent;

            if (type.equalsIgnoreCase("JOB")) {
                subject = "Application Acknowledgment: Job Opportunity at WZGAI";
                headerTitle = "Career Opportunity at WZGAI";
                bodyContent = "Thank you for your interest in joining Wealth Zone Group AI. We have successfully received your job application and are currently reviewing your profile against our current requirements.";
            } else if (type.equalsIgnoreCase("INTERNSHIP")) {
                subject = "Internship Program: Application Received – WZGAI";
                headerTitle = "Internship Program @ WZGAI";
                bodyContent = "We are pleased to receive your application for our corporate internship program. Our talent acquisition team will evaluate your details and academic background to determine the next steps.";
            } else if (type.equalsIgnoreCase("STAFFING")) {
                subject = "Staffing Solutions: Request Received – WZGAI";
                headerTitle = "Corporate Staffing Solutions";
                bodyContent = "Thank you for sharing your staffing requirements with Wealth Zone Group AI. We have successfully received your request and our enterprise solutions team will contact you shortly to discuss how we can support your organizational needs.";
            } else {
                subject = "Support Request: Inquiry Received – WZGAI";
                headerTitle = "Customer Support Inquiry";
                bodyContent = "Thank you for reaching out to WZGAI. We have received your inquiry and our support representatives will get back to you shortly with the information you requested.";
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender, "WZGAI Team");
            helper.setTo(recipient);
            helper.setSubject(subject);

            String html = "<div style='font-family: Segoe UI, Tahoma, Geneva, Verdana, sans-serif; color: #333; max-width: 650px; margin: 0 auto; border: 1px solid #e2e8f0; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.05);'>"
                    + "  <div style='background-color: #0c4a6e; color: #ffffff; padding: 25px; text-align: center;'>"
                    + "    <h2 style='margin: 0; font-size: 20px; font-weight: 500;'>" + headerTitle + "</h2>"
                    + "  </div>"
                    + "  <div style='padding: 35px; line-height: 1.7;'>"
                    + "    <p style='font-size: 16px; margin-top: 0;'>Dear <strong>" + name + "</strong>,</p>"
                    + "    <p style='font-size: 15px; color: #4a5568;'>" + bodyContent + "</p>"
                    + "    <div style='margin: 25px 0; padding: 20px; background-color: #f8fafc; border-left: 4px solid #0c4a6e; border-radius: 4px;'>"
                    + "      <strong style='color: #2d3748; display: block; margin-bottom: 8px;'>What happens next?</strong>"
                    + "      <ul style='margin: 0; padding-left: 20px; font-size: 14px; color: #4a5568;'>"
                    + "        <li>Our team will conduct an initial screening of your submission.</li>"
                    + "        <li>If shortlisted, you will receive an update regarding the interview/consultation process.</li>"
                    + "        <li>Please allow 3-5 business days for our representatives to respond.</li>"
                    + "      </ul>"
                    + "    </div>"
                    + "    <p style='font-size: 15px; color: #4a5568;'>Thank you for choosing WZGAI. We appreciate your patience.</p>"
                    + "    <p style='margin-bottom: 0; font-size: 15px;'>Best Regards,<br/><strong>Talent Acquisition & Support Team</strong><br/>Wealth Zone Group AI</p>"
                    + "  </div>"
                    + getFooterHtml()
                    + "</div>";

            helper.setText(html, true);

            // ✅ INLINE LOGO (THIS WAS MISSING)
            ClassPathResource logo = new ClassPathResource("static/images/wzgai-logo.png");
            if (logo.exists()) {
                helper.addInline("wzgaiLogo", logo);
            }

            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("❌ [UserMailService] FAILED to send to [" + recipient + "]: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getFooterHtml() {
        return "<div style='background-color: #f7fafc; padding: 25px; border-top: 1px solid #e2e8f0; text-align: center;'>"
                + "  <img src='cid:wzgaiLogo' width='120' style='margin-bottom: 12px;' alt='WZGAI Logo'/>"
                + "  <div style='font-family: Arial, sans-serif; color: #4a5568; line-height: 1.4;'>"
                + "    <strong style='font-size: 14px; color: #2d3748;'>Wealth Zone Group AI International Pvt. Ltd.</strong><br/>"
                + "    <span style='font-size: 11px; color: #718096;'>A Strategic Technology Hub for Global Innovation</span><br/>"
                + "    <div style='margin-top: 8px; font-size: 11px;'>"
                + "      2nd Floor, A2 Square Building, Cyber Hills Colony, Madhapur, Hyderabad – 500081<br/>"
                + "      Official: <a href='https://wealthzonegroupai.com' style='color: #3182ce; text-decoration: none;'>www.wealthzonegroupai.com</a>"
                + "    </div>"
                + "  </div>"
                + "</div>";
    }

}
//
// package com.app.service;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Service;
//
// import com.app.util.EmailFooterUtil;
//
// import jakarta.mail.internet.MimeMessage;
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class UserMailService {
//
// private final JavaMailSender mailSender;
//
// @Value("${spring.mail.username}")
// private String fromEmail;
//
// @Async
// public void sendUserConfirmation(
// String type,
// String to,
// String name
// ) {
//
// try {
// String subject =
// type.equalsIgnoreCase("JOB")
// ? "Job Application Received – WZGAI"
// : type.equalsIgnoreCase("INTERNSHIP")
// ? "Internship Application Received – WZGAI"
// : "Support Request Received – WZGAI";
//
// MimeMessage message = mailSender.createMimeMessage();
// MimeMessageHelper helper =
// new MimeMessageHelper(message, true, "UTF-8");
//
// helper.setFrom(fromEmail, "WZGAI Team");
// helper.setTo(to);
// helper.setSubject(subject);
//
// String html =
// "<div style='font-family:Arial;'>"
// + "<p>Dear <b>" + name + "</b>,</p>"
// + "<p>Your request has been received successfully.</p>"
// + "<p>We will contact you soon.</p>"
// + EmailFooterUtil.getFooterHtml()
// + "</div>";
//
// helper.setText(html, true);
//
// // inline logo (SAFE)
// helper.addInline(
// "wzgaiLogo",
// new org.springframework.core.io.ClassPathResource(
// "static/images/wzgai-logo.png")
// );
//
// mailSender.send(message);
//
// System.out.println("✅ USER MAIL SENT: " + to);
//
// } catch (Exception e) {
// System.err.println("❌ USER MAIL FAILED: " + e.getMessage());
// e.printStackTrace();
// }
// }
// }
//
