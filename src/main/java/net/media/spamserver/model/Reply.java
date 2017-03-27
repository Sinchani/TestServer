package net.media.spamserver.model;

public class Reply
{
    private int spamResult;
    private ServerStatus serverStatus;

    public Reply(int spamResult, ServerStatus serverStatus) {
        this.spamResult = spamResult;
        this.serverStatus = serverStatus;
    }

    public static enum ServerStatus {
        OK,
        INTERNAL_SERVER_ERROR
    }

    public int getSpamResult() {
        return spamResult;
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setSpamResult(int spamResult) {
        this.spamResult = spamResult;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }
}
