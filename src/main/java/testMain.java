import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import ch.fhnw.bpaas.model.entropy.EntropyCloudService;
import ch.fhnw.bpaas.model.entropy.EntropyCloudServiceAttribute;
import ch.fhnw.bpaas.model.questionnaire.QuestionnaireItem;
import ch.fhnw.bpaas.webservice.*;
import ch.fhnw.bpaas.webservice.exceptions.NoResultsException;

public class testMain {

	public static void main(String[] args) {
		Questionnaire qm= new Questionnaire();	
		ArrayList<EntropyCloudService> ecss = qm.createTestAttributeMap();
		
		HashMap<String, HashMap<String, Integer>> attributeMap= qm.getAttributeMap(ecss);
		
		HashMap<String, Float> entropyMap = qm.getEntropyMap(attributeMap, ecss.size());
		
		//TODO: Discuss CS[id=service6, label=null, attributes=[[id=FILE LOG RETENTION POLICY, values= [ ], domain=null] ...] Should an absent value increase Entropy for the attribute?
		String maxEntropyAttributeT = qm.getMaxEntropyAttribute(entropyMap);
		
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|---                                             IMPORTANT MESSSAGE                                                                                        ------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|---     maxEntropyAttribute is FILE LOG RETENTION POLICY, but no corresponding question with this label so we might use 'PAYMENT PLAN' as example but     ------|");
		System.out.println("|---     also 'PAYMENT PLAN' is not available, as is, but it's available if spelled as: bpaas:cloudServiceHasPaymentPlan                                   ------|");
		System.out.println("|---     so we are generating the questionnaireItem for  bpaas:cloudServiceHasPaymentPlan using the funcion: getQuestionFromAttribute()                    ------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		
		//TODO: change getQuestionFromAttribute()
		//maxEntropyAttribute is FILE LOG RETENTION POLICY, but no corresponding question with this label so we might use payment plan as example but
		// also payment plan is not correctly available but it's spelled as:  bpaas:cloudServiceHasPaymentPlan
		
		
		
		String maxEntropyAttribute="bpaas:cloudServiceHasPaymentPlan";

//		SELECT ?question ?label ?qType ?relation ?datatype ?searchnamespace ?searchType ?dTypeLabel ?rule WHERE {
//			  ?question rdfs:label ?label .
//			  ?question rdf:type ?qType .
//			  ?qType rdfs:subClassOf* questionnaire:AnswerType .
//			  ?question rdf:type ?dType .
//			  ?dType rdfs:label ?dTypeLabel .
//			  ?dType rdfs:subClassOf questionnaire:Question .
//			  ?question questionnaire:questionHasAnnotationRelation ?relation .
//			 FILTER (?relation =bpaas:cloudServiceHasPaymentPlan )
//			}
		
		
		QuestionnaireItem question = null;
		try {
			question = qm.getQuestionFromAttribute(maxEntropyAttribute);
		} catch (NoResultsException e) {
			
			e.printStackTrace();
		}
				
		
		System.out.println(question.toString());
		
		
	}
//	
//private static ArrayList<EntropyCloudService> createTestAttributeMap() {
//		
//		
//		ArrayList<EntropyCloudService> attributeMap = new ArrayList<EntropyCloudService>() ;
//		
//		//-----------------------------------------------------------------------
//		//------------ CLOUD SERVICE 1
//		//-----------------------------------------------------------------------
//		EntropyCloudServiceAttribute cs1AttributeA = new EntropyCloudServiceAttribute();
//		cs1AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
//		
//		ArrayList<String> cs1AttributeApossibleValues = new ArrayList<String>();
//		cs1AttributeApossibleValues.add("archive the log when full");
//		cs1AttributeApossibleValues.add("do not overwrite event");
//		cs1AttributeA.setValues(cs1AttributeApossibleValues);
//		
//		EntropyCloudServiceAttribute cs1AttributeB = new EntropyCloudServiceAttribute();
//		cs1AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
//		
//		ArrayList<String> cs1AttributeBpossibleValues = new ArrayList<String>();
//		cs1AttributeBpossibleValues.add("at_most_1_working_day");
//		cs1AttributeB.setValues(cs1AttributeBpossibleValues);
//		
//		EntropyCloudServiceAttribute cs1AttributeC = new EntropyCloudServiceAttribute();
//		cs1AttributeC.setId("PAYMENT PLAN"); //attribute C
//		
//		ArrayList<String> cs1AttributeCpossibleValues = new ArrayList<String>();
//		cs1AttributeCpossibleValues.add("Customizable Plan");
//		cs1AttributeC.setValues(cs1AttributeCpossibleValues);
//		
//		
//		ArrayList<EntropyCloudServiceAttribute> cs1AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
//		cs1AttributeList.add(cs1AttributeA);
//		cs1AttributeList.add(cs1AttributeB);
//		cs1AttributeList.add(cs1AttributeC);
//		EntropyCloudService cs1= new EntropyCloudService();
//		cs1.setId("service1");
//		cs1.setAttributes(cs1AttributeList);
//		attributeMap.add(cs1);
//		
//		//-----------------------------------------------------------------------
//		//------------ CLOUD SERVICE 2
//		//-----------------------------------------------------------------------
//		EntropyCloudServiceAttribute cs2AttributeA = new EntropyCloudServiceAttribute();
//		cs2AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
//		
//		ArrayList<String> cs2AttributeApossibleValues = new ArrayList<String>();
//		cs2AttributeApossibleValues.add("archive the log when full");
//		cs2AttributeA.setValues(cs2AttributeApossibleValues);
//		
//		EntropyCloudServiceAttribute cs2AttributeB = new EntropyCloudServiceAttribute();
//		cs2AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
//		
//		ArrayList<String> cs2AttributeBpossibleValues = new ArrayList<String>();
//		cs2AttributeBpossibleValues.add("at_most_1_working_day");
//		cs2AttributeB.setValues(cs2AttributeBpossibleValues);
//		
//		EntropyCloudServiceAttribute cs2AttributeC = new EntropyCloudServiceAttribute();
//		cs2AttributeC.setId("PAYMENT PLAN"); //attribute C
//		
//		ArrayList<String> cs2AttributeCpossibleValues = new ArrayList<String>();
//		cs2AttributeCpossibleValues.add("Free of Charge");
//		cs2AttributeC.setValues(cs2AttributeCpossibleValues);
//		
//		
//		ArrayList<EntropyCloudServiceAttribute> cs2AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
//		cs2AttributeList.add(cs2AttributeA);
//		cs2AttributeList.add(cs2AttributeB);
//		cs2AttributeList.add(cs2AttributeC);
//		EntropyCloudService cs2= new EntropyCloudService();
//		cs2.setId("service2");
//		cs2.setAttributes(cs2AttributeList);
//		attributeMap.add(cs2);
//		
//		//-----------------------------------------------------------------------
//		//------------ CLOUD SERVICE 3
//		//-----------------------------------------------------------------------
//		EntropyCloudServiceAttribute cs3AttributeA = new EntropyCloudServiceAttribute();
//		cs3AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
//		
//		ArrayList<String> cs3AttributeApossibleValues = new ArrayList<String>();
//		cs3AttributeApossibleValues.add("overwrite event as needed");
//		cs3AttributeA.setValues(cs3AttributeApossibleValues);
//		
//		EntropyCloudServiceAttribute cs3AttributeB = new EntropyCloudServiceAttribute();
//		cs3AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
//		
//		ArrayList<String> cs3AttributeBpossibleValues = new ArrayList<String>();
//		cs3AttributeBpossibleValues.add("at_most_1_working_day");
//		cs3AttributeB.setValues(cs3AttributeBpossibleValues);
//		
//		EntropyCloudServiceAttribute cs3AttributeC = new EntropyCloudServiceAttribute();
//		cs3AttributeC.setId("PAYMENT PLAN"); //attribute C
//		
//		ArrayList<String> cs3AttributeCpossibleValues = new ArrayList<String>();
//		cs3AttributeCpossibleValues.add("Monthly Fee");
//		cs3AttributeC.setValues(cs3AttributeCpossibleValues);
//		
//		
//		
//		ArrayList<EntropyCloudServiceAttribute> cs3AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
//		cs3AttributeList.add(cs3AttributeA);
//		cs3AttributeList.add(cs3AttributeB);
//		cs3AttributeList.add(cs3AttributeC);
//		EntropyCloudService cs3= new EntropyCloudService();
//		cs3.setId("service3");
//		cs3.setAttributes(cs3AttributeList);
//		attributeMap.add(cs3);
//		
//
//		
//		//-----------------------------------------------------------------------
//		//------------ CLOUD SERVICE 4
//		//-----------------------------------------------------------------------
//		EntropyCloudServiceAttribute cs4AttributeA = new EntropyCloudServiceAttribute();
//		cs4AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
//		
//		ArrayList<String> cs4AttributeApossibleValues = new ArrayList<String>();
//		cs4AttributeApossibleValues.add("overwrite event as needed");
//		cs4AttributeA.setValues(cs4AttributeApossibleValues);
//		
//		EntropyCloudServiceAttribute cs4AttributeB = new EntropyCloudServiceAttribute();
//		cs4AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
//		
//		ArrayList<String> cs4AttributeBpossibleValues = new ArrayList<String>();
//		cs4AttributeBpossibleValues.add("at_most_2_hours");
//		cs4AttributeB.setValues(cs4AttributeBpossibleValues);
//		
//		EntropyCloudServiceAttribute cs4AttributeC = new EntropyCloudServiceAttribute();
//		cs4AttributeC.setId("PAYMENT PLAN"); //attribute C
//		
//		ArrayList<String> cs4AttributeCpossibleValues = new ArrayList<String>();
//		cs4AttributeCpossibleValues.add("Customizable Plan");
//		cs4AttributeC.setValues(cs4AttributeCpossibleValues);
//		
//		
//		
//		ArrayList<EntropyCloudServiceAttribute> cs4AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
//		cs4AttributeList.add(cs4AttributeA);
//		cs4AttributeList.add(cs4AttributeB);
//		cs4AttributeList.add(cs4AttributeC);
//		EntropyCloudService cs4= new EntropyCloudService();
//		cs4.setId("service4");
//		cs4.setAttributes(cs4AttributeList);
//		attributeMap.add(cs4);
//		
//		
//		//-----------------------------------------------------------------------
//		//------------ CLOUD SERVICE 5
//		//-----------------------------------------------------------------------
//		EntropyCloudServiceAttribute cs5AttributeA = new EntropyCloudServiceAttribute();
//		cs5AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
//		
//		ArrayList<String> cs5AttributeApossibleValues = new ArrayList<String>();
//		cs5AttributeApossibleValues.add("archive the log when full");
//		cs5AttributeA.setValues(cs5AttributeApossibleValues);
//		
//		EntropyCloudServiceAttribute cs5AttributeB = new EntropyCloudServiceAttribute();
//		cs5AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
//		
//		ArrayList<String> cs5AttributeBpossibleValues = new ArrayList<String>();
//		cs5AttributeBpossibleValues.add("at_most_2_hours");
//		cs5AttributeB.setValues(cs5AttributeBpossibleValues);
//		
//		EntropyCloudServiceAttribute cs5AttributeC = new EntropyCloudServiceAttribute();
//		cs5AttributeC.setId("PAYMENT PLAN"); //attribute C
//		
//		ArrayList<String> cs5AttributeCpossibleValues = new ArrayList<String>();
//		cs5AttributeCpossibleValues.add("Customizable Plan");
//		cs5AttributeC.setValues(cs5AttributeCpossibleValues);
//		
//		
//		
//		ArrayList<EntropyCloudServiceAttribute> cs5AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
//		cs5AttributeList.add(cs5AttributeA);
//		cs5AttributeList.add(cs5AttributeB);
//		cs5AttributeList.add(cs5AttributeC);
//		EntropyCloudService cs5= new EntropyCloudService();
//		cs5.setId("service5");
//		cs5.setAttributes(cs5AttributeList);
//		attributeMap.add(cs5);
//		
//		
//		//-----------------------------------------------------------------------
//		//------------ CLOUD SERVICE 6
//		//-----------------------------------------------------------------------
//		EntropyCloudServiceAttribute cs6AttributeA = new EntropyCloudServiceAttribute();
//		cs6AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
//		
//		ArrayList<String> cs6AttributeApossibleValues = new ArrayList<String>();
//		cs6AttributeApossibleValues.add(" "); // TODO: TO be tested without any space inside/ With empty value
//		cs6AttributeA.setValues(cs6AttributeApossibleValues);
//		
//		EntropyCloudServiceAttribute cs6AttributeB = new EntropyCloudServiceAttribute();
//		cs6AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
//		
//		ArrayList<String> cs6AttributeBpossibleValues = new ArrayList<String>();
//		cs6AttributeBpossibleValues.add("at_most_4_hours");
//		cs6AttributeB.setValues(cs6AttributeBpossibleValues);
//		
//		EntropyCloudServiceAttribute cs6AttributeC = new EntropyCloudServiceAttribute();
//		cs6AttributeC.setId("PAYMENT PLAN"); //attribute C
//		
//		ArrayList<String> cs6AttributeCpossibleValues = new ArrayList<String>();
//		cs6AttributeCpossibleValues.add("Customizable Plan");
//		cs6AttributeC.setValues(cs6AttributeCpossibleValues);
//		
//		
//		
//		ArrayList<EntropyCloudServiceAttribute> cs6AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
//		cs6AttributeList.add(cs6AttributeA);
//		cs6AttributeList.add(cs6AttributeB);
//		cs6AttributeList.add(cs6AttributeC);
//		EntropyCloudService cs6= new EntropyCloudService();
//		cs6.setId("service6");
//		cs6.setAttributes(cs6AttributeList);
//		attributeMap.add(cs6);
//		
//		
//		
//		//-----------------------------------------------------------------------
//		//------------ CLOUD SERVICE 7
//		//-----------------------------------------------------------------------
//		EntropyCloudServiceAttribute cs7AttributeA = new EntropyCloudServiceAttribute();
//		cs7AttributeA.setId("FILE LOG RETENTION POLICY"); //attribute A
//		
//		ArrayList<String> cs7AttributeApossibleValues = new ArrayList<String>();
//		cs7AttributeApossibleValues.add("archive the log when full");
//		cs7AttributeApossibleValues.add("do not overwrite event");
//		cs4AttributeApossibleValues.add("overwrite event as needed");
//		cs7AttributeA.setValues(cs7AttributeApossibleValues);
//		
//		EntropyCloudServiceAttribute cs7AttributeB = new EntropyCloudServiceAttribute();
//		cs7AttributeB.setId("ServiceSupportResponsiveness"); //attribute B
//		
//		ArrayList<String> cs7AttributeBpossibleValues = new ArrayList<String>();
//		cs7AttributeBpossibleValues.add("at_most_1_working_day");
//		cs7AttributeB.setValues(cs7AttributeBpossibleValues);
//		
//		EntropyCloudServiceAttribute cs7AttributeC = new EntropyCloudServiceAttribute();
//		cs7AttributeC.setId("PAYMENT PLAN"); //attribute C
//		
//		ArrayList<String> cs7AttributeCpossibleValues = new ArrayList<String>();
//		cs7AttributeCpossibleValues.add("Monthly Fee");
//		cs7AttributeCpossibleValues.add("Customizable Plan");
//		cs7AttributeC.setValues(cs7AttributeCpossibleValues);
//		
//		
//		
//		ArrayList<EntropyCloudServiceAttribute> cs7AttributeList = new ArrayList<EntropyCloudServiceAttribute>();
//		cs7AttributeList.add(cs7AttributeA);
//		cs7AttributeList.add(cs7AttributeB);
//		cs7AttributeList.add(cs7AttributeC);
//		EntropyCloudService cs7= new EntropyCloudService();
//		cs7.setId("service7");
//		cs7.setAttributes(cs7AttributeList);
//		attributeMap.add(cs7);
//		
//		System.out.println("\n|-----------------------------------------------------------:");
//		System.out.println("|     attributeMap generated by createTestAttributeMap() ");
//		System.out.println("|-----------------------------------------------------------\n:");	
//		System.out.println(attributeMap.toString());
//		return attributeMap;
//			
//		
//	}
//
//private static HashMap<String, Float> getEntropyMap(HashMap<String, HashMap<String, Integer>> attributeMap, Integer tot) {
//	
//	HashMap<String, Float> entropyMap = new HashMap<String, Float>();
//	
////	for (int i = 0; i < attributeMap.size(); i++) {
////	
////		HashMap<String, Integer> attributeImap = attributeMap.get(i);
////		Float entropyI=(float) 0;
////		
////		for (int j=0; j < attributeImap.size(); j++) {
////			
////			Integer count=attributeImap.get(j);
////			Float entropyJ= entropyCalculation(count, tot);
////			entropyI=entropyI+entropyJ;
////		}
////		
////	}
//	System.out.println("\n|------------------------------------");
//	System.out.println("|EntropyMap Calculation on a total of :"+ tot +" cloud Services");
//	System.out.println("|------------------------------------");
//	for (Map.Entry<String, HashMap<String, Integer>> entry : attributeMap.entrySet()) {
//		HashMap<String, Integer> attributeImap = entry.getValue();
//		
//		//System.out.println(entry.getKey()+": "+ attributeImap.toString());
//		
//		Float entropyI=(float) 0;
//		System.out.println("\n|------------------------------------");	
//		System.out.println("|    Entropy of: "+ entry.getKey()+ "': "+ " " +entropyI);	
//		System.out.println("|------------------------------------\n\n");	
//		for (Map.Entry<String, Integer> entry1 : attributeImap.entrySet()) {
//					
//			Integer count=entry1.getValue();
//				
//			Float entropyJ= entropyCalculation(count, tot);
//			System.out.println("    Entropy of '"+ entry1.getKey()+ "': " +entropyJ);
//			System.out.println("    count: "+count);
//			System.out.println("    Previous total entropy of: "+ entry.getKey()+ "': "+ " " +entropyI);
//			entropyI=entropyI+entropyJ;
//			System.out.println("------------------------------------");
//			System.out.println("New entropy for "+entry.getKey()+": "+entropyI);
//			System.out.println("------------------------------------");
//			
//			
//			entropyMap.put(entry.getKey(), entropyI);
//		}
//	}
//	System.out.println("\n|-----------------------------------------------------------");
//	System.out.println("|     entropyMap generated by getEntropyMap() ");
//	System.out.println("|-----------------------------------------------------------\n");	
//	System.out.println(entropyMap.toString());		
//	return entropyMap;
//}
//
//private static HashMap<String, HashMap<String, Integer>> getAttributeMap(ArrayList<EntropyCloudService> ecss) {
//	
//	HashMap<String, HashMap<String, Integer>> attributeValueListAndEntropyTotal = new HashMap<String, HashMap<String, Integer>>();
//
//	Integer csCount=ecss.size();
//	Integer attributesCount = ecss.get(0).getAttributes().size(); 
//	//TODO: in the previous row, I assume that all the cloud services has the same number of attributes
//
//	//System.out.println("count of CloudServices: "+ csCount.toString());
//	
//	for (int i = 0; i < csCount; i++) {
//		ArrayList<EntropyCloudServiceAttribute> attributeListI = ecss.get(i).getAttributes();
//		//System.out.println("i: "+ i);
//		
//		for (int j = 0; j < attributesCount; j++) {
//			//System.out.println("     j: "+ j);
//			EntropyCloudServiceAttribute attributeJ = attributeListI.get(j);
//			//System.out.println("     attributeJ: "+ attributeJ.toString());
//			ArrayList<String> possibleValueList = attributeJ.getValues();
//			//System.out.println("     value: "+ possibleValueList.toString());
//			
//			//System.out.println("          attributeValueListAndEntropyTotal.containsKey("+attributeJ.getId()+") "+ attributeValueListAndEntropyTotal.containsKey(attributeJ.getId()));
//			if (!attributeValueListAndEntropyTotal.containsKey(attributeJ.getId())) {
//
//				HashMap<String, Integer> attributeJmap = new HashMap<String, Integer>();
//
//				for (int k = 0; k < possibleValueList.size(); k++) {
//					//	System.out.println("          k: "+ k);
//
//					String possibleValueK= possibleValueList.get(k);
//
//					//System.out.println("          Value "+ k + ":"+ possibleValueK);
//					//System.out.println("         	 attributeJmap: "+ attributeJmap.toString());
//					//System.out.println("                  attributeJmap.containsKey("+possibleValueK+"): "+ attributeJmap.containsKey(possibleValueK));
//					if (!attributeJmap.containsKey(possibleValueK)) {
//						//System.out.println("                         adding "+possibleValueK+" to attributeJMap");			
//						attributeJmap.put(possibleValueK, 1);
//						//System.out.println("          attributeJmap: "+ attributeJmap.toString());
//					} //for every new possible value K starts his count to 0
//					attributeValueListAndEntropyTotal.put(attributeJ.getId(), attributeJmap);
//					// System.out.println("                           attributeValueListAndEntropyTotal.toString()"+attributeValueListAndEntropyTotal.toString());
//				}
//			} else {
//
//				HashMap<String, Integer> attributeJmap = attributeValueListAndEntropyTotal.get(attributeJ.getId());
//				//System.out.println("          attributeValueListAndEntropyTotal.get(attributeJ) "+ attributeValueListAndEntropyTotal.get(attributeJ));
//				for (int k = 0; k < possibleValueList.size(); k++) {
//					String possibleValueK= possibleValueList.get(k);
//					
//					//System.out.println("          Value "+ k + ":"+ possibleValueK);
//					//System.out.println("         	 attributeJmap: "+ attributeJmap.toString());
//					
//					if (!attributeJmap.containsKey(possibleValueK)) {
//						attributeJmap.put(possibleValueK, 1);
//					}else {
//						int actualValue= attributeJmap.get(possibleValueK);
//						attributeJmap.put(possibleValueK, actualValue+1);
//					} //for every new possible value K starts his count to 0, otherwise increment his count by 1
//				}
//			}
//		}
//	}
//	System.out.println("\n|-----------------------------------------------------------");
//	System.out.println("|     attributeValueListAndEntropyTotal generated by getAttributeMap() ");
//	System.out.println("|-----------------------------------------------------------\n");
//	System.out.println(attributeValueListAndEntropyTotal.toString());
//	return attributeValueListAndEntropyTotal;
//}
//
//private static float entropyCalculation(Integer count, Integer total) {
//	
//	float prob = count/(float) total;
//	//System.out.println("\n entropyCalc\n");
//	//System.out.println("count:"+ count+" prob: "+ prob);
//	float entropy = - (float)  (prob * Math.log(prob) / Math.log(2.0)) ;
//	
//
//	//System.out.println("entropy: "+ entropy);
//	
//	return entropy;
//}
//
//private static String getMaxEntropyAttribute(HashMap<String, Float> entropyMap) {
//	
//	String maxEntropyAttr="";
//	Float max=(float) 0;
//	
////	for (int i = 0; i < entropyMap.size(); i++) {
////	if(entropyMap.get(i)>max) {
////		max=entropyMap.get(i);
////		//maxEntropyAttr=get(i); 
////	}
////	}
//	for (Map.Entry<String,Float> entry : entropyMap.entrySet()) {
//		if (entry.getValue()>max) {
//			maxEntropyAttr=entry.getKey();
//			System.out.println("New max entropy attribute is: "+entry.getKey() + " => " + entry.getValue());
//			max=entry.getValue();
//		}
//		 
//	}
//	System.out.println("\n|-----------------------------------------------------------");
//	System.out.println("|             maxEntropyAttr: "+maxEntropyAttr );
//	System.out.println("|------------------------------------------------------------\n");
//	return maxEntropyAttr;
//}
//
//
//



}
