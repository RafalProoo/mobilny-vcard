package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VCardController {

    @GetMapping("/find")
    public String find(@RequestParam String profession, Model model) throws IOException {
        String address = "https://panoramafirm.pl/szukaj?k=" + profession;
        Document doc = Jsoup.connect(address).get();

        Elements companies = doc.select("li.card");

        List<Company> companyList = new ArrayList<>();

        for (Element company : companies) {

            companyList.add(new Company(
                    company.select("a.company-name").text(),
                    company.select("div.address").text(),
                    company.select("a.ajax-modal-link").attr("data-company-email"),
                    company.select("a.icon-telephone").attr("title")));
        }

        model.addAttribute("companyList", companyList);

        return "companies";
    }

    @GetMapping("/getVCard/{company}")
    public void getVCard(@PathVariable String company) {

    }
}
