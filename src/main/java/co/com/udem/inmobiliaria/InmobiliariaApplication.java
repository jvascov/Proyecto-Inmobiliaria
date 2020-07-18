package co.com.udem.inmobiliaria;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import co.com.udem.inmobiliaria.dto.UsuarioDTO;
import co.com.udem.inmobiliaria.util.ConvertPropiedad;
import co.com.udem.inmobiliaria.util.ConvertUsuario;

@SpringBootApplication
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
	
	
	

}