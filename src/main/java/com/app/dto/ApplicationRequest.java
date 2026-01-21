package com.app.dto;

import lombok.Data;

@Data
public class ApplicationRequest {

    private String name;
    private String mobile;
    private String email;
    private String qualification;
    private String yearOfPassing;
    private String course;
    private String message;
}
