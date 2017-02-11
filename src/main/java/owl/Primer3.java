package owl;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;

public class Primer3 {

	final static Logger logger = Logger.getLogger(Primer3.class);

	public static void main(String[] args) {
		// create the base model
		String SOURCE = "/eswc.rdf";
		String NS = "http://www.eswc2006.org/technologies/ontology#";

		// create the reasoning model using the base
		OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		model.read(Primer3.class.getResourceAsStream(SOURCE),"RDF/XML");

		OntClass programme = model.createClass(NS + "Programme");
		OntClass orgEvent = model.createClass( NS + "OrganisedEvent" );
		logger.info("programme: "+programme);
		logger.info("organizedEvent: "+orgEvent);

		//model before adding new property
		logger.info("model before adding hasProgramme property");
		model.write(System.out);
		
		ObjectProperty hasProgramme = model.createObjectProperty( NS + "hasProgramme" );
		
		hasProgramme.addDomain( orgEvent );
		hasProgramme.addRange( programme );
		hasProgramme.addLabel( "has programme", "en" );
	
		//model after adding the property
		logger.info("model after adding hasProgramme property");
		model.write(System.out);
	}
	
}
