package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;

import kong.unirest.HttpResponse;
import kong.unirest.json.JSONObject;

public class ChatQuery {
    private final String APIKEY="https://app.seker.live/fm1/send-message";
    private final String ID="039575329";

    public ChatQuery(String subject,String quest,String answers) {
        try {
            HttpResponse<String> response= Unirest.get(APIKEY)
                    .queryString("id",ID)
                    .queryString("text","תתן לי רק סקר בנושא :"+subject+", מספר השאלות יהיה:"+quest+", ולכל שאלה מספר התשובות יהיו:"+answers+",תחזיר לי את זה בפורמט JSON ללא הערות וללא תוספות של מה אתה עושה והסברים.")
                            .asString();
            Question question=new ObjectMapper().readValue(response.getBody(), Question.class);
            System.out.println(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
