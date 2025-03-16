package com.email.contentWriter;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String tone;
}
