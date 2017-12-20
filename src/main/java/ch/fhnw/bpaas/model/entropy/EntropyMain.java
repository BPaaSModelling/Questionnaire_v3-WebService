package ch.fhnw.bpaas.model.entropy;

import java.util.ArrayList;



public class EntropyMain {

	private ArrayList<EntropyCloudService> createTestAttributeMap() {
		
		
		ArrayList<EntropyCloudService> attributeMap = new ArrayList<EntropyCloudService>() ;
		
		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 1
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs1AttributeA = new EntropyCloudServiceAttribute();
		cs1AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
		
		ArrayList<String> cs1AttributeApossibleValues = new ArrayList<String>();
		cs1AttributeApossibleValues.add("archive the log when full");
		cs1AttributeApossibleValues.add("do not overwrite event");
		cs1AttributeA.setPossibleValues(cs1AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs1AttributeB = new EntropyCloudServiceAttribute();
		cs1AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs1AttributeBpossibleValues = new ArrayList<String>();
		cs1AttributeBpossibleValues.add("at_most_1_working_day");
		cs1AttributeB.setPossibleValues(cs1AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs1AttributeC = new EntropyCloudServiceAttribute();
		cs1AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs1AttributeCpossibleValues = new ArrayList<String>();
		cs1AttributeCpossibleValues.add("Customizable Plan");
		cs1AttributeC.setPossibleValues(cs1AttributeCpossibleValues);
		
		
		ArrayList<EntropyCloudServiceAttribute> cs1AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs1AttributeList.add(cs1AttributeA);
		cs1AttributeList.add(cs1AttributeB);
		cs1AttributeList.add(cs1AttributeC);
		EntropyCloudService cs1= new EntropyCloudService();
		cs1.setId("service1");
		cs1.setAttributes(cs1AttributeList);
		attributeMap.add(cs1);
		
		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 2
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs2AttributeA = new EntropyCloudServiceAttribute();
		cs1AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
		
		ArrayList<String> cs2AttributeApossibleValues = new ArrayList<String>();
		cs2AttributeApossibleValues.add("archive the log when full");
		cs2AttributeApossibleValues.add("do not overwrite event");
		cs2AttributeA.setPossibleValues(cs2AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs2AttributeB = new EntropyCloudServiceAttribute();
		cs2AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs2AttributeBpossibleValues = new ArrayList<String>();
		cs2AttributeBpossibleValues.add("at_most_1_working_day");
		cs2AttributeB.setPossibleValues(cs2AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs2AttributeC = new EntropyCloudServiceAttribute();
		cs2AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs2AttributeCpossibleValues = new ArrayList<String>();
		cs2AttributeCpossibleValues.add("Customizable Plan");
		cs2AttributeC.setPossibleValues(cs2AttributeCpossibleValues);
		
		
		ArrayList<EntropyCloudServiceAttribute> cs2AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs2AttributeList.add(cs2AttributeA);
		cs2AttributeList.add(cs2AttributeB);
		cs2AttributeList.add(cs2AttributeC);
		EntropyCloudService cs2= new EntropyCloudService();
		cs2.setId("service2");
		cs2.setAttributes(cs2AttributeList);
		attributeMap.add(cs2);
		
		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 3
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs3AttributeA = new EntropyCloudServiceAttribute();
		cs1AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
		
		ArrayList<String> cs3AttributeApossibleValues = new ArrayList<String>();
		cs3AttributeApossibleValues.add("archive the log when full");
		cs3AttributeApossibleValues.add("do not overwrite event");
		cs3AttributeA.setPossibleValues(cs3AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs3AttributeB = new EntropyCloudServiceAttribute();
		cs3AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs3AttributeBpossibleValues = new ArrayList<String>();
		cs3AttributeBpossibleValues.add("at_most_1_working_day");
		cs3AttributeB.setPossibleValues(cs3AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs3AttributeC = new EntropyCloudServiceAttribute();
		cs3AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs3AttributeCpossibleValues = new ArrayList<String>();
		cs3AttributeCpossibleValues.add("Customizable Plan");
		cs3AttributeC.setPossibleValues(cs3AttributeCpossibleValues);
		
		
		ArrayList<EntropyCloudServiceAttribute> cs3AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs3AttributeList.add(cs3AttributeA);
		cs3AttributeList.add(cs3AttributeB);
		cs3AttributeList.add(cs3AttributeC);
		EntropyCloudService cs3= new EntropyCloudService();
		cs3.setId("service3");
		cs3.setAttributes(cs3AttributeList);
		attributeMap.add(cs3);
		
		
		
		return attributeMap;
		
		
		
	}
	
	public static void main(String[] args) {
		EntropyMain qe= new EntropyMain();
		ArrayList<EntropyCloudService> attributeMap= qe.createTestAttributeMap();
		System.out.println(attributeMap.toString());
	}

}

