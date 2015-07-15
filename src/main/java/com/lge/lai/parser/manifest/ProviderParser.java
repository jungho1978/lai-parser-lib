package com.lge.lai.parser.manifest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.lge.lai.common.data.Feature;
import com.lge.lai.parser.constants.ManifestAttr;

public class ProviderParser implements ComponentParser {
    private Element component;
    private ParserListener listener;
    private List<Feature> features = new ArrayList<Feature>();

    public ProviderParser(Element component, ParserListener listener) {
        this.component = component;
        this.listener = listener;
    }

    @Override
    public boolean validates() {
        String exported = component.getAttribute(ManifestAttr.EXPORTED);
        if (!exported.equals("true")) {
            return false;
        }
        return true;
    }

    @Override
    public void parse() {
        Feature feature = new Feature();
        feature.type = component.getNodeName();
        feature.className = component.getAttribute(ManifestAttr.NAME);
        feature.authorities = component.getAttribute(ManifestAttr.AUTHORITIES);
        feature.readPermission = component.getAttribute(ManifestAttr.READ_PERMISSION);
        feature.writePermission = component.getAttribute(ManifestAttr.WRITE_PERMISSION);
        features.add(feature);
        listener.onFound(component.getNodeName(), feature);
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }
}
