package common.build.request;

import common.model.User;
import common.util.Commands;

public class HelpReq extends Request {

    public HelpReq(User user) {
        super(Commands.HELP, user);
    }

    @Override
    public boolean isAuth() {
        return true;
    }
}