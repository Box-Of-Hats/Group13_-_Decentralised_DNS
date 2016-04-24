class Application{

    public static void main(String[] args){

        //JAY: I DONT THINK THIS IS HOW WE SHOULD DO IT

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