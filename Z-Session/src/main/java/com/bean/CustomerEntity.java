package com.bean;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerEntity {
	
	Integer customerId;
	String fullName;
	String email;
	String password;
	String gender;
	String birthdate;
	String contactNumber;
	String address1;
	String address2;
	String customerImagePath;
	String cusToken;
	
}