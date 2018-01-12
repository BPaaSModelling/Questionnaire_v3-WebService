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
import ch.fhnw.bpaas.model.questionnaire.Answer;
import ch.fhnw.bpaas.model.questionnaire.QuestionnaireItem;
import ch.fhnw.bpaas.model.questionnaire.QuestionnaireModel;
import ch.fhnw.bpaas.webservice.exceptions.DomainSelectionException;
import ch.fhnw.bpaas.webservice.exceptions.MinimumEntropyReached;
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

		
		System.out.println("\n----------------------------      requested parameters to get question domains      -----------------------------------" );
		
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
		//System.out.println("\n####################<start>####################");
		//System.out.println("/search genereated json: " +json);
		//System.out.println("####################<end>####################");
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
		//System.out.println("ecco quante sono le domande: " + questionList_received);
		//System.out.println("\n####################<start> getSuitableCloudServices ####################");
		//System.out.println("/requested suitable cloudservices " );
		//		ArrayList<QuestionnaireItem> questionList = new ArrayList<QuestionnaireItem>();
		//		for (int i = 0; i < questionList_received.size(); i++){
		//			Gson gson = new Gson(); 
		//			questionList.add(gson.fromJson(questionList_received.get(i), QuestionnaireItem.class));
		//		}

		//QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		Gson gson = new Gson(); 
		//QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		QuestionnaireItem[] questionList = gson.fromJson(questionList_received, QuestionnaireItem[].class);
		//System.out.println("/received " + questionList_received );
		//System.out.println("/received " + questionList.length + " questions" );
		//System.out.println("####################<end> getSuitableCloudServices ####################");
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
		//System.out.println("\n####################<start>####################");
		//System.out.println("/search genereated json: " +json);
		//System.out.println("####################<end>####################");
		return Response.status(Status.OK).entity(json).build();
	}

	private ArrayList<Answer> querySuitableCloudservices(QuestionnaireItem[] questions) throws NoResultsException{
		//TODO CHECK MULTI_SELECTION	
		System.out.println("----------------------------                                                                   Matching cloud service list creation                 ----------------------------");
				
		ArrayList<Answer> cloudServices = new ArrayList<Answer>();
		//this method takes in input all the questions, it has to:
		//1. define which questions were answered
		
		ArrayList<QuestionnaireItem> answeredQuestion = new ArrayList<QuestionnaireItem>();
		for (int i = questions.length-1; i>=0 ; i--){
			
			if (questions[i].getGivenAnswerList().size() > 0){
				answeredQuestion.add(questions[i]);
				System.out.println("=== ANSERED QUESTIONS n. "+ i+ " " +questions[i].getQuestionLabel() +"===");
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
			System.out.println("SWITCH CASE = "+answeredQuestion.get(i).getAnswerType());
			switch (answeredQuestion.get(i).getAnswerType()){
			case GlobalVariables.ANSWERTYPE_MULTI_SELECTION:
				System.out.println("inside ANSWERTYPE_MULTI_SELECTION for "+ answeredQuestion.get(i).getQuestionURI());
				
				if (answeredQuestion.get(i).getRuleToApply()!=null){
					for (int j = 0; j < answeredQuestion.get(i).getGivenAnswerList().size(); j++){
						String id = UUID.randomUUID().toString();
						//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
						queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
						
						System.out.println("inside multi search answeredQuestion.get(i).getAnnotationRelation()"+answeredQuestion.get(i).getAnnotationRelation());
						queryStr.append("?cloudService <" + searchAnnotationRelation(answeredQuestion.get(i).getAnnotationRelation(),answeredQuestion.get(i).getQuestionURI()) + "> " + id + " .");
						System.out.println("inside multi search, almost done");
					}
				} else {
					try {
						//System.out.println("------------------------------------------------------------");
						//System.out.println("                 searchAnnotationRelation    " + searchAnnotationRelation(answeredQuestion.get(i).getAnnotationRelation(),answeredQuestion.get(i).getQuestionURI()));
						queryStr.append("?cloudService <" + searchAnnotationRelation(answeredQuestion.get(i).getAnnotationRelation(),answeredQuestion.get(i).getQuestionURI()) + "> " + formatURIForQueries(answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + " .");
					} catch (NoResultsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	

				break;
			case GlobalVariables.ANSWERTYPE_VALUEINSERT:
				System.out.println("inside ANSWERTYPE_VALUEINSERT for "+ answeredQuestion.get(i).getQuestionURI());
				if (answeredQuestion.get(i).getRuleToApply()!=null){
					String id = "?"+UUID.randomUUID().toString();
					String id2 = "?"+UUID.randomUUID().toString();
					queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
					
					//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id2 + ") .");
					queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id2 + ") .");
					//filters.add("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + id2 + " ) .");
					queryStr.append("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + id2 + " ) .");

				} else {
					System.out.println("inside ELSE for "+ answeredQuestion.get(i).getQuestionURI());
					String id = "?"+UUID.randomUUID().toString();
					queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
					//filters.add("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID() + ") .");
					queryStr.append("FILTER( " + id + " " + GlobalVariables.getComparisonOperatorString(answeredQuestion.get(i).getComparisonAnswer()) + answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID() + ") .");

				}

				break;
				//author devid
			case GlobalVariables.ANSWERTYPE_SEARCH_SELECTION:
				System.out.println("inside ANSWERTYPE_SEARCH_SELECTION for "+ answeredQuestion.get(i).getQuestionURI());
				//System.out.println("answeredQuestion.get(i).getRuleToApply()="+answeredQuestion.get(i).getRuleToApply());
				if (answeredQuestion.get(i).getRuleToApply()!=null) {
					String id = UUID.randomUUID().toString();
					//rules.add("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");
					queryStr.append("?cloudService <" + answeredQuestion.get(i).getAnnotationRelation() + "> " + id + " .");
					System.out.println("BIND");
					queryStr.append("BIND (" + answeredQuestion.get(i).getRuleToApply().replaceAll(hotword_rule, answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + ") AS " + id + ") .");


				} else {
					//System.out.println("No rule to apply");
					//System.out.println("answeredQuestion.get(i)"+"i="+i+" "+answeredQuestion.get(i).toString());
					//System.out.println("Get annotation relation" + answeredQuestion.get(i).getAnnotationRelation());
					//System.out.println(formatURIForQueries(answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()));
					try {
						//System.out.println("------------------------------------------------------------");
						//System.out.println("                 searchAnnotationRelation    " + searchAnnotationRelation(answeredQuestion.get(i).getAnnotationRelation(),answeredQuestion.get(i).getQuestionURI()));
						queryStr.append("?cloudService <" + searchAnnotationRelation(answeredQuestion.get(i).getAnnotationRelation(),answeredQuestion.get(i).getQuestionURI()) + "> " + formatURIForQueries(answeredQuestion.get(i).getGivenAnswerList().get(0).getAnswerID()) + " .");
					} catch (NoResultsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;
				//end author devid

			default: //single and search answers
				System.out.println("inside default SWITCH CASE= "+answeredQuestion.get(i).getAnswerType());
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
		System.out.println("query executed for query suitable"+queryStr);
		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();

		while (results.hasNext()) {
			QuerySolution soln = results.next();
			cloudServices.add(new Answer(soln.get("?cloudService").toString(), soln.get("?csLabel").toString()));
		}
		
		//System.out.println("\n"+ cloudServices.toString()+"\n");
		System.out.println("----------------------------                                                                   Matching cloud service list creation                 ----------------------------");
		
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

	private ArrayList<Answer> getAnswerList(String element_URI) throws NoResultsException {
		//TODO fix QUERY#
		ArrayList<Answer> answers= new ArrayList<Answer>();
		
		element_URI=element_URI.replace("#", ":");
		element_URI=element_URI.replace("http://ikm-group.ch/archiMEO/", "");
		System.out.println("elementUri: "+ element_URI);
		
		//SELECT ?answer ?label WHERE { questiondata:Select_your_preferred_payment_plan questionnaire:questionHasAnswers ?answer .?answer rdfs:label ?label .}
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();

		queryStr.append("SELECT DISTINCT ?answer ?label WHERE {");
		queryStr.append(""+element_URI +" questionnaire:questionHasAnswers ?answer .");
		queryStr.append("?answer rdfs:label ?label .");
		queryStr.append("}");


		QueryExecution qexec2 = ontology.query(queryStr);
		ResultSet results = qexec2.execSelect();
		int i=0;
		//System.out.println(i);
		//System.out.println(element_URI);
		if (results.hasNext()) {
			while (results.hasNext()) {
				i++;
				System.out.println(i);
				Answer answerN= new Answer();
				String id ="";
				String label ="";
				
				QuerySolution soln = results.next();

				id= soln.get("?answer").toString();
				label= soln.get("?label").toString();

				answerN.setAnswerID(id);
				answerN.setAnswerLabel(label);
				answers.add(answerN);
			}
		} else {
			throw new NoResultsException("nore more results");
		}
		qexec2.close();
		
		
		return answers;
	}

	private String formatURIForQueries(String URI){
		if (URI.startsWith("http://")){
			return "<"+URI+">";
		}else{
			return URI;
		}
	}

	@POST		
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})		
	@Consumes(MediaType.APPLICATION_JSON)		
	@Path("/getNextQuestion")		
	public Response getFunctionalQuestions(String parsed_json) throws NoResultsException {		
		Gson gson = new Gson(); 		
		//System.out.println("\n####################<start>####################");		
		System.out.println("------------------------             Received request for next question        -------------------------" );		
		//System.out.println("####################<end>####################");		

		System.out.println("/Questionnaire received: " +parsed_json);		

		QuestionnaireModel qm = gson.fromJson(parsed_json, QuestionnaireModel.class);		
		QuestionnaireItem result = new QuestionnaireItem();		
			
		result = detectNextQuestion(qm);		

		String json = gson.toJson(result);		
		//System.out.println("\n####################<start>####################");		
		//System.out.println("/search genereated json: " +json);		
		//System.out.println("####################<end>####################");		
		return Response.status(Status.OK).entity(json).build();		
	}

	public QuestionnaireItem detectNextQuestion(QuestionnaireModel qm) throws NoResultsException{
		QuestionnaireItem pickedQuestion = new QuestionnaireItem();

		//Generate Attribute Map
		//System.out.println("questionnaire model" +qm.toString());
		
		//ArrayList<EntropyCloudService> ecss = createTestAttributeMap();
			
		if (qm.getCompletedQuestionList().size() >2){
			
			
			System.out.println("\n\n####################qm.getCompletedQuestionList().size() inside if --->"+qm.getCompletedQuestionList().size()+"####################");
						
			pickedQuestion=getNonFunctionalQuestion(qm);
			System.out.println("Picked non funcional Question ---> "+ pickedQuestion.toString());
		} else {
			
			pickedQuestion=getFunctionalQuestion(qm);	
			System.out.println("Picked functional Question ---> "+ pickedQuestion.toString());
		}
		
		return pickedQuestion;
	}

	public ArrayList<EntropyCloudService> createTestAttributeMap() {


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
		cs6AttributeApossibleValues.add(" "); // TODO: TO be tested without any space inside/ With empty value
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

		System.out.println("\n|-----------------------------------------------------------:");
		System.out.println("|     attribute 8 ");
		System.out.println("|     "+cs8.toString());
		System.out.println("|-----------------------------------------------------------\n:");	




		System.out.println("\n|-----------------------------------------------------------:");
		System.out.println("|     attributeMap generated by createTestAttributeMap() ");
		System.out.println("|-----------------------------------------------------------\n:");	
		System.out.println(attributeMap.toString());
		return attributeMap;


	}

	public HashMap<String, Float> getEntropyMap(HashMap<String, HashMap<String, Integer>> attributeMap, Integer tot) {

		HashMap<String, Float> entropyMap = new HashMap<String, Float>();

		//System.out.println("\n|------------------------------------");
		//System.out.println("|EntropyMap Calculation on a total of :"+ tot +" cloud Services");
		//System.out.println("|------------------------------------");
		for (Map.Entry<String, HashMap<String, Integer>> entry : attributeMap.entrySet()) {
			HashMap<String, Integer> attributeImap = entry.getValue();

			//System.out.println(entry.getKey()+": "+ attributeImap.toString());

			Float entropyI=(float) 0;
			//System.out.println("\n|------------------------------------");	
			//System.out.println("|    Entropy of: "+ entry.getKey()+ "': "+ " " +entropyI);	
			//System.out.println("|------------------------------------\n\n");	
			for (Map.Entry<String, Integer> entry1 : attributeImap.entrySet()) {

				Integer count=entry1.getValue();

				Float entropyJ= entropyCalculation(count, tot);
				//System.out.println("    Entropy of '"+ entry1.getKey()+ "': " +entropyJ);
			//	System.out.println("    count: "+count);
			//	System.out.println("    Previous total entropy of: "+ entry.getKey()+ "': "+ " " +entropyI);
				entropyI=entropyI+entropyJ;
			//	System.out.println("------------------------------------");
			//	System.out.println("New entropy for "+entry.getKey()+": "+entropyI);
			//	System.out.println("------------------------------------");


				entropyMap.put(entry.getKey(), entropyI);
			}
		}
	//	System.out.println("\n|-----------------------------------------------------------");
	//	System.out.println("|     entropyMap generated by getEntropyMap() ");
	//	System.out.println("|-----------------------------------------------------------");	
	//	System.out.println("|\n|"+ entropyMap.toString()+"\n|");		
	//	System.out.println("|-----------------------------------------------------------\n");
		return entropyMap;
	}

	public HashMap<String, HashMap<String, Integer>> getAttributeMap(ArrayList<EntropyCloudService> ecss) throws NoResultsException {

		HashMap<String, HashMap<String, Integer>> attributeValueListAndEntropyTotal = new HashMap<String, HashMap<String, Integer>>();

		Integer csCount=ecss.size();
		//System.out.println(ecss.toString());
		//TODO: in the previous row, I assume that all the cloud services has the same number of attributes

		ArrayList<EntropyCloudService> ecssOld =ecss;
		
		ecss= createAttributeMapFromList(ecss);
		//System.out.println("!!!!!         new ecss             !!!!!");
				
		//System.out.println("\n\nCount of CloudServices: "+ csCount.toString());

		for (int i = 0; i < csCount; i++) {
			ArrayList<EntropyCloudServiceAttribute> attributeListI = ecss.get(i).getAttributes();
//			System.out.println("cloud service n"+(i+1) +"    id): "+ ecss.get(i).getId().toString());
//			System.out.println("ecss.get("+i+").getAttributes(): ------->"+ ecss.get(i).getAttributes().toString());
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


//							System.out.println("          Value "+ k + ":"+ possibleValueK);
//							System.out.println("         	 attributeJmap: "+ attributeJmap.toString());
//							System.out.println("                  attributeJmap.containsKey("+possibleValueK+"): "+ attributeJmap.containsKey(possibleValueK));
							if (!attributeJmap.containsKey(possibleValueK)) {
							//	System.out.println("                         adding "+possibleValueK);			
								attributeJmap.put(possibleValueK, 1);
//								System.out.println("          attributeJmap: "+ attributeJmap.toString());
							} //for every new possible value K starts his count to 0


							attributeValueListAndEntropyTotal.put(attributeJ.getId(), attributeJmap);
//							System.out.println("                           attributeValueListAndEntropyTotal.toString()"+attributeValueListAndEntropyTotal.toString());
						}
					} else {

						HashMap<String, Integer> attributeJmap = attributeValueListAndEntropyTotal.get(attributeJ.getId());
//						System.out.println("          attributeValueListAndEntropyTotal.get(attributeJ) "+ attributeValueListAndEntropyTotal.get(attributeJ));
						for (int k = 0; k < possibleValueList.size(); k++) {
							String possibleValueK= possibleValueList.get(k);

//							System.out.println("          Value "+ k + ":"+ possibleValueK);
//							System.out.println("         	 attributeJmap: "+ attributeJmap.toString());

							if (!attributeJmap.containsKey(possibleValueK)) {
								attributeJmap.put(possibleValueK, 1);
							}else {
								int actualValue= attributeJmap.get(possibleValueK);
								attributeJmap.put(possibleValueK, actualValue+1);
							//	System.out.println("                         increasing count of "+possibleValueK+" to "+(actualValue+1));			

								
							} //for every new possible value K starts his count to 0, otherwise increment his count by 1
						}
					}
				}

		}
		//System.out.println("\n|-----------------------------------------------------------");
		//System.out.println("|     attributeValueListAndEntropyTotal generated by getAttributeMap() ");
		//System.out.println("|-----------------------------------------------------------\n");
		//System.out.println(attributeValueListAndEntropyTotal.toString());
		return attributeValueListAndEntropyTotal;
	}

	private static float entropyCalculation(Integer count, Integer total) {

		float prob = count/(float) total;
		//System.out.println("\n entropyCalc\n");
		//System.out.println("count:"+ count+" prob: "+ prob);
		float entropy = - (float)  (prob * Math.log(prob) / Math.log(2.0)) ;


		//System.out.println("entropy: "+ entropy);

		return entropy;
	}
	
	private Boolean checkAttrMinEntropy(Float float1) throws MinimumEntropyReached {

		Boolean flag=true;
		Float minEntropyAccepted= (float) 0.3;
		
		if (float1< minEntropyAccepted) {
			flag=false;
		}
		//System.out.println("entropy: "+ entropy);

		return flag;
	}

	public String getMaxEntropyAttribute(HashMap<String, Float> entropyMap) {

		String maxEntropyAttr="";
		Float max=(float) 0;

		//	for (int i = 0; i < entropyMap.size(); i++) {
		//	if(entropyMap.get(i)>max) {
		//		max=entropyMap.get(i);
		//		//maxEntropyAttr=get(i); 
		//	}
		//	}
		for (Map.Entry<String,Float> entry : entropyMap.entrySet()) {
			if (entry.getValue()>max) {
				maxEntropyAttr=entry.getKey();
				//System.out.println("New max entropy attribute is: "+entry.getKey() + " => " + entry.getValue());
				max=entry.getValue();
			}

		}
		
		try {
			checkAttrMinEntropy(entropyMap.get(maxEntropyAttr));
		} catch (MinimumEntropyReached e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("\n|-----------------------------------------------------------");
		//System.out.println("            maxEntropyAttr: "+maxEntropyAttr );
		//System.out.println("|------------------------------------------------------------\n");
		return maxEntropyAttr;
	}






	public String getQuestionFromAttribute(String attr) throws NoResultsException {
		String pickedQuestion ="";
		
		System.out.println("####################  EXECUTION OF getQuestionFromAttribute("+ attr+")         ####################");
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		//
		//		SELECT ?question ?label ?qType ?relation ?datatype ?searchnamespace ?searchType ?dTypeLabel ?rule WHERE {
		//			?question rdfs:label ?label .
		//			?question rdf:type ?qType . 
		//			?qType rdfs:subClassOf* questionnaire:AnswerType .
		//			?question rdf:type ?dType .
		//			?dType rdfs:label ?dTypeLabel .
		//			?dType rdfs:subClassOf questionnaire:Question . 
		//			?question questionnaire:questionHasAnnotationRelation ?relation . 
		//			OPTIONAL {?question questionnaire:valueInsertAnswerTypeHasDatatype ?datatype .}
		//			OPTIONAL {?question questionnaire:searchSelectionHasSearchNamespace ?searchnamespace .}
		//			OPTIONAL {?question questionnaire:searchSelectionOnClassesInsteadOfInstances ?searchType .}
		//			OPTIONAL {?dType questionnaire:hasOrderNumberForVisualization ?orderD}
		//			OPTIONAL {?question questionnaire:hasOrderNumberForVisualization ?orderQ}
		//			OPTIONAL {?question questionnaire:questionHasRuleToApply ?rule}
		//			FILTER (?question = questiondata:What_would_you_like_to_upload )
		//			} 

		queryStr.append("SELECT ?question ?label ?qType ?relation ?datatype ?searchnamespace ?searchType ?dTypeLabel ?rule WHERE {");
		queryStr.append("?question rdfs:label ?label .");
		queryStr.append("?question rdf:type ?qType . ");
		queryStr.append("?qType rdfs:subClassOf* questionnaire:AnswerType .");
		queryStr.append("?question rdf:type ?dType .");
		queryStr.append("?dType rdfs:label ?dTypeLabel .");
		queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
		queryStr.append("?question questionnaire:questionHasAnnotationRelation ?relation . ");
				
		if (attr.contains("#")) {
			attr= attr.replace("http://ikm-group.ch/archiMEO/", "");
			attr=attr.replace("#", ":");
			System.out.println("attr formatted");
		};
		queryStr.append("FILTER (?relation = " + attr + ")}");
		

		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		System.out.println(queryStr);
		int test=0;
		if (results.hasNext()) {
			while (results.hasNext()) {
				QuestionnaireItem question = new QuestionnaireItem();

				QuerySolution soln = results.next();
				
				pickedQuestion=soln.get("?question").toString();
				
				}
		} else {
			System.out.println("\nE");
			throw new NoResultsException("nore more results");
		}
		qexec.close();	
		
		
		System.out.println("####################  end EXECUTION OF getQuestionFromAttribute("+ attr+")         ####################");
		System.out.println("!!!!   THE QUESITON IS:  "+pickedQuestion);
		
		return pickedQuestion;

	}


	private QuestionnaireItem getNonFunctionalQuestion(QuestionnaireModel qm) throws NoResultsException {
		
		ArrayList<EntropyCloudService> ecss = getCloudServiceList(qm); 
		HashMap<String, HashMap<String, Integer>> attributeMap= getAttributeMap(ecss);
		System.out.println("attributeMap"+attributeMap.toString());		
		
		HashMap<String, Float> entropyMap = getEntropyMap(attributeMap, ecss.size());
		System.out.println("entropyMap"+entropyMap.toString());
		
		String maxEntropyAttribute = getMaxEntropyAttribute(entropyMap);		
		System.out.println("maxEntropyAttribute: "+maxEntropyAttribute );	
		
		String questionID =getQuestionFromAttribute(maxEntropyAttribute);
		QuestionnaireItem pickedQuestion = new QuestionnaireItem();

		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		
		queryStr.append("SELECT ?question ?label ?qType ?relation ?datatype ?searchnamespace ?searchType ?dTypeLabel ?rule WHERE {");
		queryStr.append("?question rdfs:label ?label .");
		queryStr.append("?question rdf:type ?qType . ");
		queryStr.append("?qType rdfs:subClassOf* questionnaire:AnswerType .");
		queryStr.append("?question rdf:type ?dType .");
		queryStr.append("?dType rdfs:label ?dTypeLabel .");
		queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
		//queryStr.append("?question questionnaire:questionHasAnnotationRelation ?relation . ");
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
		
		queryStr.append("FILTER (?question = <"+ questionID+"> )");
		queryStr.append("}");
		
		queryStr.append("ORDER BY DESC(?orderD) DESC(?orderQ)");
		System.out.println("query-->"+queryStr);
		
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
				//question.setAnnotationRelation(soln.get("?relation").toString());

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
		//queryStr.append("?question questionnaire:questionHasAnnotationRelation ?relation . ");
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
				//question.setAnnotationRelation(soln.get("?relation").toString());

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
		//queryStr.append("SELECT ?cs ?csLabel ?value WHERE {");
		
		queryStr.append("?q rdf:type ?dType .");
		queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
		queryStr.append("?q questionnaire:questionHasAnnotationRelation ?annotationRelation .");
		queryStr.append("?cs rdf:type bpaas:CloudService .");
		queryStr.append("?cs rdfs:label ?csLabel .");
		queryStr.append("?cs ?annotationRelation ?value . ");
		//generate filter based on domains

		for (int i = 0; i < qm.getSelectedDomainList().size(); i++){
			if (i != 0){
				tempStrForDomain = tempStrForDomain + " || ";
			}
			tempStrForDomain = tempStrForDomain + "?dType = <" + qm.getSelectedDomainList().get(i).getAnswerID()+">";
		}
		queryStr.append("FILTER (" + tempStrForDomain + ")");
		//queryStr.append("FILTER (?dType = questionnaire:DataSecurity || ?dType = questionnaire:Payment)");
		queryStr.append("} ORDER BY ?csLabel ?annotationRelation ?value");

		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();
		
		//System.out.println("----------------------         results.toString()            ----------\n"+results.toString());
		
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
		//System.out.println("debug prop" + debug_properties);
		
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
		//System.out.println("---------------------------------json--------------------------------");
		//System.out.println(json.toString());
		//System.out.println("\n--------------------------------- ecss.toString -----------------------\n"+ecss.toString()+"\n");
		//return Response.status(Status.OK).entity(json).build();
		return ecss;

	}
	
	private String searchAnnotationRelation(String annotationRelation, String questionURI) throws NoResultsException {
		

		if (annotationRelation!=null) {
			return annotationRelation;
		}else {

//			SELECT ?relation WHERE {
//			?question rdfs:label ?label .
//			?question rdf:type ?qType . 
//			?qType rdfs:subClassOf* questionnaire:AnswerType .
//			?question rdf:type ?dType .
//			?dType rdfs:label ?dTypeLabel .
//			?dType rdfs:subClassOf questionnaire:Question . 
//			  ?question questionnaire:questionHasAnnotationRelation ?relation .
//			FILTER (?question =questionURI )
//			}
			String questionN= questionURI.replace("http://ikm-group.ch/archiMEO/", "");
			questionN=questionN.replace("#", ":");
			ParameterizedSparqlString queryStr = new ParameterizedSparqlString();

			queryStr.append("SELECT ?relation WHERE {");
			queryStr.append("?question rdfs:label ?label .");
			queryStr.append("?question rdf:type ?qType . ");
			queryStr.append("?qType rdfs:subClassOf* questionnaire:AnswerType .");
			queryStr.append("?question rdf:type ?dType .");
			queryStr.append("?dType rdfs:label ?dTypeLabel .");
			queryStr.append("?dType rdfs:subClassOf questionnaire:Question . ");
			queryStr.append("?question questionnaire:questionHasAnnotationRelation ?relation .");
			queryStr.append("FILTER (?question = "+questionN+"" );
			queryStr.append(")}");


			QueryExecution qexec = ontology.query(queryStr);
			ResultSet results = qexec.execSelect();

			if (results.hasNext()) {
				while (results.hasNext()) {

					QuerySolution soln = results.next();
					annotationRelation=soln.get("?relation").toString();

				}
			} else {
				throw new NoResultsException("nore more results");
			}
			qexec.close();

			return annotationRelation;
		}


	}

	private ArrayList<EntropyCloudService> createAttributeMapFromList(ArrayList<EntropyCloudService> ecss) throws NoResultsException {
		
		ArrayList<String> csList=new ArrayList<String>();
		 //System.out.println("creating list of cloud services for attribute map");
		for (int i = 0; i < ecss.size(); i++){
		 if (!csList.contains(ecss.get(i).getId())) {
			 csList.add(ecss.get(i).getId());
			 String temp = ecss.get(i).getId();
			 temp=temp.replace("http://ikm-group.ch/archiMEO/", "");
			 temp=temp.replace("#", ":");
			 //System.out.println("added "+temp+" to the cloud service list");
		 }		
			
		}
		//System.out.println("the csList is" +csList.toString());
		
		ArrayList<EntropyCloudService> attributeMap= new ArrayList<EntropyCloudService>();
		
		for (int i = 0; i < csList.size(); i++){
			//System.out.println("i--------> "+i);
			String csIid=csList.get(i);
			//System.out.println("csIid ---->"+ csIid);
			//System.out.println("csList.get(i) "+csList.get(i));
			csIid=csIid.replace("http://ikm-group.ch/archiMEO/", "");
			csIid=csIid.replace("#", ":");
			//System.out.println("csIid ---->"+ csIid);
			EntropyCloudService csI= queryCloudService(csIid);
			attributeMap.add(csI);
		}
				
		
		//System.out.println(" attributeMap from createAttributeMapFromList(ArrayList<EntropyCloudService> ecss)\n"+attributeMap.toString());
		return attributeMap;
		
	}

	private EntropyCloudService queryCloudService(String csId) throws NoResultsException {
		
		EntropyCloudService cs= new EntropyCloudService();

		ArrayList<String> properties = new ArrayList<String>();
		//System.out.println("csId ---->"+ csId+"\n before queryCloudServiceProperties(csId)");
		properties= queryCloudServiceProperties(csId);
		//System.out.println("properties"+properties);
		
		//System.out.println("post queryCloudServiceProperties(csId)");
		ArrayList<EntropyCloudServiceAttribute> attributeList= new ArrayList<EntropyCloudServiceAttribute>();
		
		for (int i = 0; i < properties.size(); i++){
			EntropyCloudServiceAttribute attributeI = new EntropyCloudServiceAttribute();
			
			String attributeIid= properties.get(i);
			ArrayList<String> attributeIvalues= new ArrayList<String>();
			
			//System.out.println("csId "+csId + "\n attributeIid"+attributeIid);
			attributeIid=attributeIid.replace("http://ikm-group.ch/archiMEO/", "");
			attributeIid=attributeIid.replace("#", ":");
			
			attributeIvalues= queryCloudServiceAttributeValues(csId, attributeIid);
			
			attributeI.setId(attributeIid);
			attributeI.setValues(attributeIvalues);
			
			attributeList.add(attributeI);
						
		}
		
		cs.setAttributes(attributeList);
		cs.setId(csId);
		
		//System.out.println(" cs from queryCloudService(String csId)\n"+cs.toString());
		return cs;
	}

	private ArrayList<String> queryCloudServiceAttributeValues(String csId, String attributeId) throws NoResultsException {
		
		ArrayList<String> attributeValues= new ArrayList<String>();
		csId=csId.replace("http://ikm-group.ch/archiMEO/", "");
		csId=csId.replace("#", ":");
		
		attributeId=attributeId.replace("http://ikm-group.ch/archimeo/", "");
		attributeId=attributeId.replace("#", ":");
		
//		SELECT  ?value WHERE {
//			?cloudservice rdf:type bpaas:CloudService .
//			 ?property rdfs:domain bpaas:CloudService .
//			  ?cloudservice bpaas:cloudServiceHasPaymentPlan ?value
//			FILTER (?cloudservice = bdata:InvoiceNinja ).
//			FILTER (?property = bpaas:cloudServiceHasPaymentPlan)}
		
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();

		queryStr.append("SELECT ?cloudservice ?value WHERE {");
		queryStr.append("?cloudservice rdf:type bpaas:CloudService .");
		queryStr.append("?property rdfs:domain bpaas:CloudService .");
		queryStr.append("?cloudservice "+ attributeId+ " ?value ");
		queryStr.append("FILTER (?cloudservice = " + csId + " ).");
		queryStr.append("FILTER (?property = "+ attributeId +")}");

		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();

		if (results.hasNext()) {
			while (results.hasNext()) {
				String attributeValue ="";

				QuerySolution soln = results.next();

				attributeValue= soln.get("?value").toString();
				attributeValue.replace("http://ikm-group.ch/archimeo/", "");
				attributeValue.replace("#", ":");
				//System.out.println("attributeValue--->  "+ attributeValue);
				
				attributeValues.add(attributeValue);
			}
		}
		qexec.close();
		//System.out.println(" attributeValues from queryCloudService(String csId)\n"+attributeValues.toString());
		return attributeValues;
	}

	private ArrayList<String> queryCloudServiceProperties(String csId) throws NoResultsException {
		ArrayList<String> properties = new ArrayList<String>();
		
//		SELECT ?cloudservice ?property WHERE {
//			?cloudservice rdf:type bpaas:CloudService .
//			?property rdfs:domain bpaas:CloudService .
//			FILTER (?cloudservice = bdata:InvoiceNinja).} 
		
		csId=csId.replace("http://ikm-group.ch/archiMEO/", "");
		csId=csId.replace("#", ":");
		
		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		
		queryStr.append("SELECT DISTINCT ?cloudservice ?property WHERE {");
		queryStr.append("?cloudservice rdf:type bpaas:CloudService .");
		queryStr.append("?property rdfs:domain bpaas:CloudService .");
		queryStr.append("FILTER (?cloudservice = "+ csId+" ).}");
		

		QueryExecution qexec = ontology.query(queryStr);
		ResultSet results = qexec.execSelect();

		if (results.hasNext()) {
			while (results.hasNext()) {
				String property ="";

				QuerySolution soln = results.next();
				
				property= soln.get("?property").toString();
				
				//System.out.println("property--->  "+ property);
				property= property.replace("http://ikm-group.ch/archimeo/", "");
				property= property.replace("#", ":");
				//System.out.println("new property--->  "+ property);
						
				properties.add(property);
			}
		} else {
			throw new NoResultsException("nore more results");
		}
		qexec.close();
		
		return properties;
	}
}

