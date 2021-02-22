package com.mygoodbot;

import java.util.Random;
import com.mygoodbot.gifs.*;
import org.springframework.web.client.RestTemplate;

public class CryMeALink {

    public static String gifLinkCreate(String str) {
        RestTemplate restTemplate = new RestTemplate();
        String apiGiphyKey = "WuJjckB75aXB4nruBShg7e0t4UZVqJ3h"; //change api key here
        String richBroke = str;
        int offset = 0; //offset value (if you want to mix up things)
        int limit = 25; // giphy returns max of 50 com.mygoodbot.gifs
        int upperbound = limit + offset; //creating upperbound for request
        Random r = new Random();
        int selectRand = r.nextInt(upperbound - offset - 1);
        String jsonGiphyURL = "https://api.giphy.com/v1/gifs/search?api_key=" + apiGiphyKey + "&q=" + richBroke + "&limit=" + upperbound + "&offset=" + offset + "&rating=g&lang=en";
        Gifs responseGiphy = restTemplate.getForObject(jsonGiphyURL, Gifs.class);
        String gifLink = responseGiphy.getData().get(selectRand).getImages().getFixedHeight().getUrl();
        System.out.println("the link is: " + gifLink);
    return gifLink;
    }

}
