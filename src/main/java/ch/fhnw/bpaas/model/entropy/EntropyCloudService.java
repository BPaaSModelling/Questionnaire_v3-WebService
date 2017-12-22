package ch.fhnw.bpaas.model.entropy;

import java.util.ArrayList;

public class EntropyCloudService {
	
private String id;
private String label;
private ArrayList<EntropyCloudServiceAttribute> attributes;

public EntropyCloudService() {
	this.attributes = new ArrayList<EntropyCloudServiceAttribute>();
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getLabel() {
	return label;
}

public void setLabel(String label) {
	this.label = label;
}

public ArrayList<EntropyCloudServiceAttribute> getAttributes() {
	return attributes;
}

public void setAttributes(ArrayList<EntropyCloudServiceAttribute> attributes) {
	this.attributes = attributes;
}

@Override
public String toString() {
	return "\nCS[id=" + id + ", label=" + label + ", attributes="+"\n" + attributes + "]";
}


}
