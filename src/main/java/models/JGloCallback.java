package models;

import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

public abstract class JGloCallback implements Callback {

    @Override
    public void failed(UnirestException e) {

    }

    @Override
    public void cancelled() {

    }
}
