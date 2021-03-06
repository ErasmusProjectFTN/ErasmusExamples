package rdf;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.Logger;

public class Primer3 {

    final static Logger logger = Logger.getLogger(Primer3.class);

    public static void main(String[] args) {
        // some definitions
        String personURI = "http://somewhere/JohnSmith";
        String givenName = "John";
        String familyName = "Smith";
        String fullName = givenName + " " + familyName;
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        // and add the properties cascading style
        @SuppressWarnings("unused") Resource johnSmith = model.createResource(personURI).addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N, model.createResource().addProperty(VCARD.Given, givenName)
                        .addProperty(VCARD.Family, familyName));

        // list the statements in the graph
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement(); // get next statement
            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object

            StringBuilder sbStatement = new StringBuilder("STATEMENT:" + subject + " " + predicate + " ");
            if (object instanceof Resource) {
                sbStatement.append(object.toString());
            } else {
                // object is a literal
                sbStatement.append(" \"" + object.toString() + "\"");
            }
            logger.info(sbStatement.toString());
        }
    }
}
