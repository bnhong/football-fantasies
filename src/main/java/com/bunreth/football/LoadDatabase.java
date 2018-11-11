package com.bunreth.football;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.github.scribejava.apis.YahooApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
class LoadDatabase {
	private static final String ROSTER_RESOURCE_URL 
		= "https://fantasysports.yahooapis.com/fantasy/v2/team/380.l.467566.t.7/roster/players";
	private static final String CLIENT_ID = "YOUR CLIENT ID HERE";
	private static final String CLIENT_SECRET = "YOUR CLIENT SECRET HERE";
	
	@Bean
	CommandLineRunner initDatabase(PlayerRepository playerRepository) {

        return args -> {
        	String response = requestProtectedResource(ROSTER_RESOURCE_URL);
        	Document doc = convertStringToDocument(response);
        	loadPlayerData(playerRepository, doc);
			
			log.info("Connecting to Yahoo with OAuth...");
		};
	}
	
	private String requestProtectedResource(String resourceURL) throws IOException, InterruptedException, ExecutionException {
		OAuth20Service service = new ServiceBuilder(CLIENT_ID)
				.apiSecret(CLIENT_SECRET)
				.callback(OAuthConstants.OOB)
                .build(YahooApi20.instance());
		Scanner in = new Scanner(System.in);
		
		// Yahoo OAuth Workflow
		System.out.println("=== Yahoo OAuth Workflow ===");
		System.out.println("");

		// Authorize ScribeJava
		System.out.println("Authorizing ScribeJava...");
		System.out.println("Enter the following Authorization URL into your browser.");
		System.out.println(service.getAuthorizationUrl());
		System.out.println("Log in and allow access to the application. Copy and paste the verifier here:");
		System.out.print(">>");
		
		final String oauthVerifier = in.nextLine();
		in.close();		
		System.out.println();
		
		// Exchange the verifier for an access token
		OAuth2AccessToken accessToken = service.getAccessToken(oauthVerifier);
		System.out.println("Got the Access Token!");
		System.out.println("");
		
		// Request data from a protected resource
		final OAuthRequest request = new OAuthRequest(Verb.GET, resourceURL);
		service.signRequest(accessToken, request);
		final Response response = service.execute(request);
		
		System.out.println("Got it! Lets see what we found...");
		String responseBody = response.getBody();
        System.out.println(responseBody);
        System.out.println();
        
        return responseBody;
	}
	
	private Document convertStringToDocument(String s) throws ParserConfigurationException, SAXException, IOException {
        // Load response into a Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(s));
        Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();
        
        return doc;
	}
	
	private void loadPlayerData(PlayerRepository playerRepository, Document doc) {
		List<Player> players = new ArrayList<Player>();
        
        NodeList playerList = doc.getDocumentElement().getElementsByTagName("player");
        
        for (int i = 0; i < playerList.getLength(); i++) {        	        	
        	Node node = playerList.item(i);
        	
        	if (node.getNodeType() == Node.ELEMENT_NODE) {
	        	Element elem = (Element) node; 
				
				String firstName = elem.getElementsByTagName("first")
						.item(0).getChildNodes().item(0).getNodeValue();
				
				// Defenses do not have a last name.
				// Check if the node is empty before accessing its value.
				String lastName = "";
				if (elem.getElementsByTagName("last").item(0).getChildNodes().getLength() > 0) {
		    		lastName = elem.getElementsByTagName("last")
					.item(0).getChildNodes().item(0).getNodeValue();				
				}
	    		
				String eligiblePosition = elem.getElementsByTagName("position")
	    				.item(0).getChildNodes().item(0).getNodeValue();
	    		
				players.add(new Player(firstName, lastName, Position.valueOf(eligiblePosition)));
        	}
        }
        
        for (Player p : players) {
        	System.out.println(p.getFullName() + " - " + p.getEligiblePosition());
        	playerRepository.save(new Player(p.getFirstName(), p.getLastName(), p.getEligiblePosition()));
        }
	}
}