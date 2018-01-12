import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ch.fhnw.bpaas.model.questionnaire.*;
import javax.ws.rs.core.Response;

import ch.fhnw.bpaas.model.entropy.EntropyCloudService;
import ch.fhnw.bpaas.model.entropy.EntropyCloudServiceAttribute;
import ch.fhnw.bpaas.webservice.*;
import ch.fhnw.bpaas.webservice.exceptions.NoResultsException;

import ch.fhnw.bpaas.webservice.*;

public class TestMain {

	public static void main(String[] args) throws NoResultsException {

//----------------------------------------------------------------------//
//																		//
//TESTING getQuestionFromAttribute(maxEntropyAttribute)					//
//																		//
//----------------------------------------------------------------------//		
		
//		
//		Questionnaire qm= new Questionnaire();
//		QuestionnaireModel qModel=new QuestionnaireModel();
//		
//			
//		ArrayList<Answer> selectedDomainList = new ArrayList<Answer>();
//		
//		Answer domainAnswer = new Answer("questionnaire:Payment","Performance");
//		Answer domainAnswer1 = new Answer("questionnaire:ServiceSupport", "ServiceSupport");
//		System.out.println(domainAnswer.toString());
//		selectedDomainList.add(domainAnswer);
//		selectedDomainList.add(domainAnswer1);
//		System.out.println("selectedDomainList"+selectedDomainList.toString());
//		
//		
//		ArrayList<QuestionnaireItem> completedQuestionList=new ArrayList<QuestionnaireItem>();
		
		

//----------------------------------------------------------------------//
//																		//
//         TESTING getFunctionalQuestion()  WORK  						//
//																		//
//----------------------------------------------------------------------//		
//		It's working, in the below I'm adding empty completed question to the questionnaire model
//		the funciton detectNextQuestion check the size of qModel and return the right funcional questions
//		
//		
//		Questionnaire qm= new Questionnaire();
//		QuestionnaireModel qModel=new QuestionnaireModel();
//		
//			
//		ArrayList<Answer> selectedDomainList = new ArrayList<Answer>();
//		
//		Answer domainAnswer = new Answer("questionnaire:Payment","Performance");
//		Answer domainAnswer1 = new Answer("questionnaire:ServiceSupport", "ServiceSupport");
//		System.out.println(domainAnswer.toString());
//		selectedDomainList.add(domainAnswer);
//		selectedDomainList.add(domainAnswer1);
//		System.out.println("selectedDomainList"+selectedDomainList.toString());
//		
//		
//		ArrayList<QuestionnaireItem> completedQuestionList=new ArrayList<QuestionnaireItem>();
//		
//		//commenting any of the following pairs will change the number of question completed and therefore the result of
//		// detectNextQuestion
//		// commenting 1 pair will return the question item for APQC, commenting 2 pairs will return the question item for OBJECT, 
//		// commenting all of them will return the question item ACTION
//				
//		//QuestionnaireItem completedQuestion= new QuestionnaireItem();
//		//completedQuestionList.add(completedQuestion);
//		
//		//QuestionnaireItem completedQuestion1= new QuestionnaireItem();
//		//completedQuestionList.add(completedQuestion1);
//		
//		//QuestionnaireItem completedQuestion2= new QuestionnaireItem();
//		//completedQuestionList.add(completedQuestion2);
//		
//				
//		qModel.setCompletedQuestionList(completedQuestionList);
//		
//		System.out.println(qModel.getCompletedQuestionList().size());
//		
//		qModel.setCompletedQuestionList(completedQuestionList);
//		qModel.setSelectedQuestionDomainList(selectedDomainList);
//		
//		
//		QuestionnaireItem qI= new QuestionnaireItem();
//		
//		try {
//			qI =qm.detectNextQuestion(qModel);
//			System.out.println(qI.toString());
//		} catch (NoResultsException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//				
//		
//		
//		
//----------------------------------------------------------------------//
//																		//
//      ENTROPY TEST + getQuestionFromAttribute				WORK		//
//																		//		
//----------------------------------------------------------------------//
	
	
		
		Questionnaire qm= new Questionnaire();
				
		ArrayList<EntropyCloudService> ecss = qm.createTestAttributeMap();
	
		
		HashMap<String, HashMap<String, Integer>> attributeMap= qm.getAttributeMap(ecss);
		
		HashMap<String, Float> entropyMap = qm.getEntropyMap(attributeMap, ecss.size());
		
		//TODO: Discuss CS[id=service6, label=null, attributes=[[id=FILE LOG RETENTION POLICY, values= [ ], domain=null] ...] Should an absent value increase Entropy for the attribute?
		//String maxEntropyAttribute = qm.getMaxEntropyAttribute(entropyMap);
		
		String maxEntropyAttribute="bpaas:cloudServiceHasPaymentPlan";
		
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|---                                             IMPORTANT MESSSAGE                                                                                        ------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|---     maxEntropyAttribute is '" +maxEntropyAttribute+"' , but there's no corresponding question. For the test of getQuestionFromAttribute 	   ------|");
		System.out.println("|---     we use 'PAYMENT PLAN'. It works if spelled as: bpaas:cloudServiceHasPaymentPlan     				                              ------|");
		System.out.println("|---     so we are generating the questionnaireItem for  bpaas:cloudServiceHasPaymentPlan using the funcion: getQuestionFromAttribute()                    ------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		System.out.println("|----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		
			
		maxEntropyAttribute="bpaas:cloudServiceHasPaymentPlan";
		//Viable option to change the attribute id to an acceptable value
		//maxEntropyAttribute=maxEntropyAttribute.replace(" ","");
		
		//maxEntropyAttribute ="bpaas:cloudServiceHas"+maxEntropyAttribute;
		
		maxEntropyAttribute ="bpaas:cloudServiceHasPaymentPlan";
		System.out.println(maxEntropyAttribute);
		
//		QuestionnaireItem question = null;
//		
//		try {
//			question = qm.getQuestionFromAttribute(maxEntropyAttribute);
//		} catch (NoResultsException e) {
//			
//			e.printStackTrace();
//		}
//				
//		System.out.println(question.toString());
//		

		
		
//		//DO NOT COMMENT ANYTHING BELOW HERE!!!!!!
}



}


