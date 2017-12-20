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
		cs2AttributeA.setPossibleValues(cs2AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs2AttributeB = new EntropyCloudServiceAttribute();
		cs2AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs2AttributeBpossibleValues = new ArrayList<String>();
		cs2AttributeBpossibleValues.add("at_most_1_working_day");
		cs2AttributeB.setPossibleValues(cs2AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs2AttributeC = new EntropyCloudServiceAttribute();
		cs2AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs2AttributeCpossibleValues = new ArrayList<String>();
		cs2AttributeCpossibleValues.add("Free of Charge");
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
		cs3AttributeApossibleValues.add("overwrite event as needed");
		cs3AttributeA.setPossibleValues(cs3AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs3AttributeB = new EntropyCloudServiceAttribute();
		cs3AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs3AttributeBpossibleValues = new ArrayList<String>();
		cs3AttributeBpossibleValues.add("at_most_1_working_day");
		cs3AttributeB.setPossibleValues(cs3AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs3AttributeC = new EntropyCloudServiceAttribute();
		cs3AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs3AttributeCpossibleValues = new ArrayList<String>();
		cs3AttributeCpossibleValues.add("Monthly Fee");
		cs3AttributeC.setPossibleValues(cs3AttributeCpossibleValues);
		
		
		
		ArrayList<EntropyCloudServiceAttribute> cs3AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs3AttributeList.add(cs3AttributeA);
		cs3AttributeList.add(cs3AttributeB);
		cs3AttributeList.add(cs3AttributeC);
		EntropyCloudService cs3= new EntropyCloudService();
		cs3.setId("service3");
		cs3.setAttributes(cs3AttributeList);
		attributeMap.add(cs3);
		

		
		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 4
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs4AttributeA = new EntropyCloudServiceAttribute();
		cs1AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
		
		ArrayList<String> cs4AttributeApossibleValues = new ArrayList<String>();
		cs4AttributeApossibleValues.add("overwrite event as needed");
		cs4AttributeA.setPossibleValues(cs4AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs4AttributeB = new EntropyCloudServiceAttribute();
		cs4AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs4AttributeBpossibleValues = new ArrayList<String>();
		cs4AttributeBpossibleValues.add("at_most_2_hours");
		cs4AttributeB.setPossibleValues(cs4AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs4AttributeC = new EntropyCloudServiceAttribute();
		cs4AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs4AttributeCpossibleValues = new ArrayList<String>();
		cs4AttributeCpossibleValues.add("Customizable Plan");
		cs4AttributeC.setPossibleValues(cs4AttributeCpossibleValues);
		
		
		
		ArrayList<EntropyCloudServiceAttribute> cs4AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs4AttributeList.add(cs4AttributeA);
		cs4AttributeList.add(cs4AttributeB);
		cs4AttributeList.add(cs4AttributeC);
		EntropyCloudService cs4= new EntropyCloudService();
		cs4.setId("service4");
		cs4.setAttributes(cs4AttributeList);
		attributeMap.add(cs4);
		
		
		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 5
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs5AttributeA = new EntropyCloudServiceAttribute();
		cs1AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
		
		ArrayList<String> cs5AttributeApossibleValues = new ArrayList<String>();
		cs5AttributeApossibleValues.add("archive the log when full");
		cs5AttributeA.setPossibleValues(cs5AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs5AttributeB = new EntropyCloudServiceAttribute();
		cs5AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs5AttributeBpossibleValues = new ArrayList<String>();
		cs5AttributeBpossibleValues.add("at_most_2_hours");
		cs5AttributeB.setPossibleValues(cs5AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs5AttributeC = new EntropyCloudServiceAttribute();
		cs5AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs5AttributeCpossibleValues = new ArrayList<String>();
		cs5AttributeCpossibleValues.add("Customizable Plan");
		cs5AttributeC.setPossibleValues(cs5AttributeCpossibleValues);
		
		
		
		ArrayList<EntropyCloudServiceAttribute> cs5AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs5AttributeList.add(cs5AttributeA);
		cs5AttributeList.add(cs5AttributeB);
		cs5AttributeList.add(cs5AttributeC);
		EntropyCloudService cs5= new EntropyCloudService();
		cs5.setId("service5");
		cs5.setAttributes(cs5AttributeList);
		attributeMap.add(cs5);
		
		
		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 6
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs6AttributeA = new EntropyCloudServiceAttribute();
		cs1AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
		
		ArrayList<String> cs6AttributeApossibleValues = new ArrayList<String>();
		cs6AttributeApossibleValues.add(" "); // TODO: TO be tested without any space inside/ With empty value
		cs6AttributeA.setPossibleValues(cs6AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs6AttributeB = new EntropyCloudServiceAttribute();
		cs6AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs6AttributeBpossibleValues = new ArrayList<String>();
		cs6AttributeBpossibleValues.add("at_most_4_hours");
		cs6AttributeB.setPossibleValues(cs6AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs6AttributeC = new EntropyCloudServiceAttribute();
		cs6AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs6AttributeCpossibleValues = new ArrayList<String>();
		cs6AttributeCpossibleValues.add("Customizable Plan");
		cs6AttributeC.setPossibleValues(cs6AttributeCpossibleValues);
		
		
		
		ArrayList<EntropyCloudServiceAttribute> cs6AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs6AttributeList.add(cs6AttributeA);
		cs6AttributeList.add(cs6AttributeB);
		cs6AttributeList.add(cs6AttributeC);
		EntropyCloudService cs6= new EntropyCloudService();
		cs6.setId("service6");
		cs6.setAttributes(cs6AttributeList);
		attributeMap.add(cs6);
		
		
		
		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 7
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs7AttributeA = new EntropyCloudServiceAttribute();
		cs1AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
		
		ArrayList<String> cs7AttributeApossibleValues = new ArrayList<String>();
		cs7AttributeApossibleValues.add("archive the log when full");
		cs7AttributeApossibleValues.add("do not overwrite event");
		cs4AttributeApossibleValues.add("overwrite event as needed");
		cs7AttributeA.setPossibleValues(cs7AttributeApossibleValues);
		
		EntropyCloudServiceAttribute cs7AttributeB = new EntropyCloudServiceAttribute();
		cs7AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
		
		ArrayList<String> cs7AttributeBpossibleValues = new ArrayList<String>();
		cs7AttributeBpossibleValues.add("at_most_1_working_day");
		cs7AttributeB.setPossibleValues(cs7AttributeBpossibleValues);
		
		EntropyCloudServiceAttribute cs7AttributeC = new EntropyCloudServiceAttribute();
		cs7AttributeC.setId("PAYMENT PLAN"); //attribute C
		
		ArrayList<String> cs7AttributeCpossibleValues = new ArrayList<String>();
		cs7AttributeCpossibleValues.add("Monthly Fee");
		cs7AttributeCpossibleValues.add("Customizable Plan");
		cs7AttributeC.setPossibleValues(cs7AttributeCpossibleValues);
		
		
		
		ArrayList<EntropyCloudServiceAttribute> cs7AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs7AttributeList.add(cs7AttributeA);
		cs7AttributeList.add(cs7AttributeB);
		cs7AttributeList.add(cs7AttributeC);
		EntropyCloudService cs7= new EntropyCloudService();
		cs7.setId("service7");
		cs7.setAttributes(cs7AttributeList);
		attributeMap.add(cs7);
		
		
		
		
		
		
		
		
		
		
		
		return attributeMap;
		
		
		
	}
	
	public static void main(String[] args) {
		EntropyMain qe= new EntropyMain();
		ArrayList<EntropyCloudService> attributeMap= qe.createTestAttributeMap();
		System.out.println(attributeMap.toString());
	}

}

