package com.example.demo.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Component
public class MailUpdateScheduler {

    @Autowired
    private MailService mailService;

    //google api constants
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // to get the api permission create a project in the gcp and give the gmail api enable
    // then create a oauth for this mail and configure the settings to create it and choose the test account
    // we get a credential json file store it somewhere safe
    // read these code below and write the getCredentials method that will generate a token and store it in the tokens
    // folder if we change the scope of api then we have to delete the token folder and signing again to update the new permissions
    //now we have connected to the gmail api through the java program

    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_LABELS);
//  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_METADATA);
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_MODIFY);

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = MailUpdateScheduler.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    @Scheduled(fixedRate = 5000)
    public void updateMailData() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        String user = "me";

        ListMessagesResponse listResponse = service.users().messages()
                .list(user)
                .setQ("in:inbox is:unread")
                .execute();
        List<Message> messages = listResponse.getMessages();
        if (messages.isEmpty()) {
            System.out.println("No unread messages in the inbox.");
        } else {
            System.out.println("Unread Messages in Inbox:");
            //List<Message> fullmessage=messages.stream().forEach(message -> );
            for (Message message : messages) {
                Message fullMessage = service.users().messages().get(user, message.getId()).execute();

                List<MessagePartHeader> headers = fullMessage.getPayload().getHeaders();
                String from="";
                String subject = "";
                String date="";
                for (MessagePartHeader header : headers) {
                    if ("from".equalsIgnoreCase(header.getName())) {
                         from = header.getValue();
                        System.out.println(from);

                    }
                    else if ("subject".equalsIgnoreCase(header.getName())) {
                         subject = header.getValue();
                        System.out.println(subject);

                    }
                    else if ("date".equalsIgnoreCase(header.getName())) {
                        date = header.getValue();
                        System.out.println(date);

                    }


                }
                //replace with the mail that you use
                mailService.saveMailData(subject,from,"example@gmail.com",date);

                ModifyMessageRequest mods = new ModifyMessageRequest().setRemoveLabelIds(Collections.singletonList("UNREAD"));
                service.users().messages().modify(user, message.getId(), mods).execute();

            }

        }

        }
    }




