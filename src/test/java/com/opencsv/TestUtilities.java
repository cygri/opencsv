package com.opencsv;

public class TestUtilities {
   public static String displayStringArray(String header, String[] stringArray) {
      StringBuffer sb = new StringBuffer();
      sb.append(header);
      appendNewLine(sb);
      sb.append("Number of elements:");
      appendTab(sb);
      sb.append(stringArray.length);
      appendNewLine(sb);

      for (int i = 0; i < stringArray.length; i++) {
         sb.append("element ");
         sb.append(i);
         sb.append(':');
         appendTab(sb);
         sb.append(stringArray[i]);
         appendNewLine(sb);
      }
      return sb.toString();
   }

   private static void appendTab(StringBuffer sb) {
      sb.append('\t');
   }

   private static void appendNewLine(StringBuffer sb) {
      sb.append('\n');
   }
}
