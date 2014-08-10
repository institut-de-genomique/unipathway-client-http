package fr.cea.ig.external.unipathway.client.http;


import java.util.List;

public class Term {
    private final String        mType;
    private final String        mIdentifier;
    private final int           mAccessionNumber;
    private final String        mName;
    private final List<String>  mSynonyms;
    private final String        mDescription;
    private final String        mStatus;

    public String getType() {
        return mType;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public int getAccessionNumber() {
        return mAccessionNumber;
    }

    public String getName() {
        return mName;
    }

    public List<String> getSynonyms() {
        return mSynonyms;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getStatus() {
        return mStatus;
    }

    public Term(final String type, final String identifier, final int accessionNumber, final String mame, final List<String> synonyms, final String description, final String status) {
        mType              = type;
        mIdentifier        = identifier;
        mAccessionNumber   = accessionNumber;
        mName              = mame;
        mSynonyms          = synonyms;
        mDescription       = description;
        mStatus            = status;
    }
}
