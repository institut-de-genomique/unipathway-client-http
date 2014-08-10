package fr.cea.ig.external.unipathway.client.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UniPathway {
    private static final String BASE_URL        = "http://www.grenoble.prabi.fr/obiwarehouse/unipathway/";
    private static final String VARIANT_URL     = "/overview/PartOf_relationship/main";
    private static final String CROSSREF_URL    = "/overview/cross_ref/main";
    private static final String TERM_DEFINITION = "/overview/term_definition/main";
    private static final String METACYC_MAPPING = "/chemical_view/mapping_metacyc/partofpage.html";

    private static String getType( final String id ) throws Exception {
        String type;
        if ( id.startsWith("UPA") )
            type = "upa";
        else if ( id.startsWith( "ULS" ) )
            type = "uls";
        else if ( id.startsWith( "UER" ) )
            type = "uer";
        else if ( id.startsWith( "UCR" ) )
            type = "ucr";
        else if ( id.startsWith( "UPC" ) )
            type = "upc";
        else
            throw new Exception( "Unknown type: " + id.substring(0,3) );

        return type;
    }

    static public Map< String, List< String > > getMappedReactionsFromPathway( final String id ) throws Exception {
        final String                    type    = getType( id );
        if( !type.equals( "upa" ) )
            throw new Exception( "Only upa type is supported not: " + type );
        Document                        doc     = Jsoup.connect( BASE_URL + type + METACYC_MAPPING + "?upid=" + id).get();
        Map<Integer, String >           ref     = new HashMap<>();
        Map< String, List< String > >   header  = new HashMap<>();
        int                             index   = 0;
        int                             maxindex;
        String                          key;

        Element table = doc.select("div#mapping_metacyc_main > table.small").first();

        for ( Element row : table.select("tr > th") ) {
            header.put(row.text(), new ArrayList< String >());
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

    static public List<List< String >> getVariant( final String id ) throws Exception {
        Document                doc             = Jsoup.connect( BASE_URL + getType( id ) + VARIANT_URL + "?upid=" + id).get();
        List< List< String > >  variants        = new ArrayList<>();
        List< String >          variant;
        Elements                tableVariants   = doc.select("table  table.small");

        for( Element tableVariant: tableVariants ){
            variant = new ArrayList<>();
            for( Element row : tableVariant.select("tr > td") )
                variant.add( row.text() );
            variants.add( variant );
        }

        return variants;
    }
    
    static public CrossRef getCrossRef( final String id ) throws Exception {
        Document            doc             = Jsoup.connect(BASE_URL + getType( id ) + CROSSREF_URL + "?upid=" + id).get();
        Elements            rows            = doc.select("table.small > tbody > tr:has(th)");
        String              rowType;
        Element             column;
        Elements            subRows;
        String              description     = null;
        String              keyword         = null;
        String[]            ontology        = null;
        List< String[] >    rheaReaction    = new ArrayList<>();
        List< String[] >    metacycReaction = new ArrayList<>();
        List< String[] >    keggReaction    = new ArrayList<>();
        List< String[] >    enzymes         = new ArrayList<>();
        String[]            tmp;
        int                 index;
        List< String[] >    keggMap         = new ArrayList<>();
        List< String[] >    metacycPathway  = new ArrayList<>();
        final Pattern       ontoPattern     = Pattern.compile( "(GO:\\d+) (.+)" );
        Matcher             matcher;

        for( Element row : rows ){
            rowType = row.select("th").first().text();
            column  = row.select("td").first();
            switch ( rowType ){
                case "UniProt CC-PATHWAY":
                    description = column.text();
                    break;
                case "UniProt Keyword":
                    keyword = column.text();
                    break;
                case "Gene Ontology":
                    ontology = new String[2];
                    subRows = column.select("span > span");
                    if( subRows.size() == 0 ) {
                        matcher = ontoPattern.matcher(column.select("span").first().childNodes().get(0).toString());
                        if (!matcher.find())
                            throw new Exception("Ontology badly formatted");
                        ontology[0] = matcher.group(1);
                        ontology[1] = matcher.group(2);
                    }
                    else{
                        if(  subRows.get( 0 ).text().startsWith( "GO" ) ) {
                            ontology[0] = subRows.get(0).text();
                            ontology[1] = subRows.get(1).text();
                        }
                        else{
                            ontology[0] = subRows.get(1).text();
                            ontology[1] = subRows.get(2).text();
                        }
                    }
                    break;
                case "RhEA reaction":
                    for (Element subRow : column.select("td > span") ) {
                        tmp = new String[2];
                        tmp[0] = subRow.select("span > a").first().text();
                        tmp[1] = subRow.select("span > span").first().text();
                        rheaReaction.add(tmp);
                    }
                    break;
                case "MetaCyc reaction":
                    for (Element subRow : column.select("span") ) {
                        tmp = new String[2];
                        tmp[0] = subRow.select("a").first().text();
                        tmp[1] = subRow.childNodes().get(0).toString();
                        metacycReaction.add(tmp);
                    }
                    break;
                case "KEGG reaction":
                    for (Element subRow : column.select("span") ) {
                        tmp = new String[2];
                        tmp[0] = subRow.select("a").first().text();
                        tmp[1] = subRow.childNodes().get(0).toString();
                        keggReaction.add(tmp);
                    }
                    break;
                case "KEGG map":
                    subRows = column.select("table > tbody > tr");
                    if( subRows != null ) {
                        for (Element subRow : subRows) {
                            tmp = new String[3];
                            Elements subColumns = subRow.select("td");
                            if (subColumns.size() > 3)
                                throw new Exception("KEGG map table badly formatted");
                            for (index = 0; index < subColumns.size(); index++) {
                                tmp[index] = subColumns.get(index).text();
                            }
                            keggMap.add(tmp);
                        }
                    }
                    else{
                        for (Element subRow : column.select("span") ) {
                            tmp = new String[3];
                            tmp[0] = subRow.select("a").first().text();
                            tmp[1] = subRow.childNodes().get(0).toString();
                            keggMap.add(tmp);
                        }
                    }
                    break;
                case "MetaCyc pathway":
                    for( Element subRow :  column.select( "table > tbody > tr" ) ){
                        tmp = new String[3];
                        Elements subColumns = subRow.select( "td" );
                        if( subColumns.size() > 3 )
                            throw new Exception("MetaCyc pathway table badly formatted");
                        for(  index = 0; index < subColumns.size(); index++ ){
                            tmp[index] = subColumns.get( index ).text();
                        }
                        metacycPathway.add( tmp );
                    }
                    break;
                case "ENZYME":
                    for( Element subRow :  column.select( "td > span" ) ) {
                        tmp = new String[2];
                        Elements subColumns = subRow.select( "span > span" );
                        if( subColumns.size() != 2 )
                            throw new Exception("ENZYME field badly formatted");
                        tmp[0] = subColumns.get( 0 ).text();
                        tmp[1] = subColumns.get( 1 ).text();
                        enzymes.add( tmp );
                    }
                    break;
                default:
                    throw new Exception( "Unknown row: " + rowType );
            }
        }
        return new CrossRef( enzymes, description, keyword, ontology, rheaReaction, metacycReaction, keggReaction, keggMap, metacycPathway );
    }


    static public Term getTerm( final String id ) throws Exception {
        Document        doc             = Jsoup.connect(BASE_URL + getType(id) + TERM_DEFINITION + "?upid=" + id).get();
        Elements        rows            = doc.select("table.small > tbody > tr:has(th)");
        String          rowType;
        Element         column;
        Elements        subRows;
        String          type            = null;
        String          identifier      = null;
        int             accessionNumber = 0;
        String          name            = null;
        List<String>    synonyms        = new ArrayList<>();
        String          description     = null;
        String          status          = null;


        for( Element row : rows ) {
            rowType = row.select("th").first().text();
            column  = row.select("td").first();
            switch (rowType) {
                case "Class":
                    type = column.text();
                    break;
                case "Identifier":
                    identifier = column.select( "td > span").text();
                    break;
                case "Accession number":
                    accessionNumber = Integer.parseInt(column.select("td > span").text());
                    break;
                case "Name":
                    name = column.select( "td > span").text();
                    break;
                case "Synonym(s)":
                    subRows = column.select( "td > span" );
                    synonyms.addAll(
                                        subRows.stream()
                                               .map(Element::text)
                                               .collect(Collectors.toList())
                                   );
                    break;
                case "Short description":
                    description = column.select( "td > span").text();
                    break;
                case "Status":
                    status = column.text();
                    break;
            }
        }
        return new Term( type, identifier, accessionNumber, name, synonyms, description, status);
    }
}
