package callbacks;

import models.CustomError;

public interface AuthCallback {

    public void success();

    public void error(CustomError customError);

}
