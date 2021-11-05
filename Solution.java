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
        SolutionObject sol = new SolutionObject();
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
        int priority_Client = clients.size();
        for (Client client: clients) {
            client.priority = priority_Client;
            locations_path.put(client.id, 0);
            clientMap.put(client.id, client);
            priority_Client--;
        }
        Comparator<Packet> comparator = new Comparator<Packet>() {
            @Override
            public int compare(Packet o1, Packet o2) {
                int pri_o1 = clientMap.get(o1.client).priority;
                int pri_o2 = clientMap.get(o2.client).priority;
                if (pri_o1 > pri_o2) return -1;
                else if (pri_o1 < pri_o2) return 1;
                return 0;
            }
        };

        int contentProvider = graph.contentProvider;
        HashMap<Integer, ArrayList<Integer>> paths = Traversals.bfsPaths(graph, clients);
        LinkedList<Packet> startNode = new LinkedList<>();
        for (Client client: clients) {
            Packet packet = new Packet(client.id, paths.get(client.id);
            startNode.add(packet);
            packetMap.put(packet.client, packet);
        }
        situ_Node.put(contentProvider, startNode);
        while (!situ_Node.isEmpty()) {
            HashMap<Integer, ArrayList<Integer>> waiting_node = new HashMap<>();
            for (int num: situ_Node.keySet()) {
                int bandWidth = bandwidths.get(num);
                LinkedList<Packet> nodes = situ_Node.get(num);
                while (!nodes.isEmpty()) {
                    Packet exploringPacket = nodes.poll();
                    if (bandWidth > 0) {
                        /**
                         explore the exploring Packet
                         put the node to the next level
                         **/

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

    private void next_level(Packet packet, int currentNode) {
        int packetID = packet.client;
        ArrayList<Integer> path = packet.path;
        float tolerant = clientMap.get(packetID).alpha;
        int d = info.shortestDelays.get(packetID);
        int nextNode = path.get(locations_path.get(packetID));
        int waitingTime = waitingPacket(nextNode, packet);
        double percentage = 0.4;
        if (waitingTime > (int)(tolerant * d * percentage)) {
            ArrayList<Integer> adjacentNode = graph.get(currentNode);
            HashMap<Integer, Integer> waitingTimes = new HashMap<>();
            adjacentNode.forEach(integer -> waitingTimes.put(integer, waitingPacket(integer, packet)));
            int min_time = Collections.min(waitingTimes.values());
            for (int nodeID: waitingTimes.keySet()) {
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
