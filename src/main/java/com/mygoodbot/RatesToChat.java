package com.mygoodbot;

import org.springframework.web.client.RestTemplate;
import com.mygoodbot.stackexchange.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class RatesToChat {

    public static String rateMyCurrency(String rate) {
        if (rate.contains("list")) {
            System.out.println("Printing list");
            ArrayList<String> namesRates = getRatesNames();
            String reply = "Available currencies are: \n";
            int i=0;

            for (String s : namesRates) {
                i++;
                reply = reply + " " + s + ",";
                if (i%8 == 0) reply = reply + "\n";
            }
            reply = reply.substring(0, reply.length()-1);

            return reply;

        } else {
            try {
                int amountOfDays = 1; //default
                String exchangeRateSpec = rate; //default
                RestTemplate restTemplate = new RestTemplate();
                //parsing rates for today
                String apiStackExKey = "7ef595540eb2404ab25193eec7a6a603"; //change api key here
                String jsonRatesTodayURL = "https://openexchangerates.org/api/latest.json?app_id=" + apiStackExKey;
                RatesResponse ratesToday = restTemplate.getForObject(jsonRatesTodayURL, RatesResponse.class);

                //calculating all values
                DecimalFormat df = new DecimalFormat("0.00000");
                double comparingValue = Double.parseDouble(ratesToday.getRates().get(exchangeRateSpec).toString()); //rate of comparing value to USD today
                double oneRUBCompare = comparingValue / (double) ratesToday.getRates().get("RUB"); // 1 ruble in comparing value today
                String reply = "The rate of 1 RUB = " + df.format(oneRUBCompare) + " " + exchangeRateSpec; // + " and " + amountOfDays + " days before it was " + df.format(oneRUBCompareYesterday) + " " + exchangeRateSpec;

                return reply;

            } catch (Exception e) {
                System.out.println("Incorrect or empty currency was entered");
            }
        }

        return "Please check for typos and stuff. Correct request should look something like that: /rate USD \n\n To see all available currencies type /rate list";
    }

    public static ArrayList<String> getRatesNames() {
        RestTemplate restTemplate = new RestTemplate();
        String apiStackExKey = "7ef595540eb2404ab25193eec7a6a603"; //change api key here
        String jsonRatesTodayURL = "https://openexchangerates.org/api/latest.json?app_id=" + apiStackExKey;
        RatesResponse ratesToday = restTemplate.getForObject(jsonRatesTodayURL, RatesResponse.class);
        ArrayList<String> namesRates = new ArrayList<>(ratesToday.getRates().keySet());

        return namesRates;
    }
}
