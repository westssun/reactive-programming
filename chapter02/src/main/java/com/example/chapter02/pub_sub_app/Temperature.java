package com.example.chapter02.pub_sub_app;

import lombok.Getter;

/**
 * Temperature in Celsius.
 */
@Getter
public final class Temperature {
   private final double value;

   public Temperature(double value) {
      this.value = value;
   }

   public double getValue() {
      return value;
   }
}
