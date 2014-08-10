package fr.cea.ig.external.unipathway.client.http;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MetacycTest {

    @Test
    public void testGetMetacycMapping() throws IOException {
        Map< String, List< String > >   data    = Metacyc.getReactionsFromPathway( "UPA00033" );
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
