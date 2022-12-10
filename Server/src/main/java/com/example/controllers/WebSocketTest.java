package com.example.controllers;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

@ServerWebSocket("/ws/chat/{topic}/{username}")
public class WebSocketTest {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketTest.class);

    private final WebSocketBroadcaster broadcaster;

    public WebSocketTest(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public Publisher<String> onOpen(String topic, String username, WebSocketSession session) {
        log("onOpen", session, username, topic);
        return broadcaster.broadcast(String.format("{\"message\":\"Здравствуйте, отправьте сообщение\"}"), isValid(topic));
    }


    @OnMessage
    public Publisher<String> onMessage(String topic, String username, String message, WebSocketSession session) {
        log("onMessage", session, username, topic);
        return broadcaster.broadcast(String.format("{\"message\":\"Спасибо за сообщение\"}"), isValid(topic));
    }

    @OnClose
    public Publisher<String> onClose(String topic, String username, WebSocketSession session) {
        log("onClose", session, username, topic);
        return broadcaster.broadcast(String.format("Соединение разорвано"), isValid(topic));
    }

    private void log(String event, WebSocketSession session, String username, String topic) {
        LOG.info("* WebSocket: {} received for session {} from '{}' regarding '{}'",
                event, session.getId(), username, topic);
    }

    private Predicate<WebSocketSession> isValid(String topic) {
        return s -> topic.equals("all") //broadcast to all users
                || "all".equals(s.getUriVariables().get("topic", String.class, null)) //"all" subscribes to every topic
                || topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null)); //intra-topic chat
    }
}
