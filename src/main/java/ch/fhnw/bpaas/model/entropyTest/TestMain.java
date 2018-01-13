package ch.fhnw.bpaas.model.entropyTest;
import java.util.ArrayList;
import java.util.HashMap;
import ch.fhnw.bpaas.model.questionnaire.*;
import ch.fhnw.bpaas.model.entropy.EntropyCloudService;
import ch.fhnw.bpaas.model.entropy.EntropyCloudServiceAttribute;
import ch.fhnw.bpaas.webservice.*;
import ch.fhnw.bpaas.webservice.exceptions.NoResultsException;


public class TestMain {

	public static void main(String[] args) throws NoResultsException {


		// GENERATING LOCAL TEST DATA
		Questionnaire qm1= new Questionnaire();
		ArrayList<EntropyCloudService> ecss = createTestAttributeMap();
		HashMap<String, HashMap<String, Integer>> attributeMap= getAttributeMap(ecss);
		ArrayList<String> oldQuestions=new ArrayList<String>();
		String q1= "";
		oldQuestions.add(q1);
		String maxEntropyAttribute= "";

		//CREATING ENTROPY MAP
		HashMap<String, Float> entropyMap = qm1.getEntropyMap(attributeMap, ecss.size());
		System.out.println("\n|-----------------------------------------------------------|");
		System.out.println("Entropy Map"+ entropyMap);
		System.out.println("-----------------------------------------------------------|\n");
		
		//GETTING MAX ATTRIBUTE OUT OF THE ENTROPY MAP, ESCLUDING OLD ANSWERED QUESTIONS' ATTRIBUTE
		maxEntropyAttribute = qm1.getMaxEntropyAttribute(entropyMap, oldQuestions );
			
	}

