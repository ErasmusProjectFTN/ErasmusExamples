package owl;


import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;

public class Primer9 {

	final static Logger logger = Logger.getLogger(Primer9.class);

	public static void main(String[] args) {
		// create the base model
		String SOURCE = "/eswc.rdf";
		String NS = "http://www.eswc2006.org/technologies/ontology#";

		// create the reasoning model using the base
		OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		model.read(Primer9.class.getResourceAsStream(SOURCE),"RDF/XML");

		OntClass c = model.createClass( NS + "SomeClass" );

		// first way: use a call on OntModel
		@SuppressWarnings("unused") Individual ind0 = model.createIndividual( NS + "ind0", c );

		// second way: use a call on OntClass
		@SuppressWarnings("unused")
		Individual ind1 = c.createIndividual( NS + "ind1" );

		model.write(System.out);
	}
	
}
