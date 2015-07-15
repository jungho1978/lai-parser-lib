package com.lge.lai.parser.manifest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.annotations.VisibleForTesting;
import com.lge.lai.common.data.Feature;
import com.lge.lai.common.data.FeatureProvider;
import com.lge.lai.common.db.LGAppInterfaceDB;
import com.lge.lai.common.db.LGAppInterfaceDBException;
import com.lge.lai.parser.constants.ManifestAttr;
import com.lge.lai.parser.report.Report;

public class ManifestParser implements ParserListener {
    public static final String CATEGORY = "manifest";
    private static final String CATEGORY_PREFIX = "manifest_";

    private Report report;
    boolean writeDB;

    public ManifestParser(Report report, boolean writeDB) {
        this.report = report;
        this.writeDB = writeDB;
    }
    
    public void parse(String filepath) throws Exception {
        FeatureProvider provider = new FeatureProvider();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(filepath);

        doc.getDocumentElement().normalize();
        Element manifest = doc.getDocumentElement();
        if (manifest.getNodeName().equals(ManifestAttr.MANIFEST)) {
            parseManifest(provider, manifest);
            provider.addAllFeatures(getFeature(manifest, ManifestAttr.ACTIVITY, this));
            provider.addAllFeatures(getFeature(manifest, ManifestAttr.ACTIVITY_ALIAS, this));
            provider.addAllFeatures(getFeature(manifest, ManifestAttr.SERVICE, this));
            provider.addAllFeatures(getFeature(manifest, ManifestAttr.RECEIVER, this));
            provider.addAllFeatures(getFeature(manifest, ManifestAttr.PROVIDER, this));
            System.out.println(provider);
        }
        
        if (writeDB) {
            writeDatabase(provider);
        }
    }

    private List<Feature> getFeature(Element manifest, String component, ParserListener listener) {
        List<Feature> features = new ArrayList<Feature>();
        NodeList list = manifest.getElementsByTagName(component);
        onStarted(component);
        for (int i = 0; i < list.getLength(); i++) {
            ComponentParser parser = ComponentParserFactory.create(component, (Element)list.item(i), listener);
            if (parser.validates()) {
                parser.parse();
                features.addAll(parser.getFeatures());
            }
        }
        return features;
    }

    private void parseManifest(FeatureProvider provider, Element element) {
        provider.packageName = element.getAttribute(ManifestAttr.PACKAGE);
        provider.versionName = element.getAttribute(ManifestAttr.VERSION);
    }

    @Override
    public void onStarted(String category) {
        category = CATEGORY_PREFIX + category;
        report.addCategory(category);
    }

    @Override
    public void onFound(String category, Object object) {
        try {
            category = CATEGORY_PREFIX + category;
            report.setData(category, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeDatabase(FeatureProvider newProvider) {
        LGAppInterfaceDB.find(newProvider.packageName, newProvider.versionName);
        LGAppInterfaceDB.delete(newProvider.packageName, newProvider.versionName);
        LGAppInterfaceDB.write(newProvider);
    }
}
