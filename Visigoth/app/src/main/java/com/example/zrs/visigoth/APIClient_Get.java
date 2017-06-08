package com.example.zrs.visigoth;

/**
 * Created by nanditakannapadi on 3/31/17.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by sherriff on 10/25/16.
 */

public class APIClient_Get {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public String run(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        String responseString = response.body().string();
//        HashMap<String,String> map = new Gson().fromJson(responseString, new TypeToken<HashMap<String, String>>(){}.getType());

        return responseString;


//        Gist gist = gson.fromJson(response.body().charStream(), Gist.class);
//        for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue().content);
//        }
    }

//    static class Gist {
//        Map<String, GistFile> files;
//    }
//
//    static class GistFile {
//        String content;
//    }
//
//    static class Gist {
//        Map<String, GistFile> files;
//    }

    /*public class Gist {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("medium")
        @Expose
        private String medium;
        @SerializedName("payee_id")
        @Expose
        private String payeeId;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("transaction_date")
        @Expose
        private String transactionDate;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("payer_id")
        @Expose
        private String payerId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getPayeeId() {
            return payeeId;
        }

        public void setPayeeId(String payeeId) {
            this.payeeId = payeeId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPayerId() {
            return payerId;
        }

        public void setPayerId(String payerId) {
            this.payerId = payerId;
        }

    } */

}