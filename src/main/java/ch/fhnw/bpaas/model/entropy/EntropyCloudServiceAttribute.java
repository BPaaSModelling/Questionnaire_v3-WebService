package ch.fhnw.bpaas.model.entropy;

import java.util.ArrayList;

public class EntropyCloudServiceAttribute {
private String id;
private ArrayList<String> possibleValues;
private String annotationRelation;
public EntropyCloudServiceAttribute() {
	this.possibleValues = new ArrayList<String>();
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public ArrayList<String> getPossibleValues() {
	return possibleValues;
}
public void setPossibleValues(ArrayList<String> possibleValues) {
	this.possibleValues = possibleValues;
}
public String getAnnotationRelation() {
	return annotationRelation;
}
public void setAnnotationRelation(String annotationRelation) {
	this.annotationRelation = annotationRelation;
}


}
