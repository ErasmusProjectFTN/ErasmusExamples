package rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;

import java.io.InputStream;

public class Primer9 {

    static final String inputFileName1 = "/primer9_1.rdf";

    static final String inputFileName2 = "/primer9_2.rdf";

    final static Logger logger = Logger.getLogger(Primer9.class);

    public static void main(String args[]) {
        // create an empty model
        Model model1 = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();

        // use the class loader to find the input file
        InputStream in1 = Primer9.class.getResourceAsStream(inputFileName1);
        if (in1 == null) {
            throw new IllegalArgumentException("File: " + inputFileName1 + " not found");
        }
        InputStream in2 = Primer9.class.getResourceAsStream(inputFileName2);
        if (in2 == null) {
            throw new IllegalArgumentException("File: " + inputFileName2 + " not found");
        }

        // read the RDF/XML files
        model1.read(in1, "");
        model2.read(in2, "");

        // merge the graphs
        Model model = model1.union(model2);

        // print the graph as RDF/XML
        model.write(System.out, "RDF/XML-ABBREV");
        System.out.println();
    }
}
