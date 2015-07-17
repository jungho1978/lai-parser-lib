package com.lge.lai.parser.manifest;

import org.w3c.dom.Element;

import com.lge.lai.parser.ParserListener;
import com.lge.lai.parser.constants.Manifest;

public class ComponentParserFactory {
    public static ComponentParser create(String type, Element element, ParserListener listener) {
        if (type.equals(Manifest.PROVIDER)) {
            return new ProviderParser(element, listener);
        } else {
            return new ASBParser(element, listener);
        }
    }
}