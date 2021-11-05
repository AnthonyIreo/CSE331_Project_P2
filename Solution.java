package ub.cse.algo;

import java.util.*;

public class Solution {

    private Info info;
    private Graph graph;
    private ArrayList<Client> clients;
    private ArrayList<Integer> bandwidths;
    // My Data Structure
    private HashMap<Integer, LinkedList<Packet>> situ_Node;
    private HashMap<Integer, Client> clientMap;
    private HashMap<Integer, Packet> packetMap;
    private HashMap<Integer, Integer> locations_path;
    private HashMap<Integer, LinkedList<Packet>> level_waitingList;

    /**
     * Basic Constructor
     *
     * @param info: data parsed from input file
     */
    public Solution(Info info) {
        this.info = info;
        this.graph = info.graph;
        this.clients = info.clients;
        this.bandwidths = info.bandwidths;
        this.situ_Node = new HashMap<>(graph.size() / 2);
        this.clientMap = new HashMap<>(clients.size());
        this.locations_path = new HashMap<>(clients.size());
        this.packetMap = new HashMap<>(clients.size());
        this.level_waitingList = new HashMap<>();
    }

    /**
     * Method that returns the calculated 
     * SolutionObject as found by your algorithm
     *
     * @return SolutionObject containing the paths, priorities and bandwidths
     */
    public SolutionObject outputPaths() {
        //return solution
        SolutionObject sol = new SolutionObject();
        //sort clients with tolerant firstly then payment
        clients.sort(new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                if (o1.alpha < o2.alpha) return -1;
                else if (o1.alpha > o2.alpha) return 1;
                else {
                    if (o1.payment > o2.payment) return -1;
                    else if (o1.payment < o2.payment) return 1;
                    else return 0;
                }
            }
        });
        
        //set the property of this client
        int priority_Client = clients.size();
        //for each client
        for (Client client: clients) {
            //update this.priority
            client.priority = priority_Client;
            //update this.path
            locations_path.put(client.id, 0);
            //update the client map
            clientMap.put(client.id, client);
            //update priority for next client
            priority_Client--;
        }
        
        //create a comparsion
        Comparator<Packet> comparator = new Comparator<Packet>() {
            @Override
            //override two packet to compare
            public int compare(Packet o1, Packet o2) {
                //import two priority
                int pri_o1 = clientMap.get(o1.client).priority;
                int pri_o2 = clientMap.get(o2.client).priority;
                //return two conditions
                if (pri_o1 > pri_o2) return -1;
                else if (pri_o1 < pri_o2) return 1;
                return 0;
            }
        };

        //set the start node 
        int contentProvider = graph.contentProvider;
        //do bfs for each node first, then we will change the path of each node based on the result of bfs
        HashMap<Integer, ArrayList<Integer>> paths = Traversals.bfsPaths(graph, clients);
        //startNode shows how many packets in this node
        LinkedList<Packet> startNode = new LinkedList<>();
        
        //create a packet for each client
        //update the property for each packet
        for (Client client: clients) {
            //packet has the client's id and the path of bfs to this client
            Packet packet = new Packet(client.id, paths.get(client.id);
            //add all packets into startNode linkedlist
            startNode.add(packet);
            //update the packet map
            packetMap.put(packet.client, packet);
        }
                                       
        //input the start node and all packets into situ_Node linked list
        situ_Node.put(contentProvider, startNode);
        //when no packet, we done
        while (!situ_Node.isEmpty()) {
            //create a hash map to store the node in waiting
            HashMap<Integer, ArrayList<Integer>> waiting_node = new HashMap<>();
            //go through situ_node, for each packet do
            for (int num: situ_Node.keySet()) {
                //get this bandwidth
                int bandWidth = bandwidths.get(num);
                
                //update nodes into linked list ????
                LinkedList<Packet> nodes = situ_Node.get(num);
                //if the linked list is not empty
                while (!nodes.isEmpty()) {
                    //take out the packet from the linked list
                    Packet exploringPacket = nodes.poll();
                    //if bandwidth > 0, it means this node can pass packet
                    if (bandWidth > 0) {
                        /**
                         explore the exploring Packet
                         put the node to the next level
                         **/

                        //create the next level
                        next_level(exploringPacket, node);
                        //bandwidth -1
                        bandWidth--;
                    } else {
                        break;
                    }
                }

            }
        }
        /* TODO: Your solution goes here */
        return sol;
    }

    //update the next level of the node with the input: this packet and real index in the graph 
    private void next_level(Packet packet, int currentNode) {
        //get the goal of this packet
        int packetID = packet.client;
        //get the original path of this packet
        ArrayList<Integer> path = packet.path;
        //get the tolerant of this packet
        float tolerant = clientMap.get(packetID).alpha;
        //get the shortest path delay of this packet in ideal situation
        int d = info.shortestDelays.get(packetID);
        //get the next node in this path
        int nextNode = path.get(locations_path.get(packetID));
        //get the number of packets waiting for entering the next node
        int waitingTime = waitingPacket(nextNode, packet);
        //set the overtime
        double percentage = 0.4;
        //if the waiting packet need to wait a long time to get in the next node, we change the next node and update its path
        if (waitingTime > (int)(tolerant * d * percentage)) {
            //get all adjacent nodes of the currentNode
            ArrayList<Integer> adjacentNode = graph.get(currentNode);
            //create a map to store each adjacent node's waiting time
            HashMap<Integer, Integer> waitingTimes = new HashMap<>();
            //get each adjacent node's waiting line
            adjacentNode.forEach(integer -> waitingTimes.put(integer, waitingPacket(integer, packet)));
            //get the node with minimum waiting time, the packet will go there
            int min_time = Collections.min(waitingTimes.values());
            //search the waiting time of in the map to find the node id
            for (int nodeID: waitingTimes.keySet()) {
                //update the node just found as the next node of the current node
                if (waitingTimes.get(nodeID) == min_time) nextNode = nodeID;
            }

            // update path in packet



        }
        level_waitingList.get(nextNode).add(packet);
    }

    private int waitingPacket(int nextNode, Packet packet) {
        ArrayList<Packet> waitingList = new ArrayList<>() {
            {
                add(packet);
                addAll(situ_Node.get(nextNode));
                if (level_waitingList.containsKey(nextNode)) {
                     addAll(level_waitingList.get(nextNode));
                }
                sort(new Comparator<Packet>() {
                    @Override
                    public int compare(Packet o1, Packet o2) {
                        int pri_o1 = clientMap.get(o1.client).priority;
                        int pri_o2 = clientMap.get(o2.client).priority;
                        if (pri_o1 > pri_o2) return -1;
                        else if (pri_o1 < pri_o2) return 1;
                        return 0;
                    }
                });
            }
        };
        int numWaiting = waitingList.indexOf(packet) + 1;
        int bandWidth_nextNode = bandwidths.get(nextNode);
        return numWaiting / bandWidth_nextNode;
    }

    private ArrayList<Integer> bfsPaths(Graph graph, ArrayList<Client> clients, int startNode, int endNode) {
        /*
            Initialize the prior array with -1 for storing the node
            that is before the current one in the shortest paths
         */
        int[] priors = new int[graph.size()];
        Arrays.fill(priors, -1);

        // Run BFS, finding the nodes parent in the shortest path
        Queue<Integer> searchQueue = new LinkedList<>();
        searchQueue.add(startNode);
        while (!searchQueue.isEmpty()) {
            int node = searchQueue.poll();
            for (int neighbor : graph.get(node)) {
                if (priors[neighbor] == -1 && neighbor != graph.contentProvider) {
                    priors[neighbor] = node;
                    searchQueue.add(neighbor);
                }
            }
        }

        // Get the final shortest paths
        HashMap<Integer, ArrayList<Integer>> paths = pathsFromPriors(clients, priors);
        return paths.get(endNode);
    }

    private HashMap<Integer, ArrayList<Integer>> pathsFromPriors(ArrayList<Client> clients, int[] priors) {
        HashMap<Integer, ArrayList<Integer>> paths = new HashMap<>(clients.size());
        // For every client, traverse the prior array, creating the path
        for (Client client : clients) {
            ArrayList<Integer> path = new ArrayList<>();
            int currentNode = client.id;
            while (currentNode != -1) {
                /*
                    Add this ID to the beginning of the
                    path so the path ends with the client
                 */
                path.add(0, currentNode);
                currentNode = priors[currentNode];
            }
            paths.put(client.id, path);
        }
        return paths;
    }
}
