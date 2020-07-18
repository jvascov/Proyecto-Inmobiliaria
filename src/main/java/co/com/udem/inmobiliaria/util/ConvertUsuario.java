package co.com.udem.inmobiliaria.util;

import java.text.ParseException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import co.com.udem.inmobiliaria.dto.UsuarioDTO;
import co.com.udem.inmobiliaria.entities.Usuario;

public class ConvertUsuario {

	@Autowired
	private ModelMapper modelMaper;
	
	public Usuario convertToEntity(UsuarioDTO usuarioDTO) throws ParseException {
		return modelMaper.map(usuarioDTO, Usuario.class);
	}
	
	public UsuarioDTO convertToDTO(Usuario usuario) throws ParseException {
		return modelMaper.map(usuario, UsuarioDTO.class);
	}
	
	
}
