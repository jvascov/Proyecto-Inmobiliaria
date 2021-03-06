package co.com.udem.inmobiliaria.rest.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.udem.inmobiliaria.dto.PropiedadDTO;
import co.com.udem.inmobiliaria.dto.UsuarioDTO;
import co.com.udem.inmobiliaria.entities.Propiedad;
import co.com.udem.inmobiliaria.entities.Usuario;
import co.com.udem.inmobiliaria.exception.ResourceNotFoundException;
import co.com.udem.inmobiliaria.repositories.PropiedadRepository;
import co.com.udem.inmobiliaria.repositories.UsuarioRepository;
import co.com.udem.inmobiliaria.util.Constantes;
import co.com.udem.inmobiliaria.util.ConvertPropiedad;
import co.com.udem.inmobiliaria.util.ConvertUsuario;

@RestController
public class PropiedadController {

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Autowired
	private ConvertPropiedad convertPropiedad;

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private ConvertUsuario convertUsuario;

	@PostMapping("/propiedades/addPropiedad/{username}")
	public Map<String, String> addpropiedad(@PathVariable String username, @RequestBody PropiedadDTO propiedadDTO)
			throws ResourceNotFoundException {
		Map<String, String> response = new HashMap<>();

		try {
			Propiedad propiedad = convertPropiedad.convertToEntity(propiedadDTO);
			usuarioRepository.findByNumeroIdentif(Integer.parseInt(username)).map(usuario -> {
				propiedad.setUsuario(usuario);
				return propiedad;
			}).orElseThrow(() -> new ResourceNotFoundException("Error con usuario"));
			propiedadRepository.save(propiedad);
			response.put(Constantes.CODIGO_HTTP, "200");
			response.put(Constantes.MENSAJE_EXITO, "Registrado insertado exitosamente");
			return response;
		} catch (ParseException e) {
			response.put(Constantes.CODIGO_HTTP, "500");
			response.put(Constantes.MENSAJE_ERROR, "Ocurrió un problema al insertar");
			return response;
		}

	}

	/*
	 * @GetMapping("/propiedades") public List<PropiedadDTO> getPropiedades() {
	 * Iterable<Propiedad> iPropiedades = propiedadRepository.findAll();
	 * List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();
	 * List<PropiedadDTO> listaPropiedadDTO = new ArrayList<PropiedadDTO>();
	 * iPropiedades.iterator().forEachRemaining(listaPropiedades::add);
	 * 
	 * for (int i = 0; i < listaPropiedades.size(); i++) { try { PropiedadDTO
	 * propiedadDTO = convertPropiedad.convertToDTO(listaPropiedades.get(i));
	 * listaPropiedadDTO.add(propiedadDTO); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 * 
	 * return listaPropiedadDTO; }
	 */

