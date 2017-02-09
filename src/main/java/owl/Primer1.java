package owl;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import java.util.Iterator;

public class Primer1 {

	final static Logger logger = Logger.getLogger(Primer1.class);

	public static void main(String[] args) {
		// create the base model
		String SOURCE = "/eswc.rdf";
		String NS = "http://www.eswc2006.org/technologies/ontology#";
		OntModel base = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		base.read( Primer1.class.getResourceAsStream(SOURCE), "RDF/XML" );

		// create the reasoning model using the base
		OntModel inf = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF, base );

		// create a dummy paper for this example
		OntClass paper = base.getOntClass( NS + "Paper" );
		Individual p1 = base.createIndividual( NS + "paper1", paper );

		// list the asserted types
		logger.info("asserted types:");
		for (Iterator<Resource> i = p1.listRDFTypes(false); i.hasNext(); ) {
			logger.info( p1.getURI() + " is asserted in class " + i.next() );
		}

		// list the inferred types
		p1 = inf.getIndividual( NS + "paper1" );
		logger.info("infered types");
		for (Iterator<Resource> i = p1.listRDFTypes(false); i.hasNext(); ) {
			logger.info( p1.getURI() + " is inferred to be in class " + i.next() );
		}	
		// list the inferred types
		p1 = inf.getIndividual( NS + "paper1" );
		logger.info("asserted types, listRDFTypes(true)");
		for (Iterator<Resource> i = p1.listRDFTypes(true); i.hasNext(); ) {
			logger.info( p1.getURI() + " is asserted to be in class " + i.next() );
		}	
	}
	
}
