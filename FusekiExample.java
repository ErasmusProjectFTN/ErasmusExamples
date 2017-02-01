package test;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;

public class FusekiExample {

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
		uploadRDF(new File("test.rdf"), "http://localhost:3030/test/data");
		String insert = 
				"PREFIX si: <http://www.w3schools.com/rdf/>"
	            + "INSERT DATA"
	            + "{ <http://www.w3schools.com>    si:price    42}";
		execUpdate("http://localhost:3030/test/update",insert);
		String delete = 
				"PREFIX si: <http://www.w3schools.com/rdf/>"
	            + "DELETE DATA"
	            + "{ <http://www.w3schools.com>    si:price    42;"
	            + " si:title \"W3Schools\". "
	            + "}";
		execUpdate("http://localhost:3030/test/update",delete);
		execSelectAndPrint(
				"http://localhost:3030/test/query",
				"SELECT * WHERE {?x ?r ?y}");


	}
}