package com.example.demo.service;

import com.example.demo.entities.AccountItem;
import com.example.demo.entities.HotelService;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AccountItemRepository;
import com.example.demo.repository.HotelServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HotelServiceServiceImpl implements HotelServiceService {

	@Autowired
	private HotelServiceRepository servicioRepository;

	@Autowired
	private AccountItemRepository itemCuentaRepository;

	@Override
	@Transactional(readOnly = true)
	public List<HotelService> getAllServicios() {
		return servicioRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public HotelService getServicioById(Long id) {
		return servicioRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("No se encontró servicio con ID: " + id, id));
	}

	@Override
	public HotelService addServicio(HotelService servicio) {
		return servicioRepository.save(servicio);
	}

	@Override
	public HotelService updateServicio(Long id, HotelService servicio) {
		if (!servicioRepository.existsById(id))
			throw new NotFoundException("No se encontró servicio con ID: " + id, id);
		servicio.setId(id);
		return servicioRepository.save(servicio);
	}

	@Override
	public void deleteServicio(Long id) {
		HotelService servicio = servicioRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("No se encontró servicio con ID: " + id, id));

		List<AccountItem> itemsAsociados = itemCuentaRepository.findByHotelServiceId(id);
		if (!itemsAsociados.isEmpty()) {
			String itemsResumen = itemsAsociados.stream()
				.limit(3)
				.map(item -> "#" + item.getId() + " (cuenta #" + item.getAccount().getId() + ")")
				.collect(java.util.stream.Collectors.joining(", "));
			String sufijo = itemsAsociados.size() > 3
				? " y " + (itemsAsociados.size() - 3) + " más"
				: "";

			throw new IllegalStateException(
				"No se puede eliminar el servicio '" + servicio.getNombre() + "' porque está asociado a "
					+ itemsAsociados.size() + " consumos en cuentas: " + itemsResumen + sufijo
					+ ". Elimina o reasigna esos consumos primero."
			);
		}

		servicioRepository.delete(servicio);
	}
}

