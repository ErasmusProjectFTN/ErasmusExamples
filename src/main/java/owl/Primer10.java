package owl;


import java.util.Iterator;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

public class Primer10 {

	final static Logger logger = Logger.getLogger(Primer10.class);

	public static void main(String[] args) {
		// create the base model
		String SOURCE = "/eswc.rdf";
		String BASE = "http://www.eswc2006.org/technologies/ontology";
		
		// create the reasoning model using the base
		OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		model.read(Primer10.class.getResourceAsStream(SOURCE),"RDF/XML");

		Ontology ont = model.getOntology(BASE);
		ont.addImport(model.createResource("http://blablabla.org"));
		for (Iterator<OntResource> i = ont.listImports(); i.hasNext(); ) {
			  Resource r = (Resource) i.next();
			  logger.info("imported ontology: " + r.getURI() );
			}

	}
	
}
