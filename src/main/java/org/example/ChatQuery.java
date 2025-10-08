package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;

import kong.unirest.HttpResponse;



public abstract class  ChatQuery {
    private static final String APIKEY="https://app.seker.live/fm1/send-message";
    private static final String ID="039575329";

    public static Survey generate_ChatPoll(String title,String subject,String quest,String answers) {
        try {
            HttpResponse<String> response= Unirest.get(APIKEY)
                    .queryString("id",ID)
                    .queryString("text",
                            "החזר אך ורק JSON תקני, בשורה אחת, ללא ```json וללא טקסט נוסף. " +
                                    "המבנה חייב להיות בדיוק כך: " +
                                    "{\"survey\":{\"subject\":\"" + subject + "\",\"questions_count\":" + quest + ",\"answers_per_question\":" + answers + ",\"questions\":[{\"id\":1,\"question\":\"שאלה לדוגמה\",\"answers\":[\"תשובה 1\",\"תשובה 2\"]}]}} " +
                                    "הוראות: 1. הכנס ל-\\\"subject\\\" את הנושא " + subject +
                                    ". 2. צור בדיוק " + quest + " שאלות במערך \\\"questions\\\". " +
                                    "3. לכל שאלה יהיו בדיוק " + answers + " תשובות במערך \\\"answers\\\". " +
                                    "4. החזר רק JSON אחד, בלי שום טקסט אחר, בלי הערות.")
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
            Survey survey =new Survey();
            survey.setTitle(title);
            //מעבר פר שאלה
            for (int i=0;i<surveyNode.get("questions_count").asInt();i++) {
               Question temp_question=new Question(title,surveyNode.get("questions").get(i).get("answers").size());
                temp_question.setQuestion(surveyNode.get("questions").get(i).get("question").asText());
                //מעבר פר תשובה
                for (int j=0;j<surveyNode.get("questions").get(i).get("answers").size();j++) {
                    temp_question.addAnswer(surveyNode.get("questions").get(i).get("answers").get(j).asText());
                }

                survey.addQuestion(temp_question);
            }
            return survey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
