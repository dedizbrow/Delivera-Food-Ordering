package com.dalerleo.foodordering.models;
// CREATED BY JAVOKHIR ABDIRASHIDOV

// FOOD MODEL TO EASILY MANIPULATE FOODS OBJECT TAKEN FROM FIREBASE DATABASE
// WHICH FIELDS CORRESPONDS TO THIS FOOD CLASS OBJECT
public class Order {

  private String name;
  private String username;
  private String address;
  private int amount;
  private long price;
  private int status;


  public Order(String username, String name, int amount, long price, int status) {
    this.name = name;
    this.amount = amount;
    this.price = price;
    this.status = status;
    this.username = username;
  }

  public Order(
    String username,
    String name,
    String address,
    int amount,
    long price,
    int status) {
    this.name = name;
    this.amount = amount;
    this.price = price;
    this.status = status;
    this.username = username;

    this.address = address;
  }

  public Order() {
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public int getStatus() {
    return status;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getName() {

    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAmount() {
    return amount;
  }

  public String getAmountText() {
    return "Amount: " + amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public long getPrice() {
    return price;
  }

  public String getPriceCurrency() {
    return Long.toString(price) + " SUM";
  }

  public void setPrice(long price) {
    this.price = price;
  }
}