	public static ArrayList<EntropyCloudService> createTestAttributeMap() {


		ArrayList<EntropyCloudService> attributeMap = new ArrayList<EntropyCloudService>() ;

		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 1
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs1AttributeA = new EntropyCloudServiceAttribute();
		cs1AttributeA.setId("Log file retention policy"); //attribute A

		ArrayList<String> cs1AttributeApossibleValues = new ArrayList<String>();
		cs1AttributeApossibleValues.add("archive the log when full");
		cs1AttributeApossibleValues.add("do not overwrite event");
		cs1AttributeA.setValues(cs1AttributeApossibleValues);

		EntropyCloudServiceAttribute cs1AttributeB = new EntropyCloudServiceAttribute();
		cs1AttributeB.setId("ServiceSupportResponsiveness"); //attribute B

		ArrayList<String> cs1AttributeBpossibleValues = new ArrayList<String>();
		cs1AttributeBpossibleValues.add("at_most_1_working_day");
		cs1AttributeB.setValues(cs1AttributeBpossibleValues);

		EntropyCloudServiceAttribute cs1AttributeC = new EntropyCloudServiceAttribute();
		cs1AttributeC.setId("PAYMENT PLAN"); //attribute C

		ArrayList<String> cs1AttributeCpossibleValues = new ArrayList<String>();
		cs1AttributeCpossibleValues.add("Customizable Plan");
		cs1AttributeC.setValues(cs1AttributeCpossibleValues);


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
		cs2AttributeA.setId("Log file retention policy"); //attribute A

		ArrayList<String> cs2AttributeApossibleValues = new ArrayList<String>();
		cs2AttributeApossibleValues.add("archive the log when full");
		cs2AttributeA.setValues(cs2AttributeApossibleValues);

		EntropyCloudServiceAttribute cs2AttributeB = new EntropyCloudServiceAttribute();
		cs2AttributeB.setId("ServiceSupportResponsiveness"); //attribute B

		ArrayList<String> cs2AttributeBpossibleValues = new ArrayList<String>();
		cs2AttributeBpossibleValues.add("at_most_1_working_day");
		cs2AttributeB.setValues(cs2AttributeBpossibleValues);

		EntropyCloudServiceAttribute cs2AttributeC = new EntropyCloudServiceAttribute();
		cs2AttributeC.setId("PAYMENT PLAN"); //attribute C

		ArrayList<String> cs2AttributeCpossibleValues = new ArrayList<String>();
		cs2AttributeCpossibleValues.add("Free of Charge");
		cs2AttributeC.setValues(cs2AttributeCpossibleValues);


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
		cs3AttributeA.setId("Log file retention policy"); //attribute A

		ArrayList<String> cs3AttributeApossibleValues = new ArrayList<String>();
		cs3AttributeApossibleValues.add("overwrite event as needed");
		cs3AttributeA.setValues(cs3AttributeApossibleValues);

		EntropyCloudServiceAttribute cs3AttributeB = new EntropyCloudServiceAttribute();
		cs3AttributeB.setId("ServiceSupportResponsiveness"); //attribute B

		ArrayList<String> cs3AttributeBpossibleValues = new ArrayList<String>();
		cs3AttributeBpossibleValues.add("at_most_1_working_day");
		cs3AttributeB.setValues(cs3AttributeBpossibleValues);

		EntropyCloudServiceAttribute cs3AttributeC = new EntropyCloudServiceAttribute();
		cs3AttributeC.setId("PAYMENT PLAN"); //attribute C

		ArrayList<String> cs3AttributeCpossibleValues = new ArrayList<String>();
		cs3AttributeCpossibleValues.add("Monthly Fee");
		cs3AttributeC.setValues(cs3AttributeCpossibleValues);



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
		cs4AttributeA.setId("Log file retention policy"); //attribute A

		ArrayList<String> cs4AttributeApossibleValues = new ArrayList<String>();
		cs4AttributeApossibleValues.add("overwrite event as needed");
		cs4AttributeA.setValues(cs4AttributeApossibleValues);

		EntropyCloudServiceAttribute cs4AttributeB = new EntropyCloudServiceAttribute();
		cs4AttributeB.setId("ServiceSupportResponsiveness"); //attribute B

		ArrayList<String> cs4AttributeBpossibleValues = new ArrayList<String>();
		cs4AttributeBpossibleValues.add("at_most_2_hours");
		cs4AttributeB.setValues(cs4AttributeBpossibleValues);

		EntropyCloudServiceAttribute cs4AttributeC = new EntropyCloudServiceAttribute();
		cs4AttributeC.setId("PAYMENT PLAN"); //attribute C

		ArrayList<String> cs4AttributeCpossibleValues = new ArrayList<String>();
		cs4AttributeCpossibleValues.add("Customizable Plan");
		cs4AttributeC.setValues(cs4AttributeCpossibleValues);



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
		cs5AttributeA.setId("Log file retention policy"); //attribute A

		ArrayList<String> cs5AttributeApossibleValues = new ArrayList<String>();
		cs5AttributeApossibleValues.add("archive the log when full");
		cs5AttributeA.setValues(cs5AttributeApossibleValues);

		EntropyCloudServiceAttribute cs5AttributeB = new EntropyCloudServiceAttribute();
		cs5AttributeB.setId("ServiceSupportResponsiveness"); //attribute B

		ArrayList<String> cs5AttributeBpossibleValues = new ArrayList<String>();
		cs5AttributeBpossibleValues.add("at_most_2_hours");
		cs5AttributeB.setValues(cs5AttributeBpossibleValues);

		EntropyCloudServiceAttribute cs5AttributeC = new EntropyCloudServiceAttribute();
		cs5AttributeC.setId("PAYMENT PLAN"); //attribute C

		ArrayList<String> cs5AttributeCpossibleValues = new ArrayList<String>();
		cs5AttributeCpossibleValues.add("Customizable Plan");
		cs5AttributeC.setValues(cs5AttributeCpossibleValues);



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
		cs6AttributeA.setId("Log file retention policy"); //attribute A

		ArrayList<String> cs6AttributeApossibleValues = new ArrayList<String>();
		cs6AttributeApossibleValues.add(" "); // 
		cs6AttributeA.setValues(cs6AttributeApossibleValues);

		EntropyCloudServiceAttribute cs6AttributeB = new EntropyCloudServiceAttribute();
		cs6AttributeB.setId("ServiceSupportResponsiveness"); //attribute B

		ArrayList<String> cs6AttributeBpossibleValues = new ArrayList<String>();
		cs6AttributeBpossibleValues.add("at_most_4_hours");
		cs6AttributeB.setValues(cs6AttributeBpossibleValues);

		EntropyCloudServiceAttribute cs6AttributeC = new EntropyCloudServiceAttribute();
		cs6AttributeC.setId("PAYMENT PLAN"); //attribute C

		ArrayList<String> cs6AttributeCpossibleValues = new ArrayList<String>();
		cs6AttributeCpossibleValues.add("Customizable Plan");
		cs6AttributeC.setValues(cs6AttributeCpossibleValues);



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
		cs7AttributeA.setId("Log file retention policy"); //attribute A

		ArrayList<String> cs7AttributeApossibleValues = new ArrayList<String>();
		cs7AttributeApossibleValues.add("archive the log when full");
		cs7AttributeApossibleValues.add("do not overwrite event");
		cs4AttributeApossibleValues.add("overwrite event as needed");
		cs7AttributeA.setValues(cs7AttributeApossibleValues);

		EntropyCloudServiceAttribute cs7AttributeB = new EntropyCloudServiceAttribute();
		cs7AttributeB.setId("ServiceSupportResponsiveness"); //attribute B

		ArrayList<String> cs7AttributeBpossibleValues = new ArrayList<String>();
		cs7AttributeBpossibleValues.add("at_most_1_working_day");
		cs7AttributeB.setValues(cs7AttributeBpossibleValues);

		EntropyCloudServiceAttribute cs7AttributeC = new EntropyCloudServiceAttribute();
		cs7AttributeC.setId("PAYMENT PLAN"); //attribute C

		ArrayList<String> cs7AttributeCpossibleValues = new ArrayList<String>();
		cs7AttributeCpossibleValues.add("Monthly Fee");
		cs7AttributeCpossibleValues.add("Customizable Plan");
		cs7AttributeC.setValues(cs7AttributeCpossibleValues);



		ArrayList<EntropyCloudServiceAttribute> cs7AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs7AttributeList.add(cs7AttributeA);
		cs7AttributeList.add(cs7AttributeB);
		cs7AttributeList.add(cs7AttributeC);
		EntropyCloudService cs7= new EntropyCloudService();
		cs7.setId("service7");
		cs7.setAttributes(cs7AttributeList);
		attributeMap.add(cs7);


		//-----------------------------------------------------------------------
		//------------ CLOUD SERVICE 8
		//-----------------------------------------------------------------------
		EntropyCloudServiceAttribute cs8AttributeA = new EntropyCloudServiceAttribute();
		cs8AttributeA.setId("http://ikm-group.ch/archimeo/bpaas#cloudServiceHasTargetMarket"); //attribute A

		ArrayList<String> cs8AttributeApossibleValues = new ArrayList<String>();
		cs8AttributeApossibleValues.add("http://ikm-group.ch/archimeo/bpaas#Businesses"); 
		cs8AttributeA.setValues(cs8AttributeApossibleValues);


		ArrayList<EntropyCloudServiceAttribute> cs8AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
		cs8AttributeList.add(cs8AttributeA);
		EntropyCloudService cs8= new EntropyCloudService();
		cs8.setId("service6");
		cs8.setAttributes(cs8AttributeList);
		attributeMap.add(cs8);

//		System.out.println("\n|-----------------------------------------------------------:");
//		System.out.println("|     attribute 8 ");
//		System.out.println("|     "+cs8.toString());
//		System.out.println("|-----------------------------------------------------------\n:");	




		System.out.println("\n|-----------------------------------------------------------:");
		System.out.println("|     attributeMap generated by createTestAttributeMap() ");
		System.out.println("|-----------------------------------------------------------\n:");	
		System.out.println(attributeMap.toString());
		return attributeMap;


	}

