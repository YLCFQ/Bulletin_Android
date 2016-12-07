package com.cs175.bulletinandroid.bulletin;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lucky on 11/22/16.
 */

public class BulletinAPI {

    private String APIAddress;

    private String token;

    public String getAPIAddress(){
        return APIAddress;
    }

    public void setAPIAddress(String address){
        this.APIAddress = address;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public BulletinAPI(){

    }

    public void login(final OnRequestListener listener, final String email, final String password){
        new Thread(new Runnable(){
            public void run(){
                try{
                    Log.d("Bulletin API", "Logging in with " + email + " and password " + password);
                    URL url = new URL(getAPIAddress() + "/auth/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    os.write("{ \"email\" : \"" + email + "\", \"password\" : \"" + password + "\"}");
                    Log.d("Bulletin API" , "Logging in with JSON: { \"email\" : \"" + email + "\", \"password\" : \"" + password + "\"}");
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageTokenResponse response = gson.fromJson(sb.toString(), SuccessMessageTokenResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.Login, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.Login, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with login " + e.getMessage());
                }
            }
        }).start();
    }
    public void register(final OnRequestListener listener, final String email, final String password, final String displayName){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/register/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    Log.d("Bulletin API", "Registering with " + "{ \"email\" : \"" + email + "\", \"password\" : \"" + password + "\", \"display_name\" : \"" + displayName + "\"}");
                    os.write("{ \"email\" : \"" + email + "\", \"password\" : \"" + password + "\", \"display_name\" : \"" + displayName + "\"}");
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.Register, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.Register, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with register " + e.getMessage());
                }
            }
        }).start();
    }

    public void checkDisplayName(final OnRequestListener listener, final String displayName){
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/register/check/name/?display_name=" + displayName);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");

                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckDisplayName, response);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckDisplayName, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with checking display name " + e.getMessage());
                }
            }
        }).start();
    }

    public void checkEmail(final OnRequestListener listener, final String email){

        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/register/check/email/?email=" + email);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckEmail, response);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckEmail, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with checking email " + e.getMessage());
                }
            }
        }).start();


    }

    public void checkToken(final OnRequestListener listener, final String token){

        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/auth/?token=" + token);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckToken, response);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckToken, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with checking token " + e.getMessage());
                }
            }
        }).start();


    }

    public void getItems(final OnRequestListener listener){

        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/items/all/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        ItemResponse[] responses = gson.fromJson(sb.toString(), ItemResponse[].class);

                        for(int i=0; i<responses.length; i++){
                            responses[i].setResponseCode(connection.getResponseCode());
                        }

                        listener.onResponsesReceived(OnRequestListener.RequestType.GetItems, connection.getResponseCode(), responses);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.GetItems, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting items " + e.getMessage());
                }
            }
        }).start();


    }

    public void getMyItems(final OnRequestListener listener){

        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/items/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        ItemResponse[] responses = gson.fromJson(sb.toString(), ItemResponse[].class);

                        for(int i=0; i<responses.length; i++){
                            responses[i].setResponseCode(connection.getResponseCode());
                        }

                        listener.onResponsesReceived(OnRequestListener.RequestType.GetItems, connection.getResponseCode(), responses);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.GetItems, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting items " + e.getMessage());
                }
            }
        }).start();


    }

    public void makeConversation(final OnRequestListener listener, final String itemId){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/conversations/new/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    os.write("{ \"itemId\" : \"" + itemId + "\"}");
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = new SuccessMessageResponse();
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.MakeConversation, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.MakeConversation, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with making conversation " + e.getMessage());
                }
            }
        }).start();
    }


    public void getMyUserDetails(final OnRequestListener listener){
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/users/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        UserResponse response = gson.fromJson(sb.toString(), UserResponse.class);
                        response.setResponseCode(connection.getResponseCode());

                        listener.onResponseReceived(OnRequestListener.RequestType.GetMyUserDetails, response);


                    }else{
                        SuccessMessageResponse response = new SuccessMessageResponse();
                        response.setResponseCode(403);
                        listener.onResponseReceived(OnRequestListener.RequestType.GetMyUserDetails, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting user details " + e.getMessage());
                }
            }
        }).start();

    }

    public void getConverations(final OnRequestListener listener){
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/conversations/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        ConversationResponse[] response = gson.fromJson(sb.toString(), ConversationResponse[].class);

                        listener.onResponsesReceived(OnRequestListener.RequestType.GetConversations, connection.getResponseCode(), response);


                    }else{
                        SuccessMessageResponse response = new SuccessMessageResponse();
                        response.setResponseCode(403);
                        listener.onResponseReceived(OnRequestListener.RequestType.GetConversations, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting conversations " + e.getMessage());
                }
            }
        }).start();
    }


    public void postItem(final OnRequestListener listener, final String title, final String description, final String picture, final Double price){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/items/new/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    os.write("{ \"itemId\" : \"" + title + "\"}");

                    //{ "title" : title, "description" : description, "pictures": ["onepicture"], "price" : price}
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = new SuccessMessageResponse();
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.MakeConversation, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.MakeConversation, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with making conversation " + e.getMessage());
                }
            }
        }).start();
    }




}
