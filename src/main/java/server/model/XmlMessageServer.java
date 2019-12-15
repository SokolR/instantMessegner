package server.model;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class XmlMessageServer extends XmlMessage {
    private static final int DEFAULTPORT = 1506;
    private static final String NAMEOFFILE = "MessengerConf.xml";
    private static final String ROOTNAME = "Properties";
    private static final String INCLUDLOG = "logger";
    private static final String LEVELLOG = "levelLogger";
    private static final String NAMEPORT = "Port";
    private static final Logger LOG = Logger.getLogger(XmlMessageServer.class);

    private static ConfigParameters setDefaultParameters() {
        ConfigParameters conf = new ConfigParameters();
        conf.setPort(DEFAULTPORT);
        conf.setLog(true);
        conf.setLevelLog(String.valueOf(LogManager.getRootLogger().getLevel()));

        return conf;
    }

    private static ConfigParameters setDefaultParameters(ConfigParameters conf, String command) {
        if (command.equals(NAMEPORT)) {
            conf.setPort(DEFAULTPORT);
        }

        if (command.equals(LEVELLOG)) {
            conf.setLevelLog(String.valueOf(LogManager.getRootLogger().getLevel()));
        }

        return conf;
    }

    protected synchronized static ConfigParameters loadProperties() {
        boolean notFoundNode = false;
        paramLangXML();
        Document document = null;
        ConfigParameters conf = setDefaultParameters();

        try {
            document = builder.parse(new File(NAMEOFFILE));
        } catch (IOException | SAXException e) {
            try {
                LOG.error("Configuration file do not have required parameters, or file did not find. " +
                        "Write default parameters.");
                writeProperties(conf);
                return  conf;
            } catch (TransformerException | FileNotFoundException e1) {
                LOG.error(e1);
            }
        }

        if (document != null) {
            document.getDocumentElement().normalize();
        }

        String log;
        if ((log = readChild(document, INCLUDLOG)) != null) {
            if (log.equalsIgnoreCase("true")) {
                conf.setLog(true);
                String level;

                if ((level = readChild(document, LEVELLOG)) != null) {
                    if (setLevelLog(level)) {
                        conf.setLevelLog(level);
                    }
                } else {
                    LOG.info("Configure file do not have '" + LEVELLOG + "', it will add with default value.");
                    conf = setDefaultParameters(conf, LEVELLOG);
                    notFoundNode = true;
                }
            } else {
                if (log.equalsIgnoreCase("false")) {
                    conf.setLog(false);
                    LogManager.getRootLogger().setLevel(Level.OFF);
                } else {
                    LOG.error("MessengerConf.xml in 'logger' has mistake, it must be 'true' or 'false'. " +
                            "Use default parameter.");
                }
            }
        } else {
            LOG.info("Configure file do not have '" + INCLUDLOG + "', it will add with default value.");
            conf = setDefaultParameters(conf, LEVELLOG);
            notFoundNode = true;
        }

        String parameter;

        if ((parameter = readChild(document, NAMEPORT)) != null) {
            try {
                int port = Integer.parseInt(parameter);
                if (port  < 1024) {
                    throw null;
                }

                conf.setPort(port);
            } catch (NullPointerException | NumberFormatException e) {
                LOG.error("MessengerConf.xml in 'Port' has mistake, it must be number, and > 0.");
                conf.setPort(DEFAULTPORT);
            }
        } else {
            LOG.error("Configure file do not have 'PORT', it will add with default value " + DEFAULTPORT + ".");
            conf = setDefaultParameters(conf, NAMEPORT);
            notFoundNode = true;
        }

        if (notFoundNode) {
            try {
                writeProperties(conf);
            } catch (TransformerException | FileNotFoundException e) {
                LOG.error(e);
            }
        }

        return conf;
    }

    private static boolean setLevelLog(String level) {
        switch (level.toLowerCase()) {
            case "trace": LogManager.getRootLogger().setLevel(Level.TRACE);
                break;
            case "debug": LogManager.getRootLogger().setLevel(Level.DEBUG);
                break;
            case "info":  LogManager.getRootLogger().setLevel(Level.INFO);
                break;
            case "warn":  LogManager.getRootLogger().setLevel(Level.WARN);
                break;
            case "error": LogManager.getRootLogger().setLevel(Level.ERROR);
                break;
            case "fatal": LogManager.getRootLogger().setLevel(Level.FATAL);
                break;
            default:
                LOG.error("MessengerConf.xml in 'levelLogger' has mistakes. Use default parameter.");
                return false;
        }
        return true;
    }

    protected synchronized static void writeProperties(ConfigParameters conf) throws TransformerException, FileNotFoundException {
        paramLangXML();
        Document doc = builder.newDocument();
        Element RootElement = doc.createElement(ROOTNAME);

        writeChild(RootElement, doc, INCLUDLOG, String.valueOf(conf.isLog()));

        writeChild(RootElement, doc, LEVELLOG, conf.getLevelLog());

        writeChild(RootElement, doc, NAMEPORT, String.valueOf(conf.getPort()));

        doc.appendChild(RootElement);
        Transformer t=  TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(NAMEOFFILE)));
    }

}
