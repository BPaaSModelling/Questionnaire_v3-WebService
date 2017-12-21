package ch.fhnw.bpaas.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import ch.fhnw.bpaas.model.cloudservice.CloudServiceElementModel;
import ch.fhnw.bpaas.model.cloudservice.CloudServiceModel;
import ch.fhnw.bpaas.model.entropy.EntropyCloudService;
import ch.fhnw.bpaas.model.entropy.EntropyCloudServiceAttribute;
import ch.fhnw.bpaas.model.entropy.EntropyMain;
import ch.fhnw.bpaas.model.questionnaire.Answer;
import ch.fhnw.bpaas.model.questionnaire.QuestionnaireItem;
import ch.fhnw.bpaas.model.questionnaire.QuestionnaireModel;
import ch.fhnw.bpaas.webservice.exceptions.DomainSelectionException;
import ch.fhnw.bpaas.webservice.exceptions.NoDomainQuestionLeftException;
import ch.fhnw.bpaas.webservice.exceptions.NoResultsException;
import ch.fhnw.bpaas.webservice.ontology.OntologyManager;
import ch.fhnw.bpaas.webservice.persistence.EntropyCalculation;
import ch.fhnw.bpaas.webservice.persistence.GlobalVariables;

@Path("/questionnaire")
public class Questionnaire {
	
	private Gson gson = new Gson();
	private OntologyManager ontology = OntologyManager.getInstance();
	private boolean debug_properties = false;
	private String hotword_rule = "?value";

	
	@GET
	@Path("/getDomains")
	public Response getDomains() {
		
		System.out.println("\n####################<start>####################");
		System.out.println("/requested parameters to get question domains" );
		System.out.println("####################<end>####################");
		ArrayList<Answer> result = new ArrayList<Answer>();
		
		try {
				result = queryDomains();
				if (debug_properties){
				for (int index = 0; index < result.size(); index++){
				System.out.println("Element "+index+": ");
				System.out.println("Label -->" + result.get(index).getAnswerLabel());
				System.out.println("ID Name -->" + result.get(index).getAnswerID());
				System.out.println("");
				}
				}
		} catch (NoResultsException e) {
			e.printStackTrace();
		}
		
		String json = gson.toJson(result);
		System.out.println("\n####################<start>####################");
		System.out.println("/search genereated json: " +json);
		System.out.println("####################<end>####################");
		return Response.status(Status.OK).entity(json).build();
	}

