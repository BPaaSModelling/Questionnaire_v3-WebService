import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ch.fhnw.bpaas.model.questionnaire.*;
import javax.ws.rs.core.Response;

import ch.fhnw.bpaas.model.entropy.EntropyCloudService;
import ch.fhnw.bpaas.model.entropy.EntropyCloudServiceAttribute;
import ch.fhnw.bpaas.model.questionnaire.QuestionnaireItem;
import ch.fhnw.bpaas.webservice.*;
import ch.fhnw.bpaas.webservice.exceptions.NoResultsException;

import ch.fhnw.bpaas.webservice.*;

public class TestMain {

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



}
