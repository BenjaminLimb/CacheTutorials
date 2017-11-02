package com.benjaminlimb.tutorial.infinispan.other;

import java.sql.Date;
import java.util.Calendar;

public class Person {
  final Calendar birthDate;
  final String fullName;
  
  public Person(Calendar pBirthDate, String pFullName) 
  {
    birthDate = pBirthDate;
    fullName = pFullName;    
  }
  
    //https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html
   @Override
   public String toString() 
   {
     return String.format("%2$s who was born %1$te %1$tb %1$tY", birthDate, fullName);
   }
  
  
}
