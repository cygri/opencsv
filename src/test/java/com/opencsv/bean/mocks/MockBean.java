package com.opencsv.bean.mocks;

/**
 * Copyright 2007 Kyle Miller.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MockBean {
   private String name;
   private String id;
   private String orderNumber;
   private int num;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getOrderNumber() {
      return orderNumber;
   }

   public void setOrderNumber(String orderNumber) {
      this.orderNumber = orderNumber;
   }

   public int getNum() {
      return num;
   }

   public void setNum(int num) {
      this.num = num;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      MockBean mockBean = (MockBean) o;

      if (num != mockBean.num) return false;
      if (name != null ? !name.equals(mockBean.name) : mockBean.name != null) return false;
      if (id != null ? !id.equals(mockBean.id) : mockBean.id != null) return false;
      return !(orderNumber != null ? !orderNumber.equals(mockBean.orderNumber) : mockBean.orderNumber != null);
   }

   @Override
   public int hashCode() {
      int result = name != null ? name.hashCode() : 0;
      result = 31 * result + (id != null ? id.hashCode() : 0);
      result = 31 * result + (orderNumber != null ? orderNumber.hashCode() : 0);
      result = 31 * result + num;
      return result;
   }
}