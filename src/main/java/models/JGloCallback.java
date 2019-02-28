package models;

import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

// TODO: Complete this class
public abstract class JGloCallback implements Callback {

    @Override
    public void failed(UnirestException e) {
       // Messages.clearCache();
    }

    @Override
    public void cancelled() {

    }
}
