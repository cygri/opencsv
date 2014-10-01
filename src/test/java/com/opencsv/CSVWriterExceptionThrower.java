package com.opencsv;

import java.io.IOException;
import java.io.Writer;

public class CSVWriterExceptionThrower extends CSVWriter {
   public CSVWriterExceptionThrower(Writer writer) {
      super(writer);
   }

   @Override
   public void flush() throws IOException {
      throw new IOException("Exception thrown from Mock test flush method");
   }
}
