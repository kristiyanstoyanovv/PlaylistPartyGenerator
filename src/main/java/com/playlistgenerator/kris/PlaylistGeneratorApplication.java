package com.playlistgenerator.kris;

import com.playlistgenerator.kris.user.UserEntity;
import com.playlistgenerator.kris.song.SongService;
import com.playlistgenerator.kris.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlaylistGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaylistGeneratorApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserService userService, SongService songService) {
		return args -> {
			UserEntity admin = new UserEntity("admin", "admin", "admin@ppg.com");
			UserEntity user = new UserEntity("darkhid3","123456789","darkhid3@gmail.com");
			user.setEnabled(true);
			admin.setRole("ADMIN");
			admin.setEnabled(true);
			userService.createUser(admin);
			userService.createUser(user);


//			System.out.println(userService.findByEmail("admin@ppg.com"));
//			System.out.println(userService.findByUsername("admin"));
//			System.out.println(userService.findByUsername("admin"));
//			Song song = new Song(kris, "https://www.youtube.com/watch?v=5Oc0ja19_GU");
		};

	}

}
