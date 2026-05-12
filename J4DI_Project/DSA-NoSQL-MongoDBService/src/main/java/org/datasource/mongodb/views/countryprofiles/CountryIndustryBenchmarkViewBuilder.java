package org.datasource.mongodb.views.countryprofiles;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.datasource.mongodb.MongoDataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryIndustryBenchmarkViewBuilder {

    private List<CountryIndustryBenchmarkView> countryIndustryBenchmarkViewList = new ArrayList<>();
    private List<Document> documents;

    private MongoDataSourceConnector dataSourceConnector;

    public CountryIndustryBenchmarkViewBuilder(MongoDataSourceConnector dataSourceConnector) {
        this.dataSourceConnector = dataSourceConnector;
    }

    public List<CountryIndustryBenchmarkView> getCountryIndustryBenchmarkViewList() {
        return countryIndustryBenchmarkViewList;
    }

    public CountryIndustryBenchmarkViewBuilder build() throws Exception {
        return this.select().map();
    }

    private CountryIndustryBenchmarkViewBuilder select() {
        MongoDatabase db = dataSourceConnector.getMongoDatabase();

        MongoCollection<Document> countryProfilesCollection =
                db.getCollection("country_ai_profiles");

        this.documents = new ArrayList<>();
        countryProfilesCollection.find().into(this.documents);

        System.out.println("SELECT CountryIndustryBenchmarkView: " + this.documents.size() + " documents processed.");

        return this;
    }

    private CountryIndustryBenchmarkViewBuilder map() {
        this.countryIndustryBenchmarkViewList = new ArrayList<>();

        for (Document document : this.documents) {
            String country = document.getString("country");
            String region = document.getString("region");

            List<Document> industryBenchmarks =
                    document.getList("industry_benchmarks", Document.class);

            if (industryBenchmarks == null) {
                continue;
            }

            for (Document benchmark : industryBenchmarks) {
                Document adoption = benchmark.get("adoption", Document.class);
                Document risk = benchmark.get("risk", Document.class);

                CountryIndustryBenchmarkView view = new CountryIndustryBenchmarkView();

                view.setCountry(country);
                view.setRegion(region);
                view.setIndustry(benchmark.getString("industry"));

                if (adoption != null) {
                    view.setAvgAiAdoptionRate(getDoubleValue(adoption, "avg_ai_adoption_rate"));
                    view.setAvgAiMaturityScore(getDoubleValue(adoption, "avg_ai_maturity_score"));
                }

                if (risk != null) {
                    view.setAvgAiFailureRate(getDoubleValue(risk, "avg_ai_failure_rate"));
                }

                this.countryIndustryBenchmarkViewList.add(view);
            }
        }

        System.out.println("MAP CountryIndustryBenchmarkView: "
                + this.countryIndustryBenchmarkViewList.size()
                + " rows generated.");

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
}