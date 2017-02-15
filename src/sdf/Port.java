package sdf;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

public class Port {
    String type;
    String name;
    int rate;
    public Port(Element port) throws DataConversionException
    {
        type = port.getAttributeValue("type");
        name = port.getAttributeValue("name");
        rate = port.getAttribute("rate").getIntValue();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getRate() {
        return rate;
    }
    
}
