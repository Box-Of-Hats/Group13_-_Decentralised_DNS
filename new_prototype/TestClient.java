class TestClient{
    /*
    Used to send a testing string to listening Node Server.
    */
    public static void main(String[] args){
        String ip = ""; //Set IP of open server here
        String message = "This is a testing string."; //Set test message here.
        Client aClient = new Client();
        aClient.connectToServer(ip);
        aClient.pushMessage(message);
        aClient.disconnect();
    }
}