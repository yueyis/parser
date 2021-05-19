package parser.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import parser.fix.FixField;
import parser.fix.FixMap;
import parser.model.FixVersionOption;
import parser.model.FixMessageFormData;

@Controller
public class FixParserController {

    private static final String DEFAULT_DIALECT = "FIX44";
    @Value("${html.title}")
    private String title;
    private FixMap fixMap;

    private String text;
    private String fixdialect;
    private List<List<FixField>> fixParseResList;
    private static List<FixVersionOption> fixOptionList;

    static {
        fixOptionList = new ArrayList<FixVersionOption>();
        fixOptionList.add(new FixVersionOption("FIX42", "FIX 4.2"));
        fixOptionList.add(new FixVersionOption("FIX43", "FIX 4.3"));
        fixOptionList.add(new FixVersionOption("FIX44", "FIX 4.4"));
        fixOptionList.add(new FixVersionOption("FIX50", "FIX 5.0"));
    }


    public FixParserController() {
        setFixDialect(DEFAULT_DIALECT);
    }

    public void setFixDialect(String version) {
        this.fixdialect = version;
        String prefix = "src/main/resources/";
        String suffix = ".xml";
        this.fixMap = new FixMap(prefix + this.fixdialect + suffix);
    }

    public String getFixDialect() {
        return fixdialect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FixMap getFixMap() {
        return fixMap;
    }

    public void setFixMap(FixMap fixMap) {
        this.fixMap = fixMap;
    }

    public List<List<FixField>> getFixParseResList() {
        return fixParseResList;
    }

    public void setFixParseResList(List<List<FixField>> fixParseResList) {
        this.fixParseResList = fixParseResList;
    }


    public void parseFixMessage(String q) {

        String repQ = q.replace("\001", "|").replace("\u263A", "|").replace("&", "|").replaceAll("☺", "|");

        String[] msgs = repQ.split("\n");

        for (String s : msgs) {

            List<FixField> fixFieldList = new ArrayList<FixField>();

            String[] flds = s.split("\\|");
            for (String field : flds) {
                FixField fld = null;

                String[] kv = field.split("=");
                String k, v;

                k = kv[0].trim();
                if (k.isEmpty()) {
                    continue;
                }

                if (kv.length == 1) {
                    v = "";
                } else {
                    v = kv[1].trim();
                }

                fld = FixMap.getFieldDetail(k, this.fixMap);
                fld.setValue(v);
                fixFieldList.add(fld);
            }
            if (fixFieldList.size() > 3) {
                this.fixParseResList.add(fixFieldList);

            }
        }
    }

    @GetMapping("/fixparser")
    public String get(@RequestParam(required = false) String q, Model model) {
        fixParseResList = new ArrayList<>();
        if (q == null) {
            text = "";
        } else {
            text = q;
            parseFixMessage(q);
        }
        setFixDialect(DEFAULT_DIALECT);
        model.addAttribute("title", title);
        model.addAttribute("text", text);
        model.addAttribute("fixdialect", getFixDialect());
        model.addAttribute("fixParseResList", getFixParseResList());
        model.addAttribute("FixOptionList", fixOptionList);

        return "fixparser";

    }

    @PostMapping("/fixparser")
    public String post(HttpServletResponse response, Model model, @Valid @ModelAttribute("formdata") FixMessageFormData formData) throws IOException {

        fixParseResList = new ArrayList<>();

        String q = formData.getText();
        String version = formData.getFixdialect();
        setFixDialect(version);
        parseFixMessage(q);

        model.addAttribute("title", title);
        model.addAttribute("text", q);
        model.addAttribute("fixdialect", version);
        model.addAttribute("fixParseResList", getFixParseResList());
        model.addAttribute("FixOptionList", fixOptionList);

        return "fixparser";

    }
}