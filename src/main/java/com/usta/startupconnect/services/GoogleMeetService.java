package com.usta.startupconnect.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.usta.startupconnect.exception.MeetingCreationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleMeetService {
    
    private static final Logger logger = LoggerFactory.getLogger(GoogleMeetService.class);
    private static final String APPLICATION_NAME = "StartUpConnect Google Meet Integration";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Value("${google.calendar.timezone:America/Bogota}")
    private String timeZone;

    /**
     * Creates a Google Meet event with the specified details.
     *
     * @param summary     The title of the meeting
     * @param description The description of the meeting
     * @param startTime   ISO-8601 formatted start time
     * @param endTime     ISO-8601 formatted end time
     * @return The created Event with conference details
     * @throws MeetingCreationException if unable to create the meeting
     */
    public Event createMeetEvent(String summary, String description, String startTime, String endTime) {
        try {
            // Build the Google Calendar service
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Parse start and end times
            DateTime start = parseDateTime(startTime);
            DateTime end = parseDateTime(endTime);

            // Create event
            Event event = new Event()
                    .setSummary(summary)
                    .setDescription(description)
                    .setStart(new EventDateTime().setDateTime(start).setTimeZone(timeZone))
                    .setEnd(new EventDateTime().setDateTime(end).setTimeZone(timeZone));

            // Add Google Meet conference data
            ConferenceData conferenceData = new ConferenceData();
            CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
            createConferenceRequest.setRequestId(String.valueOf(System.currentTimeMillis()));
            conferenceData.setCreateRequest(createConferenceRequest);
            event.setConferenceData(conferenceData);

            // Insert the event
            event = service.events().insert("primary", event)
                    .setConferenceDataVersion(1)
                    .execute();

            logger.info("Event created: {}", event.getHtmlLink());
            return event;
        } catch (IOException | GeneralSecurityException e) {
            logger.error("Error creating Google Meet event", e);
            throw new MeetingCreationException("Error al crear el evento de Google Meet: " + e.getMessage(), e);
        } catch (DateTimeParseException e) {
            logger.error("Error parsing date/time", e);
            throw new MeetingCreationException("Formato de fecha/hora inválido: " + e.getMessage(), e);
        }
    }

    /**
     * Parses an ISO-8601 formatted date time string to a Google Calendar DateTime object.
     */
    private DateTime parseDateTime(String dateTimeStr) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(timeZone));
            return new DateTime(zonedDateTime.toInstant().toEpochMilli());
        } catch (DateTimeParseException e) {
            logger.error("Error parsing date: {}", dateTimeStr, e);
            throw e;
        }
    }

    /**
     * Creates an authorized Credential object.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets
        InputStream in = GoogleMeetService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
                
        // Usando un puerto diferente (9090) para evitar conflictos con otros procesos
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9090).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}