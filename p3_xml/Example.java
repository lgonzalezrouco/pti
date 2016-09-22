import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


/**
 * This class runs all the example code from the article.  There is a method
 * for each listing.  The listing that each method represents is listed in the
 * javadoc for the method.  This class also has a main method that will allow
 * you to execute any of the listings.  Run this class with no parameters to
 * get usage information.
 * This class was tested with:
 * xerces version 1.3.0
 * xalan version 2.0.1
 * jdom version beta6
 * jdk version 1.2
 *
 * @author Harry Evans (harry@tralfamadore.com)
 * @author Wes Biggs (wes@tralfamadore.com)
 */
/**
 * I've modified to run with jdom-1.0, xalan-j_2_7_0 and java 1.5.0
 * and corrected listing numbers.
 * 	Manel Guerrero
 */

public class Example {

    /**
     * Read and parse an xml document from the file at example.xml.
     * This method corresponds to the code in Listing 12.
     * @return the JDOM document parsed from the file.
     */
    public static Document readDocument() {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document anotherDocument = builder.build(new File("example.xml"));
            return anotherDocument;
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a JDOM document with elements that represent the
     * properties of a car.
     * This method corresponds to Listing 2,3,4,5,6 and 7.
     * @return a JDOM Document that represents the properties of a car.
     */
    public static Document createDocument() {
        // Create the root element
        Element carElement = new Element("car");
        //create the document
        Document myDocument = new Document(carElement);
        //add an attribute to the root element
        // carElement.addAttribute(new Attribute("vin", "123fhg5869705iop90"));
        carElement.setAttribute(new Attribute("vin", "123fhg5869705iop90"));

        //add a comment
        carElement.addContent(new Comment("Description of a car"));

        //add some child elements
        /*
         * Note that this is the first approach to adding an element and
         * textual content.  The second approach is commented out.
         */
        Element make = new Element("make");
        make.addContent("Toyota");
        carElement.addContent(make);
        //carElement.addContent(new Element("make").addContent("Toyota"));

        //add some more elements
        carElement.addContent(new Element("model").addContent("Celica"));
        carElement.addContent(new Element("year").addContent("1997"));
        carElement.addContent(new Element("color").addContent("green"));
        // carElement.addContent(new Element("license").addContent("1ABC234").addAttribute("state", "CA"));
        carElement.addContent(new Element("license").addContent("1ABC234").setAttribute("state", "CA"));

        return myDocument;
    }

    /**
     * This method accesses a child element of the root element of the
     * document built in listing 2 with the createDocument method.
     * This method corresponds to Listing 8.
     * @param myDocument the JDOM document built from Listing 2
     */
    public static void accessChildElement(Document myDocument) {
        //some setup
        Element carElement = myDocument.getRootElement();

        //Access a child element
        Element yearElement = carElement.getChild("year");

        //show success or failure
        if(yearElement != null) {
            System.out.println("Here is the element we found: " +
                yearElement.getName() + ".  Its content: " +
                yearElement.getText() + "\n");
        } else {
            System.out.println("Something is wrong.  We did not find a year Element");
        }
    }

    /**
     * This method removes a child element from a document.  The document
     * should be of the format created in Listing 2.
     * This method corresponds to Listing 9.
     * @param myDocument the JDOM document built from Listing 2.
     */
    public static void removeChildElement(Document myDocument) {
        //some setup
        System.out.println("About to remove the year element.\nThe current document:");
        outputDocument(myDocument);
        Element carElement = myDocument.getRootElement();

        //remove a child Element
        boolean removed = carElement.removeChild("year");

        //show success or failure
        if(removed) {
            System.out.println("Here is the modified document without year:");
            outputDocument(myDocument);
        } else {
            System.out.println("Something happened.  We were unable to remove the year element.");
        }
    }

    /**
     * This method shows how to use XMLOutputter to output a JDOM document to
     * the stdout.
     * This method corresponds to Listing 10.
     * @param myDocument the JDOM document built from Listing 2.
     */
    public static void outputDocument(Document myDocument) {
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(myDocument, System.out);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method shows how to use XMLOutputter to output a JDOM document to
     * a file located at myFile.xml.
     * This method corresponds to Listing 11.
     * @param myDocument the JDOM document built from Listing 2.
     */
    public static void outputDocumentToFile(Document myDocument) {
        //setup this like outputDocument
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter();

            //output to a file
            FileWriter writer = new FileWriter("myFile.xml");
            outputter.output(myDocument, writer);
            writer.close();

        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method takes a JDOM document in memory, an xsl file at example.xslt,
     * and outputs the results to stdout.
     * This method corresponds to Listing 14.
     * @param myDocument the JDOM document built from Listing 2.
     */
    public static void executeXSL(Document myDocument) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
            // Make the input sources for the XML and XSLT documents
            org.jdom.output.DOMOutputter outputter = new org.jdom.output.DOMOutputter();
            org.w3c.dom.Document domDocument = outputter.output(myDocument);
            javax.xml.transform.Source xmlSource = new javax.xml.transform.dom.DOMSource(domDocument);
            StreamSource xsltSource = new StreamSource(new FileInputStream("example.xslt"));
			//Make the output result for the finished document
            /*
             * Note that here we are just going to output the results to the
             * System.out, since we don't actually have a HTTPResponse object
             * in this example
             */
            //StreamResult xmlResult = new StreamResult(response.getOutputStream());
            StreamResult xmlResult = new StreamResult(System.out);
			//Get a XSLT transformer
			Transformer transformer = tFactory.newTransformer(xsltSource);
			//do the transform
			transformer.transform(xmlSource, xmlResult);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
		} catch(TransformerException e) {
            e.printStackTrace();
        } catch(org.jdom.JDOMException e) {
            e.printStackTrace();
        }
	}

    /**
     * Main method that allows the various methods to be used.
     * It takes a single command line parameter.  If none are
     * specified, or the parameter is not understood, it prints
     * its usage.
     */
    public static void main(String argv[]) {
        if(argv.length == 1) {
            String command = argv[0];
            if(command.equals("create")) outputDocument(createDocument());
            else if(command.equals("access")) accessChildElement(createDocument());
            else if(command.equals("remove")) removeChildElement(createDocument());
            else if(command.equals("output")) outputDocument(createDocument());
            else if(command.equals("file")) outputDocumentToFile(createDocument());
            else if(command.equals("read")) outputDocument(readDocument());
            else if(command.equals("xsl")) executeXSL(createDocument());
            else {
                System.out.println(command + " is not a valid option.");
                printUsage();
            }
        } else {
            printUsage();
        }
    }

    /**
     * Convience method to print the usage options for the class.
     */
    public static void printUsage() {
        System.out.println("Usage: Article [option] \n where option is one of the following:");
        System.out.println("  create - create a document as shown in Listing 2-7");
        System.out.println("  access - access a child element as shown in Listing 8");
        System.out.println("  remove - remove a child element as shown in Listing 9");
        System.out.println("  output - output a document to the console as shown in Listing 10");
        System.out.println("  file   - output a document to myFile.xml as shown in Listing 11");
        System.out.println("  read   - parse a document from example.xml as shown in Listing 12");
        System.out.println("  xsl    - transform a document as shown in Listing 14");
    }
}
