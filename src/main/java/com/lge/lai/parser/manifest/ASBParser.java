package com.lge.lai.parser.manifest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lge.lai.common.data.Feature;
import com.lge.lai.parser.ParserListener;
import com.lge.lai.parser.constants.Manifest;

public class ASBParser implements ComponentParser {
    private Element component;
    private ParserListener listener;
    private boolean hasIntentFilter;
    private List<Feature> features = new ArrayList<Feature>();

    public ASBParser(Element component, ParserListener listener) {
        this.component = component;
        this.listener = listener;
        this.hasIntentFilter = component.getElementsByTagName(Manifest.INTENT_FILTER)
                .getLength() > 0;
    }

    @Override
    public boolean validates() {
        String exported = component.getAttribute(Manifest.EXPORTED);
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
            NodeList actionNodes = component.getElementsByTagName(Manifest.ACTION);
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
        feature.className = element.getAttribute(Manifest.NAME);
    }

    private void parseAction(Feature feature, Element element) {
        feature.actionName = element.getAttribute(Manifest.NAME);
    }

    private void parseIntentFilter(Feature feature, Element element) {
        NodeList categoryNodes = element.getElementsByTagName(Manifest.CATEGORY);
        for (int i = 0; i < categoryNodes.getLength(); i++) {
            feature.addCategory(((Element)categoryNodes.item(i)).getAttribute(Manifest.NAME));
        }

        NodeList dataNodes = element.getElementsByTagName(Manifest.DATA);
        for (int i = 0; i < dataNodes.getLength(); i++) {
            parseData(feature, (Element)dataNodes.item(i));
        }
    }

    private void parseData(Feature feature, Element element) {
        if (element.hasAttribute(Manifest.SCHEME)) {
            feature.addScheme(element.getAttribute(Manifest.SCHEME));
        }
        if (element.hasAttribute(Manifest.HOST)) {
            feature.addHost(element.getAttribute(Manifest.HOST));
        }
        if (element.hasAttribute(Manifest.PORT)) {
            feature.addPort(element.getAttribute(Manifest.PORT));
        }
        if (element.hasAttribute(Manifest.PATH)) {
            feature.addPath(element.getAttribute(Manifest.PATH));
        }
        if (element.hasAttribute(Manifest.PATH_PATTERN)) {
            feature.addPathPattern(element.getAttribute(Manifest.PATH_PATTERN));
        }
        if (element.hasAttribute(Manifest.PATH_PREFIX)) {
            feature.addPathPrefix(element.getAttribute(Manifest.PATH_PREFIX));
        }
        if (element.hasAttribute(Manifest.MIME_TYPE)) {
            feature.addMimeType(element.getAttribute(Manifest.MIME_TYPE));
        }
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }
}
