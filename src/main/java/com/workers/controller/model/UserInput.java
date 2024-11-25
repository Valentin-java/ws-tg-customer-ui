package com.workers.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInput {
    private Long chatId;
    private String input;
}