	private ArrayList<Answer> queryDomains() throws NoResultsException{
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		ArrayList<Answer> allDomains = new ArrayList<Answer>();
		
		queryStr.append("SELECT ?domain ?label ?order WHERE {");
		queryStr.append("?domain rdfs:subClassOf questionnaire:Question .");
		queryStr.append("?domain rdfs:label ?label .");
		queryStr.append("OPTIONAL { ?domain questionnaire:hasOrderNumberForVisualization ?order  } .");
		queryStr.append("}");
		queryStr.append("ORDER BY DESC(?order)  ?label");

		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		if (results.hasNext()) {
			while (results.hasNext()) {
				Answer domain = new Answer();
				
				QuerySolution soln = results.next();
				domain.setAnswerID(soln.get("?domain").toString());
				domain.setAnswerLabel(soln.get("?label").toString());
				
				allDomains.add(domain);
			}
		} else {
			throw new NoResultsException("nore more results");
		}
		qexec.close();
		return allDomains;
	}
	
//	@GET
//	@Path("/getQuestionsFromDomains")
//	public Response getQuestionsFromDomains(@QueryParam("domains") String domains_received) {
//		Gson gson = new Gson(); 
//		Answer[] domains = gson.fromJson(domains_received, Answer[].class);
//		System.out.println("\n####################<start>####################");
//		System.out.println("/requested questions from domains" );
//		System.out.println("/received " + domains_received + " domains" );
//		System.out.println("/received " + domains.length + " domains" );
//		System.out.println("####################<end>####################");
//		ArrayList<QuestionnaireItem> result = new ArrayList<QuestionnaireItem>();
//		
//		try {
//				result = queryQuestionsFromDomains(domains);
//				if (debug_properties){
//				for (int index = 0; index < result.size(); index++){
//				System.out.println("Element "+index+": ");
//				System.out.println("QuestionLabel -->" + result.get(index).getQuestionLabel());
//				System.out.println("QuestionURI -->" + result.get(index).getQuestionURI());
//				System.out.println("QuestionURI -->" + result.get(index).getAnswerType());
//				System.out.println("");
//				}
//				}
//		} catch (NoResultsException e) {
//			e.printStackTrace();
//		}
//		
//		String json = gson.toJson(result);
//		System.out.println("\n####################<start>####################");
//		System.out.println("/search genereated json: " +json);
//		System.out.println("####################<end>####################");
//		return Response.status(Status.OK).entity(json).build();
//	}
//
//	private ArrayList<QuestionnaireItem> queryQuestionsFromDomains(Answer[] domain_received) throws NoResultsException{
//		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
//		ArrayList<QuestionnaireItem> allQuestions = new ArrayList<QuestionnaireItem>();
//		
//		
//			
//			queryStr.append("SELECT ?question ?label ?qType ?relation ?datatype ?searchnamespace ?searchType ?dTypeLabel ?rule WHERE {");
//			queryStr.append("?question rdfs:label ?label .");
//			queryStr.append("?question rdf:type ?qType . ");
//			queryStr.append("?qType rdfs:subClassOf* questionnaire:AnswerType .");
//			queryStr.append("?question rdf:type ?dType .");
//			queryStr.append("?dType rdfs:label ?dTypeLabel .");
//			queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
//			queryStr.append("?question questionnaire:questionHasAnnotationRelation ?relation . ");
//			queryStr.append("OPTIONAL {?question questionnaire:valueInsertAnswerTypeHasDatatype ?datatype .}");
//			queryStr.append("OPTIONAL {?question questionnaire:searchSelectionHasSearchNamespace ?searchnamespace .}");
//			queryStr.append("OPTIONAL {?question questionnaire:searchSelectionOnClassesInsteadOfInstances ?searchType .}");
//			queryStr.append("OPTIONAL {?dType questionnaire:hasOrderNumberForVisualization ?orderD}");
//			queryStr.append("OPTIONAL {?question questionnaire:hasOrderNumberForVisualization ?orderQ}");
//			queryStr.append("OPTIONAL {?question questionnaire:questionHasRuleToApply ?rule}");
//			String first_part = "FILTER (";
//			String middle_part = "";
//			String last_part = ")";
//			for (int i = 0; i < domain_received.length; i++){
//				if (middle_part != ""){
//					middle_part =  middle_part + " || ";
//				}
//				middle_part = middle_part + "?dType = <" + domain_received[i].getAnswerID()+">";
//			}
//			queryStr.append(first_part + middle_part + last_part);
//			queryStr.append("}");
//			queryStr.append("ORDER BY DESC(?orderD) DESC(?orderQ)");
//		
//		QueryExecution qexec = ontology.query(queryStr);
//		ResultSet results = qexec.execSelect();
//		
//		if (results.hasNext()) {
//			while (results.hasNext()) {
//				QuestionnaireItem question = new QuestionnaireItem();
//				
//				QuerySolution soln = results.next();
//				question.setQuestionURI(soln.get("?question").toString());
//				question.setQuestionLabel(soln.get("?label").toString());
//				question.setAnswerType(soln.get("?qType").toString());
//				question.setDomainLabel(soln.get("?dTypeLabel").toString());
//				question.setAnnotationRelation(soln.get("?relation").toString());
//				
//				if (soln.get("?rule") != null ){
//					question.setRuleToApply(soln.get("?rule").toString());
//					}
//				
//				if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_SINGLE_SELECTION) || 
//						soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_MULTI_SELECTION)){
//					//Call for the answers
//					question.setAnswerList(new ArrayList<Answer>(getAnswerList(soln.get("?question").toString())));
//				}else if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_SEARCH_SELECTION)){
//					//Call for the namespace
//					question.setSearchNamespace(soln.get("?searchnamespace").toString());
//					//System.out.println("=====================SEARCHTYPE: " + soln.get("?searchType"));
//					//Call for the searchType (Classes of Instances)
//					if (soln.get("?searchType").toString() == null ||
//						soln.get("?searchType").toString().equals("") ||
//						soln.get("?searchType").toString().equals(GlobalVariables.BOOLEAN_FALSE_URI)){
//						question.setSearchOnClassesInsteadOfInstances(false);
//					} else {
//						question.setSearchOnClassesInsteadOfInstances(true);
//					}	
//				}else if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_VALUEINSERT)){
//					//Call for the Datatype
//					question.setAnswerDatatype(soln.get("?datatype").toString());
//					question.setComparisonOperationAnswers(getComparisonOperations());
//				}
//				
//				allQuestions.add(question);
//			}
//		} else {
//			throw new NoResultsException("nore more results");
//		}
//		qexec.close();
//		return allQuestions;
//	}
	
