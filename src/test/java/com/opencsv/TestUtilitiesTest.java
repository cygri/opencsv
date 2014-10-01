package com.opencsv;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUtilitiesTest {

   @Test
   public void displayStringArray() {
      String[] stringArray = new String[3];

      stringArray[0] = "a";
      stringArray[1] = "b";
      stringArray[2] = "c";

      assertEquals("Header\nNumber of elements:\t3\nelement 0:\ta\nelement 1:\tb\nelement 2:\tc\n",
            TestUtilities.displayStringArray("Header", stringArray));
   }
}
