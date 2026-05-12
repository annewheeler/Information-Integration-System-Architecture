package org.datasource.mongodb.views.countryprofiles;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.datasource.mongodb.MongoDataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryAiProfileViewBuilder {

    private List<CountryAiProfileView> countryAiProfileViewList = new ArrayList<>();

    private MongoDataSourceConnector dataSourceConnector;

    public CountryAiProfileViewBuilder(MongoDataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public List<CountryAiProfileView> getCountryAiProfileViewList() {
        return countryAiProfileViewList;
    }

    public CountryAiProfileViewBuilder build() throws Exception {
        return this.select().map();
    }

    private List<Document> documents;

    private CountryAiProfileViewBuilder select() {
        MongoDatabase db = dataSourceConnector.getMongoDatabase();

        MongoCollection<Document> countryProfilesCollection =
                db.getCollection("country_ai_profiles");

        this.documents = new ArrayList<>();
        countryProfilesCollection.find().into(this.documents);

        System.out.println("SELECT CountryAiProfileView: " + this.documents.size() + " documents processed.");

        return this;
    }

    private CountryAiProfileViewBuilder map() {
        this.countryAiProfileViewList = new ArrayList<>();

        for (Document document : this.documents) {
            Document macroIndicators = document.get("macro_indicators", Document.class);
            Document aiEcosystem = document.get("ai_ecosystem", Document.class);

            CountryAiProfileView view = new CountryAiProfileView();

            view.setCountry(document.getString("country"));
            view.setRegion(document.getString("region"));

            if (macroIndicators != null) {
                view.setGdpPerCapita(getDoubleValue(macroIndicators, "gdp_per_capita"));
                view.setInternetPenetration(getDoubleValue(macroIndicators, "internet_penetration"));
                view.setDigitalMaturityIndex(getDoubleValue(macroIndicators, "digital_maturity_index"));
            }

            if (aiEcosystem != null) {
                view.setCountryAiPolicy(aiEcosystem.getString("country_ai_policy"));
                view.setAiPatentFilings2024(getIntegerValue(aiEcosystem, "ai_patent_filings_2024"));
                view.setAiResearchersPerMillion(getDoubleValue(aiEcosystem, "ai_researchers_per_million"));
            }

            this.countryAiProfileViewList.add(view);
        }

        return this;
    }

    private Double getDoubleValue(Document document, String fieldName) {
        Object value = document.get(fieldName);

        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        return Double.valueOf(value.toString());
    }

    private Integer getIntegerValue(Document document, String fieldName) {
        Object value = document.get(fieldName);

        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        return Integer.valueOf(value.toString());
    }
}