	@GET
	@Path("/getSuitableCloudservices")
	public Response getSuitableCloudServices(@QueryParam("questionList") String questionList_received) {
		System.out.println("ecco quante sono le domande: " + questionList_received);
		System.out.println("\n####################<start>####################");
		System.out.println("/requested suitable cloudservices " );
//		ArrayList<QuestionnaireItem> questionList = new ArrayList<QuestionnaireItem>();
//		for (int i = 0; i < questionList_received.size(); i++){
//			Gson gson = new Gson(); 
//			questionList.add(gson.fromJson(questionList_received.get(i), QuestionnaireItem.class));
//		}
		
		//QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		Gson gson = new Gson(); 
		//QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		System.out.println("/received " + questionList_received );
		//System.out.println("/received " + questionList.length + " questions" );
		System.out.println("####################<end>####################");
		ArrayList<Answer> result = new ArrayList<Answer>();
		
		try {
				result = querySuitableCloudservices(questionList);
				
				if (debug_properties){
				for (int index = 0; index < result.size(); index++){
				System.out.println("CS number "+index+": ");
				System.out.println("CloudServiceID -->" + result.get(index).getAnswerID());
				System.out.println("CloudServiceLabel -->" + result.get(index).getAnswerLabel());
				System.out.println("");
				}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String json = gson.toJson(result);
		System.out.println("\n####################<start>####################");
		System.out.println("/search genereated json: " +json);
		System.out.println("####################<end>####################");
		return Response.status(Status.OK).entity(json).build();
	}
	
	private ArrayList<Answer> querySuitableCloudservices(QuestionnaireItem[] questions){
		
		ArrayList<Answer> cloudServices = new ArrayList<Answer>();
		//this method takes in input all the questions, it has to:
		//1. define which questions were answered
		ArrayList<QuestionnaireItem> answeredQuestion = new ArrayList<QuestionnaireItem>();
		for (int i = questions.length-1; i>=0 ; i--){
			if (questions[i].getGivenAnswerList().size() > 0){
				answeredQuestion.add(questions[i]);
				System.out.println("=== ANSERED QUESTIONS: "+ questions[i].getQuestionLabel() +"===");
			}
		}
		
		//2. apply rules to those answers and query the triplestore to find the cloudservices suitable
		//BEWARE: "?value" will be replace with the answerID of the answer!!
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		ArrayList<String> rules = new ArrayList<String>();
		ArrayList<String> filters = new ArrayList<String>();
		
		queryStr.append("SELECT ?cloudService ?csLabel WHERE {");
		queryStr.append("?cloudService rdf:type bpaas:CloudService .");
		queryStr.append("?cloudService rdfs:label ?csLabel .");
		for (int i = 0; i < answeredQuestion.size(); i++){
			switch (answeredQuestion.get(i).getAnswerType()){
				case GlobalVariables.ANSWERTYPE_MULTI_SELECTION:
					
					if (answeredQuestion.get(i).getRuleToApply()!=null){
						for (int j = 0; j < answeredQuestion.get(i).getGivenAnswerList().size(); j++){
							String id = UUID.randomUUID().toString();
							//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
							queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
							queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
						}
					} else {
						for (int j = 0; j < answeredQuestion.get(i).getGivenAnswerList().size(); j++){
							queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + formatURIForQueries(answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + " .");
						}
					}	
					
					break;
				case GlobalVariables.ANSWERTYPE_VALUEINSERT:
					
					if (answeredQuestion.get(i).getRuleToApply()!=null){
						String id = "?"+UUID.randomUUID().toString();
						String id2 = "?"+UUID.randomUUID().toString();
						queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
						//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id2 + ") .");
						queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id2 + ") .");
						//filters.add("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + id2 + " ) .");
						queryStr.append("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + id2 + " ) .");

					} else {
						String id = "?"+UUID.randomUUID().toString();
						queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
						//filters.add("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID() + ") .");
						queryStr.append("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID() + ") .");

					}
					
					break;
					
				
				default: //single and search answers
					
					if (answeredQuestion.get(i).getRuleToApply()!=null) {
						String id = UUID.randomUUID().toString();
						//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
						queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
						queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");

						
					} else {
						queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + formatURIForQueries(answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + " .");
					}
					
					break;
			}
			
		}
		queryStr.append("}");
		queryStr.append("ORDER BY ?csLabel");
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		while (results.hasNext()) {
			QuerySolution soln = results.next();
			cloudServices.add(new Answer(soln.get("?cloudService").toString(), soln.get("?csLabel").toString()));
		}
		return cloudServices;
	}
	
	private Set<Answer> getComparisonOperations() {
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		queryStr.append("SELECT ?operation ?label WHERE {");
		queryStr.append("?operation rdf:type ?type .");
		queryStr.append("?type rdfs:subClassOf* questionnaire:LogicalOperators .");
		queryStr.append("?operation rdfs:label ?label .");
		queryStr.append("}");
		
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		Set<Answer> comparisonOps = new HashSet<Answer>();

		while (results.hasNext()) {
			QuerySolution soln = results.next();
			comparisonOps.add(new Answer(soln.get("?operation").toString(), soln.get("?label").toString()));
		}
		qexec.close();
		return comparisonOps;
	}
	
	private Set<Answer> getAnswerList(String element_URI) {
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		queryStr.append("SELECT ?answer ?label WHERE {");
		queryStr.append("<"+element_URI+">" + " questionnaire:questionHasAnswers ?answer .");
		queryStr.append("?answer rdfs:label ?label .");
		queryStr.append("}");
		
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		Set<Answer> answers = new HashSet<Answer>();

		while (results.hasNext()) {
			QuerySolution soln = results.next();
			answers.add(new Answer(soln.get("?answer").toString(), soln.get("?label").toString()));
		}
		qexec.close();
		return answers;
	}
	
	private String formatURIForQueries(String URI){
		if (URI.startsWith("http://")){
			return "<"+URI+">";
		}else{
			return URI;
		}
	}
		

	private QuestionnaireItem detectNextQuestion(QuestionnaireModel qm) throws NoResultsException{
		QuestionnaireItem pickedQuestion = new QuestionnaireItem();
		
		//Generate Attribute Map
		// ArrayList<EntropyCloudService> ecss = getCloudServiceList(qm); The correct method, for the moment I use the test data to check entropy calc
		ArrayList<EntropyCloudService> ecss = EntropyMain.createTestAttributeMap();
		
		
		if (qm.getCompletedQuestionList().size() >2){
			
			HashMap<String, HashMap<String, Integer>> attributeMap= getAttributeMap(ecss);
			
			HashMap<String, Float> entropyMap = getEntropyMap(attributeMap, ecss.size());
			
			String maxEntropyAttribute = getMaxEntropyAttribute(entropyMap);		
		
			//TODO: Select attribute with max entropy

			//Get question from the highest entropy attribute in the attributeMap
			EntropyCloudServiceAttribute attr= new EntropyCloudServiceAttribute();

			pickedQuestion=getQuestionFromAttribute(maxEntropyAttribute);
		} else {
			pickedQuestion=getFunctionalQuestion(qm);			
		}
		return pickedQuestion;
	}
	
	
	
	private String getMaxEntropyAttribute(HashMap<String, Float> entropyMap) {
		
		String maxEntropyAttr="";
		Float max=(float) 0;
		
//		for (int i = 0; i < entropyMap.size(); i++) {
//		if(entropyMap.get(i)>max) {
//			max=entropyMap.get(i);
//			//maxEntropyAttr=get(i); //TODO: change "for" to proper iterator.
//		}
//		}
		for (Map.Entry<String,Float> entry : entropyMap.entrySet()) {
			if (entry.getValue()>max) {
				maxEntropyAttr=entry.getKey();
				// System.out.println("New max entropy attribute is: "+entry.getKey() + " => " + entry.getValue());		   
			}
			 
		}

		
		
		
		return maxEntropyAttr;
	}

	private HashMap<String, Float> getEntropyMap(HashMap<String, HashMap<String, Integer>> attributeMap, Integer tot) {
		
		HashMap<String, Float> entropyMap = new HashMap<String, Float>();
		
//		for (int i = 0; i < attributeMap.size(); i++) {
//		
//			HashMap<String, Integer> attributeImap = attributeMap.get(i);
//			Float entropyI=(float) 0;
//			
//			for (int j=0; j < attributeImap.size(); j++) {
//				
//				Integer count=attributeImap.get(j);
//				Float entropyJ= entropyCalculation(count, tot);
//				entropyI=entropyI+entropyJ;
//			}
//			
//		}	
		
		for (Map.Entry<String, HashMap<String, Integer>> entry : attributeMap.entrySet()) {
			HashMap<String, Integer> attributeImap = entry.getValue();
			
			Float entropyI=(float) 0;
			for (Map.Entry<String, Integer> entry1 : attributeImap.entrySet()) {
				
				Integer count=entry1.getValue();
				Float entropyJ= entropyCalculation(count, tot);
				entropyI=entropyI+entropyJ;
				
				entropyMap.put(entry.getKey(), entropyI);
			}
			
		}
				
		return entropyMap;
	}

	private HashMap<String, HashMap<String, Integer>> getAttributeMap(ArrayList<EntropyCloudService> ecss) {
		
		HashMap<String, HashMap<String, Integer>> attributeValueListAndEntropyTotal = new HashMap<String, HashMap<String, Integer>>();

		Integer csCount=ecss.size();
		Integer attributesCount = ecss.get(0).getAttributes().size(); 
		//TODO: in the previous row, I assume that all the cloud services has the same number of attributes

		
		for (int i = 0; i < csCount; i++) {
			ArrayList<EntropyCloudServiceAttribute> attributeListI = ecss.get(i).getAttributes();

			for (int j = 0; j < attributesCount; j++) {
				EntropyCloudServiceAttribute attributeJ = attributeListI.get(j);
				ArrayList<String> possibleValueList = attributeJ.getValues();

				if (!attributeValueListAndEntropyTotal.containsKey(attributeJ.getId())) {

					HashMap<String, Integer> attributeJmap = new HashMap<String, Integer>();

					for (int k = 0; k < possibleValueList.size(); k++) {
						String possibleValueK= possibleValueList.get(k);

						if (!attributeJmap.containsKey(possibleValueK)) {
							attributeJmap.put(possibleValueK, 0);
						} //for every new possible value K starts his count to 0
					}
				} else {

					HashMap<String, Integer> attributeJmap = attributeValueListAndEntropyTotal.get(attributeJ);

					for (int k = 0; k < possibleValueList.size(); k++) {
						String possibleValueK= possibleValueList.get(k);

						if (!attributeJmap.containsKey(possibleValueK)) {
							attributeJmap.put(possibleValueK, 0);
						}else {
							int actualValue= attributeJmap.get(possibleValueK);
							attributeJmap.put(possibleValueK, actualValue+1);
						}//for every new possible value K starts his count to 0, otherwise increment his count by 1
					}
				}
			}
		}


		return attributeValueListAndEntropyTotal;
	}

	private float entropyCalculation(Integer count, Integer total) {
		
		float prob= count/total;
		float entropy = - (float)  (prob * Math.log(prob) / Math.log(2.0)) ;
		
		return entropy;
	}
	
	

	private QuestionnaireItem getQuestionFromAttribute(String attr) throws NoResultsException {
		QuestionnaireItem pickedQuestion = new QuestionnaireItem();

		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();

		queryStr.append("SELECT ?question ?label ?qType ?relation ?datatype ?searchnamespace ?searchType ?dTypeLabel ?rule WHERE {");
		queryStr.append("?question rdfs:label ?label .");
		queryStr.append("?question rdf:type ?qType . ");
		queryStr.append("?qType rdfs:subClassOf* questionnaire:AnswerType .");
		queryStr.append("?question rdf:type ?dType .");
		queryStr.append("?dType rdfs:label ?dTypeLabel .");
		queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
		queryStr.append("?question questionnaire:questionHasAnnotationRelation ?relation . ");
		queryStr.append("OPTIONAL {?question questionnaire:valueInsertAnswerTypeHasDatatype ?datatype .}");
		queryStr.append("OPTIONAL {?question questionnaire:searchSelectionHasSearchNamespace ?searchnamespace .}");
		queryStr.append("OPTIONAL {?question questionnaire:searchSelectionOnClassesInsteadOfInstances ?searchType .}");
		queryStr.append("OPTIONAL {?dType questionnaire:hasOrderNumberForVisualization ?orderD}");
		queryStr.append("OPTIONAL {?question questionnaire:hasOrderNumberForVisualization ?orderQ}");
		queryStr.append("OPTIONAL {?question questionnaire:questionHasRuleToApply ?rule}");
		
		queryStr.append("FILTER (?question = " + attr + ")");
		
		queryStr.append("}");
		queryStr.append("ORDER BY DESC(?orderD) DESC(?orderQ)");

		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();

		if (results.hasNext()) {
			while (results.hasNext()) {
				QuestionnaireItem question = new QuestionnaireItem();

				QuerySolution soln = results.next();
				question.setQuestionURI(soln.get("?question").toString());
				question.setQuestionLabel(soln.get("?label").toString());
				question.setAnswerType(soln.get("?qType").toString());
				question.setDomainLabel(soln.get("?dTypeLabel").toString());
				question.setAnnotationRelation(soln.get("?relation").toString());

				if (soln.get("?rule") != null ){
					question.setRuleToApply(soln.get("?rule").toString());
				}

				if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_SINGLE_SELECTION) || 
						soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_MULTI_SELECTION)){
					//Call for the answers
					question.setAnswerList(new ArrayList<Answer>(getAnswerList(soln.get("?question").toString())));
				}else if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_SEARCH_SELECTION)){
					//Call for the namespace
					question.setSearchNamespace(soln.get("?searchnamespace").toString());
					//System.out.println("=====================SEARCHTYPE: " + soln.get("?searchType"));
					//Call for the searchType (Classes of Instances)
					if (soln.get("?searchType").toString() == null ||
							soln.get("?searchType").toString().equals("") ||
							soln.get("?searchType").toString().equals(GlobalVariables.BOOLEAN_FALSE_URI)){
						question.setSearchOnClassesInsteadOfInstances(false);
					} else {
						question.setSearchOnClassesInsteadOfInstances(true);
					}	
				}else if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_VALUEINSERT)){
					//Call for the Datatype
					question.setAnswerDatatype(soln.get("?datatype").toString());
					question.setComparisonOperationAnswers(getComparisonOperations());
				}

				pickedQuestion = question;
			}
		} else {
			throw new NoResultsException("nore more results");
		}
		qexec.close();		

		return pickedQuestion;

	}

	private QuestionnaireItem getFunctionalQuestion(QuestionnaireModel qm) throws NoResultsException {
		QuestionnaireItem pickedQuestion = new QuestionnaireItem();
				
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
			
			queryStr.append("SELECT ?question ?label ?qType ?relation ?datatype ?searchnamespace ?searchType ?dTypeLabel ?rule WHERE {");
			queryStr.append("?question rdfs:label ?label .");
			queryStr.append("?question rdf:type ?qType . ");
			queryStr.append("?qType rdfs:subClassOf* questionnaire:AnswerType .");
			queryStr.append("?question rdf:type ?dType .");
			queryStr.append("?dType rdfs:label ?dTypeLabel .");
			queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
			queryStr.append("?question questionnaire:questionHasAnnotationRelation ?relation . ");
			queryStr.append("OPTIONAL {?question questionnaire:valueInsertAnswerTypeHasDatatype ?datatype .}");
			queryStr.append("OPTIONAL {?question questionnaire:searchSelectionHasSearchNamespace ?searchnamespace .}");
			queryStr.append("OPTIONAL {?question questionnaire:searchSelectionOnClassesInsteadOfInstances ?searchType .}");
			queryStr.append("OPTIONAL {?dType questionnaire:hasOrderNumberForVisualization ?orderD}");
			queryStr.append("OPTIONAL {?question questionnaire:hasOrderNumberForVisualization ?orderQ}");
			queryStr.append("OPTIONAL {?question questionnaire:questionHasRuleToApply ?rule}");
//			String first_part = "FILTER (";
//			String middle_part = "";
//			String last_part = ")";
//			for (int i = 0; i < domain_received.length; i++){
//				if (middle_part != ""){
//					middle_part =  middle_part + " || ";
//				}
//				middle_part = middle_part + "?dType = <" + domain_received[i].getAnswerID()+">";
//			}
//			queryStr.append(first_part + middle_part + last_part);
			if (qm.getCompletedQuestionList().size() == 0){
				queryStr.append("FILTER (?label = \"Which Object does reflect the functional requirement you want to express?\")");
			}else if (qm.getCompletedQuestionList().size() == 1){
				queryStr.append("FILTER (?label = \"Which Action does reflect the functional requirement you want to express?\")");
			}else if (qm.getCompletedQuestionList().size()== 2){
				queryStr.append("FILTER (?label = \"Which APQC category does reflect the functional requirement you want to express?\")");
			}
			queryStr.append("}");
			queryStr.append("ORDER BY DESC(?orderD) DESC(?orderQ)");
		
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		if (results.hasNext()) {
			while (results.hasNext()) {
				QuestionnaireItem question = new QuestionnaireItem();
				
				QuerySolution soln = results.next();
				question.setQuestionURI(soln.get("?question").toString());
				question.setQuestionLabel(soln.get("?label").toString());
				question.setAnswerType(soln.get("?qType").toString());
				question.setDomainLabel(soln.get("?dTypeLabel").toString());
				question.setAnnotationRelation(soln.get("?relation").toString());
				
				if (soln.get("?rule") != null ){
					question.setRuleToApply(soln.get("?rule").toString());
					}
				
				if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_SINGLE_SELECTION) || 
						soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_MULTI_SELECTION)){
					//Call for the answers
					question.setAnswerList(new ArrayList<Answer>(getAnswerList(soln.get("?question").toString())));
				}else if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_SEARCH_SELECTION)){
					//Call for the namespace
					question.setSearchNamespace(soln.get("?searchnamespace").toString());
					//System.out.println("=====================SEARCHTYPE: " + soln.get("?searchType"));
					//Call for the searchType (Classes of Instances)
					if (soln.get("?searchType").toString() == null ||
						soln.get("?searchType").toString().equals("") ||
						soln.get("?searchType").toString().equals(GlobalVariables.BOOLEAN_FALSE_URI)){
						question.setSearchOnClassesInsteadOfInstances(false);
					} else {
						question.setSearchOnClassesInsteadOfInstances(true);
					}	
				}else if (soln.get("?qType").toString().equals(GlobalVariables.ANSWERTYPE_VALUEINSERT)){
					//Call for the Datatype
					question.setAnswerDatatype(soln.get("?datatype").toString());
					question.setComparisonOperationAnswers(getComparisonOperations());
				}
				
				pickedQuestion = question;
			}
		} else {
			throw new NoResultsException("nore more results");
		}
		qexec.close();		
		
		return pickedQuestion;
	}
	

	//	@GET
