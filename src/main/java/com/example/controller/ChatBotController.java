package com.example.controller;

import com.example.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {
        String prompt = request.get("message");
        String respuesta = chatBotService.infer(prompt); // debe retornar solo texto

        return ResponseEntity.ok(respuesta);
    }

}
