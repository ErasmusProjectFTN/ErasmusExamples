package rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Primer6 {
    static final String inputFileName = "/primer6.rdf";
    static final String johnSmithURI = "http://somewhere/JohnSmith/";
    final static Logger logger = Logger.getLogger(Primer6.class);

    public static void main(String args[]) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = Primer6.class.getResourceAsStream(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model.read(new InputStreamReader(in), "");

        // retrieve the Adam Smith vcard resource from the model
        Resource vcard = model.getResource(johnSmithURI);

        // retrieve the value of the N property
        @SuppressWarnings("unused") Resource name = (Resource) vcard.getRequiredProperty(VCARD.N).getObject();
        // retrieve the given name property
        String fullName = vcard.getRequiredProperty(VCARD.FN).getString();
        // add two nick name properties to vcard
        vcard.addProperty(VCARD.NICKNAME, "Smithy").addProperty(VCARD.NICKNAME, "Adman");

        // set up the output
        logger.info("The nicknames of \"" + fullName + "\" are:");
        // list the nicknames
        StmtIterator iter = vcard.listProperties(VCARD.NICKNAME);
        while (iter.hasNext()) {
            logger.info("    " + iter.nextStatement().getObject().toString());
        }
    }
}
