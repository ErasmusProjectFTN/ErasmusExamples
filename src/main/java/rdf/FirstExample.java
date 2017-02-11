package rdf;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;

import java.io.IOException;
import java.io.InputStream;

public class FirstExample {
    public static void uploadRDF(String path, String serviceURI) throws IOException {

        // parse the file
        Model m = ModelFactory.createDefaultModel();
        try {
            InputStream in = FirstExample.class.getResource(path).openStream();
            if (in == null) {
                System.out.println("NULL");
            }
            m.read(in, null, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // upload the resulting model
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
        accessor.putModel(m);
    }

    public static void execSelectAndPrint(String serviceURI, String query) {
        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();

        ResultSetFormatter.out(System.out, results);

        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode x = soln.get("x");
            System.out.println(x);
        }
    }

    public static void execUpdate(String serviceURI, String update) {
        UpdateProcessor upp = UpdateExecutionFactory.createRemote(UpdateFactory.create(update), serviceURI);
        upp.execute();
    }

    public static void execSelectAndProcess(String serviceURI, String query) {
        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();

        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            // assumes that you have an "?x" in your query
            RDFNode x = soln.get("x");
            System.out.println(x);
        }
    }

    public static void main(String[] argv) throws IOException {
        //uploadRDF("/test.rdf", "http://localhost:3030/ds/data");
        //execSelectAndPrint("http://localhost:3030/ds/query", "SELECT * WHERE {?x ?r ?y}");

        String select  = "PREFIX  ex: <http://example.org/>\n" + "SELECT ?animal\n" + "WHERE\n"
                + "  { ?animal a ex:animal . }";
        QueryExecution q = QueryExecutionFactory.sparqlService("http://localhost:3030/mysql/sparql", select);
        ResultSet results = q.execSelect();

        ResultSetFormatter.out(System.out, results);

        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode x = soln.get("x");
            System.out.println(x);
        }

        /*
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
        */

    }
}
