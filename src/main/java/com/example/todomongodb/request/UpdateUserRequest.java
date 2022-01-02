package com.example.todomongodb.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;

    private String email;

    private String address;

    private String image;

}