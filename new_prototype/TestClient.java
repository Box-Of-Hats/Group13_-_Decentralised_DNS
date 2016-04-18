class TestClient{
    /*
    Used to send a testing string to listening Node Server.
    args[0] = ip to connect to
    args[1] = message to send (NOT IMPLEMENTED YET)
    */
    public static void main(String[] args){
        String ip = args[0]; //Set IP of open server here
        String message = "SPD,192.168.0.1;1"; //Set test message
        Client aClient = new Client();
        aClient.connectToServer(ip);
        aClient.pushMessage(message);
        aClient.disconnect();
    }
}