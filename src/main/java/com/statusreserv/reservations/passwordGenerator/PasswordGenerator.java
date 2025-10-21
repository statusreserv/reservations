package com.statusreserv.reservations.passwordGenerator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PasswordGenerator {

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("dev"));
	}

}
