package com.api;


import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public class CrptApi {

    private static final String HONEST_SIGN_API_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private static final String STANDARD_VALUE = "string";

    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(5L, 5L);
        Document document = crptApi.new Document(crptApi.new DocumentDesc(STANDARD_VALUE), STANDARD_VALUE, STANDARD_VALUE, "LP_INTRODUCE_GOODS", true, STANDARD_VALUE, STANDARD_VALUE, STANDARD_VALUE, LocalDate.of(2020, 1, 23).toString(), STANDARD_VALUE, List.of(crptApi.new Product(STANDARD_VALUE, LocalDate.of(2020, 1, 23).toString(), STANDARD_VALUE, STANDARD_VALUE, STANDARD_VALUE, LocalDate.of(2020, 1, 23).toString(), STANDARD_VALUE, STANDARD_VALUE, STANDARD_VALUE)), LocalDate.of(2020, 1, 23).toString(), STANDARD_VALUE);

        IntStream.range(0, 50).forEach(i -> {
            crptApi.getRateLimiter().acquire();
            crptApi.sendDocument(document, "");
        });
    }
    private final RateLimiter rateLimiter;
    private final long timeUnit; // time in seconds

    private final long requestLimit;

    CrptApi(long timeUnit, long requestLimit) {
        if (requestLimit < 0) {
            throw new InvalidParameterException("Number of calls less than 0 not allowed");
        }
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.rateLimiter = RateLimiter.create((double)requestLimit / (double)timeUnit);
    }

    public long getTimeUnit() {
        return timeUnit;
    }

    public long getRequestLimit() {
        return requestLimit;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public void sendDocument(Document document, String sign) {
        Gson gson = new Gson();
        String documentJsonString = gson.toJson(document);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(HONEST_SIGN_API_URL)).POST(HttpRequest.BodyPublishers.ofString(documentJsonString)).build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public class Document {
        private final DocumentDesc description;
        private final String doc_id;
        private final String doc_status;
        private final String doc_type;
        private final Boolean importRequest;
        private final String owner_inn;
        private final String participant_inn;
        private final String producer_inn;
        private final String production_date;
        private final String production_type;
        private final List<Product> products;
        private final String reg_date;
        private final String reg_number;

        public Document(DocumentDesc description, String doc_id, String doc_status, String doc_type, Boolean importRequest, String owner_inn, String participant_inn, String producer_inn, String production_date, String production_type, List<Product> products, String reg_date, String reg_number) {
            this.description = description;
            this.doc_id = doc_id;
            this.doc_status = doc_status;
            this.doc_type = doc_type;
            this.importRequest = importRequest;
            this.owner_inn = owner_inn;
            this.participant_inn = participant_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.production_type = production_type;
            this.products = products;
            this.reg_date = reg_date;
            this.reg_number = reg_number;
        }

        public DocumentDesc getDescription() {
            return description;
        }

        public String getDoc_id() {
            return doc_id;
        }

        public String getDoc_status() {
            return doc_status;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public Boolean getImportRequest() {
            return importRequest;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public String getParticipant_inn() {
            return participant_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public String getProduction_date() {
            return production_date;
        }

        public String getProduction_type() {
            return production_type;
        }

        public List<Product> getProducts() {
            return products;
        }

        public String getReg_date() {
            return reg_date;
        }

        public String getReg_number() {
            return reg_number;
        }
    }

    public class DocumentDesc {
        private final String participantInn;

        DocumentDesc(String participantInn) {
            this.participantInn = participantInn;
        }

        public String getParticipantInn() {
            return participantInn;
        }
    }

    public class Product {
        private final String certificate_document;
        private final String certificate_document_date;
        private final String certificate_document_number;
        private final String owner_inn;
        private final String producer_inn;
        private final String production_date;
        private final String tnved_code;
        private final String uit_code;
        private final String uitu_code;

        public Product(String certificate_document, String certificate_document_date, String certificate_document_number, String owner_inn, String producer_inn, String production_date, String tnved_code, String uit_code, String uitu_code) {
            this.certificate_document = certificate_document;
            this.certificate_document_date = certificate_document_date;
            this.certificate_document_number = certificate_document_number;
            this.owner_inn = owner_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.tnved_code = tnved_code;
            this.uit_code = uit_code;
            this.uitu_code = uitu_code;
        }

        public String getCertificate_document() {
            return certificate_document;
        }

        public String getCertificate_document_date() {
            return certificate_document_date;
        }

        public String getCertificate_document_number() {
            return certificate_document_number;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public String getProduction_date() {
            return production_date;
        }

        public String getTnved_code() {
            return tnved_code;
        }

        public String getUit_code() {
            return uit_code;
        }

        public String getUitu_code() {
            return uitu_code;
        }
    }
}
