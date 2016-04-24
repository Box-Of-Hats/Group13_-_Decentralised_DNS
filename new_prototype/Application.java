import java.util.Scanner;

class Application{

    public void listCommands(){
        // Print help text for application
        System.out.println("Syntax: COMMAND,Arg1,Arg2 ... ,ArgN");
        System.out.println("Enter a command:\n");
        System.out.println("\thelp - Display list of commands with options");
        System.out.println("\tquit - Exit the application and close all connections.");
        System.out.println("\tjoin [BootstrapIP] - Join a network via a bootstrap node IP.");
        System.out.println("\taddurl [URL to add] [IP to add] - Add a url to store in the system.");
        System.out.println("\tstart - Start a network as the first node.");
        System.out.println("\tlookup [URL] - Look up the IP address of a given URL.");
    }


    public static void main(String[] args){

        /*
        CMD line application that allows users to input arguments during runtime
        with the following commands as a basic package:
        -Initiate Node: Creates a Node system using Node Factory
        -Start Network: Creates a new Network from the node
        -Join Network: Joins a network from an inputted IP address
        -Add URL: gives the URL to the node to hash and assign to the correct node in the network
        -Look up URL: looks up the IP for the given URL from the node network
        */

        //Check if arguements were supplied. If not, helpful text will be displayed containing all
        // possible commands with arguement parameters.
        String commandIn;
        String[] commandList;
        Application app = new Application();;

        System.out.println("\nChord DNS Client ver 0.1\n");
        app.listCommands();

        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.print(">");
            commandIn = scanner.next();
            commandList = commandIn.split(",");
            String command = commandList[0];

            switch(command){
                case "help":
                    app.listCommands();
                    break;

                case "join":
                    System.out.println(commandList.length);
                    if (commandList.length != 2){
                        System.out.println("Bad arguements passed to 'join'. 1 argument required: BootstrapIP");
                        break;
                    }
                    else{
                        System.out.println("Joining network via BootstrapIP: " + commandList[1]);
                        break;
                    }
        
                case "start":
                    System.out.println("Starting...");
                    break;

                case "addurl":
                    System.out.println("Adding url...");
                    break;

                case "lookup":
                    System.out.println("Looking up url...");
                    break;

                case "quit":
                    System.exit(0);
                    break;
                
                case "exit":
                    System.exit(0);
                    break;

                default:
                    break;
            }    
        }
    }
}