package com.lankheet.iot.webservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

/**
 * Configuration class for an MQTT connection
 */
public class MqttConfig
{
   @NotEmpty
   private String url;
   @NotEmpty
   private String userName;
   @NotEmpty
   private String password;
   @NotEmpty
   private String caFilePath;
   @NotEmpty
   private String crtFilePath;
   @NotEmpty
   private String clientKeyFilePath;


   @JsonProperty
   public String getUrl()
   {
      return url;
   }


   @JsonProperty
   public void setUrl(String url)
   {
      this.url = url;
   }


   @JsonProperty
   public String getCaFilePath()
   {
      return caFilePath;
   }


   @JsonProperty
   public void setCaFilePath(String caFilePath)
   {
      this.caFilePath = caFilePath;
   }


   @JsonProperty
   public String getCrtFilePath()
   {
      return crtFilePath;
   }


   @JsonProperty
   public void setCrtFilePath(String crtFilePath)
   {
      this.crtFilePath = crtFilePath;
   }


   @JsonProperty
   public String getClientKeyFilePath()
   {
      return clientKeyFilePath;
   }


   @JsonProperty
   public void setClientKeyFilePath(String clientKeyFilePath)
   {
      this.clientKeyFilePath = clientKeyFilePath;
   }


   @JsonProperty
   public String getUserName()
   {
      return userName;
   }


   @JsonProperty
   public void setUserName(String userName)
   {
      this.userName = userName;
   }


   @JsonProperty
   public String getPassword()
   {
      return password;
   }


   @JsonProperty
   public void setPassword(String passWord)
   {
      this.password = passWord;
   }
}
