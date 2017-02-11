package fuseki;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;

public class FusekiTest1 {
	public static void uploadRDF(File rdf, String serviceURI)
			throws IOException {

		// parse the file
		Model m = ModelFactory.createDefaultModel();
		try  {
			FileInputStream in = new FileInputStream(rdf);
			m.read(in, null, "RDF/XML");
		}catch(Exception e)
		{

		}

		// upload the resulting model
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(serviceURI);
		accessor.putModel(m);
	}

	public static void uploadTriple(Model model, String serviceURI)
			throws IOException {
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(serviceURI);
		accessor.putModel(model);
	}


	public static void execSelectAndPrint(String serviceURI, String query) {
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		ResultSetFormatter.out(System.out, results);

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("x");
			System.out.println(x);
		}
	}

	public static void execUpdate(String serviceURI, String update)
	{
		UpdateProcessor upp = UpdateExecutionFactory.createRemote(
				UpdateFactory.create(update),
				serviceURI);
		upp.execute();
	}

	public static void execSelectAndProcess(String serviceURI, String query) {
		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI,
				query);
		ResultSet results = q.execSelect();

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			// assumes that you have an "?x" in your query
			RDFNode x = soln.get("x");
			System.out.println(x);
		}
	}

	public static void main(String[] argv) throws IOException {
//		test1();
		test2();
	}

	public static void test1() throws IOException
	{
		uploadRDF(new File("test.rdf"), "http://localhost:3030/ds/data");
		execSelectAndPrint(
				"http://localhost:3030/ds/query",
				"SELECT * WHERE {?x ?r ?y}");
		String insert =
				"PREFIX si: <http://www.w3schools.com/rdf/>"
						+ "INSERT DATA"
						+ "{ <http://www.w3schools.com>    si:price    42}";
		execUpdate("http://localhost:3030/ds/update",insert);
		execSelectAndPrint(
				"http://localhost:3030/ds/query",
				"SELECT * WHERE {?x ?r ?y}");
		String delete =
				"PREFIX si: <http://www.w3schools.com/rdf/>"
						+ "DELETE DATA"
						+ "{ <http://www.w3schools.com>    si:price    42;"
						+ " si:title \"W3Schools\". "
						+ "}";
		execUpdate("http://localhost:3030/ds/update",delete);
		execSelectAndPrint(
				"http://localhost:3030/ds/query",
				"SELECT * WHERE {?x ?r ?y}");
	}


	public static OntModel createIndividual(OntModel model)
	{
		String NS = "http://www.semanticweb.org/banevezilic/ontologies/2017/0/student#";

		// get the needed classes
		OntClass student = model.getOntClass(NS + "Student");
		OntClass diploma = model.getOntClass(NS + "Diploma");

		// create the individuals
		Individual student_ind = model.createIndividual(NS + "1128990801079", student); // jmbg
		Individual diploma_ind = model.createIndividual(NS + "1", diploma); // database primary key

		// get ObjectProperty
		ObjectProperty hasDiploma = model.getObjectProperty(NS + "hasDiploma");

		student_ind.setPropertyValue(hasDiploma, diploma_ind);

		return model;
	}

	public static Model getModel(String serviceURI)
	{
		// Get Dataset model from fuseki dataset
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(serviceURI);
		return accessor.getModel();
	}

	public static OntModel createOntModel()
	{
		// create new OntModel
		String SOURCE = "student.owl";
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		model.read(SOURCE);
		return model;
	}

	public static OntModel loadOntModel(String serviceURI) throws IOException
	{
		Model dm = getModel(serviceURI);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		dm.write(bos);
		ByteArrayInputStream ios = new ByteArrayInputStream(bos.toByteArray());
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		model.read(ios,"http://www.semanticweb.org/banevezilic/ontologies/2017/0/student#");
		return model;
	}


	public static void postTriple()
	{
		String NS = "http://www.semanticweb.org/banevezilic/ontologies/2017/0/student#";
		String insert_template = "INSERT DATA"
				+ "{ ";

		OntModel om = createOntModel();
		OntModel om1 = createOntModel();

		// get the needed classes
		OntClass student = om.getOntClass(NS + "Student");
		OntClass diploma = om.getOntClass(NS + "Diploma");

		// create the individuals
		Individual student_ind = om.createIndividual(NS + "1128990801079", student); // jmbg
		Individual diploma_ind = om.createIndividual(NS + "1", diploma); // database primary key

		// get ObjectProperty
		ObjectProperty hasDiploma = om.getObjectProperty(NS + "hasDiploma");

		student_ind.setPropertyValue(hasDiploma, diploma_ind);

		Model om2 = om.difference(om1);
		StmtIterator it = om2.listStatements();
		while(it.hasNext()) // na osnovu ovoga mogu da se rade sparql postovi na fuseki za punjenje ontologije.
		{
			String insert = new String(insert_template); // string za update
			String spo[] = it.next().toString().replace("[", "").replace("]","").replace(","," ").split("  "); // vadjenje s p o
			for(String part: spo)
			{
				insert += "<" + part + ">" + " ";
			}
			insert += ".}";
			execUpdate("http://localhost:3030/ds/update", insert); // post to fuseki
		}
	}


	public static void test2() throws IOException // da li getLock() radi nad celim fusekijem?
	{
		postTriple();
		execSelectAndPrint(
				"http://localhost:3030/ds/query",
				"SELECT * WHERE {?x ?r ?y}");
	}
}
