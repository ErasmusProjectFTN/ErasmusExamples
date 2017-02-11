package owl;

import java.util.Iterator;

import org.apache.jena.ontology.EnumeratedClass;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

public class Primer8 {

	final static Logger logger = Logger.getLogger(Primer8.class);

	public static void main(String[] args) {
		// create the base model
		String SOURCE = "/eswc.rdf";
		String NS = "http://www.eswc2006.org/technologies/ontology#";

		// create the reasoning model using the base
		OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		model.read(Primer8.class.getResourceAsStream(SOURCE),"RDF/XML");

		OntClass place = model.getOntClass( NS + "Place" );

		EnumeratedClass ukCountries =
		    model.createEnumeratedClass( NS + "UKCountries", null );
		ukCountries.addOneOf( place.createIndividual( NS + "england" ) );
		ukCountries.addOneOf( place.createIndividual( NS + "scotland" ) );
		ukCountries.addOneOf( place.createIndividual( NS + "wales" ) );
		ukCountries.addOneOf( place.createIndividual( NS + "northern_ireland" ) );

		for (Iterator<?> i = ukCountries.listOneOf(); i.hasNext(); ) {
		  Resource r = (Resource) i.next();
		  logger.info("UK country: " + r.getURI() );
		}
	}
	
}
