class TestClient{
    /*
    Used to send a testing string to listening Node Server.
    */
    public static void main(String[] args){
        String ip = "2001:0:5ef5:79fb:34d7:215c:adf6:9260"; //Set IP of open server here
        String message = "cip,This request will print the node's calculated id"; //Set test message
        Client aClient = new Client();
        aClient.connectToServer(ip);
        aClient.pushMessage(message);
        aClient.disconnect();
    }
}