package com.lge.lai.parser.manifest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lge.lai.common.data.Feature;
import com.lge.lai.common.data.FeatureProvider;
import com.lge.lai.common.db.LGAppInterfaceDB;
import com.lge.lai.parser.ParserListener;
import com.lge.lai.parser.constants.Manifest;
import com.lge.lai.parser.report.Report;

public class ManifestParser implements ParserListener {
    static Logger LOGGER = LogManager.getLogger(ManifestParser.class.getName());

    public static final String CATEGORY = "manifest";
    public static final String CATEGORY_PREFIX = CATEGORY + "_";

    private Report report;
    boolean writeDB;

    public ManifestParser(Report report, boolean writeDB) {
        this.report = report;
        this.writeDB = writeDB;
    }

    public void parse(String filePath) {
        FeatureProvider fp = new FeatureProvider();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(filePath);
        } catch (Exception e) {
            LOGGER.error(e);
            return;
        }

        doc.getDocumentElement().normalize();
        Element manifest = doc.getDocumentElement();
        if (manifest.getNodeName().equals(Manifest.MANIFEST)) {
            parseManifest(fp, manifest);
            fp.addAllFeatures(getFeature(manifest, Manifest.ACTIVITY, this));
            fp.addAllFeatures(getFeature(manifest, Manifest.ACTIVITY_ALIAS, this));
            fp.addAllFeatures(getFeature(manifest, Manifest.SERVICE, this));
            fp.addAllFeatures(getFeature(manifest, Manifest.RECEIVER, this));
            fp.addAllFeatures(getFeature(manifest, Manifest.PROVIDER, this));
            System.out.println(fp);
        }

        if (writeDB) {
            writeDatabase(fp, filePath);
        }
    }

    private List<Feature> getFeature(Element manifest, String component, ParserListener listener) {
        List<Feature> features = new ArrayList<Feature>();
        NodeList list = manifest.getElementsByTagName(component);
        onStarted(component);
        for (int i = 0; i < list.getLength(); i++) {
            ComponentParser parser = ComponentParserFactory.create(component,
                    (Element)list.item(i), listener);
            if (parser.validates()) {
                parser.parse();
                features.addAll(parser.getFeatures());
            }
        }
        return features;
    }

    private void parseManifest(FeatureProvider provider, Element element) {
        provider.packageName = element.getAttribute(Manifest.PACKAGE);
        provider.versionName = element.getAttribute(Manifest.VERSION);
    }

    @Override
    public void onStarted(String category) {
        report.addCategory(CATEGORY_PREFIX + category);
    }

    @Override
    public void onFound(String category, Object object) {
        try {
            report.setData(CATEGORY_PREFIX + category, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeDatabase(FeatureProvider newProvider, String maniestPath) {
        LGAppInterfaceDB.find(newProvider.packageName, newProvider.versionName);
        LGAppInterfaceDB.delete(newProvider.packageName, newProvider.versionName);
        LGAppInterfaceDB.write(newProvider, maniestPath);
    }
}
