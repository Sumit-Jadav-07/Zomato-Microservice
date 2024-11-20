package com.bean;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantEntity {

  Integer restaurantId;
  String restaurantName;
  String email;
  String password;
  String contactNumber;
  String category;
  String description;
  String openingHours;
  String closingHours;
  String address;
  Boolean onlineStatus;
  Boolean activeStatus = true;
  String restaurantImagePath;
  String resToken;
    
}