package com.webapp.sportmeetingpoint.application.rest.event;


import com.webapp.sportmeetingpoint.application.service.EventService;
import com.webapp.sportmeetingpoint.domain.dto.Event.EventDTO;
import com.webapp.sportmeetingpoint.domain.dto.Event.TheNumberOfNecessaryEventsDTO;
import com.webapp.sportmeetingpoint.domain.dto.ImageDTO;
import com.webapp.sportmeetingpoint.domain.entities.*;
import com.webapp.sportmeetingpoint.persistance.EventImageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping(value = "/api/for_all")
public class ReceiveEventController {

  private final EventService eventService;

  @Autowired
  public ReceiveEventController(EventService eventService) {
    this.eventService = eventService;
  }

  @Autowired
  EventImageRepository eventImageRepository;

  @RequestMapping(value = "/event/get_events", method = RequestMethod.POST)
  public ResponseEntity getAllEventsWithoutImage(@RequestBody final TheNumberOfNecessaryEventsDTO reqData)
    throws MessagingException {

    List<Integer> excludedIds = reqData.getGetUIEventsId();
    final Integer limit = reqData.getTheNumberOfNecessaryEvents();

    List<Event> eventsFromDb = new ArrayList<>();

    if (excludedIds == null || excludedIds.size() == 0)
      eventsFromDb = eventService.find(limit);
    else
      eventsFromDb = eventService.find(excludedIds, limit);

    List<EventDTO> result = eventsFromDb.stream().map(a -> {
        final EventDTO e = new EventDTO();

        UserSystem author = a.getUserAuthorActivity().getUserSystem();

        if (author != null) {
          final String authorEmail = author.getEmail();
          UserPersonalData authorPersonalData = a.getUserAuthorActivity().getUserSystem().getUserPersonalData();
          final String authorFullName = authorPersonalData.getFirstName() + " " + authorPersonalData.getLastName();
          e.setAuthorEmail(authorEmail);
          e.setAuthorFullName(authorFullName);
        } else {
          e.setAuthorEmail("-deleted-");
          e.setAuthorFullName("-deleted-");
        }


        e.setId(a.getId());
        e.setTitle(a.getTitle());
        e.setPreviewMessage(a.getPreviewMessage());
        e.setDescription(a.getDescription());
        e.setImage(null);
        e.setAddress(a.getAddress());
        e.setCategory(a.getSportCategory().getName());


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        e.setEventDate(df.format(a.getDate()));


        return e;
      }

    ).collect(Collectors.toList());


    return ResponseEntity.ok().body(result);
  }

  @RequestMapping(value = "/event/event_by_id", method = RequestMethod.GET)
  public ResponseEntity getEventInfo(@RequestParam(name = "id") final Integer paramId) {

    EventDTO result = new EventDTO();

    try {

      final Event dbEvent = eventService.findEventById(paramId);
      final UserSystem getAuthor = dbEvent.getUserAuthorActivity().getUserSystem();
      final UserPersonalData getAuthorPersonalData = getAuthor.getUserPersonalData();
      final String authorFullName = getAuthorPersonalData.getFirstName() + " " + getAuthorPersonalData.getLastName();


      result.setAuthorFullName(authorFullName);
      result.setAuthorEmail(getAuthor.getEmail());
      result.setAddress(dbEvent.getAddress());
      result.setDescription(dbEvent.getDescription());
      result.setId(paramId);
      result.setPreviewMessage(dbEvent.getPreviewMessage());
      result.setTitle(dbEvent.getTitle());
      result.setImage(null);
      result.setCategory(dbEvent.getSportCategory().getName());
//      result.setCategory(dbEvent.getCategories().get(0).getName());


      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      result.setEventDate(df.format(dbEvent.getDate()));

      return ResponseEntity.ok().body(result);

    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }


  @RequestMapping(value = "/event/image_by_id", method = RequestMethod.GET)
  public ResponseEntity<?> getEventImageById(@RequestParam("id") final Integer eventId) {

    try {
      Event event = eventService.findEventById(eventId);
      EventImage eventImage = eventImageRepository.findById(event.getEventImageId()).orElse(null);

      if (eventImage == null || eventImage.getImage() == null) {
        return ResponseEntity.ok(HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(new ImageDTO(event.getId(), eventImage.getImage()), HttpStatus.OK);
    }catch(Exception e){
      return ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }
  }

}
