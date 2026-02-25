package com.example.demo.controller;

import com.example.demo.entities.Room;
import com.example.demo.service.RoomService;
import com.example.demo.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

	@Autowired
	private RoomTypeService roomTypeService;

    @GetMapping
    public String listRooms(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
		Map<String, String> roomTypeDisplayNames = roomTypeService.getAllRoomTypes().stream()
				.collect(Collectors.toMap(rt -> rt.getCode(), rt -> rt.getDisplayName()));
		model.addAttribute("roomTypeDisplayNames", roomTypeDisplayNames);
        return "rooms/rooms-list";
    }

	@GetMapping("/table")
	public String listRoomsTable(Model model) {
		List<Room> rooms = roomService.getAllRooms();
		model.addAttribute("rooms", rooms);
		Map<String, String> roomTypeDisplayNames = roomTypeService.getAllRoomTypes().stream()
				.collect(Collectors.toMap(rt -> rt.getCode(), rt -> rt.getDisplayName()));
		model.addAttribute("roomTypeDisplayNames", roomTypeDisplayNames);
		return "rooms/rooms-table";
	}

    @GetMapping("/{id}")
    public String roomDetail(@PathVariable Long id, Model model) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isPresent()) {
            model.addAttribute("room", room.get());
			Map<String, String> roomTypeDisplayNames = roomTypeService.getAllRoomTypes().stream()
					.collect(Collectors.toMap(rt -> rt.getCode(), rt -> rt.getDisplayName()));
			model.addAttribute("roomTypeDisplayNames", roomTypeDisplayNames);
            return "rooms/room-detail";
        } else {
            return "redirect:/rooms";
        }
    }

    @GetMapping("/add")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("isNew", true);
		model.addAttribute("roomTypes", roomTypeService.getAllRoomTypes());
        return "rooms/room-form";
    }

    @PostMapping
    public String createRoom(@ModelAttribute Room room) {
        roomService.saveRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isPresent()) {
            model.addAttribute("room", room.get());
            model.addAttribute("isNew", false);
			model.addAttribute("roomTypes", roomTypeService.getAllRoomTypes());
            return "rooms/room-form";
        } else {
            return "redirect:/rooms";
        }
    }

    @PostMapping("/{id}")
    public String updateRoom(@PathVariable Long id, @ModelAttribute Room room) {
        room.setId(id);
        roomService.saveRoom(room);
        return "redirect:/rooms/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }
}
