package com.lge.lai.parser.manifest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lge.lai.common.data.Feature;
import com.lge.lai.parser.constants.ManifestAttr;

public class ASBParser implements ComponentParser {
    private Element component;
    private ParserListener listener;
    private boolean hasIntentFilter;
    private List<Feature> features = new ArrayList<Feature>();

    public ASBParser(Element component, ParserListener listener) {
        this.component = component;
        this.listener = listener;
        this.hasIntentFilter = component.getElementsByTagName(ManifestAttr.INTENT_FILTER)
                .getLength() > 0;
    }

    @Override
    public boolean validates() {
        String exported = component.getAttribute(ManifestAttr.EXPORTED);
        if (hasIntentFilter && exported.equals("false")) {
            return false;
        } else if (!hasIntentFilter && !exported.equals("true")) {
            return false;
        }
        return true;
    }

    @Override
    public void parse() {
        if (hasIntentFilter) {
            NodeList actionNodes = component.getElementsByTagName(ManifestAttr.ACTION);
            for (int i = 0; i < actionNodes.getLength(); i++) {
                Feature feature = new Feature();
                parseComponent(feature, component);

                Node actionNode = actionNodes.item(i);
                parseAction(feature, (Element)actionNode);

                Node filterNode = actionNode.getParentNode();
                parseIntentFilter(feature, (Element)filterNode);

                features.add(feature);
                listener.onFound(component.getNodeName(), feature);
            }
        } else {
            Feature feature = new Feature();
            parseComponent(feature, component);
            features.add(feature);
            listener.onFound(component.getNodeName(), feature);
        }
    }

    private void parseComponent(Feature feature, Element element) {
        feature.type = element.getNodeName();
        feature.className = element.getAttribute(ManifestAttr.NAME);
    }

    private void parseAction(Feature feature, Element element) {
        feature.actionName = element.getAttribute(ManifestAttr.NAME);
    }

    private void parseIntentFilter(Feature feature, Element element) {
        NodeList categoryNodes = element.getElementsByTagName(ManifestAttr.CATEGORY);
        for (int i = 0; i < categoryNodes.getLength(); i++) {
            feature.addCategory(((Element)categoryNodes.item(i)).getAttribute(ManifestAttr.NAME));
        }

        NodeList dataNodes = element.getElementsByTagName(ManifestAttr.DATA);
        for (int i = 0; i < dataNodes.getLength(); i++) {
            parseData(feature, (Element)dataNodes.item(i));
        }
    }

    private void parseData(Feature feature, Element element) {
        if (element.hasAttribute(ManifestAttr.SCHEME)) {
            feature.addScheme(element.getAttribute(ManifestAttr.SCHEME));
        }
        if (element.hasAttribute(ManifestAttr.HOST)) {
            feature.addHost(element.getAttribute(ManifestAttr.HOST));
        }
        if (element.hasAttribute(ManifestAttr.PORT)) {
            feature.addPort(element.getAttribute(ManifestAttr.PORT));
        }
        if (element.hasAttribute(ManifestAttr.PATH)) {
            feature.addPath(element.getAttribute(ManifestAttr.PATH));
        }
        if (element.hasAttribute(ManifestAttr.PATH_PATTERN)) {
            feature.addPathPattern(element.getAttribute(ManifestAttr.PATH_PATTERN));
        }
        if (element.hasAttribute(ManifestAttr.PATH_PREFIX)) {
            feature.addPathPrefix(element.getAttribute(ManifestAttr.PATH_PREFIX));
        }
        if (element.hasAttribute(ManifestAttr.MIME_TYPE)) {
            feature.addMimeType(element.getAttribute(ManifestAttr.MIME_TYPE));
        }
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }
}