	public static HashMap<String, HashMap<String, Integer>> getAttributeMap(ArrayList<EntropyCloudService> ecss) throws NoResultsException {

		HashMap<String, HashMap<String, Integer>> attributeValueListAndEntropyTotal = new HashMap<String, HashMap<String, Integer>>();

		Integer csCount=ecss.size();
		
		System.out.println("\n\nCount of CloudServices: "+ csCount.toString());

		for (int i = 0; i < csCount; i++) {
			ArrayList<EntropyCloudServiceAttribute> attributeListI = ecss.get(i).getAttributes();
			
			System.out.println("\ncloud service n"+(i+1) +"    id): "+ ecss.get(i).getId().toString()+ ", it contains: ");
			System.out.println(ecss.get(i).getAttributes().toString());
			Integer attributesCount = ecss.get(i).getAttributes().size(); 

			for (int j = 0; j < attributesCount; j++) {
				//				System.out.println("attributes scanned ["+ (j+1) +"/"+ attributesCount);


				EntropyCloudServiceAttribute attributeJ = attributeListI.get(j);
				//					System.out.println("     attributeJ: "+ attributeJ.toString());
				ArrayList<String> possibleValueList = attributeJ.getValues();
				//					System.out.println("     value: "+ possibleValueList.toString());

				//					System.out.println("          attributeValueListAndEntropyTotal.containsKey("+attributeJ.getId()+") "+ attributeValueListAndEntropyTotal.containsKey(attributeJ.getId()));
				if (!attributeValueListAndEntropyTotal.containsKey(attributeJ.getId())) {

					HashMap<String, Integer> attributeJmap = new HashMap<String, Integer>();

					for (int k = 0; k < possibleValueList.size(); k++) {
						//	System.out.println("          k: "+ k);

						String possibleValueK= possibleValueList.get(k);


						System.out.println("          Value "+ k + ":"+ possibleValueK);
						System.out.println("         	 attributeJmap: "+ attributeJmap.toString());
						System.out.println("                  attributeJmap.containsKey("+possibleValueK+"): "+ attributeJmap.containsKey(possibleValueK));
						if (!attributeJmap.containsKey(possibleValueK)) {
							//	System.out.println("                         adding "+possibleValueK);			
							attributeJmap.put(possibleValueK, 1);
							//								System.out.println("          attributeJmap: "+ attributeJmap.toString());
						} //for every new possible value K starts his count to 0


						attributeValueListAndEntropyTotal.put(attributeJ.getId(), attributeJmap);
						System.out.println("                           attributeValueListAndEntropyTotal.toString()"+attributeValueListAndEntropyTotal.toString());
					}
				} else {

					HashMap<String, Integer> attributeJmap = attributeValueListAndEntropyTotal.get(attributeJ.getId());
					//						System.out.println("          attributeValueListAndEntropyTotal.get(attributeJ) "+ attributeValueListAndEntropyTotal.get(attributeJ));
					for (int k = 0; k < possibleValueList.size(); k++) {
						String possibleValueK= possibleValueList.get(k);

						System.out.println("          Value "+ k + ":"+ possibleValueK);
						System.out.println("         	 attributeJmap: "+ attributeJmap.toString());

						if (!attributeJmap.containsKey(possibleValueK)) {
							attributeJmap.put(possibleValueK, 1);
						}else {
							int actualValue= attributeJmap.get(possibleValueK);
							attributeJmap.put(possibleValueK, actualValue+1);
							System.out.println("                         increasing count of "+possibleValueK+" to "+(actualValue+1));			


						} //for every new possible value K starts his count to 0, otherwise increment his count by 1
					}
				}
			}

		}
		System.out.println("\n|-----------------------------------------------------------");
		System.out.println("|     Attribute Map generated ");
		System.out.println("|-----------------------------------------------------------\n");
		System.out.println(attributeValueListAndEntropyTotal.toString());
		return attributeValueListAndEntropyTotal;
	}


}


