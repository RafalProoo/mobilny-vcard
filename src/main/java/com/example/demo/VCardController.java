package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class VCardController {

    @GetMapping("/find")
    public String find(@RequestParam String profession) throws IOException {
        String address = "https://panoramafirm.pl/szukaj?k=" + profession;
        Document doc = Jsoup.connect(address).get();

        Elements companies = doc.select("li.card");

        for (Element company : companies) {
            System.out.println(company.select("a.company-name").text());
            System.out.println(company.select("div.address").text());
            System.out.println(company.select("a.icon-telephone").attr("title"));
            System.out.println(company.select("a.ajax-modal-link").attr("data-company-email"));
        }

        return "companies";
    }
}
