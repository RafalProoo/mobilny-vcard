package com.example.demo;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VCardController {

    private static final String begin = "BEGIN:VCARD\r\n";
    private static final String version = "VERSION:4.0\r\n";
    private static final String end = "END:VCARD\r\n";
    private static final String address = "ADR;TYPE=WORK:;;";
    private static final String name = "FN:";
    private static final String telephone = "TEL;WORK:";
    private static final String email = "EMAIL:";

    @GetMapping("/find")
    public String find(@RequestParam String profession, Model model) throws IOException {
        String address = "https://panoramafirm.pl/szukaj?k=" + profession;
        Document doc = Jsoup.connect(address).get();

        Elements companies = doc.select("li.card");

        List<Company> companyList = new ArrayList<>();

        for (Element company : companies) {

            companyList.add(new Company(
                    company.select("a.company-name").text().trim(),
                    company.select("div.address").text().trim(),
                    company.select("a.ajax-modal-link").attr("data-company-email").trim(),
                    company.select("a.icon-telephone").attr("title").trim()));
        }

        model.addAttribute("companyList", companyList);

        return "companies";
    }

    @GetMapping("/getVCard/{company}")
    public ResponseEntity<Resource> getVCard(@PathVariable String company) throws IOException {
        Company selectedCompany = new Gson().fromJson(company, Company.class);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(begin);
        stringBuilder.append(version);
        stringBuilder.append(name).append(selectedCompany.getName()).append("\r\n");
        stringBuilder.append(address).append(selectedCompany.getAddress()).append("\r\n");
        stringBuilder.append(email).append(selectedCompany.getEmail()).append("\r\n");
        stringBuilder.append(telephone).append(selectedCompany.getTelephone()).append("\r\n");
        stringBuilder.append(end);

        String fileName = selectedCompany.getName().trim() + ".vcf";

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(stringBuilder.toString());
        writer.close();

        Path path = Paths.get(fileName);
        Resource resource = null;

        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println(selectedCompany.toString());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
