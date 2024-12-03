package com.example.buddyapp.authentication.otp

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailSender {

    fun sendEmail(toEmail: String, subject: String, messageBody: String) {
        val username = "buddyauthentication@gmail.com" // Ganti dengan email pengirim
        val password = "olzl uqxh ghgm yrcw" // Ganti dengan password aplikasi Gmail

        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(username))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
            message.subject = subject
            message.setText(messageBody)

            Transport.send(message)
            println("Email sent successfully to $toEmail")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
