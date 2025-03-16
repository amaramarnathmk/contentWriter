package com.email.contentWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailService {
    private final WebClient webClient;
    @Value("${gemini.api.key}")
    private String geminiapiKey;
    @Value("${gemini.api.url}")
    private String geminiapiURL;

    public EmailService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateEmailReply(EmailRequest emailRequest)
    {

        // build prompt

        String prompt = buildPrompt(emailRequest);

        //craft a request
        Map<String, Object> jsonRequestBody = Map.of("contents" , Map.of("parts", Map.of("text",prompt)));
        //do request get response
    String response=webClient.post().uri(geminiapiURL+geminiapiKey)
            .header("Content-Type","application/json")
            .bodyValue(jsonRequestBody)
            .retrieve().bodyToMono(String.class).block();
        //return response

      return getgeminiResponse(response);
    }

    private String getgeminiResponse(String response) {
        try
        {
        ObjectMapper objectMapper=new ObjectMapper();
            JsonNode treeNode=objectMapper.readTree(response);
            return treeNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text").asText();

        }
        catch (Exception pException)
        {
    return "exception in email generation"+pException;
        }
    }

    // create prompt for gemini
    public String buildPrompt(EmailRequest emailRequest)
    {
        StringBuilder prompt= new StringBuilder();
    //    prompt.append("Given the following email, generate only the reply email content without any additional " +
     //           "information. Do not include a subject line, or any extra details");
        prompt.append("generate an email reply for the following email content. please don't generate a subject line or any extra details");
        if(emailRequest.getTone() !=null & !emailRequest.getTone().isEmpty())
        {
            prompt.append(" use a"+emailRequest.getTone());
        }
        prompt.append(" original email: " +emailRequest.getEmailContent());

        return prompt.toString();
    }
}
