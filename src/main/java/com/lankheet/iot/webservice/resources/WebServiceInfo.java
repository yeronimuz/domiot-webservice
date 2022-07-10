package com.lankheet.iot.webservice.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.util.Formatter;
import java.util.jar.Manifest;

/**
 * Class that provides information of this web service
 */
public class WebServiceInfo
{
   public static final String MAIN_CLASS     = "Main-Class";
   public static final String TITLE          = "Implementation-Title";
   public static final String VERSION        = "Implementation-Version";
   public static final String CLASSIFIER     = "Implementation-Classifier";
   public static final String BUILT_BY       = "Built-By";
   public static final String BUILD_TIME     = "Build-Time";
   public static final String BUILD_REVISION = "Build-Revision";
   public static final String CREATED_BY     = "Created-By";
   public static final String BUILD_JDK      = "Build-Jdk";
   public static final String BUILD_OS       = "Build-OS";

   Manifest manifest;


   /**
    * Constructor.
    *
    * @throws IOException Manifest not found
    */
   public WebServiceInfo()
      throws IOException
   {
      manifest = new Manifest();
      manifest.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
   }


   @JsonProperty
   public String getApplicationTitle()
   {
      return manifest.getMainAttributes().getValue(TITLE);
   }


   @JsonProperty
   public String getVersion()
   {
      return manifest.getMainAttributes().getValue(VERSION);
   }


   @JsonProperty
   public String getWebServiceInfo()
   {
      StringBuilder stringBuilder = new StringBuilder();
      try(Formatter fmt = new Formatter(stringBuilder))
      {
         fmt.format("%s %s-%s", getMFItemValue(TITLE), getMFItemValue(VERSION), getMFItemValue(CLASSIFIER));
      }
      return stringBuilder.toString();
   }


   private String getMFItemValue(String manifestItem)
   {
      return manifest.getMainAttributes().getValue(manifestItem);
   }
}
