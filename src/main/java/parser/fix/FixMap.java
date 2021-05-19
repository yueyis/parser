package parser.fix;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.WordUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FixMap {

    private String filePath;

    private String version;

    private HashMap<String, FixField> fieldMap = new HashMap<String, FixField>();

    public HashMap<String, FixField> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(HashMap<String, FixField> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public FixMap() {
        buildMapping();
    }

    public FixMap(String file) {
        this.filePath = file;
        buildMapping();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public void buildMapping() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(FilenameUtils.getName(this.filePath));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            Node rootNode = doc.getElementsByTagName("fields").item(0);

            NodeList nodeList = rootNode.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                addField(nodeList.item(i));
            }


        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
    }

    public List<String> stringToList(String csv) {
        String[] elements = csv.split(",");
        List<String> list = Arrays.asList(elements);
        List<String> trimFilterList = list.stream()
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());
        return trimFilterList;
    }


    private void addField(Node node) {

        FixField field = new FixField();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String tagNumber = element.getAttribute("number");
            String tagName = element.getAttribute("name");
            String label = "";
            if (element.hasAttribute("label")) {
                label = element.getAttribute("label");
            }
            field.setTag(tagNumber);
            field.setName(tagName);
            List<String> tagLabelList = stringToList(label);
            if(tagLabelList.isEmpty()) {
                tagLabelList.add("Standard");
            }

            field.setLabeList(tagLabelList);

            if (node.hasChildNodes()) {
                NodeList subNodeList = element.getChildNodes();

                for (int i = 0; i < subNodeList.getLength(); i++) {
                    if (subNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                        Element e = (Element) subNodeList.item(i);
                        String v = e.getAttribute("enum");
                        String d = WordUtils.capitalizeFully(e.getAttribute("description").replace('_', ' '), null);
                        field.addMmap(v, d);
                    }
                }
            }

            this.fieldMap.put(tagNumber, field);
        }
    }

    public boolean hasTag(String key) {
        boolean hasKey;

        hasKey = this.fieldMap.containsKey(key);
        return hasKey;
    }


    public static FixField getFieldDetail(String key, FixMap fixMap) {

        if (fixMap.hasTag(key)) {
            FixField field = fixMap.getFieldMap().get(key);

            FixField fld = new FixField(field);
            return fld;

        } else {
            FixField fld = new FixField(key);
            return fld;
        }
    }


}