	@GetMapping("/propiedades")
	public List<PropiedadDTO> obtenerPropiedades() throws ResourceNotFoundException {
		Iterable<Propiedad> iPropiedad = propiedadRepository.findAll();
		List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();
		List<PropiedadDTO> listaPropiedadesDTO = new ArrayList<PropiedadDTO>();
		iPropiedad.iterator().forEachRemaining(listaPropiedades::add);
		for (int i = 0; i < listaPropiedades.size(); i++) {
			UsuarioDTO usuarioDTO2 = null;
			try {
				PropiedadDTO propiedadDTO = convertPropiedad.convertToDTO(listaPropiedades.get(i));
				usuarioDTO2 = usuarioRepository.findById(listaPropiedades.get(i).getUsuario().getId()).map(usuario -> {
					UsuarioDTO usuarioDTO = null;
					try {
						usuarioDTO = convertUsuario.convertToDTO(usuario);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return usuarioDTO;
				}).orElseThrow(() -> new ResourceNotFoundException("Error con usuario"));

				propiedadDTO.setUsuarioDTO(usuarioDTO2);
				listaPropiedadesDTO.add(propiedadDTO);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return listaPropiedadesDTO;
	}

	@GetMapping("/propiedad/{id}")
	public Propiedad buscarPropiedad(@PathVariable Long id) {
		return propiedadRepository.findById(id).get();
	}

	@GetMapping("/propiedades/filtro")
	public List<PropiedadDTO> filtrarPropiedades(@RequestParam Map<String, String> customQuery) {

		Iterable<Propiedad> iPropiedad = propiedadRepository.obtenerPropiedades(
				Long.parseLong(customQuery.get("area")), Integer.parseInt(customQuery.get("habitaciones")),
				Long.parseLong(customQuery.get("valor")));

		List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();
		List<PropiedadDTO> listaPropiedadesDTO = new ArrayList<PropiedadDTO>();
		iPropiedad.iterator().forEachRemaining(listaPropiedades::add);
		for (int i = 0; i < listaPropiedades.size(); i++) {
			try {
				PropiedadDTO propiedadFiltroDTO = convertPropiedad.convertToDTO(listaPropiedades.get(i));
				listaPropiedadesDTO.add(propiedadFiltroDTO);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return listaPropiedadesDTO;

	}

	@GetMapping("/propiedades/filtroavanzado")
	public List<PropiedadDTO> filtrarPropiedadesAvanzado(@RequestBody List<PropiedadDTO> listaPropiedadDTO)
			throws ResourceNotFoundException, ParseException {

		System.out.println(listaPropiedadDTO);
		PropiedadDTO propiedadDTOMin = listaPropiedadDTO.get(0);
		PropiedadDTO propiedadDTOMax = listaPropiedadDTO.get(1);

		Iterable<Propiedad> iPropiedad = propiedadRepository.obtenerPropiedadesAvanzado(propiedadDTOMin.getValor(),
				propiedadDTOMax.getValor(), propiedadDTOMin.getHabitaciones(), propiedadDTOMax.getHabitaciones(),
				propiedadDTOMin.getArea(), propiedadDTOMax.getArea());

		List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();
		List<PropiedadDTO> listaPropiedadesDTO = new ArrayList<PropiedadDTO>();
		iPropiedad.iterator().forEachRemaining(listaPropiedades::add);
		for (int i = 0; i < listaPropiedades.size(); i++) {
			try {
				PropiedadDTO propiedadFiltroDTO = convertPropiedad.convertToDTO(listaPropiedades.get(i));
				listaPropiedadesDTO.add(propiedadFiltroDTO);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return listaPropiedadesDTO;

	}

	@GetMapping("/propiedadesUsuario/{username}")
	public List<PropiedadDTO> obtenerPropiedadesusuario(@PathVariable String username)
			throws ResourceNotFoundException {
		Optional<Usuario> user = usuarioRepository.findByNumeroIdentif(Integer.parseInt(username));

		Iterable<Propiedad> iPropiedad = propiedadRepository.obtenerPropiedadesUsuario(user.get().getId());
		List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();
		List<PropiedadDTO> listaPropiedadesDTO = new ArrayList<PropiedadDTO>();
		iPropiedad.iterator().forEachRemaining(listaPropiedades::add);
		for (int i = 0; i < listaPropiedades.size(); i++) {
			UsuarioDTO userDTO = null;
			try {
				PropiedadDTO propiedadDTO = convertPropiedad.convertToDTO(listaPropiedades.get(i));
				userDTO = usuarioRepository.findById(listaPropiedades.get(i).getUsuario().getId()).map(usuario -> {
					UsuarioDTO usuarioDTO = null;
					try {
						usuarioDTO = convertUsuario.convertToDTO(usuario);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return usuarioDTO;
				}).orElseThrow(() -> new ResourceNotFoundException("Error con usuario"));

				propiedadDTO.setUsuarioDTO(userDTO);
				listaPropiedadesDTO.add(propiedadDTO);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return listaPropiedadesDTO;
	}

}
