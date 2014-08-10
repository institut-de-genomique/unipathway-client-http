package fr.cea.ig.external.unipathway.client.http;


import java.util.List;

public class CrossRef {

    private final List<String[]>    mEnzymes;
    private final String            mDescription;
    private final String            mKeyword;
    private final String[]          mOntology;
    private final List< String[] >  mRheaReaction;
    private final List< String[] >  mMetacycReaction;
    private final List< String[] >  mKeggReaction;
    private final List< String[] >  mKeggMap;
    private final List< String[] >  mMetacycPathway;

    public CrossRef( final List<String[]> enzymes, final String description, final String keyword, final String[] ontology,
                     final List<String[]> rheaReaction, final List<String[]> metacycReaction, final List<String[]> keggReaction, final List<String[]> keggMap,
                     final List<String[]> metacycPathway ){
        mEnzymes        = enzymes;
        mDescription    = description;
        mKeyword        = keyword;
        mOntology       = ontology;
        mRheaReaction   = rheaReaction;
        mMetacycReaction= metacycReaction;
        mKeggReaction   = keggReaction;
        mKeggMap        = keggMap;
        mMetacycPathway = metacycPathway;

    }

    public List<String[]> getEnzymes() {
        return mEnzymes;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public String[] getOntology() {
        return mOntology;
    }

    public List<String[]> getmRheaReaction() {
        return mRheaReaction;
    }

    public List<String[]> getmMetacycReaction() {
        return mMetacycReaction;
    }

    public List<String[]> getKeggMap() {
        return mKeggMap;
    }

    public List<String[]> getMetacycPathway() {
        return mMetacycPathway;
    }
}
