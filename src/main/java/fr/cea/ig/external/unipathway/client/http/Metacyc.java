package fr.cea.ig.external.unipathway.client.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metacyc {
    static final String url= "http://www.grenoble.prabi.fr/obiwarehouse/unipathway/upa/chemical_view/mapping_metacyc/partofpage.html";

    static public Map< String, List< String > > getReactionsFromPathway( final String id ) throws IOException {
        Document                        doc     = Jsoup.connect(url + "?upid=" + id).get();;
        Map<Integer, String >           ref     = new HashMap<Integer, String >();
        Map< String, List< String > >   header  = new HashMap< String, List< String > >();
        int                             index   = 0;
        int                             maxindex= 0;
        String                          key     = null;

        Element table = doc.select("div#mapping_metacyc_main > table.small").first();

        for ( Element row : table.select("tr > th") ) {
            header.put(row.text(), new ArrayList());
            ref.put( index, row.text() );
            index++;
        }

        maxindex    = --index;
        index       = 0;

        for ( Element row : table.select("tr > td") ) {
            key = ref.get( index );
            header.get(key).add( row.text() );
            index = ( index >= maxindex )? 0 : ++index;
        }

        return header;
    }
}
