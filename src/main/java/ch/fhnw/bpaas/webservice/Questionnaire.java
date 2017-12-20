package ch.fhnw.bpaas.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
	
//	/**
//	 * @author Devid
//	 * @param functional requirements: action, object
//	 * @param questionnaire
//	 * @return the id of the max entropy question of selected functional requirement
//	 * @throws NoActionAndObjectQuestionLeftException
//	 * @throws NoDomainQuestionLeftException 
//	 */
//	
//	public String selectQuestionFromFunctional(String action, String obj, String apqc) throws NoResultsException {
//		
//		EntropyCalculation e = new EntropyCalculation();
//		
//		//getting the list of cloud services' id matching with action and object and apqc
//		
//		
//		//getting attribute list of selected cloud services and creating HashMap for their attribute	
//		HashMap<String, ArrayList<String>> attributeMap=new HashMap<String, ArrayList<String>>();
//		attributeMap=getAttributeMap(action, obj, apqc);
//		
//		//entropy calculation, return max entropy question based on the attribute
//		HashMap<String, Float> a = e.getEntropyforAttributes(attributeMap);
//		e.displayAttributesWithEntropy(a);
//		
//		//return the id of the max entropy question of selected functional requirement
//		String question = e.getAttributeOrQuestionWithMaxEntropy(a);
//		
//		return question;
//	}
//
//	
//	
//	
//	
//	
//	}
//	*/
	
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getNextQuestion")
	public Response getFunctionalQuestions(String parsed_json) {
		Gson gson = new Gson(); 
		System.out.println("\n####################<start>####################");
		System.out.println("/Received request for next question" );
		System.out.println("####################<end>####################");
		
		System.out.println("/Questionnaire received: " +parsed_json);
		
		QuestionnaireModel qm = gson.fromJson(parsed_json, QuestionnaireModel.class);
		QuestionnaireItem result = new QuestionnaireItem();
		try {
				result = detectNextQuestion(qm.getCompletedQuestionList().size(), qm);
				
		} catch (NoResultsException e) {
			e.printStackTrace();
		}
		
		String json = gson.toJson(result);
		System.out.println("\n####################<start>####################");
		System.out.println("/search genereated json: " +json);
		System.out.println("####################<end>####################");
		return Response.status(Status.OK).entity(json).build();
	}

	private QuestionnaireItem detectNextQuestion(int num_of_completed_questions, QuestionnaireModel qm) throws NoResultsException{
		QuestionnaireItem pickedQuestion = new QuestionnaireItem();
		
		if (num_of_completed_questions >2){
			
			
			QuestionnaireModel thisQuestionnaire = new QuestionnaireModel(); 
			
			ArrayList<QuestionnaireItem> questions= thisQuestionnaire.getCompletedQuestionList();
			
			
			//TODO: Get list of attributes
			//TODO: Create attribute map
			//TODO: Calculate entropy and check if not 0
			//TODO: Select attribute with max entropy
			
			String pickedAttributeQuestion="";//temp variable to store the result of the entropy calculation
			

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
				
				//filter (?relation = pickedAttributeQuestion);  to check
				queryStr.append("FILTER (?relation = "+ pickedAttributeQuestion + ")");
				
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
			
		} else {
			
		
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
			if (num_of_completed_questions == 0){
				queryStr.append("FILTER (?label = \"Which Object does reflect the functional requirement you want to express?\")");
			}else if (num_of_completed_questions == 1){
				queryStr.append("FILTER (?label = \"Which Action does reflect the functional requirement you want to express?\")");
			}else if (num_of_completed_questions== 2){
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
		
		
			}
		return pickedQuestion;
		}
	
	
//	
//	/**
//	 * @author Devid
//	 * @param Funcional requirement, action, obj, apqc
//	 * @return Attribute Map based on funcional requirement 
//	 * @throws NoDomainQuestionLeftException 
//	 * @throws NoActionAndObjectQuestionLeftException
//	 */
//	
//	private HashMap<String, ArrayList<String>> getAttributeMap(String action, String obj, String apqc ) throws NoResultsException{
//		
//		HashMap<String,String> attributeList=new HashMap<String,String>();
//		attributeList=queryAttributeFullListFromCloudServiceList();
//
//		HashMap<String, ArrayList<String>> attributeMap=new HashMap<String, ArrayList<String>>();
//
//		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
//		//query creation
//		String cols="";
//
//		for (int i = 0; i < attributeList.size(); i++) {
//			cols="?"+attributeList.get(i).toString()+" ";
//		}
//
//		queryStr.append("SELECT ?cloudservice "+ cols +" WHERE {");
//		queryStr.append("?cloudservice rdf:type bpaas:CloudService .");
//
//		queryStr.append("?cloudservice bpaas:cloudServiceHasObject ?object .");
//		queryStr.append("?cloudservice bpaas:cloudServiceHasAction ?action .");
//		queryStr.append("?cloudservice bpaas:cloudServiceHasAction ?apqc .");
//
//		for (int j = 0; j < attributeList.size(); j++) {
//			String key= attributeList.get(j);
//			queryStr.append("?cloudservice bpaas:cloudServiceHasAction ?apqc .");
//		}	
//
//		queryStr.append("FILTER (?object = fbpdo:"+obj+ ").");
//		queryStr.append("FILTER (?action = fbpdo:"+action+ ").");
//		queryStr.append("FILTER (?action = fbpdo:"+apqc+ ").");
//		queryStr.append("}");		
//
//		// query execution		
//		QueryExecution qexec = ontology.query(queryStr);
//		ResultSet results = qexec.execSelect();
//
//		// resultMap will contain the Cloud Services <string, arrayList string>
//		ArrayList<String> resultMap = new ArrayList<String>();
//
//		//storing result from query in HashMap containing cloudsocket's information
//		if(!results.hasNext()){ //if result is empty throw the exception
//			qexec.close();
//			throw new NoResultsException("no cloud services for this action and object have been found");
//		}else{
//			//scan result and populate the array of Cloud Services matching with action and object
//			while(results.hasNext()) {
//				QuerySolution soln=results.next();
//				String value=soln.get("?cloudservice").toString();//TODO CHECK THIS ID
//				resultMap.add(value);
//			}
//		}
//	return attributeMap;
//
//
//	}
//	
//	
//	/** 
//	 * @author Devid
//	 * @return HashMap<String,String> of Cloud Services' properties and annotationRelation
//	 * @throws NoFunctionalRequirementLeftException
//	 *
//	 */
//	
//	private HashMap<String,String> queryAttributeFullListFromCloudServiceList() throws NoResultsException {
//		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
//		//query creation
//		queryStr.append("SELECT ?question ?ar WHERE {");
//		queryStr.append("?qClass rdfs:subClassOf* questionnaire:Question .");
//		queryStr.append("?question rdf:type ?qClass .");
//		queryStr.append("?question questionnaire:questionHasAnnotationRelation ?ar");
//		queryStr.append("}");		
//		
//		// query execution		
//		QueryExecution qexec = ontology.query(queryStr);
//		ResultSet results = qexec.execSelect();
//		
//		// resultMap will contain property and his annotationRelation
//		HashMap<String, String> resultMap = new HashMap<String, String>();
//		if(!results.hasNext()){
//			qexec.close();
//			throw new NoResultsException("all domain questions have been answerd; asking for new domain");
//		}else{
//			
//			while(results.hasNext()){
//				QuerySolution soln = results.next();
//				String key = soln.get("?question").toString();
//				String value = null;
//				try{
//					value = soln.get("?ar").toString();
//				}catch(NullPointerException e){
//					value = soln.get("?ar").toString();;
//				}
//				
//				if(!resultMap.containsKey(key)){
//					resultMap.put(key, value);
//				}
//			}
//			qexec.close();
//			return resultMap;
//		}
//
//	}
	
	private ArrayList<EntropyCloudService> getCloudServiceList(){
		
		//TODO:Ste buon divertimento :D io mi metto a fare il test set e l'entropia
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		queryStr.append("SELECT ?cs ?label ?properties ?values WHERE {");
		queryStr.append("?cs a bpaas:CloudService .");
		queryStr.append("?cs rdfs:label ?label . ");
		queryStr.append("?properties rdfs:domain bpaas:CloudService .");
		queryStr.append("?cs ?properties ?values");
		queryStr.append("} ORDER BY ?cs ?label ?properties ?values");
		
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		Set<Answer> answers = new HashSet<Answer>();

		while (results.hasNext()) {
			QuerySolution soln = results.next();
			answers.add(new Answer(soln.get("?answer").toString(), soln.get("?label").toString()));
		}
		qexec.close();
		return new ArrayList<EntropyCloudService>();
		
	}
	
}

