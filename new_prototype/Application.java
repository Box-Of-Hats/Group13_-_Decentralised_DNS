import java.util.Scanner;

class Application{

    public void listCommands(){
        // Print help text for application
        System.out.println();
        System.out.println("Syntax:\tCOMMAND Arg1 Arg2 ... ArgN");
        System.out.println("Commands:");
        System.out.println("\thelp - Display list of commands with options");
        System.out.println("\tstart - Start a new network as the first node in the system.");
        System.out.println("\tjoin [BootstrapIP] - Join a network via a bootstrap node IP.");
        System.out.println("\taddurl [URL to add] [IP to add] - Add a url to store in the system.");
        System.out.println("\tdelurl [URL to delete] - Delete a url from the system. (Currently Disabled)");
        System.out.println("\tlookup [URL] - Look up the IP address of a given URL.");
        System.out.println("\tinfo - Print information about the current node");
        System.out.println("\tquit - Exit the application and close all connections.");
    }


    public static void main(String[] args){

        /*
        CMD line application that allows users to input arguments during runtime
        with the following commands as a basic package:
        -Initiate Node: Creates a Node system using Node Factory //!! This is done automatically when the client is launched
        -Start Network: Creates a new Network from the node
        -Join Network: Joins a network from an inputted IP address
        -Add URL: gives the URL to the node to hash and assign to the correct node in the network
        -Look up URL: looks up the IP for the given URL from the node network
        -Delete a url

        */

        //Output introduction text:
        System.out.println("\nChord DNS Client ver 0.1337");
        Application app = new Application();
        app.listCommands();

        String commandIn; //The current input command string
        String[] commandList; //The current input command string, split into its components
        Boolean inSystem = false;
        
        //Initialise node, server and client on this application.
        NodeFactory factory = new NodeFactory();
        Node node = factory.makeNode();

        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.print("\n>");
            commandIn = scanner.nextLine();
            commandList = commandIn.split("\\s");
            String command = commandList[0];

            switch(command){
                case "help":
                case "h":
                    app.listCommands();
                    break;

                case "join":
                case "j":
                    if(inSystem == false){
                        if (commandList.length != 2){
                            System.out.println("Error:\tBad arguements passed to 'join'.");
                            System.out.println("\t1 argument required: Bootstrap IP");
                            System.out.println("\tTo start a new network without a bootstrap IP, use command 'start'");
                            break;
                        }
                        else{
                            String ipToConnectTo = commandList[1];
                            System.out.println("Attempting to join existing network via Bootstrap IP: " + ipToConnectTo);
                            try{ //Attempt to connect to system
                                node.join(ipToConnectTo);
                                inSystem = true;
                                System.out.println("Successfully joined network.");
                                break;
                            } catch (NullPointerException e){ //Catch error if the IP was incorrect.
                                System.out.println("Error:\tCould not find existing node with IP: " + ipToConnectTo);
                                System.out.println("\tPlease check that the IP you entered is correct.");
                                System.out.println("\tA network was not joined.");
                                break;
                            }
                        }
                    }
                    else {
                        System.out.println("Error:\tCould not join network");
                        System.out.println("\tNode is already in a network.");
                        System.out.println("\tA network was not joined.");
                        break;
                    }
        
                case "start":
                case "s":
                    if(inSystem == false){
                        System.out.println("Initialising new chord network...");
                        node.join();
                        inSystem = true;
                        System.out.println("Successfully created network.");
                        break;
                    }
                    else {
                        System.out.println("Error:\tCould not start new network");
                        System.out.println("\tNode is already in a network.");
                        System.out.println("\tA new network was not created.");
                        break;
                    }

                case "addurl":
                case "a":
                    if (inSystem == true){
                        if (commandList.length != 3){
                            System.out.println("Error:\tBad arguements passed to 'addurl'.");
                            System.out.println("\t2 arguments required: URL, IP");
                            break;
                        }
                        else {
                            String urlToAdd = commandList[1];
                            String ipToAdd = commandList[2];
                            System.out.println("Attempting to add URL/IP pair to the system...");
                            node.passData(urlToAdd, ipToAdd);
                            break;
                        }
                    }
                    else{
                        System.out.println("Error:\tCan not add url.") ;
                        System.out.println("\tNode is not connected to a network");
                        break;
                    }

                    

                case "delurl":
                case "d":
                    System.out.println("Error: Feature disabled in current version.");
                    break;
                    //Delete URL has been ommitted here to prevent standard users from deleting URLs.
                    //In the future, a system may be put in place to allow admins to delete them.
                    //Re-enable this feature here:
                    //if (inSystem == true){
                    //    if (commandList.length != 2){
                    //        System.out.println("Error:\tBad arguements passed to 'delurl'.");
                    //        System.out.println("\t1 argument1 required: URL");
                    //        break;
                    //    }
                    //    else {
                    //        String urlToDelete = commandList[1];
                    //        System.out.println("Deleting URL from system: " + urlToDelete);
                    //        //Create exception for deletedata, if the data is not in the system!
                    //        node.deleteData(urlToDelete);
                    //        break;
                    //    }
                    //}
                    //else {
                    //    System.out.println("Error:\tCould not delete URL");
                    //    System.out.println("\tNode is not currently in a network");
                    //    break;
                    //}


                case "lookup":
                case "l":
                    if (inSystem == true){
                        if (commandList.length != 2){
                            System.out.println("Error:\tBad arguements passed to 'lookup'.");
                            System.out.println("\t1 argument required: URL");
                            break;
                        }
                        else {
                            String urlToLookup = commandList[1];
                            System.out.println("Looking up URL: " + urlToLookup);
                            String ipFound = node.fetchData(urlToLookup);
                            if (ipFound.equals("null")){
                                System.out.println("Could not find URL: " + urlToLookup);
                                break;
                            }
                            else{
                                System.out.println("IP found: " + ipFound);
                                break;
                            }
                        }
                    }
                    else {
                        System.out.println("Error:\tCould not look up URL.");
                        System.out.println("\tNode is not currently in a network");
                        break;
                    }
                        

                case "info":
                case "i":
                    System.out.println("Node IP: " + node.getIp());
                    System.out.println("Current GUID: " + node.getGuid());
                    System.out.println("In a network?: " + inSystem);
                    break;

                case "quit":
                case "q":
                case "exit":
                    System.out.println("Closing Connections & Reallocating Data...");
                    node.quit();
                    System.out.println("Exiting System");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Command not found: '" + command + "'");
                    System.out.println("Type 'help' for a list of commands.");
                    break;
            }    
        }
    }
}