package com.alok.pixabay

import com.alok.pixabay.model.PixabayApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PixabaySampleDataProvider {

    fun getSamplePixabayApiValidResponse(): PixabayApiResponse {
        /**
         * this response is copy pasted for query = "fruits" and page = "1"
         * to keep the response small, I've added only one object in the JSON response
         * */

        val rawData = "{\n" +
                "   \"total\": 35585,\n" +
                "   \"totalHits\": 500,\n" +
                "   \"hits\": [\n" +
                "      {\n" +
                "         \"id\": 2310212,\n" +
                "         \"pageURL\": \"https://pixabay.com/illustrations/fruit-lemon-art-pattern-design-2310212/\",\n" +
                "         \"type\": \"illustration\",\n" +
                "         \"tags\": \"fruit, lemon, art\",\n" +
                "         \"previewURL\": \"https://cdn.pixabay.com/photo/2017/05/13/17/31/fruit-2310212_150.jpg\",\n" +
                "         \"previewWidth\": 150,\n" +
                "         \"previewHeight\": 150,\n" +
                "         \"webformatURL\": \"https://pixabay.com/get/g60e07d1af902d8ea558c1d9b700907dce16899bb4af9afcb7e9ffef87f3b7bfcedfda821ed13253e4930c959dfe0401ca979034e059d2fdde269d91a91129543_640.jpg\",\n" +
                "         \"webformatWidth\": 640,\n" +
                "         \"webformatHeight\": 640,\n" +
                "         \"largeImageURL\": \"https://pixabay.com/get/gf972bf58323ea75c7f145d2f38e342e93d7218efdfd67526a53e6a687a16ae6f823ec8f18a207bef5aea34164a5015eef8ed39da8124f7a737169fb198709145_1280.jpg\",\n" +
                "         \"imageWidth\": 2048,\n" +
                "         \"imageHeight\": 2048,\n" +
                "         \"imageSize\": 715632,\n" +
                "         \"views\": 183013,\n" +
                "         \"downloads\": 84563,\n" +
                "         \"collections\": 1543,\n" +
                "         \"likes\": 918,\n" +
                "         \"comments\": 151,\n" +
                "         \"user_id\": 1216526,\n" +
                "         \"user\": \"Yousz\",\n" +
                "         \"userImageURL\": \"\"\n" +
                "      }" +
                "   ]" +
                "}"
        val type = object: TypeToken<PixabayApiResponse>() {}.type
        return Gson().fromJson(rawData, type) as PixabayApiResponse
    }

    fun getSamplePixabayApiCorruptedResponse(): PixabayApiResponse? {
        val rawData = ""
        val type = object: TypeToken<PixabayApiResponse>() {}.type
        return Gson().fromJson(rawData, type) as PixabayApiResponse?
    }
}