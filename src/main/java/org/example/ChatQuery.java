package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;

import kong.unirest.HttpResponse;


public class ChatQuery {
    private final String APIKEY="https://app.seker.live/fm1/send-message";
    private final String ID="039575329";

    public ChatQuery(String subject,String quest,String answers) {
        try {
            HttpResponse<String> response= Unirest.get(APIKEY)
                    .queryString("id",ID)
                    .queryString("text","תתן לי רק סקר בנושא :"+subject+", מספר השאלות יהיה:"+quest+", ולכל שאלה מספר התשובות יהיו:"+answers+",תחזיר לי את זה בפורמט JSON ללא הערות וללא תוספות של מה אתה עושה והסברים.")
                            .asString();
            ObjectMapper mapper = new ObjectMapper();

// קורא את ה־JSON הראשי
            JsonNode root = mapper.readTree(response.getBody());

// שולף את השדה extra שהוא String שמכיל JSON
            String extraJson = root.get("extra").asText().trim();

// אם יש תווים מיותרים כמו newlines, אפשר לנקות
            extraJson = extraJson.replaceAll("^```json", "").replaceAll("```$", "").trim();

// עכשיו parse מחדש את ה־JSON שבתוך extra
            JsonNode extraNode = mapper.readTree(extraJson);

// שולף את survey
            JsonNode surveyNode = extraNode.get("survey");

// ממפה ל-Survey
            Survey survey = mapper.treeToValue(surveyNode, Survey.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
