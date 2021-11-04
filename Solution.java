package ub.cse.algo;

import java.util.*;

public class Solution {

    private Info info;
    private Graph graph;
    private ArrayList<Client> clients;
    private ArrayList<Integer> bandwidths;

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
        for (Client client: clients) {
            client.priority = size_Client;
            size_Client--;
        }
        int contentProvider = graph.contentProvider;
        HashMap<Integer, ArrayList<Integer>> paths = Traversals.bfsPaths(graph, clients);
        HashMap<Integer, Integer> step_path = new HashMap<>(clients.size());
        for (Client client: clients) {
            client.priority = size_Client;
            step_path.put(client.id, 0);
            size_Client--;
        }
        HashMap<Integer, Queue<Packet>> situ_Node = new HashMap<>(graph.size());
        Queue<Client> startNode = new LinkedList<>() {
             {
                 addAll(clients);
                 sort();
            }
        };
        startNode.addAll(clients);
        situ_Node.put(contentProvider, startNode);
        while (!situ_Node.isEmpty()) {
            for (int num: situ_Node.keySet()) {
                int bandWidth = bandwidths.get(num);
                Queue<Client> nodes = startNode;
                while (!nodes.isEmpty()) {
                    Client exploringClient =
                    if (bandWidth > 0) {
                        /** for loop
                         explore the next node
                         put the node to the next level
                         **/
                        bandWidth--;
                    } else {
                        break;
                    }
                }

            }
        }
        System.out.println(graph.get(2962));
        /* TODO: Your solution goes here */
        sol.paths = Traversals.bfsPaths(graph, clients);
        return sol;
    }

    private int outputPaths_Helper() {
        return 0;
    }


}
