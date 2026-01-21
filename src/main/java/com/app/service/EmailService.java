
package com.app.service;

import java.io.File;
// import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserMailService userMailService; // ✅ separate async service

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${hr.email}")
    private String hrEmail;

    // @Value("${support.email}")
    // private String supportEmail;

    // =====================================================
    // 📩 ADMIN EMAIL (JOB / INTERNSHIP / SUPPORT)
    // =====================================================
    public void sendEmail(
            String applicationType,
            String name,
            String mobile,
            String email,
            String qualification,
            String year,
            String role,
            String messageText,
            File attachment) {

        try {
            String applicationId = "APP-" + System.currentTimeMillis();

            String subject;
            String heading;
            String toEmail;

            switch (applicationType.toUpperCase()) {

                case "JOB":
                    heading = "Recruitment Notification: New Job Application Received";
                    subject = "RECRUITMENT | " + name + " | Job Application | ID: " + applicationId;
                    toEmail = hrEmail;
                    break;

                case "INTERNSHIP":
                    heading = "Internship Program: New Application Received";
                    subject = "INTERNSHIP | " + name + " | Application | ID: " + applicationId;
                    toEmail = hrEmail;
                    break;

                case "SUPPORT":
                    heading = "Client Query: New Support/Contact Request";
                    subject = "CLIENT INQUIRY | " + name + " | Support Request";
                    toEmail = hrEmail;
                    break;

                case "STAFFING":
                    heading = "Staffing Solutions: New Request Received";
                    subject = "STAFFING REQUEST | " + name + " | " + role; // role is used for Company Name here
                    toEmail = hrEmail;
                    break;

                default:
                    throw new IllegalArgumentException("Invalid application type");
            }

            System.out.println("📩 Sending ADMIN mail to: " + toEmail);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "WZGAI Portal");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(buildAdminHtml(
                    applicationType, heading, applicationId,
                    name, email, mobile,
                    qualification, year, role, messageText), true);

            // 🔒 Inline logo SAFE
            ClassPathResource logo = new ClassPathResource("static/images/wzgai-logo.png");
            if (logo.exists()) {
                helper.addInline("wzgaiLogo", logo);
            }

            if (!applicationType.equalsIgnoreCase("SUPPORT")
                    && !applicationType.equalsIgnoreCase("STAFFING")
                    && attachment != null
                    && attachment.exists()) {
                helper.addAttachment(attachment.getName(), attachment);
            }

            mailSender.send(message);

        } catch (Exception e) {
            // ❌ NEVER block application flow
            System.err.println("❌ ADMIN MAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }

        // ✅ Always trigger user mail
        userMailService.sendUserConfirmation(applicationType, email, name);
    }

    public void sendStaffingRequestEmail(com.app.entity.StaffingRequest request) {
        sendEmail(
                "STAFFING",
                request.getFullName(),
                request.getPhoneNumber(),
                request.getEmail(),
                "N/A", // qualification
                "N/A", // year
                request.getCompanyName(), // using role field for company name in buildAdminHtml
                request.getStaffingNeeds(),
                null);
    }

    // =====================================================
    // 📄 ADMIN EMAIL HTML
    // =====================================================
    private String buildAdminHtml(
            String type,
            String heading,
            String appId,
            String name,
            String email,
            String mobile,
            String qualification,
            String year,
            String role,
            String message) {
        String typeLabel = type.equalsIgnoreCase("SUPPORT") ? "Inquiry" : "Application";

        return "<div style='font-family: Segoe UI, Tahoma, Geneva, Verdana, sans-serif; color: #333; max-width: 700px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;'>"
                + "  <div style='background-color: #0c4a6e; color: #ffffff; padding: 20px; text-align: center;'>"
                + "    <h2 style='margin: 0; font-size: 22px; font-weight: 600;'>" + heading + "</h2>"
                + "  </div>"
                + "  <div style='padding: 30px; line-height: 1.6;'>"
                + "    <p style='font-size: 16px; margin-top: 0;'>Dear HR Team,</p>"
                + "    <p style='font-size: 14px;'>A new <strong>" + type.toLowerCase() + "</strong> "
                + typeLabel.toLowerCase()
                + " has been submitted through the corporate portal. Below are the candidate/client details for your review:</p>"

                + "    <div style='background-color: #f9fafb; border: 1px solid #edf2f7; border-radius: 6px; padding: 20px; margin: 20px 0;'>"
                + "      <table width='100%' cellpadding='8' cellspacing='0' style='font-size: 14px; border-collapse: collapse;'>"
                + "        <tr style='border-bottom: 1px solid #edf2f7;'><td><strong style='color: #4a5568;'>Full Name</strong></td><td style='color: #2d3748;'>"
                + name + "</td></tr>"
                + "        <tr style='border-bottom: 1px solid #edf2f7;'><td><strong style='color: #4a5568;'>Email Address</strong></td><td style='color: #2d3748;'><a href='mailto:"
                + email + "' style='color: #3182ce; text-decoration: none;'>" + email + "</a></td></tr>"
                + "        <tr style='border-bottom: 1px solid #edf2f7;'><td><strong style='color: #4a5568;'>Phone Number</strong></td><td style='color: #2d3748;'>"
                + mobile + "</td></tr>"

                + (type.equalsIgnoreCase("STAFFING")
                        ? "        <tr style='border-bottom: 1px solid #edf2f7;'><td><strong style='color: #4a5568;'>Company Name</strong></td><td style='color: #2d3748;'>"
                                + role + "</td></tr>"
                        : "")

                + (!type.equalsIgnoreCase("SUPPORT") && !type.equalsIgnoreCase("STAFFING")
                        ? "        <tr style='border-bottom: 1px solid #edf2f7;'><td><strong style='color: #4a5568;'>Qualification</strong></td><td style='color: #2d3748;'>"
                                + qualification + "</td></tr>"
                                + "        <tr style='border-bottom: 1px solid #edf2f7;'><td><strong style='color: #4a5568;'>Experience/Batch</strong></td><td style='color: #2d3748;'>"
                                + year + "</td></tr>"
                                + "        <tr style='border-bottom: 1px solid #edf2f7;'><td><strong style='color: #4a5568;'>Post Applied For</strong></td><td style='color: #2d3748;'>"
                                + role + "</td></tr>"
                                + "        <tr style='border-bottom: 1px solid #edf2f7;'><td><strong style='color: #4a5568;'>Application ID</strong></td><td style='color: #2d3748;'><code>"
                                + appId + "</code></td></tr>"
                        : "")

                + "        <tr><td valign='top'><strong style='color: #4a5568;'>"
                + (type.equalsIgnoreCase("STAFFING") ? "Staffing Needs" : "Message Details")
                + "</strong></td><td style='color: #2d3748;'>"
                + message + "</td></tr>"
                + "      </table>"
                + "    </div>"

                + "    <p style='font-size: 13px; color: #718096; text-align: right; margin-bottom: 0;'>"
                + "      System Timestamp: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
                + "    </p>"
                + "  </div>"
                + getFooterHtml()
                + "</div>";
    }

    // private String getFooterHtml() {
    // return "<table width='100%' cellpadding='0' cellspacing='0' "
    // + "style='background-color:#e0f2fe; padding:24px;'>"
    //
    // + "<tr>"
    // + "<td>"
    //
    // + "<table width='100%' cellpadding='0' cellspacing='0' "
    // + "style='font-family:Arial, sans-serif; font-size:12px; color:#082f49;'>"
    //
    // + "<tr>"
    //
    // // LEFT: Logo only
    // + "<td width='20%' valign='middle'>"
    // + "<img src='cid:wzgaiLogo' width='150' "
    // + "style='display:block;' alt='Wealth Zone Group AI Logo'/>"
    // + "</td>"
    //
    // // MIDDLE: Company name + description (centered vertically)
    // + "<td width='40%' valign='middle' style='padding-left:10px;'>"
    // + "<b style='font-size:24px;'>Wealth Zone Group AI International Pvt.
    // Ltd.</b><br/><br/>"
    // + "<span style='font-size:13px;'>"
    // + "Leading Technology Solutions provider delivering innovative IT "
    // + "and business services worldwide."
    // + "</span>"
    // + "</td>"
    //
    // // RIGHT: Address + website
    // + "<td width='40%' align='right' valign='middle'>"
    // + "2nd Floor, A2 Square Building<br/>"
    // + "Jai Hind Gandhi Road, Cyber Hills Colony<br/>"
    // + "VIP Hills, Silicon Valley, Madhapur<br/>"
    // + "Hyderabad – 500081, Telangana, India<br/><br/>"
    // + "<a href='https://wealthzonegroupai.com' "
    // + "style='color:#0c4a6e; text-decoration:none; font-weight:bold;'>"
    // + "www.wealthzonegroupai.com</a>"
    // + "</td>"
    //
    // + "</tr>"
    // + "</table>"
    //
    // + "</td>"
    // + "</tr>"
    // + "</table>";
    // }
    //
    private String getFooterHtml() {
        return "<div style='background-color: #f7fafc; padding: 25px; border-top: 1px solid #e2e8f0; text-align: center;'>"
                + "  <img src='cid:wzgaiLogo' width='130' style='margin-bottom: 15px;' alt='WZGAI Logo'/>"
                + "  <div style='font-family: Arial, sans-serif; color: #4a5568; line-height: 1.5;'>"
                + "    <strong style='font-size: 16px; color: #2d3748;'>Wealth Zone Group AI International Pvt. Ltd.</strong><br/>"
                + "    <span style='font-size: 12px;'>Global Hub for Advanced AI & Technology Solutions</span><br/>"
                + "    <div style='margin-top: 10px; font-size: 12px; color: #718096;'>"
                + "      2nd Floor, A2 Square Building, Cyber Hills Colony, VIP Hills, Silicon Valley, Madhapur<br/>"
                + "      Hyderabad – 500081, Telangana, India<br/>"
                + "      Website: <a href='https://wealthzonegroupai.com' style='color: #3182ce; text-decoration: none;'>www.wealthzonegroupai.com</a>"
                + "    </div>"
                + "  </div>"
                + "</div>";
    }

}
// package com.app.service;
//
// import java.io.File;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;
//
// import jakarta.mail.internet.MimeMessage;
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class EmailService {
//
// private final JavaMailSender mailSender;
// private final UserMailService userMailService;
//
// @Value("${spring.mail.username}")
// private String fromEmail;
//
// @Value("${hr.email}")
// private String hrEmail;
//
// @Value("${support.email}")
// private String supportEmail;
//
// // =====================================================
// // 📩 ADMIN EMAIL (JOB / INTERNSHIP / SUPPORT)
// // =====================================================
// public void sendEmail(
// String applicationType,
// String name,
// String mobile,
// String email,
// String qualification,
// String year,
// String role,
// String messageText,
// File attachment
// ) {
//
// try {
// String applicationId = "APP-" + System.currentTimeMillis();
//
// String heading;
// String subject;
// String toEmail;
//
// switch (applicationType.toUpperCase()) {
// case "JOB":
// heading = "New Job Application";
// subject = heading + " | ID: " + applicationId;
// toEmail = hrEmail;
// break;
//
// case "INTERNSHIP":
// heading = "New Internship Application";
// subject = heading + " | ID: " + applicationId;
// toEmail = hrEmail;
// break;
//
// case "SUPPORT":
// heading = "New Support Request";
// subject = heading + " | " + name;
// toEmail = supportEmail;
// break;
//
// default:
// throw new IllegalArgumentException("Invalid application type");
// }
//
// System.out.println("📩 Sending ADMIN mail to: " + toEmail);
//
// MimeMessage message = mailSender.createMimeMessage();
// MimeMessageHelper helper =
// new MimeMessageHelper(message, true, "UTF-8");
//
// helper.setFrom(fromEmail, "WZGAI Portal");
// helper.setTo(toEmail);
// helper.setSubject(subject);
// helper.setText(
// buildAdminHtml(
// applicationType,
// heading,
// applicationId,
// name,
// email,
// mobile,
// qualification,
// year,
// role,
// messageText
// ),
// true
// );
//
// // ✅ Safe inline logo
// ClassPathResource logo =
// new ClassPathResource("static/images/wzgai-logo.png");
// if (logo.exists()) {
// helper.addInline("wzgaiLogo", logo);
// }
//
// // ✅ Attach resume only for Job / Internship
// if (!applicationType.equalsIgnoreCase("SUPPORT")
// && attachment != null
// && attachment.exists()) {
// helper.addAttachment(attachment.getName(), attachment);
// }
//
// mailSender.send(message);
//
// } catch (Exception e) {
// System.err.println("❌ ADMIN MAIL FAILED: " + e.getMessage());
// e.printStackTrace();
// }
//
// // ✅ Always send user confirmation (ASYNC)
// userMailService.sendUserConfirmation(applicationType, email, name);
// }
//
// // =====================================================
// // 📄 ADMIN EMAIL HTML
// // =====================================================
// private String buildAdminHtml(
// String type,
// String heading,
// String appId,
// String name,
// String email,
// String mobile,
// String qualification,
// String year,
// String role,
// String message
// ) {
//
// return "<div style='font-family:Arial;'>"
// + "<h3>" + heading + "</h3>"
//
// + (!type.equalsIgnoreCase("SUPPORT")
// ? "<p><b>Application ID:</b> " + appId + "</p>"
// : "")
//
// + "<table border='1' cellpadding='6' cellspacing='0' width='100%'>"
// + "<tr><td><b>Name</b></td><td>" + name + "</td></tr>"
// + "<tr><td><b>Email</b></td><td>" + email + "</td></tr>"
// + "<tr><td><b>Mobile</b></td><td>" + mobile + "</td></tr>"
//
// + (!type.equalsIgnoreCase("SUPPORT")
// ? "<tr><td><b>Qualification</b></td><td>" + qualification + "</td></tr>"
// + "<tr><td><b>Experience / Year</b></td><td>" + year + "</td></tr>"
// + "<tr><td><b>Applied For</b></td><td>" + role + "</td></tr>"
// : "")
//
// + "<tr><td><b>Message</b></td><td>" + message + "</td></tr>"
// + "</table>"
//
// + "<p>Submitted on: "
// + LocalDateTime.now().format(
// DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
// + "</p>"
//
// + getFooterHtml()
// + "</div>";
// }
//
// // =====================================================
// // 📌 COMMON FOOTER (ADMIN + USER)
// // =====================================================
// private String getFooterHtml() {
// return "<hr/>"
// + "<table width='100%' style='font-family:Arial; font-size:12px;'>"
// + "<tr>"
// + "<td width='40%'>"
// + "<img src='cid:wzgaiLogo' width='120'/>"
// + "</td>"
// + "<td width='60%' align='right'>"
// + "<b>Wealth Zone Group AI International Pvt Ltd</b><br/>"
// + "<a href='https://wealthzonegroupai.com'>"
// + "www.wealthzonegroupai.com</a><br/><br/>"
// + "Madhapur, Hyderabad – 500081<br/>"
// + "Telangana, India"
// + "</td>"
// + "</tr>"
// + "</table>";
// }
// }
