package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entities.RoomType;
import com.example.demo.service.RoomTypeService;

@Controller
@RequestMapping("/roomtypes")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping("")
    public String listTypes(Model model) {
        // both "/roomtypes" and "/roomtypes/table" render the same table view
        List<RoomType> types = roomTypeService.getAllRoomTypes();
        model.addAttribute("roomTypes", types);
        return "rooms/roomtype-tabla";
    }

    @GetMapping("/table")
    public String listTypesTable(Model model) {
        // simple delegate to keep route working
        return listTypes(model);
    }

    @GetMapping("/{code}")
    public String typeDetail(@PathVariable("code") String code, Model model) {
        Optional<RoomType> type = roomTypeService.getRoomTypeByCode(code);
        if (type.isEmpty()) return "redirect:/roomtypes";
        return "redirect:/roomtypes/edit/" + type.get().getCode();
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("type", new RoomType());
        return "rooms/roomtype-form";
    }

    @PostMapping("/add")
    public String saveType(@ModelAttribute("type") RoomType type) {
        roomTypeService.saveRoomType(type);
        return "redirect:/roomtypes";
    }

    @GetMapping("/edit/{code}")
    public String editForm(@PathVariable("code") String code, Model model) {
        Optional<RoomType> type = roomTypeService.getRoomTypeByCode(code);
        if (type.isPresent()) {
            model.addAttribute("type", type.get());
            return "rooms/roomtype-form";
        } else {
            return "redirect:/roomtypes";
        }
    }

    @PostMapping("/edit/{code}")
    public String updateType(@ModelAttribute("type") RoomType type, @PathVariable("code") String code) {
        // ensure code is set
        type.setCode(code);
        roomTypeService.saveRoomType(type);
        return "redirect:/roomtypes";
    }

    @PostMapping("/delete/{code}")
    public String deleteType(@PathVariable("code") String code) {
        roomTypeService.deleteByCode(code);
        return "redirect:/roomtypes";
    }
}
