package ru.vsu.hospital.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vsu.hospital.model.dto.EventLogDto;
import ru.vsu.hospital.model.entity.EventCounter;
import ru.vsu.hospital.model.request.SendEventRequest;
import ru.vsu.hospital.service.business.EventService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public void sendEvent(@RequestBody SendEventRequest request) {
        eventService.sendEvent(request);
    }

    @GetMapping
    public List<EventLogDto> getEventLog() {
        return eventService.getEventLog();
    }

    @GetMapping("/counters")
    public List<EventCounter> getCounters() {
        return eventService.getCounters();
    }
}
