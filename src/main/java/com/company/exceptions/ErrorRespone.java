package com.company.exceptions;

public class ErrorRespone {
    private String message;

    public ErrorRespone(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
