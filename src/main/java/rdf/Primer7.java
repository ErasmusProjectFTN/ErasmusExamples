package rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.Logger;

import java.io.InputStream;

public class Primer7 {
    final static Logger logger = Logger.getLogger(Primer7.class);

    static final String inputFileName = "/primer7.rdf";

    public static void main(String args[]) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = Primer7.class.getResourceAsStream(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model.read(in, "");

        // select all the resources with a VCARD.FN property
        ResIterator iter = model.listResourcesWithProperty(VCARD.FN);
        if (iter.hasNext()) {
            logger.info("The graph contains vcards for:");
            while (iter.hasNext()) {
                logger.info("  " + iter.nextResource().getRequiredProperty(VCARD.FN).getString());
            }
        } else {
            logger.info("No vcards were found in the graph");
        }
    }
}
