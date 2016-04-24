class Application{

    public static void main(String[] args){

        /*
        JAY: I DONT THINK THIS IS HOW WE SHOULD DO IT
        I think it should be a CMD line application that allows users to input arguments during runtime
        with the following commands as a basic package:
        -Initiate Node: Creates a Node system using Node Factory
        -Start Network: Creates a new Network from the node
        -Join Network: Joins a network from an inputted IP address
        -Add URL: gives the URL to the node to hash and assign to the correct node in the network
        -Look up URL: looks up the IP for the given URL from the node network
        */

        //Check if arguements were supplied. If not, helpful text will be displayed containing all
        // possible commands with arguement parameters.
        if (args.length == 0){
            System.out.println("No arguements supplied so no actions have been taken.");
            System.out.println("Available actions:");
            System.out.println("\tjoin [-BootstrapIP]  :Join a network via the ip of a bootstrap node.");
        }
        else{
            String action = args[0];
            switch(action){
                case "join":
                            System.out.println("Joining network via BootstrapIP: " + args[1]);
                            break;
                        }
            
        }

    }

}