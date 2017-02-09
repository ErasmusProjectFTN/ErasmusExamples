package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;

public class FusekiTest1 {
	public static void uploadRDF(File rdf, String serviceURI)
			throws IOException {

		// parse the file
		Model m = ModelFactory.createDefaultModel();
		try (FileInputStream in = new FileInputStream(rdf)) {
			m.read(in, null, "RDF/XML");
		}

		// upload the resulting model
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(serviceURI);
		accessor.putModel(m);
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
}
