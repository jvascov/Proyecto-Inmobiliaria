package co.com.udem.inmobiliaria.rest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.com.udem.inmobiliaria.dto.UsuarioDTO;
import co.com.udem.inmobiliaria.entities.User;
import co.com.udem.inmobiliaria.entities.Usuario;
import co.com.udem.inmobiliaria.repositories.UserRepository;
import co.com.udem.inmobiliaria.repositories.UsuarioRepository;
import co.com.udem.inmobiliaria.util.Constantes;
import co.com.udem.inmobiliaria.util.ConvertUsuario;

@RestController
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ConvertUsuario convertUsuario;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/usuarios/addUsuario")
	public Map<String, String> addUsuario(@RequestBody UsuarioDTO usuarioDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			Usuario usuario = convertUsuario.convertToEntity(usuarioDTO);
			usuario.setPassword(this.passwordEncoder.encode(usuario.getPassword()));
			usuarioRepository.save(usuario);
			userRepository.save(User.builder().username(Long.toString(usuario.getNumeroIdentif()))
					.password(usuario.getPassword()).roles(Arrays.asList("ROLE_USER"))
					.build());
			response.put(Constantes.CODIGO_HTTP, "200");
			response.put(Constantes.MENSAJE_EXITO, "Registro exitoso");
			return response;
		} catch (Exception e) {
			response.put(Constantes.CODIGO_HTTP, "500");
			response.put(Constantes.MENSAJE_ERROR, "Ocurrió un problema al insertar");
			return response;
		}
	}

	@GetMapping("/usuarios")
	public List<UsuarioDTO> getUsuarios() {
		Iterable<Usuario> iUsuarios = usuarioRepository.findAll();
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		List<UsuarioDTO> listaUsuariosDTO = new ArrayList<UsuarioDTO>();
		iUsuarios.iterator().forEachRemaining(listaUsuarios::add);

		for (int i = 0; i < listaUsuarios.size(); i++) {
			try {
				UsuarioDTO usuarioDTO = convertUsuario.convertToDTO(listaUsuarios.get(i));
				listaUsuariosDTO.add(usuarioDTO);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		return listaUsuariosDTO;
	}

	@GetMapping("/usuarios/{numeroIdentif}")
	public Usuario buscarUsuario(@PathVariable int numeroIdentif) {
		return usuarioRepository.findByNumeroIdentif(numeroIdentif).get();
	}
}
