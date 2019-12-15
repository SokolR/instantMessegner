package server.model;

import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XmlMessage {
    private static final Logger LOG = Logger.getLogger(XmlMessage.class);
    private static final String ROOT_ELEMENT = "XmlMessage";
    private static final String ID_USER = "IdUser";
    private static final String ELSE_PREFERENCE = "preference";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String MESSAGE = "message";
    private static final String DIALOG_ID = "dialogID";
    private static final String LIST_USER = "list_user";
    protected static DocumentBuilder builder;

    protected static  void paramLangXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOG.error("ParamLangXML err ", e);
        }
    }

    protected static void writeChild(Element RootElement, Document doc, String strTeg, String str ) {
        Element NameElementTitle = doc.createElement(strTeg);
        NameElementTitle.appendChild(doc.createTextNode(str));
        RootElement.appendChild(NameElementTitle);
    }

    public static void writeXMLinStream(XmlSet xmlSet, OutputStream out) throws TransformerException {

        paramLangXML();

        Document doc         = builder.newDocument();
        Element  RootElement = doc.createElement(ROOT_ELEMENT);
        writeChild(RootElement, doc, ID_USER, String.valueOf(xmlSet.getIdUser()));

        if (xmlSet.getKeyDialog() != 0) {
            writeChild(RootElement, doc, DIALOG_ID, String.valueOf(xmlSet.getKeyDialog()));
        }
        if (xmlSet.getMessage() != null) {
            writeChild(RootElement, doc, MESSAGE, xmlSet.getMessage());
        }

        if (xmlSet.getList() != null) {
            Element      elist;
            Integer      count = 1;
            List<String> list  = xmlSet.getList();

            for (String name : list) {
                elist = doc.createElement(LIST_USER);
                RootElement.appendChild(elist);

                elist.setAttribute(ID, count.toString());
                writeChild(elist, doc, NAME, name);
                count++;
            }
        }

        if (xmlSet.getProperty() != null) {
            writeChild(RootElement, doc, ELSE_PREFERENCE, xmlSet.getProperty());
        }

        doc.appendChild(RootElement);
        Transformer t =  TransformerFactory.newInstance().newTransformer();

        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        t.transform(new DOMSource(doc), new StreamResult(out));
    }

    protected static String readChild(Document document, String strTeg) {
        NodeList nList = document.getElementsByTagName(strTeg);
        Node     node  = nList.item(0);
        return node == null ? null : node.getTextContent();
    }

    public static  XmlSet readXmlFromStream(InputStream in) throws IOException, SAXException {
        XmlSet xmlSet = new XmlSet(-1);

        paramLangXML();
        Document document;
        document = builder.parse(in);
        document.getDocumentElement().normalize();

        String result;

        if ((result = readChild(document, ID_USER)) != null) {
            xmlSet.setIdUser(Long.parseLong(result));
        }

        try {
            if ((result = readChild(document, MESSAGE)) != null) {
                    xmlSet.setMessage(result);
            }

            if ((result = readChild(document, DIALOG_ID)) != null) {
                    xmlSet.setKeyDialog(Integer.parseInt(result));
            }
        } catch (Exception e){
            LOG.error("messageID and ID ", e);
            LOG.debug("message, ID " + Arrays.toString(e.getStackTrace()));
        }

        List<String> list  = new ArrayList<>();
        NodeList     nList = document.getElementsByTagName(LIST_USER);

        if (nList != null) {
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    list.add(eElement.getElementsByTagName(NAME).item(0).getTextContent());
                }
            }
            xmlSet.setList(list);
        }

        try {
            if ((result = readChild(document, ELSE_PREFERENCE)) != null ){
                xmlSet.setProperty(result);
            }
        } catch (Exception e){
            LOG.debug("else_preference ", e);
        }

        if(xmlSet.getIdUser() != -1) {
            return xmlSet;
        } else {
            return null;
        }
    }
}
