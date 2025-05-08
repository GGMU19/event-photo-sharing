package com.joeburin.event_photo_sharing.tmp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class PasswordEncoderTest {
    public static void main(String[] args) {
        System.out.println("nonattendeePass: " + new BCryptPasswordEncoder().encode("nonattendeePass"));
    }
}