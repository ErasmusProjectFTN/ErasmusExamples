package rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.Logger;

public class Example1 {
    final static Logger logger = Logger.getLogger(Example1.class);

    public static void main(String[] args) {
        // some definitions
        String personURI = "http://somewhere/JohnSmith";
        String fullName = "John Smith";

        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        Resource johnSmith = model.createResource(personURI);

        // add the property
        johnSmith.addProperty(VCARD.FN, fullName);

        logger.info("John Smith object: " + johnSmith);
    }
}