//	@Path("/testEntropy")
//	public Response  getCloudServiceList(){
	public ArrayList<EntropyCloudService> getCloudServiceList(QuestionnaireModel qm){
		String tempStrForDomain = "";
		
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		queryStr.append("SELECT ?q ?dType ?annotationRelation ?cs ?csLabel ?value WHERE {");
		queryStr.append("?q rdf:type ?dType .");
		queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
		queryStr.append("?q questionnaire:questionHasAnnotationRelation ?annotationRelation .");
		queryStr.append("?cs rdf:type bpaas:CloudService .");
		queryStr.append("?cs rdfs:label ?csLabel .");
		queryStr.append("?cs ?annotationRelation ?value");
		//generate filter based on domains
		
		for (int i = 0; i < qm.getSelectedDomainList().size(); i++){
			if (i != 0){
				tempStrForDomain = tempStrForDomain + " || ";
			}
			tempStrForDomain = tempStrForDomain + "?dType = " + qm.getSelectedDomainList().get(i).getAnswerID();
		}
		queryStr.append("FILTER (" + tempStrForDomain + ")");
		//queryStr.append("FILTER (?dType = questionnaire:DataSecurity || ?dType = questionnaire:Payment)");
		queryStr.append("} ORDER BY ?csLabel ?annotationRelation ?value");
		
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		ArrayList<EntropyCloudService> ecss = new ArrayList<EntropyCloudService>();
		EntropyCloudService currentCSE = null;
		EntropyCloudServiceAttribute currentCsAttribute = null;
		ArrayList<String> currentAttributeValues = null;
		String currentCsName = "";
		String currentCsAttributeName = "";

		while (results.hasNext()) {

			QuerySolution soln = results.next();
			//if the cloudservice that I am parsing is different from the previous one
			if (!soln.get("?cs").toString().equals(currentCsName)){

				//I create a new one
				if (!currentCsName.equals("")){
					//but first, I check if it's the first
					ecss.add(currentCSE);
				}
				currentCSE = new EntropyCloudService();
				currentCSE.setId(soln.get("?cs").toString());
				currentCSE.setLabel(soln.get("?csLabel").toString());
				currentCsName = soln.get("?cs").toString();
			}
			//I parse attributes
			//I check if I am parsing a new attribute
			if (!currentCsAttributeName.equals(soln.get("?annotationRelation").toString())){
				
				if (currentAttributeValues != null){
					//if it is not the initial null array, then I add those values to the current property
					currentCsAttribute.setValues(currentAttributeValues);
					currentCSE.getAttributes().add(currentCsAttribute);
				}
				//I create a new array to hold all the values for the next attribute
				currentAttributeValues = new ArrayList<String>();
				//I set the new current attribute for matching
				currentCsAttributeName = soln.get("?annotationRelation").toString();
				//And I create a new Attribute
				currentCsAttribute = new EntropyCloudServiceAttribute();
				currentCsAttribute.setId(soln.get("?annotationRelation").toString());
				currentCsAttribute.setDomain(soln.get("?dType").toString());
			}
			currentAttributeValues.add(soln.get("?value").toString());	
		}
		if (debug_properties){
		for (int i = 0; i < ecss.size(); i++){
			System.out.println("=======");
			System.out.println(ecss.get(i).getId()+ " - " + ecss.get(i).getLabel());
			for (int j = 0; j < ecss.get(i).getAttributes().size(); j++){
				System.out.println("----> " + ecss.get(i).getAttributes().get(j).getId() + " - " + ecss.get(i).getAttributes().get(j).getDomain());
				for (int k = 0; k < ecss.get(i).getAttributes().get(j).getValues().size(); k++){
					System.out.println("---------> " + ecss.get(i).getAttributes().get(j).getValues().get(k));
				}
			}
		}
		}
		qexec.close();
		
		Gson gson = new Gson(); 
		
		String json = gson.toJson(ecss);
		
		//return Response.status(Status.OK).entity(json).build();
		return ecss;
		
	}
	

	
}

