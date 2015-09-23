package com.opencsv.bean;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class BeanField {
   private final Field field;
   private final boolean required;

   public BeanField(Field field, boolean required) {
      this.field = field;
      this.required = required;
   }

   public Field getField() {
      return this.field;
   }

   public boolean isRequired() {
      return this.required;
   }

   public <T> void setFieldValue(T bean, String value) throws IllegalAccessException {
      if (required && StringUtils.isBlank(value)) {
         throw new IllegalStateException(String.format("Field '%s' is mandatory but no value was provided.", field.getName()));
      }

      if (StringUtils.isNotBlank(value)) {
         Class<?> fieldType = field.getType();
         field.setAccessible(true);
         if (fieldType.equals(Boolean.TYPE)) {
            field.setBoolean(bean, Boolean.valueOf(value.trim()));
         } else if (fieldType.equals(Byte.TYPE)) {
            field.setByte(bean, Byte.valueOf(value.trim()));
         } else if (fieldType.equals(Double.TYPE)) {
            field.setDouble(bean, Double.valueOf(value.trim()));
         } else if (fieldType.equals(Float.TYPE)) {
            field.setFloat(bean, Float.valueOf(value.trim()));
         } else if (fieldType.equals(Integer.TYPE)) {
            field.setInt(bean, Integer.valueOf(value.trim()));
         } else if (fieldType.equals(Long.TYPE)) {
            field.setLong(bean, Long.valueOf(value.trim()));
         } else if (fieldType.equals(Short.TYPE)) {
            field.setShort(bean, Short.valueOf(value.trim()));
         } else if (fieldType.equals(Character.TYPE)) {
            field.setChar(bean, value.charAt(0));
         } else if (fieldType.isAssignableFrom(String.class)) {
            field.set(bean, value);
         } else {
            throw new IllegalStateException(String.format("Unable to set field value for field '%s' with value '%s' " +
                    "- type is unsupported. Use primitive and String types only.", fieldType, value));
         }
      }
   }
}