package com.library.applicationstarter.controllers;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/messages")
public class MockMessagingController {

    private static final List<String> BOOK_AVAILABLE_RESPONSES = Arrays.asList(
            "Great! The book is available. When would you like to pick it up?",
            "Sure, the book is available. We can arrange a time to meet."
    );

    private static final List<String> INTERESTED_RESPONSES = Arrays.asList(
            "Thank you for your interest! Let me know if you need more details about the book.",
            "I'm glad you're interested! What would you like to know?"
    );

    private static final List<String> NEGOTIATE_RESPONSES = Arrays.asList(
            "I understand. Let me know what works for you in terms of price or exchange conditions.",
            "Let's negotiate! What are you proposing?"
    );

    private static final List<String> EXCHANGE_RESPONSES = Arrays.asList(
            "Thanks for exchanging the book! Hope you enjoy reading it.",
            "The exchange is complete! Happy reading."
    );

    private static final List<String> GENERAL_RESPONSES = Arrays.asList(
            "How can I help you with your book exchange?",
            "Can I help you with any questions about the exchange process?"
    );

    @PostMapping("/send")
    public Response sendMessage(@RequestBody Message message) {
        String userMessage = message.getMessage().toLowerCase();

        String reply = "Sorry, I didn't understand that. Could you clarify?";

        if (userMessage.contains("book available")) {
            reply = getRandomResponse(BOOK_AVAILABLE_RESPONSES);
        } else if (userMessage.contains("interested")) {
            reply = getRandomResponse(INTERESTED_RESPONSES);
        } else if (userMessage.contains("negotiate")) {
            reply = getRandomResponse(NEGOTIATE_RESPONSES);
        } else if (userMessage.contains("exchange")) {
            reply = getRandomResponse(EXCHANGE_RESPONSES);
        } else {
            reply = getRandomResponse(GENERAL_RESPONSES);
        }

        return new Response(reply);
    }

    private String getRandomResponse(List<String> responses) {
        Random random = new Random();
        return responses.get(random.nextInt(responses.size()));
    }

    static class Message {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    static class Response {
        private String response;

        public Response(String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}

