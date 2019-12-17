package com.webapp.sportmeetingpoint.application.rest.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.webapp.sportmeetingpoint.application.security.jwt.JwtUser;
import com.webapp.sportmeetingpoint.application.service.EventService;
import com.webapp.sportmeetingpoint.application.service.UserSystemService;
import com.webapp.sportmeetingpoint.domain.dto.EventDTO;
import com.webapp.sportmeetingpoint.domain.entities.Event;
import com.webapp.sportmeetingpoint.domain.entities.UserSystem;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = "/api/event")
public class EventController {

  private final UserSystemService userSystemService;
  private final EventService eventService;

  @Autowired
  public EventController(UserSystemService userSystemService, EventService eventService) {
    this.userSystemService = userSystemService;
    this.eventService = eventService;
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST,
          consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> addNewEvent(
          @RequestParam("file")  MultipartFile file,
          @RequestParam("data")  String data
  ) throws IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JwtUser jwtUser = (JwtUser)authentication.getPrincipal();
    UserSystem userSystem = userSystemService.findById(jwtUser.getId());
    EventDTO eventDTO = null;
  
  
    if(!data.isEmpty() && data != null){
      eventDTO = new ObjectMapper().readValue(data, EventDTO.class);
    }
    
    if(!file.isEmpty()){
      try {
    
        Byte[] byteImage = new Byte[file.getBytes().length];
    
        int i = 0;
    
        for (byte b : file.getBytes()){
          byteImage[i++] = b;
        }
  
        eventDTO.setImage(byteImage);
        
      } catch (IOException e) {
        log.error("Error occurred", e);
        e.printStackTrace();
      }
    }
    
    
    char c = 'a';
    Event e = new Event();
    e.setTitle(eventDTO.getTitle());
    e.setDate(new Date());
    e.setDescription(eventDTO.getDescription());
    e.setPreviewMessage(eventDTO.getPreviewMessage());
    e.setIsExpired(false);
    e.setAddress(eventDTO.getAddress());
    e.setImage(eventDTO.getImage());
    
    Event result = eventService.saveEvent(e, userSystem);

    return ResponseEntity.ok(HttpStatus.CREATED);
  }

  @GetMapping("/all_events")
  public ResponseEntity<Map<Integer, EventDTO>> getAllEventsWithoutImage() {

    List<Event> allEvents=eventService.allEvents();
    Map<Integer, EventDTO> result = new HashMap<>();
    
    allEvents.forEach(a -> {
      a.setImage(null);
      EventDTO e = new EventDTO();
      
      e.setAddress(a.getAddress());
      e.setDescription(a.getDescription());
      e.setPreviewMessage(a.getPreviewMessage());
      e.setTitle(a.getTitle());
      e.setImage(a.getImage());
      result.put(a.getId(), e);
      
    });
 
    
    return ResponseEntity.ok(result);
  }



}
