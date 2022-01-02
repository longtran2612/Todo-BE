package com.example.todomongodb.request;

import lombok.Data;

import java.util.List;

@Data
public class PhoneNumber {
    private List<String> phoneNumbers;
}
