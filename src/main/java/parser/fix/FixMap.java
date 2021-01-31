package parser.fix;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

        File xmlFile = new File(this.filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
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


    private void addField(Node node) {

        FixField field = new FixField();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String t = element.getAttribute("number");
            String n = element.getAttribute("name");
            field.setTag(t);
            field.setName(n);

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

            this.fieldMap.put(t, field);
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