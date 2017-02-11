package rdf;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.Logger;

import java.io.InputStream;

public class Primer8 {
    final static Logger logger = Logger.getLogger(Primer8.class);

    static final String inputFileName = "/primer8.rdf";

    public static void main(String args[]) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = Primer8.class.getResourceAsStream(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model.read(in, "");

        // select all the resources with a VCARD.FN property
        // whose value ends with "Smith"
        StmtIterator iter = model.listStatements(new SimpleSelector(null, VCARD.FN, (RDFNode) null) {
            @Override public boolean selects(Statement s) {
                return s.getString().endsWith("Smith");
            }
        });
        if (iter.hasNext()) {
            logger.info("The graph contains vcards for:");
            while (iter.hasNext()) {
                logger.info("  " + iter.nextStatement().getString());
            }
        } else {
            logger.info("No Smith's were found in the graph");
        }
    }
}
