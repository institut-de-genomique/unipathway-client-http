package fr.cea.ig.external.unipathway.client.http;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class UniPathwayTest {

    @Test
    public void testGetPathway() throws Exception {
        List<List<String>> variants = UniPathway.getVariant("UPA00034");

        assertTrue( variants.size() == 4 );


        assertTrue( variants.get(0).size() == 3 );
        assertTrue( variants.get(0).get(0).equals( "ULS00006" ) );
        assertTrue( variants.get(0).get(1).equals( "ULS00010" ) );
        assertTrue(variants.get(0).get(2).equals("ULS00011"));

        assertTrue( variants.get(1).size() == 4 );
        assertTrue( variants.get(1).get(0).equals( "ULS00006" ) );
        assertTrue( variants.get(1).get(1).equals( "ULS00008" ) );
        assertTrue( variants.get(1).get(2).equals( "ULS00009" ) );
        assertTrue(variants.get(1).get(3).equals("ULS00011"));


        assertTrue( variants.get(2).size() == 4 );
        assertTrue( variants.get(2).get(0).equals( "ULS00006" ) );
        assertTrue( variants.get(2).get(1).equals( "ULS00007" ) );
        assertTrue( variants.get(2).get(2).equals( "ULS00009" ) );
        assertTrue( variants.get(2).get(3).equals( "ULS00011" ) );


        assertTrue( variants.get(3).size() == 4 );
        assertTrue( variants.get(3).get(0).equals( "ULS00006" ) );
        assertTrue( variants.get(3).get(1).equals( "ULS00227" ) );
        assertTrue( variants.get(3).get(2).equals( "ULS00009" ) );
        assertTrue( variants.get(3).get(3).equals( "ULS00011" ) );
    }

    @Test
    public void testGetCrossRefUPA() throws Exception {
        CrossRef ref = UniPathway.getCrossRef("UPA00034");
        assertTrue(ref.getDescription().equals("402.34 Amino-acid biosynthesis; L-lysine biosynthesis via DAP pathway"));
        assertTrue( ref.getKeyword().equals("no mapping") );
        assertTrue( ref.getKeggMap().size() == 5 );
        assertTrue(ref.getKeggMap().get(2)[0].equals("map00300"));
        assertTrue( ref.getMetacycPathway().size() == 4 );
        assertTrue(ref.getMetacycPathway().get(1)[2].equals("(2 / 9 reactions in common)"));
    }

    @Test
    public void testGetCrossRefUER() throws Exception {
        CrossRef ref = UniPathway.getCrossRef("UER00015");
        assertTrue(ref.getEnzymes().get(0)[0].equals("EC 2.7.2.4"));
        assertTrue( ref.getOntology()[0].equals("GO:0004072") );
    }

    @Test
    public void testGetTermUPA() throws Exception {
        Term term = UniPathway.getTerm("UPA00034");
        assertTrue( term.getType().equals( "Pathway" ) );
        assertTrue(term.getIdentifier().equals("UPA00034"));
        assertTrue(term.getAccessionNumber() == 34);
    }

    @Test
    public void testGetMetacycMapping() throws Exception {
        Map< String, List< String > > data    = UniPathway.getMappedReactionsFromPathway("UPA00033");
        assertTrue(data.containsKey("Label"));
        assertTrue( data.containsKey( "Enzymatic-reaction"      ) );
        assertTrue( data.containsKey( "Rhea master reaction"    ) );
        assertTrue( data.containsKey( "MetaCyc reaction"        ) );
        assertTrue( data.containsKey( "MetaCyc pathway"         ) );

        assertTrue(data.get("Label").get(0).equals("L-alpha-aminoadipate from 2-oxoglutarate: step 1/5"));
        assertTrue(data.get("Enzymatic-reaction").get(0).equals("UER00028"));
        assertTrue(data.get("Rhea master reaction").get(0).equals("-"));
        assertTrue(data.get("MetaCyc reaction").get(0).equals("HOMOCITRATE-SYNTHASE-RXN"));

        assertTrue( data.get( "Label" ).get(6).equals( "L-lysine from L-alpha-aminoadipate (Thermus route): step 2/5" ) );
        assertTrue( data.get( "Enzymatic-reaction" ).get(6).equals( "UER00036" ) );
        assertTrue( data.get( "Rhea master reaction" ).get(6).equals( "-" ) );
        assertTrue( data.get( "MetaCyc reaction" ).get(6).equals( "RXN-5182" ) );

        assertTrue( data.get( "Label" ).get(13).equals( "L-lysine from L-alpha-aminoadipate (fungal route): step 3/3" ) );
        assertTrue( data.get( "Enzymatic-reaction" ).get(13).equals( "UER00034" ) );
        assertTrue( data.get( "Rhea master reaction" ).get(13).equals( "-" ) );
        assertTrue( data.get( "MetaCyc reaction" ).get(13).equals( "1.5.1.7-RXN" ) );
    }
}
