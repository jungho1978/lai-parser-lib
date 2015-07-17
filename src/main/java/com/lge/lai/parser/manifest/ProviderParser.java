package com.lge.lai.parser.manifest;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.lge.lai.common.data.Feature;
import com.lge.lai.common.db.dao.BaseDAO;
import com.lge.lai.parser.ParserListener;
import com.lge.lai.parser.constants.Manifest;

public class ProviderParser implements ComponentParser {
    static Logger LOGGER = LogManager.getLogger(BaseDAO.class.getName());
    
    private Element component;
    private ParserListener listener;
    private List<Feature> features = new ArrayList<Feature>();

    public ProviderParser(Element component, ParserListener listener) {
        this.component = component;
        this.listener = listener;
    }

    @Override
    public boolean validates() {
        String exported = component.getAttribute(Manifest.EXPORTED);
        if (!exported.equals("true")) {
            return false;
        }
        return true;
    }

    @Override
    public void parse() {
        Feature feature = new Feature();
        feature.type = component.getNodeName();
        feature.className = component.getAttribute(Manifest.NAME);
        feature.authorities = component.getAttribute(Manifest.AUTHORITIES);
        feature.readPermission = component.getAttribute(Manifest.READ_PERMISSION);
        feature.writePermission = component.getAttribute(Manifest.WRITE_PERMISSION);
        features.add(feature);
        listener.onFound(component.getNodeName(), feature);
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }
}
