package co.com.udem.inmobiliaria;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.com.udem.inmobiliaria.dto.UsuarioDTO;
import co.com.udem.inmobiliaria.util.ConvertPropiedad;
import co.com.udem.inmobiliaria.util.ConvertUsuario;

@SpringBootApplication
@EnableDiscoveryClient
public class InmobiliariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InmobiliariaApplication.class, args);
	}
	
	@Bean
	public ConvertUsuario convertUsuario() {
		return new ConvertUsuario();
	}
	
	@Bean
	public ConvertPropiedad ConvertPropiedad() {
		return new ConvertPropiedad();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public UsuarioDTO usuarioDTO() {
		return new UsuarioDTO();
	}
	
	@Bean
	PasswordEncoder getEncoder() {
	return new BCryptPasswordEncoder();
	}
	

}
