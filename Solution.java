package ub.cse.algo;

import java.util.*;

public class Solution {

    private Info info;
    private Graph graph;
    private ArrayList<Client> clients;
    private ArrayList<Integer> bandwidths;
    private HashMap<Integer, Queue<Packet>> situ_Node;

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
        int size_Client = clients.size();
        HashMap<Integer, Integer> step_path = new HashMap<>(clients.size());
        for (Client client: clients) {
            client.priority = size_Client;
            step_path.put(client.id, 0);
            size_Client--;
        }
        int contentProvider = graph.contentProvider;
        HashMap<Integer, ArrayList<Integer>> paths = Traversals.bfsPaths(graph, clients);
        Queue<Packet> startNode = new LinkedList<>() {
             {
                 for (Client client: clients) {
                     add(new Packet(client.id, paths.get(client.id)));
                 }
            }
        };
        situ_Node.put(contentProvider, startNode);
        while (!situ_Node.isEmpty()) {
            HashMap<Integer, ArrayList<Integer>> waiting_node = new HashMap<>();
            for (int num: situ_Node.keySet()) {
                int bandWidth = bandwidths.get(num);
                Queue<Packet> nodes = situ_Node.get(num);
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

}
