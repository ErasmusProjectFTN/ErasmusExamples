package owl;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.log4j.Logger;

public class Primer7 {

	final static Logger logger = Logger.getLogger(Primer7.class);

	public static void main(String[] args) {
		// create the base model
		String SOURCE = "/eswc.rdf";
		String NS = "http://www.eswc2006.org/technologies/ontology#";

		// create the reasoning model using the base
		OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		model.read(Primer7.class.getResourceAsStream(SOURCE),"RDF/XML");
		
		// get the class references
		OntClass place = model.createClass( NS + "Place" );
		OntClass indTrack = model.createClass( NS + "IndustryTrack" );

		// get the property references
		ObjectProperty hasPart = model.createObjectProperty( NS + "hasPart" );
		ObjectProperty hasLoc = model.createObjectProperty( NS + "hasLocation" );

		// create the UK instance
		Individual uk = place.createIndividual( NS + "united_kingdom" );

		// now the anonymous restrictions
		HasValueRestriction ukLocation =
			model.createHasValueRestriction( null, hasLoc, uk );
		HasValueRestriction hasIndTrack =
			model.createHasValueRestriction( null, hasPart, indTrack );

		// finally create the intersection class
		@SuppressWarnings("unused")
		IntersectionClass ukIndustrialConf =
			model.createIntersectionClass( NS + "UKIndustrialConference",
					model.createList( new RDFNode[] {ukLocation, hasIndTrack} ) );
		
		model.write(System.out);
	}
	
}
