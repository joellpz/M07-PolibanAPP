package m07.joellpz.poliban.tools;

import m07.joellpz.poliban.model.MsgModal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Retrofit API interface for making HTTP requests.
 */
public interface RetrofitAPI {

    /**
     * Sends a GET request to the specified URL and retrieves a message.
     *
     * @param url The URL to send the GET request to.
     * @return A Call object representing the asynchronous HTTP request.
     */
    @GET
    Call<MsgModal> getMessage(@Url String url);
}
